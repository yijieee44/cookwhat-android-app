package com.example.cookwhat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class RecipeModel {
    private String id = "";
    private String userId = "";
    private int num_fav = 0;
    private String title = "";
    private List<String> tags = new ArrayList<>();
    @JsonIgnore
    private List<IngredientModel> ingredients = new ArrayList<>();
    @JsonIgnore
    private List<UtensilModel> utensils = new ArrayList<>();
    @JsonIgnore
    private List<RecipeCommentModel> comments = new ArrayList<>();
    private List<RecipeStepModel> steps = new ArrayList<>();
    private String createdTime = "";

    public RecipeModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public RecipeModelDB toRecipeModelDB() {
        RecipeModelDB recipeModelDB = new RecipeModelDB();
        recipeModelDB.setId(this.getId());
        recipeModelDB.setUserId(this.getUserId());
        recipeModelDB.setTitle(this.getTitle());
        recipeModelDB.setNumFav(this.getNum_fav());
        recipeModelDB.setCreatedTime(this.getCreatedTime());
        recipeModelDB.setSteps(this.getSteps());
        recipeModelDB.setTags(this.getTags());
        recipeModelDB.setComments(this.getComments());

        List<String> ingName = new ArrayList<>();
        List<Integer> ingIcon = new ArrayList<>();
        List<String> ingMemo = new ArrayList<>();
        List<Double> ingQuantity = new ArrayList<>();
        List<Double> ingWeight = new ArrayList<>();
        List<String> ingUnitQuantity = new ArrayList<>();
        List<String> ingUnitWeight = new ArrayList<>();

        for(IngredientModel model : this.getIngredients()) {
            ingName.add(model.getName());
            ingIcon.add(model.getIcon());
            ingMemo.add(model.getMemo());
            ingQuantity.add(model.getQuantity());
            ingWeight.add(model.getWeight());
            ingUnitQuantity.add(model.getUnitQuantity());
            ingUnitWeight.add(model.getUnitWeight());
        }

        List<String> utName = new ArrayList<>();
        List<Integer> utIcon = new ArrayList<>();
        List<String> utMemo = new ArrayList<>();
        for(UtensilModel model : this.getUtensils()) {
            utName.add(model.getName());
            utIcon.add(model.getIcon());
            utMemo.add(model.getMemo());
        }

        recipeModelDB.setIngName(ingName);
        recipeModelDB.setIngIcon(ingIcon);
        recipeModelDB.setIngMemo(ingMemo);
        recipeModelDB.setIngQuantity(ingQuantity);
        recipeModelDB.setIngWeight(ingWeight);
        recipeModelDB.setIngUnitQuantity(ingUnitQuantity);
        recipeModelDB.setIngUnitWeight(ingUnitWeight);

        recipeModelDB.setUtName(utName);
        recipeModelDB.setUtIcon(utIcon);
        recipeModelDB.setUtMemo(utMemo);

        return recipeModelDB;
    }
}
