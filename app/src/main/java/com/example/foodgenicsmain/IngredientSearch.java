package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IngredientSearch extends AppCompatActivity {

    private SearchAltAdapter adapter;
    private List<SearchResult> exampleList=new ArrayList<>();
    SearchResult item;

    DatabaseReference fb= FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Database");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredient);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Search with Ingredients");
        fillExampleList();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("onQueryTextSubmit ", s);
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i("onQueryTextChange ", s);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    private void fillExampleList() {

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ing = ds.child("ingredients").getValue().toString();
                    String titl = ds.child("title").getValue().toString();
                    String inst = ds.child("instructions").getValue().toString();
                    String p_l = ds.child("picture_link").getValue().toString();
                    String recipe_id = ds.getKey();
                    int call = Integer.parseInt(ds.child("cal").getValue().toString());
                    int proo = Integer.parseInt(ds.child("pro").getValue().toString());
                    int fatt = Integer.parseInt(ds.child("fat").getValue().toString());
                    int carbs = Integer.parseInt(ds.child("carbs").getValue().toString());
                    item = new SearchResult(ing,inst,titl,p_l,recipe_id,call,proo,fatt,carbs);

                    exampleList.add(item);
                }
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(IngredientSearch.this);
                adapter = new SearchAltAdapter(exampleList,getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
