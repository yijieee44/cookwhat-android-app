package com.example.cookwhat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.CreateCaptionFragment;
import com.example.cookwhat.fragments.CreateShowGalleryFragment;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    RecipeModel newRecipe = new RecipeModel();

    public RecipeModel getNewRecipe() {
        return newRecipe;
    }

    public void setNewRecipe(RecipeModel newRecipe) {
        this.newRecipe = newRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.FABBack);
        FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.FABNext);

        backButton.setEnabled(false);
        backButton.setClickable(false);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.FragmentContainerCreate);
                if (currentFragment instanceof CreateCaptionFragment) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.FragmentContainerCreate, new CreateShowGalleryFragment());
                    fragmentTransaction.commit();

                    backButton.setEnabled(false);
                    backButton.setClickable(false);
                    nextButton.setEnabled(true);
                    nextButton.setClickable(true);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.FragmentContainerCreate);
                if (currentFragment instanceof CreateShowGalleryFragment) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.FragmentContainerCreate, new CreateCaptionFragment());
                    fragmentTransaction.commit();

                    nextButton.setEnabled(false);
                    nextButton.setClickable(false);
                    backButton.setEnabled(true);
                    backButton.setClickable(true);
                }
            }
        });

        // hide floating button when keyboard is visible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen) {
                            nextButton.setVisibility(View.GONE);
                            backButton.setVisibility(View.GONE);
                        } else {
                            nextButton.setVisibility(View.VISIBLE);
                            backButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}