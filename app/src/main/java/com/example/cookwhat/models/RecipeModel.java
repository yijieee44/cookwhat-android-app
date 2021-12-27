package com.example.cookwhat.models;

import java.util.ArrayList;
import java.util.List;

public class RecipeModel {
    int id = 0;
    int userId = 0;
    int num_fav = 0;
    String title = "";
    List<String> tags = new ArrayList<>();
    List<IngredientModel> ingredients = new ArrayList<>();
    List<UtensilModel> utensils = new ArrayList<>();
    List<RecipeCommentModel> comments = new ArrayList<>();
    List<RecipeStepModel> steps = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNum_fav() {
        return num_fav;
    }

    public void setNum_fav(int num_fav) {
        this.num_fav = num_fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public List<UtensilModel> getUtensils() {
        return utensils;
    }

    public void setUtensils(List<UtensilModel> utensils) {
        this.utensils = utensils;
    }

    public List<RecipeCommentModel> getComments() {
        return comments;
    }

    public void setComments(List<RecipeCommentModel> comments) {
        this.comments = comments;
    }

    public List<RecipeStepModel> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStepModel> steps) {
        this.steps = steps;
    }
}
