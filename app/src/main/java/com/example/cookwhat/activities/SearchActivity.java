package com.example.cookwhat.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.MarketIngredientAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.widget.LinearLayout;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.UtensilModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {
    RecyclerView ingredientRecycleView;
    RecyclerView utensilRecycleView;


    List<IngredientModel> ingredientModelList;
    List<UtensilModel> utensilModelList;
    IngredientModel[] ingredientModels;
    UtensilModel[] utensilModels;
    Context context;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager1;
    EditText searchIngredients;

    EditText searchUtensils;
    BottomSheetDialog bottomSheetDialog;

    List<Integer> selIngredientsItem = new ArrayList<Integer>();
    List<Integer> selUtensilsItem = new ArrayList<Integer>();
    List<Integer> displayIngredientItem = new ArrayList<Integer>();
    List<Integer> displayUtensilItem = new ArrayList<Integer>();
    List<String> selCustomIngredients = new ArrayList<String>();
    List<String> selCustomUtensils = new ArrayList<String>();

    IngredientAdapter ingredientAdapter;
    UtensilAdapter utensilAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        int[] ingredientsIcon = Constants.INGREDIENTS_ICON;
        int[] ingredientsName = Constants.INGREDIENTS_NAME;

        // add a back icon on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientModelList = new ArrayList<>();
        utensilModelList = new ArrayList<>();
        int size = selIngredientsItem.size();
        int sizeCustom = selCustomIngredients.size();
        ingredientModels = new IngredientModel[size + sizeCustom];
        utensilModels = new UtensilModel[size];
        context = SearchActivity.this;
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager1 = new LinearLayoutManager(this);


        for (int i = 0; i < size; i++) {
            ingredientModels[i] = new IngredientModel();
            ingredientModels[i].setName(getString(ingredientsName[selIngredientsItem.get(i)]));
            ingredientModels[i].setQuantity(Double.valueOf(1));
            ingredientModels[i].setMemo("Description");
            ingredientModels[i].setIcon(ingredientsIcon[selIngredientsItem.get(i)]);
            ingredientModelList.add(ingredientModels[i]);

//            utensilModels[i] = new UtensilModel();
//            utensilModels[i].setName(name1[i]);
//            utensilModels[i].setMemo(description1[i]);
//            utensilModelList.add(utensilModels[i]);
        }

        ingredientRecycleView = (RecyclerView) findViewById(R.id.activity_search_ingredient_list);
        ingredientRecycleView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientRecycleView.getContext(), linearLayoutManager.getOrientation());
        ingredientRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        ingredientRecycleView.setNestedScrollingEnabled(false);
        ingredientAdapter = new IngredientAdapter(context, ingredientModelList);
        ingredientRecycleView.setAdapter(ingredientAdapter);


        utensilRecycleView = (RecyclerView) findViewById(R.id.activity_search_utensil_list);
        utensilRecycleView.setLayoutManager(linearLayoutManager1);
        utensilRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        utensilRecycleView.setNestedScrollingEnabled(false);
        utensilAdapter = new UtensilAdapter(context, utensilModelList);
        utensilRecycleView.setAdapter(utensilAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BtnIngredientsBottomSheet(View view) {

        bottomSheetDialog = new BottomSheetDialog(this);
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

        int[] ingredientsIcon = Constants.INGREDIENTS_ICON;
        int[] ingredientsName = Constants.INGREDIENTS_NAME;

        displayIngredientItem = Arrays.stream(ingredientsIcon).boxed().collect(Collectors.toList());

        GridView ingredientsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Ingredients);
        LinearLayout ingredientLayoutCantFind = (LinearLayout) bottomSheetDialog.findViewById(R.id.LayoutIngCantFind);
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, ingredientsName, ingredientsIcon) {
             @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                 if (selIngredientsItem.contains(displayIngredientItem.get(position))) {
                     color = getResources().getColor(R.color.light_yellow);
                 }

                view.setBackgroundColor(color);

                return view;
             }
        };

        ingredientsGridView.setAdapter(marketIngredientAdapter);

        ingredientsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View viewPrev;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (selIngredientsItem.contains(displayIngredientItem.get(position))) {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        selIngredientsItem.remove(Integer.valueOf(displayIngredientItem.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(ingredientsName[position]));
                        ingredientAdapter.removeIngredient(ingredientModel);
                        ingredientAdapter.notifyDataSetChanged();
                    } else {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selIngredientsItem.add(displayIngredientItem.get(position));
                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(ingredientsName[position]));
                        ingredientModel.setIcon(ingredientsIcon[position]);
                        ingredientAdapter.addIngredient(ingredientModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        searchIngredients = (EditText) bottomSheetDialog.findViewById(R.id.EditTextSearchIngredient);
        searchIngredients.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void afterTextChanged(Editable s) {
                displayIngredientItem.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<ingredientsName.length; i++) {
                    if(getResources().getString(ingredientsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayIngredientItem.add(ingredientsIcon[i]);
                        searchName.add(ingredientsName[i]);

                        if (selIngredientsItem.contains(displayIngredientItem.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayIngredientItem.size() > 0) {
                    ingredientsGridView.setVisibility(View.VISIBLE);
                    ingredientLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayIngredientItem.stream().mapToInt(i -> i).toArray();
                    int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                    MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, searchNameArray, searchIconArray) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            int color = Color.TRANSPARENT; // Transparent
                            if (selectedIndex.contains(position)) {
                                color = getResources().getColor(R.color.light_yellow); // Opaque Blue
                            }

                            view.setBackgroundColor(color);

                            return view;
                        }
                    };


                    ingredientsGridView.setAdapter(marketIngredientAdapter);
                } else {
                    ingredientsGridView.setVisibility(View.GONE);
                    ingredientLayoutCantFind.setVisibility(View.VISIBLE);
                    Button btnCustomIngredient = (Button) ingredientLayoutCantFind.findViewById(R.id.BtnCustomIngredients);
                    btnCustomIngredient.setText(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomIngredient);

        for (int i=0; i<selCustomIngredients.size(); i++) {
            String chipItem = selCustomIngredients.get(i);
            Chip chip = new Chip(SearchActivity.this);
            chip.setText(chipItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(chipItem);
                    IngredientModel ingredientModel = new IngredientModel();
                    ingredientModel.setName(chipItem);
                    ingredientAdapter.removeIngredient(ingredientModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }


        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BtnUtensilsBottomSheet(View view) {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_utensils);

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

        int[] utensilsIcon = Constants.UTENSILS_ICON;
        int[] utensilsName = Constants.UTENSILS_NAME;

        displayUtensilItem = Arrays.stream(utensilsIcon).boxed().collect(Collectors.toList());

        GridView utensilsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Utensils);
        LinearLayout utensilLayoutCantFind = (LinearLayout) bottomSheetDialog.findViewById(R.id.LayoutUntCantFind);
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, utensilsName, utensilsIcon) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selUtensilsItem.contains(displayUtensilItem.get(position))) {
                    color = getResources().getColor(R.color.light_yellow);
                }

                view.setBackgroundColor(color);

                return view;
            }
        };

        utensilsGridView.setAdapter(marketIngredientAdapter);

        utensilsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View viewPrev;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (selUtensilsItem.contains(displayUtensilItem.get(position))) {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        selUtensilsItem.remove(Integer.valueOf(displayUtensilItem.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(utensilsName[position]));
                        utensilAdapter.removeUtensil(utensilModel);

                    } else {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selUtensilsItem.add(displayUtensilItem.get(position));
                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(utensilsName[position]));
                        utensilModel.setIcon(utensilsIcon[position]);
                        utensilAdapter.addUtensil(utensilModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        searchUtensils = (EditText) bottomSheetDialog.findViewById(R.id.EditTextSearchUtensil);
        searchUtensils.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void afterTextChanged(Editable s) {
                displayUtensilItem.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<utensilsName.length; i++) {
                    if(getResources().getString(utensilsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayUtensilItem.add(utensilsIcon[i]);
                        searchName.add(utensilsName[i]);

                        if (selUtensilsItem.contains(displayUtensilItem.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayUtensilItem.size() > 0) {
                    utensilsGridView.setVisibility(View.VISIBLE);
                    utensilLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayUtensilItem.stream().mapToInt(i -> i).toArray();
                    int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                    MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(SearchActivity.this, searchNameArray, searchIconArray) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            int color = Color.TRANSPARENT; // Transparent
                            if (selectedIndex.contains(position)) {
                                color = getResources().getColor(R.color.light_yellow); // Opaque Blue
                            }

                            view.setBackgroundColor(color);

                            return view;
                        }
                    };


                    utensilsGridView.setAdapter(marketIngredientAdapter);
                } else {
                    utensilsGridView.setVisibility(View.GONE);
                    utensilLayoutCantFind.setVisibility(View.VISIBLE);
                    Button btnCustomUtensil = (Button) utensilLayoutCantFind.findViewById(R.id.BtnCustomUtensils);
                    btnCustomUtensil.setText(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomUtensil);

        for (int i=0; i<selCustomUtensils.size(); i++) {
            String chipItem = selCustomUtensils.get(i);
            Chip chip = new Chip(SearchActivity.this);
            chip.setText(chipItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomUtensils.remove(chipItem);
                    UtensilModel utensilModel = new UtensilModel();
                    utensilModel.setName(chipItem);
                    utensilAdapter.removeUtensil(utensilModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }


        bottomSheetDialog.show();
    }

    public void BtnAddCustom(View view) {
        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomIngredient);
        String newItem = searchIngredients.getText().toString();

        if (!selCustomIngredients.contains(newItem)) {
            Chip chip = new Chip(this);
            chip.setText(newItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(newItem);
                    IngredientModel ingredientModel = new IngredientModel();
                    ingredientModel.setName(newItem);
                    ingredientAdapter.removeIngredient(ingredientModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomIngredients.add(newItem);
            IngredientModel ingredientModel = new IngredientModel();
            ingredientModel.setName(newItem);
            ingredientModel.setIcon(R.drawable.i0067_others);
            ingredientAdapter.addIngredient(ingredientModel);
        }
    }

    public void BtnAddCustomUtensil(View view) {
        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomUtensil);
        String newItem = searchUtensils.getText().toString();

        if (!selCustomUtensils.contains(newItem)) {
            Chip chip = new Chip(this);
            chip.setText(newItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomUtensils.remove(newItem);
                    UtensilModel utensilModel = new UtensilModel();
                    utensilModel.setName(newItem);
                    utensilAdapter.removeUtensil(utensilModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomUtensils.add(newItem);
            UtensilModel utensilModel = new UtensilModel();
            utensilModel.setName(newItem);
            utensilModel.setIcon(R.drawable.i0067_others);
            utensilAdapter.addUtensil(utensilModel);
        }
    }
}