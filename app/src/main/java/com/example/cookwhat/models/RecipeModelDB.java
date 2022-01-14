package com.example.cookwhat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeModelDB implements Serializable {
    private String id;
    private String userId;
    private String title;
    private int numFav;
    private String createdTime;
    private ArrayList<RecipeStepModel> steps;
    private ArrayList<String> tags;
    private ArrayList<String> ingName;
    private ArrayList<Integer> ingIcon;
    private ArrayList<String> ingMemo;
    private ArrayList<Integer> ingQuantity;
    private ArrayList<String> ingUnitQuantity;
    private ArrayList<String> ingUnitWeight;
    private ArrayList<String> utName;
    private ArrayList<Integer> utIcon;
    private ArrayList<String> utMemo;
    private ArrayList<RecipeCommentModel> comments;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumFav() {
        return numFav;
    }

    public void setNumFav(int numFav) {
        this.numFav = numFav;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public ArrayList<RecipeStepModel> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RecipeStepModel> steps) {
        this.steps = steps;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getIngName() {
        return ingName;
    }

    public void setIngName(ArrayList<String> ingName) {
        this.ingName = ingName;
    }

    public ArrayList<Integer> getIngIcon() {
        return ingIcon;
    }

    public void setIngIcon(ArrayList<Integer> ingIcon) {
        this.ingIcon = ingIcon;
    }

    public ArrayList<String> getIngMemo() {
        return ingMemo;
    }

    public void setIngMemo(ArrayList<String> ingMemo) {
        this.ingMemo = ingMemo;
    }

    public ArrayList<Integer> getIngQuantity() {
        return ingQuantity;
    }

    public void setIngQuantity(ArrayList<Integer> ingQuantity) {
        this.ingQuantity = ingQuantity;
    }

    public ArrayList<String> getIngUnitQuantity() {
        return ingUnitQuantity;
    }

    public void setIngUnitQuantity(ArrayList<String> ingUnitQuantity) {
        this.ingUnitQuantity = ingUnitQuantity;
    }

    public ArrayList<String> getIngUnitWeight() {
        return ingUnitWeight;
    }

    public void setIngUnitWeight(ArrayList<String> ingUnitWeight) {
        this.ingUnitWeight = ingUnitWeight;
    }

    public ArrayList<String> getUtName() {
        return utName;
    }

    public void setUtName(ArrayList<String> utName) {
        this.utName = utName;
    }

    public ArrayList<Integer> getUtIcon() {
        return utIcon;
    }

    public void setUtIcon(ArrayList<Integer> utIcon) {
        this.utIcon = utIcon;
    }

    public ArrayList<String> getUtMemo() {
        return utMemo;
    }

    public void setUtMemo(ArrayList<String> utMemo) {
        this.utMemo = utMemo;
    }

    public ArrayList<RecipeCommentModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<RecipeCommentModel> comments) {
        this.comments = comments;
    }



}
