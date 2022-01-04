package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.CreateActivity;
import com.example.cookwhat.adapters.EditPhotosAdapter;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.example.cookwhat.models.UtensilModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePreviewFragment extends Fragment {
    TextView recipeTitle;
    TextView recipeCaption;
    List<RecipeStepModel> recipeStepModels = new ArrayList<>();
    RecipeModel recipeModel;
    int currImageIndex = 0;
    ImageSlider imageSlider;
    Dialog INUDialog;
    ChipGroup chipGroup;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePreviewFragment newInstance(String param1, String param2) {
        CreatePreviewFragment fragment = new CreatePreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_preview, container, false);
        recipeModel = ((CreateActivity)getActivity()).getNewRecipe();
        recipeStepModels = recipeModel.getSteps();

        // set title
        recipeTitle = (TextView) view.findViewById(R.id.TVRecipeTitle);
        recipeTitle.setText(recipeModel.getTitle());

        // set captions
        recipeCaption = (TextView) view.findViewById(R.id.TVRecipeCaption);

        if (recipeStepModels.size() > currImageIndex) {
            recipeCaption.setText(recipeStepModels.get(currImageIndex).getStep());
        }

        imageSlider = view.findViewById(R.id.CreateImageSlider);
        imageSlider.setItemChangeListener(new ItemChangeListener() {
            @Override
            public void onItemChanged(int position) {
                if (recipeStepModels.size() > position) {
                    currImageIndex = position;
                    String currImageCaption = recipeStepModels.get(currImageIndex).getStep();

                    if (currImageCaption.isEmpty()) {
                        for (int i = currImageIndex; i > 0; i--) {
                            if (!recipeStepModels.get(i).getStep().isEmpty()) {
                                currImageCaption = recipeStepModels.get(i).getStep();
                                break;
                            }
                        }
                    }

                    recipeCaption.setText(currImageCaption);
                }
            }
        });

        List<SlideModel> newSlideModel = new ArrayList<>();

        for(RecipeStepModel model : recipeStepModels) {
            newSlideModel.add(new SlideModel(model.getImage(), null,null));
        }

        imageSlider.setImageList(newSlideModel);

        // set up ingredient and utensils button
        INUDialog = new Dialog(getActivity());
        Button viewINU = (Button) view.findViewById(R.id.BtnINU);
        viewINU.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showINUDialog();
            }
        });


        // tag
        chipGroup = (ChipGroup) view.findViewById(R.id.ChipGroupTag);

        for (String tag : recipeModel.getTags()) {
            Chip chip = new Chip(getActivity());
            chip.setText(tag);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }


        return view;
    }

    private void showINUDialog() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        INUDialog.setCancelable(true);
        INUDialog.setContentView(R.layout.dialog_inu);

        List<IngredientModel> ingredientModels = recipeModel.getIngredients();
        List<UtensilModel> utensilModels = recipeModel.getUtensils();

        if(ingredientModels.size()>0) {
            RecyclerView ingredientsRecyclerView = (RecyclerView) INUDialog.findViewById(R.id.RVIngredients);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
            ingredientsRecyclerView.setAdapter(new IngredientAdapter(getContext(), ingredientModels));

        } else {
            TextView noIngredientTextView = (TextView) INUDialog.findViewById(R.id.TVNoIngredients);
            noIngredientTextView.setVisibility(View.VISIBLE);
        }

        if(utensilModels.size()>0) {
            RecyclerView utensilsRecyclerView = (RecyclerView) INUDialog.findViewById(R.id.RVUtensils);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            utensilsRecyclerView.setLayoutManager(linearLayoutManager);
            utensilsRecyclerView.setAdapter(new UtensilAdapter(getContext(), utensilModels));

        } else {
            TextView noUtensilsTextView = (TextView) INUDialog.findViewById(R.id.TVNoUtensils);
            noUtensilsTextView.setVisibility(View.VISIBLE);
        }

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.75);

        INUDialog.getWindow().setLayout(width, height);

        INUDialog.show();
    }
}