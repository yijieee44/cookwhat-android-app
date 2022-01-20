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
    int count = 0;
    String catName = "";
    Button BtnAddFav, BtnArabic, BtnChinese, BtnEuropean, BtnIndia, BtnJapan, BtnKorea, BtnMediterranean,
    BtnSEA, BtnOther;

    public FavouriteCategoryDialogFragment(UserModelDB userModelDB, RecipeModelDB recipeModelDB) {
        this.userModelDB = userModelDB;
        this.recipeId = recipeModelDB.getId();
        this.recipeModelDB = recipeModelDB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_background));
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

        BtnAddFav = (Button) getActivity().findViewById(R.id.BtnAddFav);
        BtnArabic = (Button) view.findViewById(R.id.BtnArabic);
        BtnChinese = (Button) view.findViewById(R.id.BtnChinese);
        BtnEuropean = (Button) view.findViewById(R.id.BtnEuropean);
        BtnIndia = (Button) view.findViewById(R.id.BtnIndia);
        BtnJapan = (Button) view.findViewById(R.id.BtnJapan);
        BtnKorea = (Button) view.findViewById(R.id.BtnKorea);
        BtnMediterranean = (Button) view.findViewById(R.id.BtnMediterranean);
        BtnSEA = (Button) view.findViewById(R.id.BtnSEA);
        BtnOther = (Button) view.findViewById(R.id.BtnOther);

        Map<String, ArrayList<String>> favouriteCategory = userModelDB.getFavouriteCategory();

        if (favouriteCategory != null){
            for (Map.Entry<String,ArrayList<String>> entry : favouriteCategory.entrySet())
                if (entry.getValue().contains(recipeId)){
                    count++;
                    catName = entry.getKey();;
                    if (entry.getKey().equals(BtnArabic.getText())){BtnArabic.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnChinese.getText())){BtnChinese.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnEuropean.getText())){BtnEuropean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnIndia.getText())){BtnIndia.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnJapan.getText())){BtnJapan.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnKorea.getText())){BtnKorea.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnMediterranean.getText())){BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnSEA.getText())){BtnSEA.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    else if (entry.getKey().equals(BtnOther.getText())){BtnOther.setBackgroundColor(getResources().getColor(R.color.dark_yellow));}
                    break;
                }
        }

        BtnArabic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Arabic");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Arabic", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnArabic.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnArabic.getText().toString();
                    BtnArabic.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Arabic", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnArabic.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnArabic.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnChinese.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Chinese");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Chinese", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnChinese.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnChinese.getText().toString();
                    BtnChinese.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Chinese", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnChinese.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnChinese.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnEuropean.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("European");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("European", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnEuropean.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnEuropean.getText().toString();
                    BtnEuropean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("European", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnEuropean.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnEuropean.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnIndia.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Indian");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Indian", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnIndia.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnIndia.getText().toString();
                    BtnIndia.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Indian", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnIndia.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnIndia.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnJapan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Japanese");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Japanese", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnJapan.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnJapan.getText().toString();
                    BtnJapan.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Japanese", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnJapan.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnJapan.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnKorea.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Korean");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Korean", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnKorea.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnKorea.getText().toString();
                    BtnKorea.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Korean", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnKorea.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnKorea.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnMediterranean.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Mediterranean");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Mediterranean", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnMediterranean.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnMediterranean.getText().toString();
                    BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Mediterranean", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnMediterranean.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnSEA.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("South-East Asia");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("South-East Asia", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnSEA.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnSEA.getText().toString();
                    BtnSEA.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("South-East Asia", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnSEA.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnSEA.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
                }
            }
        });

        BtnOther.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                ArrayList<String> recipeids = favouriteCategory.get("Others");
                if (recipeids.isEmpty() || !recipeids.contains(recipeId)){
                    recipeids.add(recipeId);
                    favouriteCategory.put("Others", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Saved recipe to " + BtnOther.getText(), Toast.LENGTH_SHORT).show();
                    if (count==1){
                        ArrayList<String> recipeids2 = favouriteCategory.get(catName);
                        recipeids2.remove(recipeId);
                        favouriteCategory.put(catName, recipeids2);
                        userModelDB.setFavouriteCategory(favouriteCategory);
                        updateUser(userModelDB);
                        deselectAllButtons();
                    }
                    if (count==0){
                        recipeModelDB.setNumFav(recipeModelDB.getNumFav() + 1);
                        updateRecipe(recipeModelDB);
                        BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                        BtnAddFav.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                        count++;
                    }
                    catName = BtnOther.getText().toString();
                    BtnOther.setBackgroundColor(getResources().getColor(R.color.dark_yellow));
                }
                else if (recipeids.contains(recipeId)){
                    recipeids.remove(recipeId);
                    favouriteCategory.put("Others", recipeids);
                    userModelDB.setFavouriteCategory(favouriteCategory);
                    updateUser(userModelDB);
                    Toast.makeText(getContext(), "Recipe is removed from " + BtnOther.getText(), Toast.LENGTH_SHORT).show();
                    recipeModelDB.setNumFav(recipeModelDB.getNumFav() - 1);
                    updateRecipe(recipeModelDB);
                    BtnAddFav.setText(String.valueOf(recipeModelDB.getNumFav()));
                    BtnAddFav.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    BtnOther.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
                    count--;
                    catName = "";
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

    public void deselectAllButtons(){
        BtnArabic.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnChinese.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnEuropean.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnIndia.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnJapan.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnKorea.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnMediterranean.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnSEA.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
        BtnOther.setBackgroundColor(getResources().getColor(R.color.weirdyellow));
    }
}