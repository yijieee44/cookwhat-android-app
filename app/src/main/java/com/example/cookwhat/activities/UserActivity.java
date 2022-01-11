package com.example.cookwhat.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.CreateShowGalleryFragment;
import com.example.cookwhat.fragments.FavouriteFragment;
import com.example.cookwhat.fragments.ViewProfileFragment;

public class UserActivity extends AppCompatActivity {

    //retrieve db follow id and name list;
    //set usermodel follow id and namelist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String fragmentname = getIntent().getStringExtra("fragmentname");
        startFragment(fragmentname);
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FavouriteCategoryFragment f1 = new FavouriteCategoryFragment();
        FavouriteFragment f2 = new FavouriteFragment();
        ft.add(R.id.NHFUser, f1);
        ft.add(R.id.NHFUser,f2);
        ft.commit();*/


        //NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFUser);



    }

    public void startFragment(String fragmentname){
        Fragment nfhuser = getSupportFragmentManager().findFragmentById(R.id.NHFUser);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragmentname.equals("viewprofilefragment")){
            transaction.replace(R.id.NHFUser, new ViewProfileFragment()).commit();
        }
    }

    public void setCategoryName(String categoryName) {
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FavouriteFragment f2 = new FavouriteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryName",categoryName);
        f2.setArguments(bundle);
        ft.add(R.id.NHFUser,f2);
        ft.commit();
    }





}