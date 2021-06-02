package com.example.foodgenicsmain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommedationAdapter extends RecyclerView.Adapter<RecommedationAdapter.ViewHolder> {
    private List<SearchResult> recipeList;
    Context context;
    SearchResult currentItem;
    private List<SearchResult> recipeListFull;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView  respi ;
        ImageView p_l;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            respi = itemView.findViewById(R.id.instruct);
            p_l = itemView.findViewById(R.id.img_resp);
        }
    }

    public RecommedationAdapter(List<SearchResult> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
        this.recipeListFull = new ArrayList<>(recipeList);
    }

    @NonNull
    @Override
    public RecommedationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("in view holder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new RecommedationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommedationAdapter.ViewHolder holder, final int position) {
        currentItem = recipeList.get(position);
        holder.respi.setText(currentItem.getTitle());
        Picasso.with(context).load(currentItem.getPicture_link()).into(holder.p_l);
        holder.p_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayRecipe.class);
                intent.putExtra("Current Recipe Set", recipeList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeListFull.size();
    }
}
