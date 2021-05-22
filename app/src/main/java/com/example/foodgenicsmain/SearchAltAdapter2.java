package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAltAdapter2 extends RecyclerView.Adapter<SearchAltAdapter2.ViewHolder> implements Filterable {

    private List<SearchResult> recipeList;
    private List<SearchResult> recipeListfull;
    SearchResult currentItem;
    private Context context;

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView ingred, respi, instr;
        ImageView p_l;
        ViewHolder(View itemView) {
            super(itemView);
            //instr = itemView.findViewById(R.id.ingred);
            //ingred = itemView.findViewById(R.id.res_name);
            respi = itemView.findViewById(R.id.instruct);
            p_l = itemView.findViewById(R.id.img_resp);
        }
    }
    SearchAltAdapter2(List<SearchResult> recipeList,Context context) {
        this.context = context;
        this.recipeList = recipeList;
        recipeListfull = new ArrayList<>(recipeList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        currentItem = recipeList.get(position);
        //holder.ingred.setText(currentItem.getIngredients());
        holder.respi.setText(currentItem.getTitle());
        //holder.instr.setText(currentItem.getInstructions());
        Picasso.with(context).load(currentItem.getPicture_link()).into(holder.p_l);
       holder.p_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayRecipe.class);
                intent.putExtra("Current Recipe Set", recipeList.get(position));
                context.startActivity(intent);
            }
        });
        Log.i("instr ",currentItem.getInstructions()+" ");
    }
    @Override
    public int getItemCount() {
        return recipeList.size();
    }
    //logic for searching ingredients
    private Filter examplefilter= new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.i("in filter "," filterrrrr");
            List<SearchResult> filterlist=new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filterlist.addAll(recipeListfull);
            }
            else{
                String pattrn=constraint.toString().toLowerCase().trim();
                for(SearchResult item :recipeListfull){
                    if(item.getTitle().toLowerCase().contains(pattrn)){
                        filterlist.add(item);
                    }
                }
            }
            Log.i("Returning result ", "back");
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipeList.clear();
            recipeList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
