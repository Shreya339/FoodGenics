package com.example.foodgenicsmain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DisplayAddedRecipeList extends RecyclerView.Adapter<DisplayAddedRecipeList.ViewHolder> {

    HashMap<SearchResult,Integer> added_resipes;
    Context context;

    DisplayAddedRecipeList(HashMap<SearchResult,Integer> added_recipes, Context context){
        this.added_resipes = added_recipes;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingred, respi, instr;
        ImageView p_l;
        ViewHolder(View itemView) {
            super(itemView);
            respi = itemView.findViewById(R.id.instruct);
            p_l = itemView.findViewById(R.id.img_resp);
        }
    }
    @NonNull
    @Override
    public DisplayAddedRecipeList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new DisplayAddedRecipeList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayAddedRecipeList.ViewHolder holder, int position) {

        for(Map.Entry<SearchResult,Integer> set: added_resipes.entrySet()){
            SearchResult cur = set.getKey();
            holder.respi.setText(cur.getTitle());
            Picasso.with(context).load(cur.getPicture_link()).into(holder.p_l);
        }
    }

    @Override
    public int getItemCount() {
        return added_resipes.size();
    }
}
