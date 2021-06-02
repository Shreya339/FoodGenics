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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddRecipeAdapter extends RecyclerView.Adapter<AddRecipeAdapter.ViewHolder> implements Filterable {

    List<SearchResult> recipeList;
    Context context;
    private List<SearchResult> recipeListfull;
    SearchResult currentItem;
    EditText cal;
    TextView pro,carbs,fat,c;
    HashMap<SearchResult,Integer> added_resipes;
    ProgressBar calP;
    int tot_cal;
    DisplayAddedRecipeList displayAddedRecipeList;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView  respi;
        ImageView p_l;
        ViewHolder(View itemView) {
            super(itemView);
            respi = itemView.findViewById(R.id.instruct);
            p_l = itemView.findViewById(R.id.img_resp);
        }
    }

    AddRecipeAdapter(List<SearchResult> recipeList,int tot_cal ,Context context){
        this.context = context;
        this.recipeList = recipeList;
        recipeListfull = new ArrayList<>(recipeList);
        this.tot_cal = tot_cal;
    }


    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    @NonNull
    @Override
    public AddRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new AddRecipeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddRecipeAdapter.ViewHolder holder, int position) {
        currentItem = recipeList.get(position);
        holder.respi.setText(currentItem.getTitle());
        Picasso.with(context).load(currentItem.getPicture_link()).into(holder.p_l);
        //added_resipes = new HashMap<>();

        holder.p_l.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                cal = ((Activity)context).findViewById(R.id.edittext_cal);
                pro = ((Activity)context).findViewById(R.id.prot_text);
                carbs = ((Activity)context).findViewById(R.id.carb_text);
                fat = ((Activity)context).findViewById(R.id.fatt_text);
                c = ((Activity)context).findViewById(R.id.cal_text);
                calP = ((Activity)context).findViewById(R.id.cal_progress);

                currentItem.setCal(Integer.parseInt(cal.getText().toString())+currentItem.getCalorie());
                currentItem.setCarbs(Integer.parseInt(carbs.getText().toString())+currentItem.getCarbs());
                currentItem.setFat(Integer.parseInt(fat.getText().toString())+currentItem.getFat());
                currentItem.setProtien(Integer.parseInt(pro.getText().toString())+currentItem.getProtien());

                System.out.println("calllll"+cal);
                cal.setText(currentItem.getCalorie()+"");
                c.setText(currentItem.getCalorie()+"");
                System.out.println(pro.getText().toString()+" "+currentItem.getProtien());
                pro.setText(currentItem.getProtien()+"");
                fat.setText(currentItem.getFat()+"");
                carbs.setText(currentItem.getCarbs()+"");

                double temp = Integer.parseInt(cal.getText().toString());
                calP.setProgress((int)Math.round((temp/tot_cal)*100));

                DatabaseReference fb = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("UserPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_"+currentItem.getTitle()+"_Plans");
                fb.child("cur_cal").setValue(currentItem.getCalorie());
                fb.child("cur_carbs").setValue(currentItem.getCarbs());
                fb.child("cur_protien").setValue(currentItem.getProtien());
                fb.child("cur_fat").setValue(currentItem.getFat());

                try {
                    File f = new File(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        FileInputStream fis = context.openFileInput(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        InputStreamReader inputStreamReader =
                                new InputStreamReader(fis, StandardCharsets.UTF_8);
                        StringBuilder stringBuilder = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                            String line = reader.readLine();
                            while (line != null) {
                                stringBuilder.append(line).append('\n');
                                line = reader.readLine();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                            String contents = stringBuilder.toString();
                        System.out.println(contents);
                            added_resipes = (HashMap<SearchResult, Integer>) deserialize(contents);


                    } catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }


                if(added_resipes.containsKey(currentItem)){
                    added_resipes.put(currentItem,added_resipes.get(currentItem)+1);

                }else{
                    added_resipes.put(currentItem,1);
                }


                try{
                    FileOutputStream fos = context.openFileOutput(FirebaseAuth.getInstance().getCurrentUser().getUid(), Context.MODE_PRIVATE);

                    fos.write(serialize(added_resipes));
                    Toast.makeText(context,"Calories added",Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Object deserialize(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64.decode(s,Base64.DEFAULT);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}
