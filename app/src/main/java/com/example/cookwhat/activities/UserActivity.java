package com.example.cookwhat.activities;

import android.content.Intent;
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
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        String fragmentname = getIntent().getStringExtra("fragmentname");
        startFragmentViewOtherProfile(fragmentname);


        Intent i = getIntent();
        this.userID = i.getStringExtra("userID");
        System.out.println("In user activity"+userID);

        //NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFUser);

    }

    public String getUserID(){
        return this.userID;
    }

    public void startFragmentViewOtherProfile(String fragmentname){
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

    public void getUserIDToView(String userID, String userName, Boolean isFollowing){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("viewUserID", userID);
        bundle.putString("viewUsername", userName);
        bundle.putBoolean("isFollowing", isFollowing);
        fragment.setArguments(bundle);
        ft.replace(R.id.NHFUser,fragment);
        ft.commit();
    }





}