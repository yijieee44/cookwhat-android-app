package com.example.cookwhat.models;

import java.util.List;

public class RecipeModelSearch extends RecipeModelDB{
    List<Integer> nonMatchingIngredientIndex;
    List<Integer> nonMatchingUtensilIndex;

    public RecipeModelSearch(){

    }

    public RecipeModelSearch(RecipeModelDB recipeModelDB){
        this.setId(recipeModelDB.getId());
        this.setComments(recipeModelDB.getComments());
        this.setCreatedTime(recipeModelDB.getCreatedTime());
        this.setIngIcon(recipeModelDB.getIngIcon());
        this.setIngMemo(recipeModelDB.getIngMemo());
        this.setIngName(recipeModelDB.getIngName());
        this.setIngQuantity(recipeModelDB.getIngQuantity());
        this.setIngUnitQuantity(recipeModelDB.getIngUnitQuantity());
        this.setIngUnitWeight(recipeModelDB.getIngUnitWeight());
        this.setSteps(recipeModelDB.getSteps());
        this.setNumFav(recipeModelDB.getNumFav());
        this.setTags(recipeModelDB.getTags());
        this.setTitle(recipeModelDB.getTitle());
        this.setUtIcon(recipeModelDB.getUtIcon());
        this.setUtMemo(recipeModelDB.getUtMemo());
        this.setUtName(recipeModelDB.getUtName());
        this.setUserId(recipeModelDB.getUserId());
    }

    public List<Integer> getNonMatchingIngredientIndex() {
        return nonMatchingIngredientIndex;
    }

    public void setNonMatchingIngredientIndex(List<Integer> nonMatchingIngredientIndex) {
        this.nonMatchingIngredientIndex = nonMatchingIngredientIndex;
    }

    public List<Integer> getNonMatchingUtensilIndex() {
        return nonMatchingUtensilIndex;
    }

    public void setNonMatchingUtensilIndex(List<Integer> nonMatchingUtensilIndex) {
        this.nonMatchingUtensilIndex = nonMatchingUtensilIndex;
    }
}
