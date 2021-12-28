package com.example.cookwhat.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.CreateActivity;
import com.example.cookwhat.adapters.EditPhotosAdapter;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateShowGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateShowGalleryFragment extends Fragment {
    ImageSlider imageSlider;
    ArrayList<SlideModel> selectedImages = new ArrayList<>();
    int MAX_IMAGES_ALLOWED = 9;
    Dialog editDialog;
    List<RecipeStepModel> recipeStepModels = new ArrayList<>();


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        if(data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            int count = data.getClipData().getItemCount();

                            // limit to 9 photos
                            if(selectedImages.size() + count > MAX_IMAGES_ALLOWED) {
                                Toast.makeText(getActivity(), "Only " + MAX_IMAGES_ALLOWED + " images/videos allowed.", Toast.LENGTH_LONG).show();
                                count = MAX_IMAGES_ALLOWED - selectedImages.size();
                            }

                            for (int i = 0; i < count; i++) {
                                Uri imageUrl = data.getClipData().getItemAt(i).getUri();
                                selectedImages.add(new SlideModel(imageUrl.toString(), null,null));
                                recipeStepModels.add(new RecipeStepModel(imageUrl.toString(), ""));
                            }
                            imageSlider.setImageList(selectedImages);
                        } else if(data.getData() != null) {
                            Uri imagePath = data.getData();

                            // limit to 9 photos
                            if(selectedImages.size() >= 9) {
                                Toast.makeText(getActivity(), "Only " + MAX_IMAGES_ALLOWED + " images/videos allowed.", Toast.LENGTH_LONG).show();
                            } else {
                                selectedImages.add(new SlideModel(imagePath.toString(), null, null));
                                recipeStepModels.add(new RecipeStepModel(imagePath.toString(), ""));
                                imageSlider.setImageList(selectedImages);
                            }
                        }
                    }
                    RecipeModel recipeFromActivity = ((CreateActivity)getActivity()).getNewRecipe();
                    recipeFromActivity.setSteps(recipeStepModels);

                    ((CreateActivity)getActivity()).setNewRecipe(recipeFromActivity);
                }
            });

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateShowGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateShowGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateShowGalleryFragment newInstance(String param1, String param2) {
        CreateShowGalleryFragment fragment = new CreateShowGalleryFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_show_gallery, container, false);

        editDialog = new Dialog(getActivity());
        recipeStepModels = ((CreateActivity)getActivity()).getNewRecipe().getSteps();

        Button button = (Button) view.findViewById(R.id.BtnAddImages);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getImagesFromGallery();
            }
        });

        Button editButton = (Button) view.findViewById(R.id.BtnEditImages);
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showEditDialog();
            }
        });

        imageSlider = view.findViewById(R.id.CreateImageSlider);

        List<SlideModel> newSlideModel = new ArrayList<>();

        for(RecipeStepModel model : recipeStepModels) {
            newSlideModel.add(new SlideModel(model.getImage(), null,null));
        }

        imageSlider.setImageList(newSlideModel);

        return view;

    }

    public void getImagesFromGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
    }

    public void showEditDialog() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        editDialog.setCancelable(true);
        editDialog.setContentView(R.layout.dialog_edit_photos);

        if(recipeStepModels.size()>0) {
            RecyclerView photosRecyclerView = (RecyclerView) editDialog.findViewById(R.id.RVPhotos);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());;
            photosRecyclerView.setLayoutManager(linearLayoutManager);
            photosRecyclerView.setAdapter(new EditPhotosAdapter(getContext(), recipeStepModels));

            editDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    EditPhotosAdapter editPhotosAdapter = (EditPhotosAdapter) photosRecyclerView.getAdapter();
                    recipeStepModels = editPhotosAdapter.getRecipeStepModels();
                    List<SlideModel> newSlideModel = new ArrayList<>();

                    for(RecipeStepModel model : recipeStepModels) {
                        newSlideModel.add(new SlideModel(model.getImage(), null,null));
                    }

                    imageSlider.setImageList(newSlideModel);

                    RecipeModel recipeFromActivity = ((CreateActivity)getActivity()).getNewRecipe();
                    recipeFromActivity.setSteps(recipeStepModels);

                    ((CreateActivity)getActivity()).setNewRecipe(recipeFromActivity);
                }
            });

        } else {
            TextView noPhotoTextView = (TextView) editDialog.findViewById(R.id.TVNoPhoto);
            noPhotoTextView.setVisibility(View.VISIBLE);
        }

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.75);

        editDialog.getWindow().setLayout(width, height);

        editDialog.show();

    }
}