package com.example.cookwhat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.MarketIngredientAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView ingredientRecycleView;
    String[] name = {"Ingredient 1", "Ingredient 2", "Ingredient 3"};
    String[] description = {"Description 1","Description 2","Description 3"};
    double[] quantity = {200, 100, 120};
    String[] unit = {"g", "g", "g"};

    List<IngredientModel> ingredientModelList;
    IngredientModel[] ingredientModels;
    Context context;
    LinearLayoutManager linearLayoutManager;

    RecyclerView utensilRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // add a back icon on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientModelList = new ArrayList<>();
        int size = name.length;
        ingredientModels = new IngredientModel[size];
        context = SearchActivity.this;
        linearLayoutManager = new LinearLayoutManager(this);

        for (int i = 0; i < size; i++) {
            ingredientModels[i] = new IngredientModel();
            ingredientModels[i].setName(name[i]);
            ingredientModels[i].setQuantity(quantity[i]);
            ingredientModels[i].setMemo(description[i]);
            ingredientModels[i].setUnit(unit[i]);
            ingredientModelList.add(ingredientModels[i]);
        }

        ingredientRecycleView = (RecyclerView) findViewById(R.id.activity_search_ingredient_list);
        ingredientRecycleView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientRecycleView.getContext(), linearLayoutManager.getOrientation());
        ingredientRecycleView.addItemDecoration(dividerItemDecoration);  //for divider

        ingredientRecycleView.setAdapter(new IngredientAdapter(context, ingredientModelList));

//
//        utensilRecycleView = (RecyclerView) findViewById(R.id.activity_search_utensil_list);
//        utensilRecycleView.setLayoutManager(linearLayoutManager);
////        utensilRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
//
//        utensilRecycleView.setAdapter(new IngredientAdapter(context, ingredientModelList));
    }

    public void BtnIngredientsBottomSheet(View view) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_ingredients);

        try {
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setDraggable(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        int[] ingredients_icon = Constants.INGREDIENTS_ICON;
        int[] ingredients_name = Constants.INGREDIENTS_NAME;

        GridView ingredientsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Ingredients);
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, ingredients_name, ingredients_icon);
        ingredientsGridView.setAdapter(marketIngredientAdapter);

        bottomSheetDialog.show();
    }
}