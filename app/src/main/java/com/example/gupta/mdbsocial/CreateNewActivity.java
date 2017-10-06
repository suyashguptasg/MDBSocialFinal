package com.example.gupta.mdbsocial;

import android.app.Activity;
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

public class CreateNewActivity extends AppCompatActivity implements View.OnClickListener{

    // Creates layout for user to input data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);
        (findViewById(R.id.photoButton)).setOnClickListener(this);
        (findViewById(R.id.back)).setOnClickListener(this);

    }
    //Acts upon receiving a picture from the user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = ((EditText) findViewById(R.id.eventNameText)).getText().toString();
        String description = ((EditText) findViewById(R.id.eventDescriptionText)).getText().toString();
        String date = ((EditText) findViewById(R.id.eventDateText)).getText().toString();
        FireBaseUtils.addNew(requestCode, resultCode, data, this, name, description, date);
    }
    // On click listener added to photo/back buttons
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photoButton:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.back:
                startActivity(new Intent(CreateNewActivity.this, FeedActivity.class));
                break;
        }

    }
}
