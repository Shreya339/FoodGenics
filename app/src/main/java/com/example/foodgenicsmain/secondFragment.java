package com.example.foodgenicsmain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link secondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class secondFragment extends Fragment {

    RecyclerView recyclerView;
    EditText searchField;
    Button search_btn;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public secondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment secondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static secondFragment newInstance(String param1, String param2) {
        secondFragment fragment = new secondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        Log.i("info","in override");

        searchField = view.findViewById(R.id.search_field);
        search_btn = view.findViewById(R.id.search_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Database");

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchField.getText().toString();

                Log.i("info",searchText+" ");

                Query query = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText +"\uf8ff");

                Log.i("info",searchText);

                FirebaseRecyclerOptions<SearchResult> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<SearchResult>().setQuery(query, SearchResult.class).build();
                adapter = new FirebaseRecyclerAdapter<SearchResult, secondFragment.SearchViewHolder>(firebaseRecyclerOptions) {

                    @NonNull
                    @Override
                    public secondFragment.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        Log.i("info","inside search");
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_layout, parent, false);

                        return new secondFragment.SearchViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull secondFragment.SearchViewHolder holder, int position, @NonNull SearchResult model) {
                        Log.i("get recipie name",model.getTitle()+" ");
                        Log.i("get ingredients",model.getIngredients()+" ");
                        holder.setDefault(model.getIngredients(),model.getInstructions(),model.getTitle());
                    }
                };
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.startListening();

            }
        });

    }
    public static class SearchViewHolder extends  RecyclerView.ViewHolder{
        View sView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("info","in inner class");
            sView = itemView;
        }

        public void setDefault(String resp,String ingredients, String instruc){
            TextView resps = sView.findViewById(R.id.res_name);
            resps.setText(resp);
            Log.i("recipe name",resp+" ");
            TextView ingred = sView.findViewById(R.id.ingred);
            ingred.setText(ingredients);
            TextView instr  = sView.findViewById(R.id.instruct);
            instr.setText(instruc);
        }
    }
}