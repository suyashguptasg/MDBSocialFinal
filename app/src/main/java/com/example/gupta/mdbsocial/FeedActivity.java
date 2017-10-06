package com.example.gupta.mdbsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Gupta on 9/25/2017.
 */

public class FeedActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Message> messages = new ArrayList<>();
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
    // Calls utils for creation of feed, creates FAB
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_list);
        FireBaseUtils.feedActivity(messages, ref, this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }
    // Sets listener to fab to open up CreateNewActivity.class
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CreateNewActivity.class);
        startActivity(intent);
    }

}



