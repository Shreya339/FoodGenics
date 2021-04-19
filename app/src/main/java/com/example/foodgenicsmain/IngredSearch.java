package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class IngredSearch extends AppCompatActivity {

    private EditText mSearchField;
    private Button mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingred_search);

        mUserDatabase = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference("Database");


        mSearchField = (EditText) findViewById(R.id.s_f);
        mSearchBtn = findViewById(R.id.s_b);

        mResultList = (RecyclerView) findViewById(R.id.r_v);
        String searchText = mSearchField.getText().toString();

        Query query = mUserDatabase.orderByChild("ingredients").startAt(searchText).endAt(searchText + "\uf8ff");

        Log.i("info",searchText);

        FirebaseRecyclerOptions<SearchResult> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<SearchResult>().setQuery(query, SearchResult.class).build();
        adapter = new FirebaseRecyclerAdapter<SearchResult, SearchViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.i("info","inside search");
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_layout, parent, false);

                return new SearchViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull SearchResult model) {
                Log.i("info","in adapter");
                holder.setDefault(model.getRecipe_name(),model.getIngredients(),model.getInstructions());
            }
        };
        mResultList.setAdapter(adapter);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String searchText = mSearchField.getText().toString();

                //firebaseUserSearch(searchText);
                adapter.startListening();

            }
        });

    }
    private void firebaseUserSearch(String searchText) {

        Toast.makeText(IngredSearch.this, "Started Search", Toast.LENGTH_LONG).show();

        Log.i("info",adapter.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class SearchViewHolder extends  RecyclerView.ViewHolder{
        View sView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("info","in inner class");
            sView = itemView;
        }

        public void setDefault(String resp,String ingredients, String instruc){
            Log.i("set default","in set");
            TextView resps = sView.findViewById(R.id.res_name);
            resps.setText(resp);
            TextView ingred = sView.findViewById(R.id.ingred);
            ingred.setText(ingredients);
            TextView instr  = sView.findViewById(R.id.instruct);
            instr.setText(instruc);
        }
    }


}