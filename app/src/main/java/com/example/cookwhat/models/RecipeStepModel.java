package com.example.cookwhat.models;

public class RecipeStepModel {
    private String image;
    private String step;
    private int sequence;
    private String recipeId;


    public RecipeStepModel(){

    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }


    public RecipeStepModel(String image, String step) {
        this.image = image;
        this.step = step;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
