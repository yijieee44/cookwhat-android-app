package com.example.cookwhat.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.CreateCaptionFragment;
import com.example.cookwhat.fragments.CreatePreviewFragment;
import com.example.cookwhat.fragments.CreateShowGalleryFragment;
import com.example.cookwhat.models.RecipeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;


public class CreateActivity extends AppCompatActivity {
    RecipeModel newRecipe = new RecipeModel();
    FragmentManager fragmentManager;
    FloatingActionButton backButton;
    FloatingActionButton nextButton;
    

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

        fragmentManager = getSupportFragmentManager();

        backButton = (FloatingActionButton) findViewById(R.id.FABBack);
        nextButton = (FloatingActionButton) findViewById(R.id.FABNext);

        backButton.setEnabled(false);
        backButton.setClickable(false);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Show Gallery < Caption < Preview
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.FragmentContainerCreate);
                if (currentFragment instanceof CreateCaptionFragment) {
                    toShowGallery();
                    
                } else if (currentFragment instanceof CreatePreviewFragment) {
                    toCreateCaption();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Show Gallery > Caption > Preview
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.FragmentContainerCreate);
                if (currentFragment instanceof CreateShowGalleryFragment) {
                    toCreateCaption();
                    
                } else if (currentFragment instanceof CreateCaptionFragment) {
                   toPreview();
                }
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        FragmentContainerView fragmentContainer = (FragmentContainerView) findViewById(R.id.FragmentContainerCreate);

        // hide floating button when keyboard is visible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen) {
                            nextButton.setVisibility(View.GONE);
                            backButton.setVisibility(View.GONE);
                            params.setMargins(0,0,0,0);
                            fragmentContainer.setLayoutParams(params);
                        } else {
                            nextButton.setVisibility(View.VISIBLE);
                            backButton.setVisibility(View.VISIBLE);
                            params.setMargins(0,0,0,80);
                            fragmentContainer.setLayoutParams(params);
                        }
                    }
                });
    }
    
    private void toShowGallery() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainerCreate, new CreateShowGalleryFragment());
        fragmentTransaction.commit();

        backButton.setEnabled(false);
        backButton.setClickable(false);
        nextButton.setEnabled(true);
        nextButton.setClickable(true);

        nextButton.setImageDrawable(ContextCompat.getDrawable(CreateActivity.this, R.drawable.ic_baseline_chevron_right_24));
    }
    
    private void toCreateCaption() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainerCreate, new CreateCaptionFragment());
        fragmentTransaction.commit();

        backButton.setEnabled(true);
        backButton.setClickable(true);
        nextButton.setEnabled(true);
        nextButton.setClickable(true);

        nextButton.setImageDrawable(ContextCompat.getDrawable(CreateActivity.this, R.drawable.ic_baseline_chevron_right_24));
    }
    
    private void toPreview() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainerCreate, new CreatePreviewFragment());
        fragmentTransaction.commit();

        nextButton.setEnabled(false);
        nextButton.setClickable(false);
        backButton.setEnabled(true);
        backButton.setClickable(true);

        nextButton.setImageDrawable(ContextCompat.getDrawable(CreateActivity.this, R.drawable.ic_baseline_check_24));
    }


}