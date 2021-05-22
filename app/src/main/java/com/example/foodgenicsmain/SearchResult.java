package com.example.foodgenicsmain;

import java.io.Serializable;

public class SearchResult implements Serializable {

    String ingredients, title, instructions, picture_link;

    public SearchResult(){

    }

    public SearchResult( String ingredients, String instructions, String title, String picture_link){
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.title = title;
        this.picture_link = picture_link;
    }

    public String getPicture_link() {
        return picture_link;
    }

    public void setPicture_link(String picture_link) {
        this.picture_link = picture_link;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
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
