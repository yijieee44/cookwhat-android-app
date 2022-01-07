package com.example.cookwhat.models;

public class IngredientModel {
    private String recipeId;
    private String name;
    private Double quantity;
    private String unit;
    private String memo;
    private int icon;
    private boolean required;

    public String getRecipeId() { return recipeId; }

    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
