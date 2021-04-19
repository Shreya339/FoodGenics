package com.example.foodgenicsmain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 4000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
                                      public void run() {
                                          // Check if user is signed in (non-null) and update UI accordingly.
                                          FirebaseUser currentUser = mAuth.getCurrentUser();
                                          if (currentUser != null) {

                                              Intent intent = new Intent(MainActivity.this, HomePage.class);
                                              startActivity(intent);
                                              finish();
                                          } else {
                                              Intent intent = new Intent(MainActivity.this, Login.class);
                                              startActivity(intent);
                                              finish();
                                          }
                                      }
                                  }, SPLASH_TIME_OUT);

    }
}