package com.example.cookwhat.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.cookwhat.R;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavouriteCategoryDialogFragment extends DialogFragment {
    private IngredientAndUtensilDialogFragment.DialogListener listener;

    UserModelDB userModelDB;
    String recipeId;
    RecipeModelDB recipeModelDB;
    public CollectionReference userdb;
    public CollectionReference recipedb;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public int count = 0;

    public FavouriteCategoryDialogFragment(UserModelDB userModelDB, RecipeModelDB recipeModelDB) {
        this.userModelDB = userModelDB;
        this.recipeId = recipeModelDB.getId();
        this.recipeModelDB = recipeModelDB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_white_background));
        setCancelable(true);

        return inflater.inflate(R.layout.fragment_favourite_category_dialog, null);
    }

    @Override
    public void onResume(){
        super.onResume();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int)(metrics.widthPixels*0.90);
        int height = (int)(metrics.heightPixels*0.75);

        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        userdb = db.collection("user");
        recipedb = db.collection("recipe");

        Button BtnAddFav = (Button) getActivity().findViewById(R.id.BtnAddFav);
        Button BtnArabic = (Button) view.findViewById(R.id.BtnArabic);
        Button BtnChinese = (Button) view.findViewById(R.id.BtnChinese);
        Button BtnEuropean = (Button) view.findViewById(R.id.BtnEuropean);
        Button BtnIndia = (Button) view.findViewById(R.id.BtnIndia);
        Button BtnJapan = (Button) view.findViewById(R.id.BtnJapan);
        Button BtnKorea = (Button) view.findViewById(R.id.BtnKorea);
        Button BtnMediterranean = (Button) view.findViewById(R.id.BtnMediterranean);
        Button BtnSEA = (Button) view.findViewById(R.id.BtnSEA);
        Button BtnOther = (Button) view.findViewById(R.id.BtnOther);

        Map<String, ArrayList<String>> favouriteCategory = userModelDB.getFavouriteCategory();

        if (favouriteCategory != null){
            for (Map.Entry<String,ArrayList<String>> entry : favouriteCategory.entrySet())
                if (entry.getValue().contains(recipeId)){
                    count++;
                    if (entry.getKey().equals(BtnArabic.getText())){BtnArabic.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnChinese.getText())){BtnChinese.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnEuropean.getText())){BtnEuropean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnIndia.getText())){BtnIndia.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnJapan.getText())){BtnJapan.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnKorea.getText())){BtnKorea.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnMediterranean.getText())){BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnSEA.getText())){BtnSEA.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnOther.getText())){BtnOther.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                }
        }

        BtnArabic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("Arabic", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnArabic.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnArabic.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("Arabic");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("Arabic", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnArabic.getText(), Toast.LENGTH_SHORT).show();
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnArabic.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("Arabic", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnArabic.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnArabic.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("Arabic", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnArabic.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnArabic.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnChinese.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory == null){
                    Map<String, ArrayList<String>> favouriteCategory = new HashMap<>();
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("Chinese", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnChinese.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnChinese.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("Chinese");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("Chinese", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnChinese.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnChinese.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("Chinese", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnChinese.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnChinese.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("Chinese", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnChinese.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnChinese.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnEuropean.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("European", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnEuropean.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnEuropean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("European");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("European", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnEuropean.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnEuropean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("European", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnEuropean.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnEuropean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("European", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnEuropean.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnEuropean.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnIndia.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("India", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnIndia.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnIndia.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("India");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("India", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnIndia.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnIndia.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;

                    }
                    if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("India", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnIndia.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnIndia.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("India", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnIndia.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnIndia.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnJapan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("Japan", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnJapan.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnJapan.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("Japan");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("Japan", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnJapan.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnJapan.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("Japan", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnJapan.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnJapan.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("Japan", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnJapan.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnJapan.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnKorea.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("Korea", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnKorea.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnKorea.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("Korea");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("Korea", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnKorea.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnKorea.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("Korea", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnKorea.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnKorea.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("Korea", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnKorea.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnKorea.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnMediterranean.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("Mediterranean", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnMediterranean.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("Mediterranean");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("Mediterranean", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnMediterranean.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("Mediterranean", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnMediterranean.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("Mediterranean", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnMediterranean.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnSEA.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("South-East-Asia", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnSEA.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnSEA.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("South-East-Asia");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("South-East-Asia", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnSEA.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnSEA.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("South-East-Asia", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnSEA.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnSEA.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("South-East-Asia", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnSEA.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnSEA.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

        BtnOther.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if (favouriteCategory.isEmpty()){
                    ArrayList<String> recipeids = new ArrayList<>();
                    recipeids.add(recipeId);
                    favouriteCategory.put("Other", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnOther.getText(), Toast.LENGTH_SHORT).show();
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        BtnOther.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                    }
                    count++;
                }
                else {
                    ArrayList<String> recipeids = favouriteCategory.get("Other");
                    if (recipeids == null){
                        recipeids = new ArrayList<>();
                        recipeids.add(recipeId);
                        favouriteCategory.put("Other", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnOther.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnOther.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    }
                    else if (recipeids.isEmpty() || !recipeids.contains(recipeId)) {
                        recipeids.add(recipeId);
                        favouriteCategory.put("Other", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Saved recipe to " + BtnOther.getText(), Toast.LENGTH_SHORT).show();
                        if (count==0){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                            BtnOther.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        }
                        count++;
                    } else if (recipeids.contains(recipeId)) {
                        recipeids.remove(recipeId);
                        favouriteCategory.put("Other", recipeids);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        Toast.makeText(getContext(), "Recipe is removed from " + BtnOther.getText(), Toast.LENGTH_SHORT).show();
                        if (count==1){
                            recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                            updateRecipe(recipeModelDB);
                            BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                            BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                            BtnOther.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                        }
                        count--;
                    }
                }
            }
        });

    }

    public void updateRecipe(RecipeModelDB recipeModelDB){
        recipedb.document(recipeModelDB.getId()).set(recipeModelDB)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("SUCCESS", "DocumentSnapshot successfully written!");
                        }
                        else {
                            Log.w("ERROR", "Error writing document", task.getException());
                        }
                    }
                });
    }


    public void updateUser(UserModelDB userModelDB){
        userdb.document(userModelDB.getUserId()).set(userModelDB)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("SUCCESS", "DocumentSnapshot successfully written!");
                        }
                        else {
                            Log.w("ERROR", "Error writing document", task.getException());
                        }
                    }
                });
    }


    public void setDialogListener(IngredientAndUtensilDialogFragment.DialogListener listener){
        this.listener = listener;
    }


    public interface DialogListener {
        void onFinishEditDialog();
    }
}