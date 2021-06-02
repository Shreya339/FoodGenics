package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DisplayRecipe extends AppCompatActivity {

    TextView r_name,instructions_tv,ingredients_tv,rating_text;
    ImageView picture_link_iv;
    RatingBar ratingBar;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    String recipe_id,disease ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        r_name = findViewById(R.id.resp_name);
        instructions_tv = findViewById(R.id.instructions_text_view);
        ingredients_tv = findViewById(R.id.ingredients_text_view);
        picture_link_iv = findViewById(R.id.picrture_resp);
        ratingBar = findViewById(R.id.ratingBar);
        rating_text = findViewById(R.id.ratingValue);

        final SearchResult currentItem = (SearchResult) getIntent().getSerializableExtra("Current Recipe Set");

        r_name.setText(currentItem.getTitle());

        FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                disease = snapshot.child("disease").getValue().toString();
                Log.i("disease in data ",disease);

                String ing = currentItem.getIngredients();
                String ins = currentItem.getInstructions();

                if(disease.equals("High Blood Pressure")){

                    ing = ing.replaceAll("salt","rock salt/ Himalayan salt");
                    ing = ing.replaceAll("oils","/lesser quantity of oils and fats");
                    ing = ing.replaceAll("sugar","jaggery");
                    ing = ing.replaceAll("peanuts","peanuts(lesser quantity)");

                    ins = ins.replaceAll("salt","rock salt/ Himalayan salt");
                    ins = ins.replaceAll("oils","/lesser quantity of oils and fats");
                    ins = ins.replaceAll("sugar","jaggery");
                    ins = ins.replaceAll("peanuts","peanuts(lesser quantity)");

                }

                Log.i("disease ",disease);

                if(disease.equals("Diabetes")){

                    ing = ing.replaceAll("sugar","Jaggery");
                    ing = ing.replaceAll("oils","/lesser quantity of oils and fats");
                    ing = ing.replaceAll("white flour","whole wheat");
                    ing = ing.replaceAll("milk","skimmed milk");
                    ing = ing.replaceAll("butter","butter(lesser quantity)");

                    ins = ins.replaceAll("sugar","Jaggery");
                    ins = ins.replaceAll("oils","/lesser quantity of oils and fats");
                    ins = ins.replaceAll("white flour","whole wheat");
                    ins = ins.replaceAll("milk","skimmed milk");
                    ins = ins.replaceAll("butter","butter(lesser quantity)");

                    Log.i("in diabetes ",ing);

                }

                Log.i("the edited string ",ing);
                ingredients_tv.setText(ing);
                instructions_tv.setText(ins);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Picasso.with(getApplicationContext()).load(currentItem.getPicture_link()).into(picture_link_iv);
        recipe_id = currentItem.getRecipe_id();


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, boolean fromUser) {
                rating_text.setText(String.valueOf(rating));
                ratingBar.setEnabled(false);
                ratingBar.setIsIndicator(true);

                Query query =  FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Rating").exists()){
                            if(snapshot.child("Rating").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + recipe_id).exists()){
                                String msg = "You have already rated this Recipe with " + snapshot.child("Rating").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + recipe_id).child("rating").getValue() + " rating. Do you want to change this rating to " + ratingBar.getRating();

                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayRecipe.this);
                                alertDialogBuilder.setTitle("Already rated recipe");
                                alertDialogBuilder.setMessage(msg);
                                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Rating").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+recipe_id).child("rating").setValue(rating);
                                        dialog.cancel();
                                    }
                                });
                                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialogBuilder.show();
                            }else{
                                RatingModelClass ratingModelClass = new RatingModelClass(recipe_id, FirebaseAuth.getInstance().getCurrentUser().getUid(), String.valueOf(rating));

                                FirebaseDatabase.getInstance("https://foodgenics-8973c-default-rtdb.firebaseio.com").getReference().child("Rating").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + recipe_id).setValue(ratingModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(DisplayRecipe.this, "Rating Recorded", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}