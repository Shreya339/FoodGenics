package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SearchAltAdapter extends RecyclerView.Adapter<SearchAltAdapter.ViewHolder> implements Filterable {

    private List<SearchResult> exampleList;
    private List<SearchResult> exampleListFull;
    SearchResult currentItem;
    private Context context;
    String pattrn;

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
    SearchAltAdapter(List<SearchResult> exampleList,Context context) {
        this.context = context;
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        currentItem = exampleList.get(position);
        holder.respi.setText(currentItem.getTitle());
        Picasso.with(context).load(currentItem.getPicture_link()).into(holder.p_l);
        holder.p_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayRecipe.class);
                intent.putExtra("Current Recipe Set", exampleList.get(position));
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        Log.i("in get count ", String.valueOf(exampleList.size()));
        return exampleList.size();
    }

    //logic for searching ingredients
    private Filter examplefilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.i("in filter "," filterrrrr");
            String[] constraint_s = constraint.toString().split(",");

            FutureTask[] parallel_tasks = new FutureTask[5];
            for(int i = 0;i<constraint_s.length;i++){
                Callable callable = new ParallelExecution(exampleListFull,constraint_s[i],constraint_s[i]);
                parallel_tasks[i] = new FutureTask(callable);
                Thread t1 = new Thread(parallel_tasks[i]);
                t1.start();
            }
            FilterResults filterResults=new FilterResults();
            List<SearchResult> tempList = new ArrayList<>();

            try {
                tempList = (List<SearchResult>) parallel_tasks[0].get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i=1;i<constraint_s.length;i++){

                try {
                    tempList.retainAll((List<SearchResult>) parallel_tasks[i].get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            filterResults.values = tempList;
            Log.i("Returning result ", "back");
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List)results.values);
            //Log.i("publishing results ",exampleList.get(0).toString());
            notifyDataSetChanged();
        }
    };
}
