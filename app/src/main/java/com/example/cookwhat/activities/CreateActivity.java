package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.CreateCaptionFragment;
import com.example.cookwhat.fragments.CreatePreviewFragment;
import com.example.cookwhat.fragments.CreateShowGalleryFragment;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.RecipeStepModel;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.time.format.DateTimeFormatter;


public class CreateActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageRef;
    CollectionReference recipedb;
    RecipeModel newRecipe = new RecipeModel();
    FragmentManager fragmentManager;
    FloatingActionButton backButton;
    FloatingActionButton nextButton;
    FirebaseUser user;
    DateTimeFormatter formatter;
    boolean isAllImageUploaded = true;
    List<Boolean> fileUploadCheck = new ArrayList<>();
    Dialog loadingDialog;
    boolean isEdit;

    public RecipeModel getNewRecipe() {
        return newRecipe;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setNewRecipe(RecipeModel newRecipe) {
        this.newRecipe = newRecipe;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            newRecipe.setUserId(user.getUid());
        }

        formatter = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.getDefault() )
                        .withZone( ZoneId.systemDefault() );


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        fragmentManager = getSupportFragmentManager();

        loadingDialog = new Dialog(this);

        // prefil for edit
        RecipeModel editRecipeModel = (RecipeModel) getIntent().getSerializableExtra("recipeModel");
        if (editRecipeModel != null) {
            newRecipe = editRecipeModel;
            getSupportActionBar().setTitle(getResources().getString(R.string.edit_recipe));
            isEdit = true;
        }

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
                    boolean check = checkShowGallery();
                    if (check) {
                        toCreateCaption();
                    }
                    
                } else if (currentFragment instanceof CreateCaptionFragment) {
                   boolean check = checkCreateCaption();
                    if(check) {
                        toPreview();
                    }
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

    private boolean checkShowGallery() {
        // at least one image
        if(newRecipe.getSteps().size()==0) {
            Toast.makeText(CreateActivity.this,
                    "At least one image need to be added.",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    private boolean checkCreateCaption() {
        // check title
        if(newRecipe.getTitle().equals("")) {
            Toast.makeText(CreateActivity.this,
                    "Title cannot be empty",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toCreate() {
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

        int width = (int)(getResources().getDisplayMetrics().widthPixels);
        int height = (int)(getResources().getDisplayMetrics().heightPixels);

        loadingDialog.getWindow().setLayout(width, height);

        recipedb = db.collection("recipe");

        if(isEdit) {
            RecipeModelDB newRecipeDB = newRecipe.toRecipeModelDB();
            recipedb.document(newRecipe.getId()).set(newRecipeDB)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingDialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra("recipeModelDB", newRecipeDB);
                                setResult(123, intent);
                                finish();
                            } else {
                                Toast.makeText(CreateActivity.this,
                                        "Fail to edit, please try again",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        } else {
            for (RecipeStepModel step : newRecipe.getSteps()) {
                fileUploadCheck.add(false);
            }

            // upload photos on storage first
            for (int i = 0; i < newRecipe.getSteps().size(); i++) {
                loadingDialog.show();
                final int index = i;
                RecipeStepModel step = newRecipe.getSteps().get(i);
                String imageId = UUID.randomUUID().toString();
                StorageReference ref = storageRef.child("images/" + imageId);

                ref.putFile(Uri.parse(step.getImage())).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                newRecipe.getSteps().get(index).setImage(imageId);
                                fileUploadCheck.set(index, true);

                                if (isAllImageUploaded == true && !fileUploadCheck.contains(false)) {
                                    newRecipe.setCreatedTime(formatter.format(Instant.now()));

                                    recipedb.document().set(newRecipe.toRecipeModelDB()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loadingDialog.dismiss();
                                                Intent intent = new Intent();
                                                setResult(122, intent);
                                                finish();
                                            } else {
                                                Toast.makeText(CreateActivity.this,
                                                        "Fail to create, please try again",
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                                isAllImageUploaded = false;
                                            }
                                        }
                                    });
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isAllImageUploaded = false;
                    }
                });

                if (isAllImageUploaded == false) {
                    loadingDialog.cancel();
                    Toast.makeText(CreateActivity.this,
                            "Fail to create, please try again",
                            Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}