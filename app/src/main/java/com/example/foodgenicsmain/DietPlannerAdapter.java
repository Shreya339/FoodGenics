package com.example.foodgenicsmain;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DietPlannerAdapter extends RecyclerView.Adapter<DietPlannerAdapter.ViewHolder> {

    private List<PlanCardModel> planList;
    PlanCardModel currentPlan;
    Context context;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    StringBuilder stringBuilder;
    String contents;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference();

    Calendar c = Calendar.getInstance();


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,endate;
        CardView planCard;
        Button del;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.plancardtitle);
            endate = itemView.findViewById(R.id.end_date);
            planCard= itemView.findViewById(R.id.planCard);
            del = itemView.findViewById(R.id.delete_btn);
        }
    }

    DietPlannerAdapter(List<PlanCardModel> planList, Context context){
        this.planList = planList;
        this.context = context;
    }


    @NonNull
    @Override
    public DietPlannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_card_layout, parent, false);
        return new DietPlannerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DietPlannerAdapter.ViewHolder holder, final int position) {
        currentPlan = planList.get(position);
        holder.title.setText(currentPlan.getCard_title());
        try{
            System.out.println(dateFormat.format(currentPlan.getCreated_date()));
            c.setTime(dateFormat.parse(dateFormat.format(currentPlan.getCreated_date())));
        }catch(Exception e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, currentPlan.getNum_of_days());
        String newDate = dateFormat.format(c.getTime());
        holder.endate.setText(newDate);


        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("UserPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_"+holder.title.getText().toString()+"_Plans").removeValue();
                planList.remove(planList.get(position));
            }
        });

        holder.planCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CalorieInfo.class);
                intent.putExtra("PlanCardModel",planList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

}
