package com.example.cookwhat.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.CreateActivity;
import com.example.cookwhat.adapters.EditPhotosAdapter;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.MarketIngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.example.cookwhat.models.UtensilModel;
import com.example.cookwhat.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    RecipeModel recipeModel;

    RecyclerView ingredientRecycleView;
    RecyclerView utensilRecycleView;

    List<IngredientModel> ingredientModelList;
    List<UtensilModel> utensilModelList;
    Context context;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager1;

    EditText searchIngredients;
    EditText searchUtensils;
    BottomSheetDialog bottomSheetDialog;

    List<Integer> selIngredientsItem = new ArrayList<Integer>();
    List<Integer> selUtensilsItem = new ArrayList<Integer>();
    List<Integer> displayIngredientItem = new ArrayList<Integer>();
    List<Integer> displayUtensilItem = new ArrayList<Integer>();
    List<String> selCustomIngredients = new ArrayList<String>();
    List<String> selCustomUtensils = new ArrayList<String>();

    IngredientAdapter ingredientAdapter;
    UtensilAdapter utensilAdapter;


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
        recipeModel = ((CreateActivity)getActivity()).getNewRecipe();
        recipeStepModels = recipeModel.getSteps();

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

        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager1 = new LinearLayoutManager(context);


        Button selectIngredient = (Button) view.findViewById(R.id.BtnSelectIngredient);
        selectIngredient.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                BtnIngredientsBottomSheet(view);
            }
        });

        Button selectUtensil = (Button) view.findViewById(R.id.BtnSelectUtensil);
        selectUtensil.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                BtnUtensilsBottomSheet(view);
            }
        });

        // set list
        ingredientModelList = recipeModel.getIngredients();
        ingredientRecycleView = (RecyclerView) view.findViewById(R.id.activity_create_ingredient_list);
        ingredientRecycleView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientRecycleView.getContext(), linearLayoutManager.getOrientation());
        ingredientRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        ingredientAdapter = new IngredientAdapter(context, ingredientModelList);
        ingredientRecycleView.setAdapter(ingredientAdapter);

        utensilModelList = recipeModel.getUtensils();
        utensilRecycleView = (RecyclerView) view.findViewById(R.id.activity_create_utensil_list);
        utensilRecycleView.setLayoutManager(linearLayoutManager1);
        utensilRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        utensilAdapter = new UtensilAdapter(context, utensilModelList);
        utensilRecycleView.setAdapter(utensilAdapter);


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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BtnIngredientsBottomSheet(View view) {

        bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_ingredients);

        try {
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setDraggable(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int[] ingredientsIcon = Constants.INGREDIENTS_ICON;
        int[] ingredientsName = Constants.INGREDIENTS_NAME;

        displayIngredientItem = Arrays.stream(ingredientsIcon).boxed().collect(Collectors.toList());

        GridView ingredientsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Ingredients);
        LinearLayout ingredientLayoutCantFind = (LinearLayout) bottomSheetDialog.findViewById(R.id.LayoutIngCantFind);
        Button customIngredientButton = bottomSheetDialog.findViewById(R.id.BtnCustomIngredients);
        customIngredientButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        BtnAddCustom(view);
                    }
                }
        );
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), ingredientsName, ingredientsIcon) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selIngredientsItem.contains(displayIngredientItem.get(position))) {
                    color = getResources().getColor(R.color.light_yellow);
                }

                view.setBackgroundColor(color);

                return view;
            }
        };

        ingredientsGridView.setAdapter(marketIngredientAdapter);

        ingredientsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View viewPrev;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (selIngredientsItem.contains(displayIngredientItem.get(position))) {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        selIngredientsItem.remove(Integer.valueOf(displayIngredientItem.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(ingredientsName[position]));
                        ingredientAdapter.removeIngredient(ingredientModel);

                        recipeModel.setIngredients(ingredientAdapter.getIngredientList());
                    } else {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selIngredientsItem.add(displayIngredientItem.get(position));
                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(ingredientsName[position]));
                        ingredientModel.setIcon(ingredientsIcon[position]);
                        ingredientAdapter.addIngredient(ingredientModel);

                        recipeModel.setIngredients(ingredientAdapter.getIngredientList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        searchIngredients = (EditText) bottomSheetDialog.findViewById(R.id.EditTextSearchIngredient);
        searchIngredients.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void afterTextChanged(Editable s) {
                displayIngredientItem.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<ingredientsName.length; i++) {
                    if(getResources().getString(ingredientsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayIngredientItem.add(ingredientsIcon[i]);
                        searchName.add(ingredientsName[i]);

                        if (selIngredientsItem.contains(displayIngredientItem.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayIngredientItem.size() > 0) {
                    ingredientsGridView.setVisibility(View.VISIBLE);
                    ingredientLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayIngredientItem.stream().mapToInt(i -> i).toArray();
                    int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                    MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), searchNameArray, searchIconArray) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            int color = Color.TRANSPARENT; // Transparent
                            if (selectedIndex.contains(position)) {
                                color = getResources().getColor(R.color.light_yellow); // Opaque Blue
                            }

                            view.setBackgroundColor(color);

                            return view;
                        }
                    };


                    ingredientsGridView.setAdapter(marketIngredientAdapter);
                } else {
                    ingredientsGridView.setVisibility(View.GONE);
                    ingredientLayoutCantFind.setVisibility(View.VISIBLE);
                    Button btnCustomIngredient = (Button) ingredientLayoutCantFind.findViewById(R.id.BtnCustomIngredients);
                    btnCustomIngredient.setText(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomIngredient);

        for (int i=0; i<selCustomIngredients.size(); i++) {
            String chipItem = selCustomIngredients.get(i);
            Chip chip = new Chip(view.getContext());
            chip.setText(chipItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(chipItem);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }


        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BtnUtensilsBottomSheet(View view) {

        bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_utensils);

        try {
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setDraggable(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int[] utensilsIcon = Constants.UTENSILS_ICON;
        int[] utensilsName = Constants.UTENSILS_NAME;

        displayUtensilItem = Arrays.stream(utensilsIcon).boxed().collect(Collectors.toList());

        GridView utensilsGridView = (GridView) bottomSheetDialog.findViewById(R.id.Grid_Market_Utensils);
        LinearLayout utensilLayoutCantFind = (LinearLayout) bottomSheetDialog.findViewById(R.id.LayoutUntCantFind);
        Button customUtensilButton = bottomSheetDialog.findViewById(R.id.BtnCustomUtensils);
        customUtensilButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        BtnAddCustomUtensil(view);
                    }
                }
        );
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), utensilsName, utensilsIcon) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selUtensilsItem.contains(displayUtensilItem.get(position))) {
                    color = getResources().getColor(R.color.light_yellow);
                }

                view.setBackgroundColor(color);

                return view;
            }
        };

        utensilsGridView.setAdapter(marketIngredientAdapter);

        utensilsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View viewPrev;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (selUtensilsItem.contains(displayUtensilItem.get(position))) {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        selUtensilsItem.remove(Integer.valueOf(displayUtensilItem.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(utensilsName[position]));
                        utensilAdapter.removeUtensil(utensilModel);

                        recipeModel.setUtensils(utensilAdapter.getUtensilList());
                    } else {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selUtensilsItem.add(displayUtensilItem.get(position));
                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(utensilsName[position]));
                        utensilModel.setIcon(utensilsIcon[position]);
                        utensilAdapter.addUtensil(utensilModel);

                        recipeModel.setUtensils(utensilAdapter.getUtensilList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        searchUtensils = (EditText) bottomSheetDialog.findViewById(R.id.EditTextSearchUtensil);
        searchUtensils.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void afterTextChanged(Editable s) {
                displayUtensilItem.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<utensilsName.length; i++) {
                    if(getResources().getString(utensilsName[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayUtensilItem.add(utensilsIcon[i]);
                        searchName.add(utensilsName[i]);

                        if (selUtensilsItem.contains(displayUtensilItem.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayUtensilItem.size() > 0) {
                    utensilsGridView.setVisibility(View.VISIBLE);
                    utensilLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayUtensilItem.stream().mapToInt(i -> i).toArray();
                    int[] searchNameArray = searchName.stream().mapToInt(i -> i).toArray();

                    MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), searchNameArray, searchIconArray) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            int color = Color.TRANSPARENT; // Transparent
                            if (selectedIndex.contains(position)) {
                                color = getResources().getColor(R.color.light_yellow); // Opaque Blue
                            }

                            view.setBackgroundColor(color);

                            return view;
                        }
                    };


                    utensilsGridView.setAdapter(marketIngredientAdapter);
                } else {
                    utensilsGridView.setVisibility(View.GONE);
                    utensilLayoutCantFind.setVisibility(View.VISIBLE);
                    Button btnCustomUtensil = (Button) utensilLayoutCantFind.findViewById(R.id.BtnCustomUtensils);
                    btnCustomUtensil.setText(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomUtensil);

        for (int i=0; i<selCustomUtensils.size(); i++) {
            String chipItem = selCustomUtensils.get(i);
            Chip chip = new Chip(view.getContext());
            chip.setText(chipItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomUtensils.remove(chipItem);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }


        bottomSheetDialog.show();
    }

    public void BtnAddCustom(View view) {
        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomIngredient);
        String newItem = searchIngredients.getText().toString();

        if (!selCustomIngredients.contains(newItem)) {
            Chip chip = new Chip(view.getContext());
            chip.setText(newItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(newItem);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomIngredients.add(newItem);
        }
    }

    public void BtnAddCustomUtensil(View view) {
        ChipGroup chipGroup = (ChipGroup) bottomSheetDialog.findViewById(R.id.ChipGroupCustomUtensil);
        String newItem = searchUtensils.getText().toString();

        if (!selCustomUtensils.contains(newItem)) {
            Chip chip = new Chip(view.getContext());
            chip.setText(newItem);
            chip.setChipBackgroundColorResource(R.color.light_yellow);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroup.removeView(chip);
                    selCustomIngredients.remove(newItem);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomUtensils.add(newItem);
        }
    }
}