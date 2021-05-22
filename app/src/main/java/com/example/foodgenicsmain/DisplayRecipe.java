package com.example.foodgenicsmain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayRecipe extends AppCompatActivity {

    TextView r_name,instructions_tv,ingredients_tv,rating_text;
    ImageView picture_link_iv;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        r_name = findViewById(R.id.resp_name);
        instructions_tv = findViewById(R.id.instructions_text_view);
        ingredients_tv= findViewById(R.id.ingredients_text_view);
        picture_link_iv = findViewById(R.id.picrture_resp);
        ratingBar = findViewById(R.id.ratingBar);
        rating_text = findViewById(R.id.ratingValue);

        SearchResult currentItem = (SearchResult) getIntent().getSerializableExtra("Current Recipe Set");

        r_name.setText(currentItem.getTitle());
        instructions_tv.setText(currentItem.getInstructions());
        ingredients_tv.setText(currentItem.getIngredients());
        Picasso.with(getApplicationContext()).load(currentItem.getPicture_link()).into(picture_link_iv);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_text.setText(String.valueOf(rating));
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