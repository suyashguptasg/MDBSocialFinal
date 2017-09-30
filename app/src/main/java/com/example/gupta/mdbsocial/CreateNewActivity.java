package com.example.gupta.mdbsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Date;
import java.util.Calendar;


/**
 * Created by Gupta on 9/25/2017.
 */

public class CreateNewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);

        ((Button) findViewById(R.id.photoButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        ((Button) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewActivity.this, FeedActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final String key = ref.child("socials").push().getKey();
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-d237d.appspot.com/");
            // Named riversref just for Aayush
            StorageReference riversRef = storageRef.child(key + ".png");
            riversRef.putFile(data.getData()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(CreateNewActivity.this, "need an image!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Set all data values for new event
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    String name = ((EditText) findViewById(R.id.eventNameText)).getText().toString();
                    String description = ((EditText) findViewById(R.id.eventDescriptionText)).getText().toString();
                    String date = ((EditText) findViewById(R.id.eventDateText)).getText().toString();
                    ref.child("messages").child(key).child("email").setValue(email);
                    ref.child("messages").child(key).child("name").setValue(name);
                    ref.child("messages").child(key).child("description").setValue(description);
                    ref.child("messages").child(key).child("date").setValue(date);
                    ref.child("messages").child(key).child("url").setValue(key);
                    ref.child("messages").child(key).child("interested").setValue("0");
                    ref.child("messages").child(key).child("timestamp").setValue(-1*System.currentTimeMillis());
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);
                }
            });

        }


    }
}
