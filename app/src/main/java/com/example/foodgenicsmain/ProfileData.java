package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ProfileData extends AppCompatActivity {
    String username,email,disease;
    int profile_pic_id;

    TextView username_field,email_field,disease_field;
    ImageView t_img,b_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_data);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Profile Details");

        username_field = findViewById(R.id.username_field);
        email_field = findViewById(R.id.email_field);
        disease_field = findViewById(R.id.health_field);
        t_img = findViewById(R.id.top_half);

        FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("from  db  ",snapshot.toString());
                     username = snapshot.child("username").getValue().toString();
                     email = snapshot.child("email").getValue().toString();
                     disease = snapshot.child("disease").getValue().toString();
                     profile_pic_id = Integer.parseInt(snapshot.child("profile_img_id").getValue().toString());

                username_field.setText(username);
                email_field.setText(email);
                disease_field.setText(disease);
                t_img.setImageResource(profile_pic_id);

                Log.i("from  db  ",username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}