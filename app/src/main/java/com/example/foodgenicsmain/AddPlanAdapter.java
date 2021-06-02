package com.example.foodgenicsmain;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class AddPlanAdapter extends RecyclerView.Adapter<AddPlanAdapter.ViewHolder> implements Filterable {
    List<SearchResult> recipeList;
    Context context;
    private List<SearchResult> recipeListfull;
    SearchResult currentItem;
    EditText cal;
    TextView pro,carbs,fat;
    HashMap<SearchResult,Integer> added_resipes;


    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView  respi;
        ImageView p_l;
        ViewHolder(View itemView) {
            super(itemView);
            respi = itemView.findViewById(R.id.instruct);
            p_l = itemView.findViewById(R.id.img_resp);
        }
    }

    public AddPlanAdapter(List<SearchResult> recipeList, EditText cal, Context context) {
        Log.i("in here.......","in here");
        this.context = context;
        this.recipeList = recipeList;
        recipeListfull = new ArrayList<>(recipeList);
    }

    @NonNull
    @Override
    public AddPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new AddPlanAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddPlanAdapter.ViewHolder holder, int position) {
        currentItem = recipeList.get(position);
        holder.respi.setText(currentItem.getTitle());
        Picasso.with(context).load(currentItem.getPicture_link()).into(holder.p_l);
        added_resipes = new HashMap<>();

        holder.p_l.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                cal = ((Activity)context).findViewById(R.id.cur_cal);
                pro = ((Activity)context).findViewById(R.id.protien);
                carbs = ((Activity)context).findViewById(R.id.carbs);
                fat = ((Activity)context).findViewById(R.id.fat);
                System.out.println("calllll"+cal);
                cal.setText(Integer.parseInt(cal.getText().toString())+currentItem.getCalorie()+"");
                pro.setText(Integer.parseInt(pro.getText().toString())+currentItem.getProtien()+"");
                fat.setText(Integer.parseInt(fat.getText().toString())+currentItem.getFat()+"");
                carbs.setText(Integer.parseInt(carbs.getText().toString())+currentItem.getCarbs()+"");

                if(added_resipes.containsKey(currentItem.getRecipe_id())){
                    added_resipes.put(currentItem,added_resipes.get(currentItem.getRecipe_id())+1);
                }else{
                    added_resipes.put(currentItem,1);
                }
                try{
                    FileOutputStream fos = context.openFileOutput(FirebaseAuth.getInstance().getCurrentUser().getUid(), Context.MODE_PRIVATE);

                    fos.write(serialize(added_resipes));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context,"Calories added",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static byte[] serialize(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.encode(baos.toByteArray(),Base64.DEFAULT);
    }
}
