package com.example.cookwhat.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.SearchActivity;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.MarketIngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.RecipeModelSearch;
import com.example.cookwhat.models.UtensilModel;
import com.example.cookwhat.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchIngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchIngredientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView ingredientRecycleView;
    RecyclerView utensilRecycleView;

    int[] INGREDIENTS_ICON = Constants.INGREDIENTS_ICON;
    int[] INGREDIENTS_NAME = Constants.INGREDIENTS_NAME;
    int[] UTENSILS_ICON = Constants.UTENSILS_ICON;
    int[] UTENSIL_NAME = Constants.UTENSILS_NAME;

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
    TextView noIngredient;
    TextView noUtensil;

    List<Integer> selIngredientsItem = new ArrayList<Integer>();
    List<Integer> selUtensilsItem = new ArrayList<Integer>();
    List<Integer> displayIngredientIcon = new ArrayList<Integer>();
    List<Integer> displayIngredientName = new ArrayList<Integer>();
    List<Integer> displayUtensilIcon = new ArrayList<Integer>();
    List<Integer> displayUtensilName = new ArrayList<Integer>();
    List<String> selCustomIngredients = new ArrayList<String>();
    List<String> selCustomUtensils = new ArrayList<String>();

    IngredientAdapter ingredientAdapter;
    UtensilAdapter utensilAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchIngredientFragment() {
        // Required empty public constructor
    }

    public static SearchIngredientFragment newInstance(String param1, String param2) {
        SearchIngredientFragment fragment = new SearchIngredientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_ingredient, container, false);



        Button selectIngredient = (Button) view.findViewById(R.id.BtnSearchIngredient);
        selectIngredient.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                BtnIngredientsBottomSheet(view);
            }
        });

        Button selectUtensil = (Button) view.findViewById(R.id.BtnSearchUtensil);
        selectUtensil.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                BtnUtensilsBottomSheet(view);
            }
        });

        noIngredient = view.findViewById(R.id.TVNoIngredient);
        noUtensil = view.findViewById(R.id.TVNoUtensil);

        int[] ingredientsIcon = Constants.INGREDIENTS_ICON;
        int[] ingredientsName = Constants.INGREDIENTS_NAME;


        ingredientModelList = ((SearchActivity)getActivity()).getIngredientModelList();
        utensilModelList = ((SearchActivity)getActivity()).getUtensilModelList();

        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager1 = new LinearLayoutManager(view.getContext());



        ingredientRecycleView = (RecyclerView) view.findViewById(R.id.activity_search_ingredient_list);
        ingredientRecycleView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientRecycleView.getContext(), linearLayoutManager.getOrientation());
        ingredientRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        ingredientRecycleView.setNestedScrollingEnabled(false);
        ingredientAdapter = new IngredientAdapter(context, ingredientModelList);
        ingredientRecycleView.setAdapter(ingredientAdapter);


        utensilRecycleView = (RecyclerView) view.findViewById(R.id.activity_search_utensil_list);
        utensilRecycleView.setLayoutManager(linearLayoutManager1);
        utensilRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        utensilRecycleView.setNestedScrollingEnabled(false);
        utensilAdapter = new UtensilAdapter(context, utensilModelList);
        utensilRecycleView.setAdapter(utensilAdapter);

        for (IngredientModel model : ingredientModelList) {
            if(model.getIcon() == -1) {
                //custom
                selCustomIngredients.add(model.getName());
            } else {
                selIngredientsItem.add(INGREDIENTS_ICON[model.getIcon()]);
            }
        }

        for (UtensilModel model : utensilModelList) {
            if(model.getIcon() == -1) {
                selCustomUtensils.add(model.getName());
            } else {
                selUtensilsItem.add(UTENSILS_ICON[model.getIcon()]);
            }
        }

        if(ingredientModelList.size()<=0){
            noIngredient.setVisibility(View.VISIBLE);
        }
        else{
            noIngredient.setVisibility(View.GONE);
        }

        if(utensilModelList.size()<=0){
            noUtensil.setVisibility(View.VISIBLE);
        }
        else{
            noUtensil.setVisibility(View.GONE);
        }


        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BtnIngredientsBottomSheet(View view) {

        bottomSheetDialog = new BottomSheetDialog(view.getContext());
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

        displayIngredientIcon = Arrays.stream(ingredientsIcon).boxed().collect(Collectors.toList());
        displayIngredientName = Arrays.stream(ingredientsName).boxed().collect(Collectors.toList());

        GridView ingredientsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Ingredients);
        LinearLayout ingredientLayoutCantFind = (LinearLayout) bottomSheetDialog.findViewById(R.id.LayoutIngCantFind);
        Button customIngredientButton = bottomSheetDialog.findViewById(R.id.BtnCustomIngredients);
        customIngredientButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        BtnAddCustom(view);
                    }
                }
        );
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), ingredientsName, ingredientsIcon) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selIngredientsItem.contains(displayIngredientIcon.get(position))) {
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
                    if (selIngredientsItem.contains(displayIngredientIcon.get(position))) {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        selIngredientsItem.remove(Integer.valueOf(displayIngredientIcon.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(displayIngredientName.get(position)));
                        ingredientModel.setIcon(getIndex(INGREDIENTS_ICON, displayIngredientIcon.get(position)));
                        ingredientAdapter.removeIngredient(ingredientModel);
                        ingredientAdapter.notifyDataSetChanged();
                        ingredientModelList = ingredientAdapter.getIngredientList();
                        if(ingredientModelList.size()<=0){
                            noIngredient.setVisibility(View.VISIBLE);
                        }
                        else{
                            noIngredient.setVisibility(View.GONE);
                        }
                    } else {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selIngredientsItem.add(displayIngredientIcon.get(position));
                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(displayIngredientName.get(position)));
                        ingredientModel.setIcon(getIndex(INGREDIENTS_ICON, displayIngredientIcon.get(position)));
                        ingredientAdapter.addIngredient(ingredientModel);
                        ingredientModelList = ingredientAdapter.getIngredientList();
                        if(ingredientModelList.size()<=0){
                            noIngredient.setVisibility(View.VISIBLE);
                        }
                        else{
                            noIngredient.setVisibility(View.GONE);
                        }
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
                displayIngredientIcon.clear();
                displayIngredientName.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<ingredientsName.length; i++) {
                    if(getResources().getString(ingredientsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayIngredientIcon.add(ingredientsIcon[i]);
                        displayIngredientName.add(ingredientsName[i]);
                        searchName.add(ingredientsName[i]);

                        if (selIngredientsItem.contains(displayIngredientIcon.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayIngredientIcon.size() > 0) {
                    ingredientsGridView.setVisibility(View.VISIBLE);
                    ingredientLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayIngredientIcon.stream().mapToInt(i -> i).toArray();
                    int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                    MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), searchNameArray, searchIconArray) {
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
            Chip chip = new Chip(view.getContext());
            chip.setText(chipItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setCloseIconTintResource(R.color.black);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(chipItem);
                    IngredientModel ingredientModel = new IngredientModel();
                    ingredientModel.setName(chipItem);
                    ingredientModel.setIcon(-1);
                    ingredientAdapter.removeIngredient(ingredientModel);
                    ingredientModelList = ingredientAdapter.getIngredientList();
                    if(ingredientModelList.size()<=0){
                        noIngredient.setVisibility(View.VISIBLE);
                    }
                    else{
                        noIngredient.setVisibility(View.GONE);
                    }
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }


        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BtnUtensilsBottomSheet(View view) {

        bottomSheetDialog = new BottomSheetDialog(view.getContext());
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

        displayUtensilIcon = Arrays.stream(utensilsIcon).boxed().collect(Collectors.toList());
        displayUtensilName = Arrays.stream(utensilsName).boxed().collect(Collectors.toList());

        GridView utensilsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Utensils);
        LinearLayout utensilLayoutCantFind = (LinearLayout) bottomSheetDialog.findViewById(R.id.LayoutUntCantFind);
        Button customUtensilButton = bottomSheetDialog.findViewById(R.id.BtnCustomUtensils);
        customUtensilButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        BtnAddCustomUtensil(view);
                    }
                }
        );
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), utensilsName, utensilsIcon) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selUtensilsItem.contains(displayUtensilIcon.get(position))) {
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
                    if (selUtensilsItem.contains(displayUtensilIcon.get(position))) {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        selUtensilsItem.remove(Integer.valueOf(displayUtensilIcon.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(displayUtensilName.get(position)));
                        utensilModel.setIcon(getIndex(UTENSILS_ICON, displayUtensilIcon.get(position)));
                        utensilAdapter.removeUtensil(utensilModel);
                        utensilModelList = utensilAdapter.getUtensilList();
                        if(utensilModelList.size()<=0){
                            noUtensil.setVisibility(View.VISIBLE);
                        }
                        else{
                            noUtensil.setVisibility(View.GONE);
                        }

                    } else {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selUtensilsItem.add(displayUtensilIcon.get(position));
                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(displayUtensilName.get(position)));
                        utensilModel.setIcon(getIndex(UTENSILS_ICON, displayUtensilIcon.get(position)));
                        utensilAdapter.addUtensil(utensilModel);
                        utensilModelList = utensilAdapter.getUtensilList();
                        if(utensilModelList.size()<=0){
                            noUtensil.setVisibility(View.VISIBLE);
                        }
                        else{
                            noUtensil.setVisibility(View.GONE);
                        }
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
                displayUtensilIcon.clear();
                displayUtensilName.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<utensilsName.length; i++) {
                    if(getResources().getString(utensilsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayUtensilIcon.add(utensilsIcon[i]);
                        displayUtensilName.add(utensilsName[i]);
                        searchName.add(utensilsName[i]);

                        if (selUtensilsItem.contains(displayUtensilIcon.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayUtensilIcon.size() > 0) {
                    utensilsGridView.setVisibility(View.VISIBLE);
                    utensilLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayUtensilIcon.stream().mapToInt(i -> i).toArray();
                    int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                    MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), searchNameArray, searchIconArray) {
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
            Chip chip = new Chip(view.getContext());
            chip.setText(chipItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setCloseIconTintResource(R.color.black);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomUtensils.remove(chipItem);
                    UtensilModel utensilModel = new UtensilModel();
                    utensilModel.setName(chipItem);
                    utensilModel.setIcon(-1);
                    utensilAdapter.removeUtensil(utensilModel);
                    utensilModelList = utensilAdapter.getUtensilList();
                    if(utensilModelList.size()<=0){
                        noUtensil.setVisibility(View.VISIBLE);
                    }
                    else{
                        noUtensil.setVisibility(View.GONE);
                    }
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
            Chip chip = new Chip(view.getContext());
            chip.setText(newItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setCloseIconTintResource(R.color.black);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(newItem);
                    IngredientModel ingredientModel = new IngredientModel();
                    ingredientModel.setIcon(-1);
                    ingredientModel.setName(newItem);
                    ingredientAdapter.removeIngredient(ingredientModel);
                    ingredientModelList = ingredientAdapter.getIngredientList();
                    if(ingredientModelList.size()<=0){
                        noIngredient.setVisibility(View.VISIBLE);
                    }
                    else{
                        noIngredient.setVisibility(View.GONE);
                    }
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomIngredients.add(newItem);
            IngredientModel ingredientModel = new IngredientModel();
            ingredientModel.setName(newItem);
            ingredientModel.setIcon(-1);
            ingredientAdapter.addIngredient(ingredientModel);
            ingredientModelList = ingredientAdapter.getIngredientList();
            if(ingredientModelList.size()<=0){
                noIngredient.setVisibility(View.VISIBLE);
            }
            else{
                noIngredient.setVisibility(View.GONE);
            }
        }
    }

    public void BtnAddCustomUtensil(View view) {
        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomUtensil);
        String newItem = searchUtensils.getText().toString();

        if (!selCustomUtensils.contains(newItem)) {
            Chip chip = new Chip(view.getContext());
            chip.setText(newItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setCloseIconTintResource(R.color.black);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomUtensils.remove(newItem);
                    UtensilModel utensilModel = new UtensilModel();
                    utensilModel.setIcon(-1);
                    utensilModel.setName(newItem);
                    utensilAdapter.removeUtensil(utensilModel);
                    utensilModelList = utensilAdapter.getUtensilList();
                    if(utensilModelList.size()<=0){
                        noUtensil.setVisibility(View.VISIBLE);
                    }
                    else{
                        noUtensil.setVisibility(View.GONE);
                    }
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomUtensils.add(newItem);
            UtensilModel utensilModel = new UtensilModel();
            utensilModel.setName(newItem);
            utensilModel.setIcon(-1);
            utensilAdapter.addUtensil(utensilModel);
            utensilModelList = utensilAdapter.getUtensilList();
            if(utensilModelList.size()<=0){
                noUtensil.setVisibility(View.VISIBLE);
            }
            else{
                noUtensil.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private int getIndex(int[] list, int item) {
        for(int i=0; i<list.length; i++) {
            if (list[i] == item){
                return i;
            }
        }
        return -1;
    }
}