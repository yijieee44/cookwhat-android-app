package com.example.cookwhat.models;

public class RecipeStepModel {
    private String image;
    private String step;

    public RecipeStepModel(){

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
