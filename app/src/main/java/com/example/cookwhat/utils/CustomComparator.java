package com.example.cookwhat.utils;

import com.example.cookwhat.models.RecipeStepModel;

import java.util.Comparator;

public class CustomComparator implements Comparator<RecipeStepModel> {
    @Override
    public int compare(RecipeStepModel o1, RecipeStepModel o2) {
        return String.valueOf(o1.getSequence()).compareTo(String.valueOf(o2.getSequence()));
    }
}
