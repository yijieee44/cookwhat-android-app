package com.example.cookwhat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
//        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFFavourite);
//        NavController navController = host.getNavController();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("usermodel", userModelDB);
        bundle.putString("selectedCategoryName", selectedCategoryName);
//        navController.navigate(R.id.DestFav, bundle);
        favouriteFragment.setArguments(bundle);
        transaction.replace(R.id.NHFFavourite, favouriteFragment,"FavouriteFragment");
        System.out.println("favouriteActivity"+selectedCategoryName);
        transaction.addToBackStack("stack");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("FRAGMENTSTACK", ""+getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ) {
            getSupportFragmentManager().popBackStack();
        }
        else {
            Intent intent = new Intent();
            setResult(122, intent);
            finish();
        }
    }


}