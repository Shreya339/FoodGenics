package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.text.TextUtils;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private EditText e_username,e_email,e_password,e_confirmpassword;
    private String username,email,password,confirmpassword,disease;
    private Spinner e_disease;
    private Button signin_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        e_username = findViewById(R.id.username);
        e_email = findViewById(R.id.email);
        e_password = findViewById(R.id.password);
        e_confirmpassword = findViewById(R.id.cpassword);
        e_disease = findViewById(R.id.disease_spinner);
        signin_button = findViewById(R.id.signin_b);


        e_disease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Register.this,parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            this.getSupportActionBar().hide();
        } catch (Exception e) {

        }
        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.this.finish();
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

        public void registerUser(){

            Toast.makeText(Register.this,"entered reg user",Toast.LENGTH_LONG).show();

            email = e_email.getText().toString().trim();
            password = e_password.getText().toString().trim();
            confirmpassword = e_confirmpassword.getText().toString().trim();
            username = e_username.getText().toString().trim();
            disease = e_disease.getSelectedItem().toString();

            if (TextUtils.isEmpty(email)||TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmpassword))
                Toast.makeText(Register.this,"Fields can't be empty",Toast.LENGTH_LONG).show();
            else if(!password.equals(confirmpassword))
                Toast.makeText(Register.this, "Please enter the same password", Toast.LENGTH_LONG).show();

            else{
                Log.i("creating user-","inside");
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i("in success auth-","success");
                                    //store in db
                                    User usr = new User(username,email,disease);
                                    Log.i("test :-","after user");
                                    FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.i("in success db-","success");
                                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                                Log.d("success:- ", "createUserWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent intent = new Intent(Register.this, HomePage.class);
                                                startActivity(intent);
                                            } else {
                                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                firebaseUser.delete();
                                                Toast.makeText(Register.this, "Sorry registration unsuccessful", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                } else {
                                    Log.w("exception:- ", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                                });
            }
        }


    }
