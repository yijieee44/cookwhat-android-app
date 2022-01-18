package com.example.cookwhat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.EditAboutMe;
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

        String fragmentname = getIntent().getStringExtra("fragmentname");
        startFragment(fragmentname);


        //Intent i = getIntent();
        //this.userID = i.getStringExtra("userID");


        //NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFUser);

    }


    public void startFragment(String fragmentname){
        Fragment nfhuser = getSupportFragmentManager().findFragmentById(R.id.NHFUser);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragmentname.equals("viewprofilefragment")){

            transaction.replace(R.id.NHFUser, new ViewProfileFragment()).commit();
        }
        else if(fragmentname.equals("EditAboutMe")){

            UserModelDB usermodel = (UserModelDB) getIntent().getSerializableExtra("usermodel");
            System.out.println("Usermodel in useractivity:"+ usermodel.getUserName());
            EditAboutMe editAboutMe = new EditAboutMe();
            Bundle bundle = new Bundle();
            bundle.putSerializable("usermodel", usermodel);
            editAboutMe.setArguments(bundle);
            transaction.replace(R.id.NHFUser,editAboutMe).commit();
        }
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