package com.example.foodgenicsmain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link firstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class firstFragment extends Fragment {

    RecyclerView recyclerView;
    EditText searchField;
    Button search_btn;
    private DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public firstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static firstFragment newInstance(String param1, String param2) {
        firstFragment fragment = new firstFragment();
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
        final View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.i("info","in override");

        searchField = view.findViewById(R.id.search_field);
        search_btn = view.findViewById(R.id.search_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Database");

        Log.i("info","in override");

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleText();
            }
        });
    }
    public void handleText() {
        Log.i("info","in handle");

        String search_word = searchField.getText().toString();

        Query query = databaseReference.orderByChild("ingredients").startAt(search_word).endAt(search_word+"\uf8ff");
        Log.i("info", String.valueOf(query));
        FirebaseRecyclerOptions<SearchModel> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<SearchModel>().setQuery(query, SearchModel.class).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<SearchModel, firstFragment.SearchViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public firstFragment.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull firstFragment.SearchViewHolder holder, int position, @NonNull SearchModel model) {
                Log.i("info","in adapter");
                holder.setDefault(model.getSearchResult());
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class SearchViewHolder extends  RecyclerView.ViewHolder{
        View sView;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("info","in inner class");
            sView = itemView;
        }

        public void setDefault(String i_res){
            Log.i("set default","in set");
            TextView ingred = sView.findViewById(R.id.res_name);
            ingred.setText(i_res);
        }
    }
}