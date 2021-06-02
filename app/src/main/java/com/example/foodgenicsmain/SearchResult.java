package com.example.foodgenicsmain;

import java.io.Serializable;

public class SearchResult implements Serializable {

    String ingredients, title, instructions, picture_link,recipe_id;
    int cal,pro,fat,carbs;

    public SearchResult(){

    }

    public SearchResult( String ingredients, String instructions, String title, String picture_link,String recipe_id,int cal,int pro,int fat,int carbs){
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.title = title;
        this.picture_link = picture_link;
        this.recipe_id = recipe_id;
        this.cal= cal;
        this.pro = pro;
        this.fat = fat;
        this.carbs = carbs;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
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

    public int getCalorie() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public int getProtien() {
        return pro;
    }

    public void setProtien(int pro) {
        this.pro = pro;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
}
