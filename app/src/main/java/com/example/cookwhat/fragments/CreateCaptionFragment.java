package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.CreateActivity;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCaptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCaptionFragment extends Fragment {
    List<RecipeStepModel> recipeStepModels = new ArrayList<>();
    ImageSlider imageSlider;
    EditText editTitle;
    EditText editCaption;
    EditText editAddTag;
    Button buttonAddTag;
    ChipGroup chipGroup;
    int currImageIndex = 0;

    public CreateCaptionFragment() {
        // Required empty public constructor
    }

    public static CreateCaptionFragment newInstance(String param1, String param2) {
        CreateCaptionFragment fragment = new CreateCaptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_caption, container, false);
        RecipeModel recipeActivity = ((CreateActivity)getActivity()).getNewRecipe();

        // set title
        editTitle = (EditText) view.findViewById(R.id.ETCreateRecipeTitle);

        editTitle.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                 }

                 @Override
                 public void afterTextChanged(Editable s) {
                     recipeActivity.setTitle(s.toString());
                 }
             }
            );

        editTitle.setText(recipeActivity.getTitle());
        editTitle.setSelection(editTitle.getText().length());

        recipeStepModels = recipeActivity.getSteps();

        // set captions
        editCaption = (EditText) view.findViewById(R.id.ETCreateRecipeCaption);
        if (recipeStepModels.size() > 0) {
            editCaption.setText(recipeStepModels.get(currImageIndex).getStep());
            editCaption.setSelection(editCaption.getText().length());
        }

        editCaption.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
             }

             @Override
             public void afterTextChanged(Editable s) {
                 recipeStepModels.get(currImageIndex).setStep(s.toString());
             }
         }
        );

        imageSlider = view.findViewById(R.id.CreateImageSlider);
        imageSlider.setItemChangeListener(new ItemChangeListener() {
            @Override
            public void onItemChanged(int position) {
                if (recipeStepModels.size() > position) {
                    //store previous captions
                    String currText = editCaption.getText().toString();

                    currImageIndex = position;
                    editCaption.setText(recipeStepModels.get(currImageIndex).getStep());
                    editCaption.setSelection(editCaption.getText().length());
                }
            }
        });

        List<SlideModel> newSlideModel = new ArrayList<>();

        for(RecipeStepModel model : recipeStepModels) {
            newSlideModel.add(new SlideModel(model.getImage(), null,null));
        }

        imageSlider.setImageList(newSlideModel);


        // tags
        editAddTag = (EditText) view.findViewById(R.id.ETCreateRecipeTag);
        buttonAddTag = (Button) view.findViewById(R.id.BtnAddTag);

        chipGroup = (ChipGroup) view.findViewById(R.id.ChipGroupTag);

        buttonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTag = "#" + editAddTag.getText().toString();
                RecipeModel recipeActivity = ((CreateActivity)getActivity()).getNewRecipe();

                if (!recipeActivity.getTags().contains(newTag)) {
                    Chip chip = new Chip(getActivity());
                    chip.setText(newTag);
                    chip.setChipBackgroundColorResource(R.color.light_yellow);
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(chip);
                            List<String> prevTags = recipeActivity.getTags();
                            prevTags.remove(newTag);
                            recipeActivity.setTags(prevTags);
                        }
                    });
                    chip.setTextColor(getResources().getColor(R.color.black));

                    chipGroup.addView(chip);
                    editAddTag.setText("");
                    List<String> prevTags = recipeActivity.getTags();
                    prevTags.add(newTag);
                    recipeActivity.setTags(prevTags);
                }
            }
        });


        return view;
    }
}