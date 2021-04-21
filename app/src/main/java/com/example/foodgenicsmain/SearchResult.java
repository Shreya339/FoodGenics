package com.example.foodgenicsmain;

public class SearchResult {

    String ingredients, recipe_name, instructions;

    public SearchResult(){

    }

    public SearchResult(String ingredients, String recipe_name, String instructions){
        this.ingredients = ingredients;
        this.recipe_name = recipe_name;
        this.ingredients = instructions;
    }
    public SearchResult(String ingredients){
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
