package com.example.gupta.mdbsocial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Create all elements to login/move to sign up screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        (findViewById(R.id.loginButton)).setOnClickListener(this);
        (findViewById(R.id.signupButton)).setOnClickListener(this);

        Glide.with(this).load("https://images-platform.99static.com/oxwBp_LS9TZoLaZc9Wq8DAuvXxk=/0x0:1668x1668/500x500/top/smart/99designs-contests-attachments/78/78423/attachment_78423913")
                .override(750, 250).centerCrop().into(((ImageView) findViewById(R.id.imageView2)));

    }
    // Check user login credentials
    private void attemptLogin() {
        String email = ((EditText) findViewById(R.id.emailView)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordView)).getText().toString();
        FireBaseUtils.logIn(email, password, this);
    }

    // Move user to sign up screen
    private void attemptSignup() {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                attemptLogin();
                break;
            case R.id.signupButton:
                attemptSignup();
                break;
        }
    }
}

