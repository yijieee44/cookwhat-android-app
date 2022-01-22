package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.MarketIngredientAdapter;
import com.example.cookwhat.database.UserTableContract;
import com.example.cookwhat.fragments.CreateShowGalleryFragment;
import com.example.cookwhat.fragments.SearchIngredientFragment;
import com.example.cookwhat.fragments.SearchResultFragment;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.RecipeModelSearch;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.UtensilModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    List<IngredientModel> ingredientModelList;
    List<UtensilModel> utensilModelList;
    FirebaseFirestore firestoreDb;
    Button searchButton;

    List<UserModelDB> userList;
    List<RecipeModelSearch> recipeSearchResult;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        loadingDialog = new Dialog(this);
        firestoreDb = FirebaseFirestore.getInstance();
        fragmentManager = getSupportFragmentManager();
        ingredientModelList = new ArrayList<>();
        utensilModelList = new ArrayList<>();

        searchButton = findViewById(R.id.BtnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(IngredientModel ingredient: ingredientModelList){
                    Log.d("INGREDIENT::", ingredient.getName());
                }
                search();
            }
        });

    }

    private void search(){
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

        int width = (int)(getResources().getDisplayMetrics().widthPixels);
        int height = (int)(getResources().getDisplayMetrics().heightPixels);

        loadingDialog.getWindow().setLayout(width, height);
        loadingDialog.show();
        List<IngredientModel> ingredientModelToSearch = ingredientModelList;
        List<UtensilModel> utensilModelToSearch = utensilModelList;
        List<String> ingredientNameToSearch = new ArrayList<>();
        List<String> utensilNameToSearch = new ArrayList<>();
        List<RecipeModelDB> queriedRecipes = new ArrayList<>();
        for(IngredientModel ingredient: ingredientModelToSearch){
            ingredientNameToSearch.add(ingredient.getName());
        }
        for(UtensilModel utensil: utensilModelToSearch){
            utensilNameToSearch.add(utensil.getName());
        }
        if(ingredientNameToSearch.size() > 0){
            firestoreDb.collection("recipe")
                    .whereArrayContainsAny("ingName", ingredientNameToSearch)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                    final ObjectMapper mapper = new ObjectMapper();
                                    try{
                                        RecipeModelDB queriedRecipe = mapper.convertValue(document.getData(), RecipeModelDB.class);
                                        queriedRecipe.setId(document.getId());
                                        queriedRecipes.add(queriedRecipe);


                                    }
                                    catch (Exception e){

                                        Log.w("ERROR", e.toString());
                                    }
                                }
                                organizeAndNavigateToResult(queriedRecipes, ingredientNameToSearch, utensilNameToSearch);


                            } else {
                                loadingDialog.cancel();
                                Log.w("ERROR", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        else if(utensilNameToSearch.size() > 0){
            firestoreDb.collection("recipe")
                    .whereArrayContainsAny("utName", utensilNameToSearch)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                    final ObjectMapper mapper = new ObjectMapper();
                                    try{
                                        RecipeModelDB queriedRecipe = mapper.convertValue(document.getData(), RecipeModelDB.class);
                                        queriedRecipe.setId(document.getId());
                                        queriedRecipes.add(queriedRecipe);


                                    }
                                    catch (Exception e){

                                        Log.w("ERROR", e.toString());
                                    }
                                }
                                organizeAndNavigateToResult(queriedRecipes, ingredientNameToSearch, utensilNameToSearch);


                            } else {
                                loadingDialog.cancel();
                                Log.w("ERROR", "Error getting documents.", task.getException());
                                Toast.makeText(SearchActivity.this,
                                        "Fail to search, please try again",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
        else{
            loadingDialog.cancel();
            Toast.makeText(SearchActivity.this,
                    "Please select at least 1 ingredient or utensil to search.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void organizeAndNavigateToResult(List<RecipeModelDB> queriedRecipes, List<String> ingredientNameList, List<String> utensilNameList){
        List<RecipeModelSearch> recipeModelSearchList = new ArrayList<>();
        for(RecipeModelDB recipe: queriedRecipes){
            List<Integer> nonMatchingUtensilIndex = new ArrayList<>();
            List<Integer> nonMatchingIngredientIndex = new ArrayList<>();
            List<String> recipeIngredientNameList = recipe.getIngName();
            for(String ingName: recipeIngredientNameList){
                boolean searched = false;
                for(String searchName: ingredientNameList){
                    if(searchName.equals(ingName)){
                        searched = true;
                        break;
                    }
                }
                if(!searched){
                    nonMatchingIngredientIndex.add(recipeIngredientNameList.indexOf(ingName));
                }
            }
            List<String> recipeUtensilNameList = recipe.getUtName();
            for(String utName: recipeUtensilNameList){
                boolean searched = false;
                for(String searchName: utensilNameList){
                    if(searchName.equals(utName)){
                        searched = true;
                        break;
                    }
                }
                if(!searched){
                    nonMatchingUtensilIndex.add(recipeUtensilNameList.indexOf(utName));
                }
            }

            RecipeModelSearch recipeModelSearch = new RecipeModelSearch(recipe);
            recipeModelSearch.setNonMatchingIngredientIndex(nonMatchingIngredientIndex);
            recipeModelSearch.setNonMatchingUtensilIndex(nonMatchingUtensilIndex);
            recipeModelSearchList.add(recipeModelSearch);
        }
        Collections.sort(recipeModelSearchList, new Comparator<RecipeModelSearch>() {
            @Override
            public int compare(RecipeModelSearch o1, RecipeModelSearch o2) {
                int o1MatchingSize = o1.getIngName().size() + o1.getUtName().size() - o1.getNonMatchingIngredientIndex().size() - o1.getNonMatchingUtensilIndex().size();
                int o2MatchingSize = o2.getIngName().size() + o2.getUtName().size() - o2.getNonMatchingIngredientIndex().size() - o2.getNonMatchingUtensilIndex().size();
                if(o1MatchingSize == o2MatchingSize){
                    return 0;
                }
                else if(o1MatchingSize < o2MatchingSize){
                    return 1;
                }
                else{
                    return -1;
                }
            }
        });

        List<String> userIds = new ArrayList<>();
        if(recipeModelSearchList.size() <=0){
            userList = new ArrayList<>();
            recipeSearchResult = new ArrayList<>();

            loadingDialog.dismiss();
            toResult();
        }
        else{
            for(RecipeModelSearch searchModel: recipeModelSearchList){
                Log.d("SEQUENCE::", searchModel.getTitle());
                userIds.add(searchModel.getUserId());
            }
            List<UserModelDB> tempUserModelArrayList = new ArrayList<>();
            List<UserModelDB> userModelList = new ArrayList<>();
            firestoreDb.collection("user")
                    .whereIn("userId", userIds)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                    final ObjectMapper mapper = new ObjectMapper();
                                    tempUserModelArrayList.add(mapper.convertValue(document.getData(), UserModelDB.class));
                                }
                                for (String userId: userIds){
                                    for (UserModelDB userModel: tempUserModelArrayList){
                                        String userId2 = userModel.getUserId();
                                        if (userId.equals(userId2)){
                                            userModelList.add(userModel);
                                            break;
                                        }
                                    }
                                }
                                userList = userModelList;
                                recipeSearchResult = recipeModelSearchList;
                                loadingDialog.dismiss();
                                toResult();

                            } else {
                                loadingDialog.cancel();
                                Log.w("ERROR", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

    }

    private void toResult(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_search_fragment_container, new SearchResultFragment());
        fragmentTransaction.addToBackStack("stack");
        fragmentTransaction.commit();

        searchButton.setVisibility(View.GONE);

    }

    public List<IngredientModel> getIngredientModelList() {
        return ingredientModelList;
    }

    public void setIngredientModelList(List<IngredientModel> ingredientModelList) {
        this.ingredientModelList = ingredientModelList;
    }

    public List<UtensilModel> getUtensilModelList() {
        return utensilModelList;
    }

    public void setUtensilModelList(List<UtensilModel> utensilModelList) {
        this.utensilModelList = utensilModelList;
    }

    public List<UserModelDB> getUserList() {
        return userList;
    }

    public void setUserList(List<UserModelDB> userList) {
        this.userList = userList;
    }

    public List<RecipeModelSearch> getRecipeSearchResult() {
        return recipeSearchResult;
    }

    public void setRecipeSearchResult(List<RecipeModelSearch> recipeSearchResult) {
        this.recipeSearchResult = recipeSearchResult;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("FRAGMENTSTACK", ""+getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ) {
            getSupportFragmentManager().popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_search_fragment_container, new SearchIngredientFragment());
            fragmentTransaction.commit();

            searchButton.setVisibility(View.VISIBLE);
        }
        else {
            Intent intent = new Intent();
            setResult(122, intent);
            finish();
        }
    }
}