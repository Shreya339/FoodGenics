package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FirebaseAuth auth;

    int url1 = R.drawable.ingrdients;
    int url2 = R.drawable.re;
    int url3 = R.drawable.caloriebar;
    List<String> res_ids;
    SearchResult item;
    private List<SearchResult> recipeList;
    TextView loading;
    RecommedationAdapter recommedationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        recipeList =new ArrayList<>();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(HomePage.this,HomePage.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_search_ingred:
                        Intent intent1 = new Intent(HomePage.this, IngredientSearch.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_search_recipe:
                        Intent intent2 = new Intent(HomePage.this,RecipeSearch.class);
                        startActivity(intent2);
                        break;
                    case R.id.dietPlanner:
                        Intent intent4 = new Intent(HomePage.this,DietPlanner.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_set:
                        Intent intent3 = new Intent(HomePage.this,ProfileData.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_about:
                        Toast.makeText(HomePage.this,"we are great",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_cont:
                        Toast.makeText(HomePage.this,"hiiiiiiii",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_logout:
                        logOut();
                }
                return false;
            }
        });

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        SliderView sliderView = findViewById(R.id.slider);

        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        OkHttpClient client=new OkHttpClient();
        String api_url = "https://foodgenicollaborativefiltering.herokuapp.com/"+FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("user_id ",api_url);
        Request request= new Request.Builder()
                .url(api_url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("in error");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res_ids = new ArrayList<>();
                if(response.isSuccessful()) {
                    final ResponseBody myResponse = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse.string());
                        Log.i("jsonobject ",jsonObject.toString());
                        JSONObject foodId = jsonObject.getJSONObject("food_id");
                        Log.i("ffo ids ", foodId.toString());
                        JSONObject predicted_rating = jsonObject.getJSONObject("predicted_rating");
                        Log.i(" predicted rating ",predicted_rating.toString());

                        Iterator<String> keys = predicted_rating.keys();
                        while(keys.hasNext()){
                            String key = keys.next();
                            Log.i("in while th ekey is ",key);
                            Log.i(" rating ",predicted_rating.get(key).toString());
                            if(Double.valueOf(predicted_rating.get(key).toString())<3.5){
                                Log.i("in if ","in if");
                                break;
                            }
                            res_ids.add(foodId.get(key).toString());
                            System.out.println(res_ids);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DatabaseReference fb = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Database");

                    fb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            loading = findViewById(R.id.loading);
                            loading.setVisibility(View.VISIBLE);
                            for(String id : res_ids){
                                System.out.println(id);
                                if(snapshot.hasChild(id)){
                                    Log.i("in if of datasnapshot ", String.valueOf(snapshot.hasChild(id)));
                                    String ing = snapshot.child(id).child("ingredients").getValue().toString();
                                    String titl = snapshot.child(id).child("title").getValue().toString();
                                    String inst = snapshot.child(id).child("instructions").getValue().toString();
                                    String picture_link = snapshot.child(id).child("picture_link").getValue().toString();
                                    String recipe_id = id;
                                    int call = Integer.parseInt(snapshot.child(id).child("cal").getValue().toString());
                                    int proo = Integer.parseInt(snapshot.child(id).child("pro").getValue().toString());
                                    int fatt = Integer.parseInt(snapshot.child(id).child("fat").getValue().toString());
                                    int carbs = Integer.parseInt(snapshot.child(id).child("carbs").getValue().toString());
                                    item = new SearchResult(ing,inst,titl,picture_link,recipe_id,call,proo,fatt,carbs);

                                    recipeList.add(item);
                                }
                            }
                            loading.setVisibility(View.INVISIBLE);
                            System.out.println(recipeList);
                            RecyclerView recyclerView = findViewById(R.id.recommendation_recycler_view);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomePage.this);
                            System.out.println("in between recycler view");
                            recommedationAdapter = new RecommedationAdapter(recipeList,getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(recommedationAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }
            }

        });



    }

    public void logOut(){
        auth=FirebaseAuth.getInstance();
        auth.signOut();
        finish();
        Intent intent = new Intent(HomePage.this,Login.class);
        startActivity(intent);
    }
}