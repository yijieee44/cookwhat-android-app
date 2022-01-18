package com.example.cookwhat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.ViewProfileFragment;
import com.example.cookwhat.models.UserModelDB;

public class UserActivity extends AppCompatActivity {

    //retrieve db follow id and name list;
    //set usermodel follow id and namelist;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



        //Intent i = getIntent();
        //this.userID = i.getStringExtra("userID");


        //NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFUser);

    }


    public void startFragment(String userId, UserModelDB userModelDB){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putSerializable("userModel", userModelDB);
        ViewProfileFragment fragment = new ViewProfileFragment();
        System.out.println("putBundle here:"+userId);
        fragment.setArguments(bundle);
        transaction.replace(R.id.NHFUser,fragment);
        transaction.commit();
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


    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}