package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPlan extends AppCompatActivity {

    EditText title_edittext, totCal_edittext,cur_cal;
    private Spinner cal_spinner;
    Button submit_btn;
    SearchView search_recipe_btn;
    AddPlanAdapter addPlanAdapter;
    TextView pro,fat,carbs;
    DatabaseReference fb = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Database");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference();
    SearchResult item;
    Date date;
    SimpleDateFormat formatter;
    private List<SearchResult> recipeList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add a diet plan");


        title_edittext = findViewById(R.id.plantitle);
        totCal_edittext = findViewById(R.id.cal_limit);
        cal_spinner = findViewById(R.id.num_of_days);
        submit_btn = findViewById(R.id.createplan_btn);
        cur_cal = findViewById(R.id.cur_cal);
        pro = findViewById(R.id.protien);
        fat = findViewById(R.id.fat);
        carbs = findViewById(R.id.carbs);

        fillRecipeList();

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        search_recipe_btn = findViewById(R.id.searchview);

        search_recipe_btn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                addPlanAdapter.getFilter().filter(newText);
                return false;
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PlanCardModel planCardModel = new PlanCardModel(Integer.parseInt(cur_cal.getText().toString()),Integer.parseInt(totCal_edittext.getText().toString()),Integer.parseInt(carbs.getText().toString()),Integer.parseInt(pro.getText().toString()),Integer.parseInt(fat.getText().toString()),Integer.parseInt(cal_spinner.getSelectedItem().toString()),title_edittext.getText().toString(),formatter.format(date));
                databaseReference.child("UserPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_"+title_edittext.getText()+"_Plans").setValue(planCardModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Plan added!!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddPlan.this,CalorieInfo.class);
                        intent.putExtra("PlanCardModel",planCardModel);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void fillRecipeList() {

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ing = ds.child("ingredients").getValue().toString();
                    String titl = ds.child("title").getValue().toString();
                    String inst = ds.child("instructions").getValue().toString();
                    String picture_link = ds.child("picture_link").getValue().toString();
                    String recipe_id = ds.getKey();
                    int call = Integer.parseInt(ds.child("cal").getValue().toString());
                    int proo = Integer.parseInt(ds.child("pro").getValue().toString());
                    int fatt = Integer.parseInt(ds.child("fat").getValue().toString());
                    int carbs = Integer.parseInt(ds.child("carbs").getValue().toString());
                    item = new SearchResult(ing,inst,titl,picture_link,recipe_id,call,proo,fatt,carbs);

                    Log.i("after setting ingred", item.getIngredients() + " ");
                    Log.i("after setting instr", item.getInstructions() + " ");

                    recipeList.add(item);
                }
                RecyclerView recyclerView = findViewById(R.id.add_recipe_recyclerview);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddPlan.this);
                Log.i("before calling adapter",cur_cal.getText().toString());
                System.out.println("in add plan calll"+cur_cal);
                addPlanAdapter = new AddPlanAdapter(recipeList,cur_cal, AddPlan.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(addPlanAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}