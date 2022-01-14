package com.example.cookwhat.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.LoginFragment;
import com.example.cookwhat.fragments.RegisterCompleteFragment;

public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       /* NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFLogin);
        NavController navController = host.getNavController();


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);*/

    }

    public void setRegisterDetails(String[]details){

        System.out.println(details);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        RegisterCompleteFragment registerCompleteFragment = new RegisterCompleteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", details[0]);
        bundle.putString("email",details[1]);
        bundle.putString("password",details[2]);
        registerCompleteFragment.setArguments(bundle);
        ft.setReorderingAllowed(true);
        //ft.add(R.id.NHFLogin,)

        ft.replace(R.id.NHFLogin,registerCompleteFragment);
        ft.addToBackStack(null);
        ft.commit();



    }

    public void onRegisterSuccessful(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.NHFLogin, loginFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}