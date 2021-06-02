package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalorieInfo extends AppCompatActivity {
    ProgressBar calP;
    PlanCardModel planCardModel;
    TextView cl,cb,p,f,ti;
    SearchView sv;
    SearchResult item;
    EditText cur_cal;
    int tot_cal;
    private List<SearchResult> recipeList =new ArrayList<>();
    AddRecipeAdapter addRecipeAdapter;
    DisplayAddedRecipeList displayAddedRecipeList;
    HashMap<SearchResult,Integer> added_resipes;
    DatabaseReference fb = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Database");

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Your Calorie Tracker");

        added_resipes = new HashMap<>();
        calP = findViewById(R.id.cal_progress);
        ti = findViewById(R.id.ti);

        cl = findViewById(R.id.cal_text);
        cb = findViewById(R.id.carb_text);
        p = findViewById(R.id.prot_text);
        f = findViewById(R.id.fatt_text);
        cur_cal = findViewById(R.id.edittext_cal);

        planCardModel = (PlanCardModel) getIntent().getSerializableExtra("PlanCardModel");
        fillRecipeList();

        cur_cal.setText(planCardModel.getCur_cal()+"");
        tot_cal = planCardModel.getTot_cal();
        double temp = Integer.parseInt(cur_cal.getText().toString());

        ti.setText(planCardModel.getCard_title());
        if(Integer.parseInt(cur_cal.getText().toString())>tot_cal){
            Toast.makeText(CalorieInfo.this,"You have exceeded your calorie limit",Toast.LENGTH_LONG).show();
        }
        calP.setProgress((int)Math.round((temp/tot_cal)*100));

        cl.setText(planCardModel.getCur_cal()+"");
        cb.setText(planCardModel.getCur_carbs()+"");
        p.setText(planCardModel.getCur_protien()+"");
        f.setText(planCardModel.getCur_fat()+"");



        try {
            File f = new File(FirebaseAuth.getInstance().getCurrentUser().getUid());
            System.out.println("after getting f "+f);
                FileInputStream fis = getApplication().openFileInput(FirebaseAuth.getInstance().getCurrentUser().getUid());
                InputStreamReader inputStreamReader =
                        new InputStreamReader(fis, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append('\n');
                        line = reader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                    String contents = stringBuilder.toString();
                    added_resipes = (HashMap<SearchResult, Integer>) deserialize(contents);

                    Log.i("before recucler view", added_resipes.size() + "");
                }




        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }




        sv = findViewById(R.id.searchview_incalv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                addRecipeAdapter.getFilter().filter(newText);
                return false;
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
                RecyclerView recyclerView = findViewById(R.id.add_recipe_recyclerview_in_calv);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CalorieInfo.this);
                addRecipeAdapter = new AddRecipeAdapter(recipeList, tot_cal,CalorieInfo.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(addRecipeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static Object deserialize(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64.decode(s,Base64.DEFAULT);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}