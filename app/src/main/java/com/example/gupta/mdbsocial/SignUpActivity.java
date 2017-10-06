package com.example.gupta.mdbsocial;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Gupta on 9/25/2017.
 */

public class SignUpActivity extends AppCompatActivity  implements View.OnClickListener{
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Sets up sign up screen
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        (findViewById(R.id.signupButton)).setOnClickListener(this);

        Glide.with(this).load("https://images-platform.99static.com/oxwBp_LS9TZoLaZc9Wq8DAuvXxk=/0x0:1668x1668/500x500/top/smart/99designs-contests-attachments/78/78423/attachment_78423913")
                .override(750,250).centerCrop().into(((ImageView) findViewById(R.id.imageView2)));
    }
    // Attempts to signup user
    public void attemptSignup() {
        String email = ((EditText) findViewById(R.id.emailView)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();
        FireBaseUtils.attemptSignup(email, password, this);
    }

    @Override
    public void onClick(View v) {
        attemptSignup();
    }
}

