package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DietPlanner extends AppCompatActivity {
    List<PlanCardModel> planList;
    FloatingActionButton addplan;
    DatabaseReference databaseReference;
    DietPlannerAdapter dietPlannerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Plan your daily meals");
        planList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("UserPlans");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.hasChildren()) {
                        String regex ="^("+FirebaseAuth.getInstance().getCurrentUser().getUid()+")\\w+";

                        System.out.println(snap.getKey().toString());
                        if(snap.getKey().startsWith(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            System.out.println("in if");
                            PlanCardModel p = new PlanCardModel(Integer.parseInt(snap.child("cur_cal").getValue().toString()), Integer.parseInt(snap.child("tot_cal").getValue().toString()), Integer.parseInt(snap.child("cur_carbs").getValue().toString()), Integer.parseInt(snap.child("cur_protien").getValue().toString()), Integer.parseInt(snap.child("cur_fat").getValue().toString()), Integer.parseInt(snap.child("num_of_days").getValue().toString()), snap.child("card_title").getValue().toString(), snap.child("created_date").getValue().toString());
                            planList.add(p);
                        }

                    }
                    RecyclerView recyclerView = findViewById(R.id.plancards);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DietPlanner.this);
                    dietPlannerAdapter = new DietPlannerAdapter(planList, getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(dietPlannerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addplan = findViewById(R.id.addplan_btn);
        addplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietPlanner.this,AddPlan.class);
                startActivity(intent);
            }
        });
    }
}