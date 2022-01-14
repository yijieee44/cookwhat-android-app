package com.example.cookwhat.models;

import java.io.Serializable;
import java.util.List;

public class RecipeModelDB implements Serializable {
    private String id;
    private String userId;
    private String title;
    private int numFav;
    private String createdTime;
    private List<RecipeStepModel> steps;
    private List<String> tags;
    private List<String> ingName;
    private List<Integer> ingIcon;
    private List<String> ingMemo;
    private List<Integer> ingQuantity;
    private List<String> ingUnitQuantity;
    private List<String> ingUnitWeight;
    private List<String> utName;
    private List<Integer> utIcon;
    private List<String> utMemo;
    private List<RecipeCommentModel> comments;

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

    public List<RecipeStepModel> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStepModel> steps) {
        this.steps = steps;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getIngName() {
        return ingName;
    }

    public void setIngName(List<String> ingName) {
        this.ingName = ingName;
    }

    public List<Integer> getIngIcon() {
        return ingIcon;
    }

    public void setIngIcon(List<Integer> ingIcon) {
        this.ingIcon = ingIcon;
    }

    public List<String> getIngMemo() {
        return ingMemo;
    }

    public void setIngMemo(List<String> ingMemo) {
        this.ingMemo = ingMemo;
    }

    public List<Integer> getIngQuantity() {
        return ingQuantity;
    }

    public void setIngQuantity(List<Integer> ingQuantity) {
        this.ingQuantity = ingQuantity;
    }

    public List<String> getIngUnitQuantity() {
        return ingUnitQuantity;
    }

    public void setIngUnitQuantity(List<String> ingUnitQuantity) {
        this.ingUnitQuantity = ingUnitQuantity;
    }

    public List<String> getIngUnitWeight() {
        return ingUnitWeight;
    }

    public void setIngUnitWeight(List<String> ingUnitWeight) {
        this.ingUnitWeight = ingUnitWeight;
    }

    public List<String> getUtName() {
        return utName;
    }

    public void setUtName(List<String> utName) {
        this.utName = utName;
    }

    public List<Integer> getUtIcon() {
        return utIcon;
    }

    public void setUtIcon(List<Integer> utIcon) {
        this.utIcon = utIcon;
    }

    public List<String> getUtMemo() {
        return utMemo;
    }

    public void setUtMemo(List<String> utMemo) {
        this.utMemo = utMemo;
    }

    public List<RecipeCommentModel> getComments() {
        return comments;
    }

    public void setComments(List<RecipeCommentModel> comments) {
        this.comments = comments;
    }



}
