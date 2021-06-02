package com.example.foodgenicsmain;

public class RatingModelClass {
    String recipe_id,user_id,rating;

    public RatingModelClass(String recipe_id, String user_id, String rating) {
        this.recipe_id = recipe_id;
        this.user_id = user_id;
        this.rating = rating;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
