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

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_list);
        final RecyclerView recyclerAdapter = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager(this));
        //If a new event isn't being created, return the list of current data
        if (getList().size()==0){
            final ArrayList<Message> messages = new ArrayList<>();
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
            ref.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot m :  dataSnapshot.getChildren()) {
                        messages.add(new Message(m.child("email").getValue(String.class), m.child("url").getValue(String.class),
                                m.child("interested").getValue(String.class),
                                m.child("name").getValue(String.class),m.child("description").getValue(String.class)
                                , m.child("date").getValue(String.class), m.child("timestamp").getValue(Long.class)));
                    }
                    FeedAdapter adapter = new FeedAdapter(getApplicationContext(), messages);
                    recyclerAdapter.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else {
            FeedAdapter adapter = new FeedAdapter(getApplicationContext(), getList());
            recyclerAdapter.setAdapter(adapter);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNewActivity.class);
                startActivity(intent);
            }
        });

    }


    //Retrieve data is message is being added
    private ArrayList<Message> getList() {
        final ArrayList<Message> messages = new ArrayList<>();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/messages");
        ref.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messages.add(new Message(dataSnapshot.child("email").getValue(String.class), dataSnapshot.child("url").getValue(String.class),
                        dataSnapshot.child("interested").getValue(String.class),
                        dataSnapshot.child("name").getValue(String.class),dataSnapshot.child("description").getValue(String.class)
                , dataSnapshot.child("date").getValue(String.class), dataSnapshot.child("timestamp").getValue(Long.class)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                recreate();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return messages;
    }
}



