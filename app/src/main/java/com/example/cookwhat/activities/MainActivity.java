package com.example.cookwhat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.cookwhat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind NavHostFragment with NavController
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFMain);
        NavController navController = host.getNavController();

        setupBottomNavMenu(navController);
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // disable the middle empty button
        bottomNav.getMenu().getItem(1).setEnabled(false);
    }

    public void BtnSearchRecipe(View v){
        Intent intentSearchActivity = new Intent(this, SearchActivity.class);
        startActivity(intentSearchActivity);
    }

}