package com.example.foodgenicsmain;

import android.util.Log;

public class SearchResult {

    String ingredients, title, instructions;

    public SearchResult(){

    }

    public SearchResult( String ingredients, String instructions, String title){
        this.ingredients = ingredients;
        this.ingredients = instructions;
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        Log.i("in search result",instructions+" ");
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
