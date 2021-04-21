package com.example.foodgenicsmain;

import android.util.Log;

public class SearchResult {

    String ingredients, title, instructions;

    public SearchResult(){

    }

    public SearchResult( String ingredients, String instructions, String title){
        this.ingredients = ingredients;
        this.title = title;
        this.ingredients = instructions;
    }
    public SearchResult(String ingredients){
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getRecipe_name() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipe_name(String title) {
        Log.i("in search result",title+" ");
        this.title = title;
    }

    public void setInstructions(String instructions) {
        Log.i("in search result",instructions+" ");
        this.instructions = instructions;
    }
}
