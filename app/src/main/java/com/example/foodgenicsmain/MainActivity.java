package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 4000;
    private FirebaseAuth mAuth;
    Animation firstAnim, secondAnim, thirdAnim;
    ImageView logo;
    TextView name,slogan;
    List<String> res_ids;
    SearchResult item;
    private List<SearchResult> recipeList;
    RecommedationAdapter recommedationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //animation
        firstAnim = AnimationUtils.loadAnimation(this, R.anim.first_logo_anim);
        secondAnim = AnimationUtils.loadAnimation(this, R.anim.second_name_anim);
        thirdAnim = AnimationUtils.loadAnimation(this, R.anim.third_slogan_anim);

        //hooks
        logo = findViewById(R.id.app_logo);
        name = findViewById(R.id.app_name);
        slogan = findViewById(R.id.app_slogan);

        logo.setAnimation(firstAnim);
        name.setAnimation(secondAnim);
        slogan.setAnimation(thirdAnim);


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