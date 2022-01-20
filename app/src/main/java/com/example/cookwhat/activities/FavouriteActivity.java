package com.example.cookwhat.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.FavouriteFragment;
import com.example.cookwhat.models.UserModelDB;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }

    public void startFavourite(UserModelDB userModelDB, String selectedCategoryName){
        FavouriteFragment favouriteFragment = new FavouriteFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("usermodel", userModelDB);
        bundle.putString("selectedCategoryName", selectedCategoryName);
        favouriteFragment.setArguments(bundle);
        transaction.replace(R.id.NHFFavourite, favouriteFragment,"FavouriteFragment");
        System.out.println("favouriteActivity"+selectedCategoryName);
        transaction.commit();
    }
}