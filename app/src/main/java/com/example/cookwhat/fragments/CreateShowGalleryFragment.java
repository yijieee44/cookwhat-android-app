package com.example.cookwhat.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateShowGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateShowGalleryFragment extends Fragment {
    ImageSlider imageSlider;
    ArrayList<SlideModel> selected_images = new ArrayList<>();
    int MAX_IMAGES_ALLOWED = 9;

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
                            if(selected_images.size() + count > MAX_IMAGES_ALLOWED) {
                                Toast.makeText(getActivity(), "Only " + MAX_IMAGES_ALLOWED + " images/videos allowed.", Toast.LENGTH_LONG).show();
                                count = MAX_IMAGES_ALLOWED - selected_images.size();
                            }

                            for (int i = 0; i < count; i++) {
                                Uri imageUrl = data.getClipData().getItemAt(i).getUri();
                                selected_images.add(new SlideModel(imageUrl.toString(), null,null));
                            }
                            imageSlider.setImageList(selected_images);
                        } else if(data.getData() != null) {
                            Uri imagePath = data.getData();

                            // limit to 9 photos
                            if(selected_images.size() >= 9) {
                                Toast.makeText(getActivity(), "Only " + MAX_IMAGES_ALLOWED + " images/videos allowed.", Toast.LENGTH_LONG).show();
                            } else {
                                selected_images.add(new SlideModel(imagePath.toString(), null, null));
                                imageSlider.setImageList(selected_images);
                            }
                        }
                    }
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

        Button button = (Button) view.findViewById(R.id.BtnAddImages);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getImagesFromGallery();
            }
        });

        imageSlider = view.findViewById(R.id.CreateImageSlider);

        ArrayList<SlideModel> images = new ArrayList<>();
        imageSlider.setImageList(images);

        return view;

    }

    public void getImagesFromGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
    }
}