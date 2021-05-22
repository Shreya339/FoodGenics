package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FirebaseAuth auth;

    int url1 = R.drawable.ingrdients;
    int url2= R.drawable.re;
    int url3= R.drawable.caloriebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);



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
                    case R.id.nav_set:
                        Toast.makeText(HomePage.this,"yeah this is settings",Toast.LENGTH_LONG).show();
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

    }

    public void logOut(){
        auth=FirebaseAuth.getInstance();
        auth.signOut();
        finish();
        Intent intent = new Intent(HomePage.this,Login.class);
        startActivity(intent);
    }
}