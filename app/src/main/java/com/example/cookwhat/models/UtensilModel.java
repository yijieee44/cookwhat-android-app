package com.example.cookwhat.models;

import java.io.Serializable;

public class UtensilModel implements Serializable {
    private String recipeId;
    private String name;
    private String memo;
    private int icon;

    public String getRecipeId() { return recipeId; }

    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
