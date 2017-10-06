package com.example.gupta.mdbsocial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Gupta on 10/3/2017.
 */

public class FireBaseUtils {


    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("Login state:", "onAuthStateChange:signed_in:" + user.getUid());
            } else {
                Log.d("Login state:", "onAuthStateChanged: signed_out");
            }
        }
    };

    //Logs user in
    public static void logIn(String email, String password, final Context context) {
            if (!email.equals("") && !password.equals("")) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Email", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Email fail", "signInWithEmail:failed", task.getException());
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(context, FeedActivity.class);
                            ((Activity)context).startActivity(intent);
                        }
                    }
                });
            }
    }
    //Signs user up
    public static void attemptSignup(String email, String password, final Context context) {
        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, "Failed :(",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(context, FeedActivity.class);
                                ((Activity) context).startActivity(intent);
                            }
                        }
                    });
        }
    }
    // Adds new messages to be presented
    public static void addNew(int requestCode, int resultCode, Intent data, final Context context, final String name,
                              final String description, final String date){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final String key = ref.child("socials").push().getKey();
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocial-d237d.appspot.com/");
            // Named riversref just for Aayush
            StorageReference riversRef = storageRef.child(key + ".png");
            riversRef.putFile(data.getData()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "need an image!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Set all data values for new event
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    ref.child("messages").child(key).child("email").setValue(email);
                    ref.child("messages").child(key).child("name").setValue(name);
                    ref.child("messages").child(key).child("description").setValue(description);
                    ref.child("messages").child(key).child("date").setValue(date);
                    ref.child("messages").child(key).child("url").setValue(key);
                    ref.child("messages").child(key).child("interested").setValue("0");
                    ref.child("messages").child(key).child("timestamp").setValue(-1*System.currentTimeMillis());
                    Intent intent = new Intent(context, FeedActivity.class);
                    ((Activity) context).startActivity(intent);
                }
            });

        }
    }
    // Creaets the feed
   public static void feedActivity(final ArrayList<Message> messages, DatabaseReference ref, final Context context) {
        final RecyclerView recyclerAdapter = (RecyclerView)((Activity) context).findViewById(R.id.recyclerView);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager((Activity) context));
        final FeedAdapter adapter = new FeedAdapter(context, messages);
        ref.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot m : dataSnapshot.getChildren()) {
                    messages.add(new Message(m.child("email").getValue(String.class), m.child("url").getValue(String.class),
                            m.child("interested").getValue(String.class),
                            m.child("name").getValue(String.class), m.child("description").getValue(String.class)
                            , m.child("date").getValue(String.class), m.child("timestamp").getValue(Long.class)));
                }
                recyclerAdapter.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
    // Attempts to set image to replace glide (Currently does not work)
    public static void setImage(final Context context, final Message m, final FeedAdapter.CustomViewHolder holder, final StorageReference ref) {
        class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
            protected Bitmap doInBackground(String... strings) {
                try {
                    return Glide.with(context).load(ref).asBitmap().into(100, 100).get();
                } catch (Exception e) {
                    return null;
                }
            }
            protected void onPostExecute(Bitmap result) {
                ((Activity) context).findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                holder.imageView.setImageBitmap(result);
            }
        }
    }

}
