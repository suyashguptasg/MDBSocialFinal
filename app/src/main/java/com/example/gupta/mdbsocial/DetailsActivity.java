package com.example.gupta.mdbsocial;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Gupta on 9/25/2017.
 */

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Message m;
    Button back;

    // Presents all the data, creates a switch for user to indicate interest
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            m = (Message) bd.get("message");
        }
        ((TextView) findViewById(R.id.nameView)).setText(m.name);
        ((TextView) findViewById(R.id.emailView)).setText(m.email);
        ((TextView) findViewById(R.id.descriptionView)).setText(m.description);
        ((TextView) findViewById(R.id.dateView)).setText(m.date);
        ((TextView) findViewById(R.id.interestedView)).setText(getString(R.string.numinterested) + " " + m.interested);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-d237d.appspot.com/").child(m.url + ".png"))
                .into(((ImageView) findViewById(R.id.photoView)));
        final Switch rsvpSwitch = (Switch) findViewById(R.id.interestedSwitch);
        // Switch to indicate if a person is interested
        //Cool feature where if you go set interested then come back it'll mark you uninterested
        //That way, if you find out your friend is coming too, you can add them to the interested number
        rsvpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            ref.child("messages").child(m.getUrl()).child("interested").setValue((String.valueOf(Integer.parseInt(m.interested) + 1)));
                            m.setInterested((String.valueOf(Integer.parseInt(m.interested) + 1)));
                            ((TextView) findViewById(R.id.interestedView)).setText("Number Interested: " + " " + m.interested);
                            Toast toast = Toast.makeText(getApplicationContext(), "You are interested in this event!", Toast.LENGTH_SHORT);
                            toast.show();
                            return null;
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            Log.d("error", "postTransaction:onComplete:" + databaseError);
                        }
                    });

                } else {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            ref.child("messages").child(m.getUrl()).child("interested").setValue((String.valueOf(Integer.parseInt(m.interested) - 1)));
                            m.setInterested((String.valueOf(Integer.parseInt(m.interested) - 1)));
                            ((TextView) findViewById(R.id.interestedView)).setText(getString(R.string.numinterested) +  m.interested);
                            return null;
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            Log.d("error", "postTransaction:onComplete:" + databaseError);
                        }
                    });

                }
            }
        });
    }
    // Adds listener to back button
    @Override
    public void onClick(View view) {
        startActivity(new Intent(DetailsActivity.this, FeedActivity.class));
    }

}
