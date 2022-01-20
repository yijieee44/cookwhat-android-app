package com.example.cookwhat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.LoginFragment;
import com.example.cookwhat.fragments.RegisterCompleteFragment;


public class LoginActivity extends AppCompatActivity {

    ;

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }

    public void setRegisterDetails(String[]details){

        System.out.println(details);
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();

//        RegisterCompleteFragment registerCompleteFragment = new RegisterCompleteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", details[0]);
        bundle.putString("email",details[1]);
        bundle.putString("password",details[2]);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFLogin);
        NavController navController = host.getNavController();
        navController.navigate(R.id.DestRegisterComplete, bundle);

//        registerCompleteFragment.setArguments(bundle);
//        ft.setReorderingAllowed(true);
//        //ft.add(R.id.NHFLogin,)
//
//        ft.replace(R.id.NHFLogin,registerCompleteFragment);
//        ft.addToBackStack(null);
//        ft.commit();

    }

    public void onRegisterSuccessful(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.NHFLogin, loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}