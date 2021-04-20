package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager viewPager2;
    TabLayout tabLayout;
    TabItem firsttabItem, secondtabItem, thirdtabItem;
    PagerAdapter adapter;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        viewPager2 = findViewById(R.id.viewPager);
        firsttabItem = findViewById(R.id.tab_1);
        secondtabItem = findViewById(R.id.tab_2);
        thirdtabItem = findViewById(R.id.tab_3);
        tabLayout = findViewById(R.id.tabLayout);

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

        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void logOut(){
        auth=FirebaseAuth.getInstance();
        auth.signOut();
        finish();
        Intent intent = new Intent(HomePage.this,Login.class);
        startActivity(intent);
    }
}