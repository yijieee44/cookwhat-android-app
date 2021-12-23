package com.example.cookwhat.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.MarketIngredientAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.util.Log;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.UtensilModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView ingredientRecycleView;
    RecyclerView utensilRecycleView;

    String[] name = {"Ingredient 1", "Ingredient 2", "Ingredient 3"};
    String[] name1 = {"Utensil 1", "Utensil 2", "Utensil 3"};
    String[] description = {"Description 1","Description 2","Description 3"};
    String[] description1 = {"Description 1","Description 2","Description 3"};
    double[] quantity = {200, 100, 120};
    String[] unit = {"g", "g", "g"};

    List<IngredientModel> ingredientModelList;
    List<UtensilModel> utensilModelList;
    IngredientModel[] ingredientModels;
    UtensilModel[] utensilModels;
    Context context;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // add a back icon on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientModelList = new ArrayList<>();
        utensilModelList = new ArrayList<>();
        int size = name.length;
        int size1 = name1.length;
        ingredientModels = new IngredientModel[size];
        utensilModels = new UtensilModel[size];
        context = SearchActivity.this;
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager1 = new LinearLayoutManager(this);


        for (int i = 0; i < size; i++) {
            ingredientModels[i] = new IngredientModel();
            ingredientModels[i].setName(name[i]);
            ingredientModels[i].setQuantity(quantity[i]);
            ingredientModels[i].setMemo(description[i]);
            ingredientModels[i].setUnit(unit[i]);
            ingredientModelList.add(ingredientModels[i]);

            utensilModels[i] = new UtensilModel();
            utensilModels[i].setName(name1[i]);
            utensilModels[i].setMemo(description1[i]);
            utensilModelList.add(utensilModels[i]);
        }

        ingredientRecycleView = (RecyclerView) findViewById(R.id.activity_search_ingredient_list);
        ingredientRecycleView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientRecycleView.getContext(), linearLayoutManager.getOrientation());
        ingredientRecycleView.addItemDecoration(dividerItemDecoration);  //for divider

        ingredientRecycleView.setAdapter(new IngredientAdapter(context, ingredientModelList));


        utensilRecycleView = (RecyclerView) findViewById(R.id.activity_search_utensil_list);
        utensilRecycleView.setLayoutManager(linearLayoutManager1);
        utensilRecycleView.addItemDecoration(dividerItemDecoration);  //for divider

        utensilRecycleView.setAdapter(new UtensilAdapter(context, utensilModelList));
    }

    public void BtnIngredientsBottomSheet(View view) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_ingredients);
        int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;

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

        int[] ingredientsIcon = Constants.INGREDIENTS_ICON;
        int[] ingredientsName = Constants.INGREDIENTS_NAME;




        GridView ingredientsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Ingredients);
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, ingredientsName, ingredientsIcon);
        ingredientsGridView.setAdapter(marketIngredientAdapter);
//        ingredientsGridView.setMinimumHeight((int) (screenHeight * 0.75));

        EditText searchIngredients = (EditText) bottomSheetDialog.findViewById(R.id.EditTextSearchIngredient);
        searchIngredients.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void afterTextChanged(Editable s) {
                List<Integer> searchIcon = new ArrayList<>();
                List<Integer> searchName = new ArrayList<>();

                for(int i=0; i<ingredientsName.length; i++) {
                    if(getResources().getString(ingredientsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        searchIcon.add(ingredientsIcon[i]);
                        searchName.add(ingredientsName[i]);
                    }
                }


                int[] searchIconArray = searchIcon.stream().mapToInt(i -> i).toArray();
                int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, searchNameArray, searchIconArray);
                ingredientsGridView.setAdapter(marketIngredientAdapter);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        bottomSheetDialog.show();
    }
}