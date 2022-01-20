package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.UserProfileFragment;
import com.example.cookwhat.fragments.ViewProfileFragment;
import com.example.cookwhat.models.UserModelDB;

public class ViewProfileActivity extends AppCompatActivity {

    String userId;
    UserModelDB userModelDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent i = getIntent();
        this.userId = i.getStringExtra("userId");
        this.userModelDB = (UserModelDB) i.getSerializableExtra("userModel");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (userModelDB.getUserId().equals(userId)){
            getSupportActionBar().setTitle("My Profile");
            UserProfileFragment fragment = new UserProfileFragment();
            transaction.replace(R.id.FragmentContainerViewProfile,fragment);
        }
        else{
            getSupportActionBar().setTitle("View Profile");
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            ViewProfileFragment fragment = new ViewProfileFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.FragmentContainerViewProfile,fragment);
        }
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(122, intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}