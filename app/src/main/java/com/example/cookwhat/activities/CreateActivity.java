package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.CreateCaptionFragment;
import com.example.cookwhat.fragments.CreatePreviewFragment;
import com.example.cookwhat.fragments.CreateShowGalleryFragment;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class CreateActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageRef;
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

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

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
                } else if (currentFragment instanceof CreatePreviewFragment) {
                    toCreate();
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

        backButton.setEnabled(true);
        backButton.setClickable(true);

        nextButton.setImageDrawable(ContextCompat.getDrawable(CreateActivity.this, R.drawable.ic_baseline_check_24));
    }

    private void toCreate() {
//        List<String> imagesId = new ArrayList<>();
//
//        // upload photos on storage first
//        for(int i=0; i<newRecipe.getSteps().size(); i++) {
//            RecipeStepModel step = newRecipe.getSteps().get(i);
//            String imageId = UUID.randomUUID().toString();
//            StorageReference ref = storageRef.child("images/" + imageId);
//
//            ref.putFile(Uri.parse(step.getImage())).addOnSuccessListener(
//                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                            imagesId.add(imageId);
//                        }
//                    }
//            ) .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e)
//                        {
//                            Toast.makeText(CreateActivity.this,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
//                            return;
//                        }
//                    });
//        }
//
//        for (int i=0; i < newRecipe.getSteps().size(); i++) {
//            newRecipe.getSteps().get(i).setImage(imagesId.get(i));
//        }
//
//        db.collection("recipe").document().set(newRecipe);

    }


}