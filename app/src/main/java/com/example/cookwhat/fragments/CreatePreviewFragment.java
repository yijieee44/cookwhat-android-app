package com.example.cookwhat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.CreateActivity;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;

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
    int currImageIndex = 0;
    ImageSlider imageSlider;


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
        RecipeModel recipeActivity = ((CreateActivity)getActivity()).getNewRecipe();
        recipeStepModels = recipeActivity.getSteps();

        // set title
        recipeTitle = (TextView) view.findViewById(R.id.TVRecipeTitle);
        recipeTitle.setText(recipeActivity.getTitle());

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

        return view;
    }
}