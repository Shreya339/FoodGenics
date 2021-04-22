package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchAltAdapter extends RecyclerView.Adapter<SearchAltAdapter.ViewHolder> implements Filterable {

    private List<SearchResult> exampleList;
    private List<SearchResult> exampleListFull;
    SearchResult currentItem;

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView ingred, respi, instr;
        ViewHolder(View itemView) {
            super(itemView);
            ingred = itemView.findViewById(R.id.ingred);
            instr = itemView.findViewById(R.id.res_name);
            respi = itemView.findViewById(R.id.instruct);
        }
    }
    SearchAltAdapter(List<SearchResult> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentItem = exampleList.get(position);
        holder.ingred.setText(currentItem.getIngredients());
        holder.respi.setText(currentItem.getTitle());
        holder.instr.setText(currentItem.getInstructions());
        Log.i("instr ",currentItem.getInstructions()+" ");
    }
    @Override
    public int getItemCount() {
        return exampleList.size();
    }
    //logic for searching ingredients
    private Filter examplefilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchResult> filterlist=new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filterlist.addAll(exampleListFull);
            }
            else{
                String pattrn=constraint.toString().toLowerCase().trim();
                for(SearchResult item :exampleListFull){
                    if(item.getIngredients().toLowerCase().contains(pattrn)){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
