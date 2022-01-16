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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
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
import com.example.cookwhat.utils.RecyclerItemClickListener;
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
    int MAX_IMAGES_ALLOWED = 9;
    int[] INGREDIENTS_ICON = Constants.INGREDIENTS_ICON;
    int[] INGREDIENTS_NAME = Constants.INGREDIENTS_NAME;
    int[] UTENSILS_ICON = Constants.UTENSILS_ICON;
    int[] UTENSIL_NAME = Constants.UTENSILS_NAME;
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
    List<Integer> displayIngredientIcon = new ArrayList<Integer>();
    List<Integer> displayIngredientName = new ArrayList<Integer>();
    List<Integer> displayUtensilIcon = new ArrayList<Integer>();
    List<Integer> displayUtensilName = new ArrayList<Integer>();
    List<String> selCustomIngredients = new ArrayList<String>();
    List<String> selCustomUtensils = new ArrayList<String>();

    IngredientAdapter ingredientAdapter;
    UtensilAdapter utensilAdapter;

    LinearLayout noPhotoDescription;


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    ArrayList<SlideModel> selectedImages = new ArrayList<>();

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
                                recipeStepModels.add(new RecipeStepModel(imageUrl.toString(), ""));
                            }

                            for(RecipeStepModel model : recipeStepModels) {
                                selectedImages.add(new SlideModel(model.getImage(), null,null));
                            }

                            imageSlider.setImageList(selectedImages);
                        } else if(data.getData() != null) {
                            Uri imagePath = data.getData();

                            // limit to 9 photos
                            if(selectedImages.size() >= 9) {
                                Toast.makeText(getActivity(), "Only " + MAX_IMAGES_ALLOWED + " images/videos allowed.", Toast.LENGTH_LONG).show();
                            } else {
                                recipeStepModels.add(new RecipeStepModel(imagePath.toString(), ""));

                                for(RecipeStepModel model : recipeStepModels) {
                                    selectedImages.add(new SlideModel(model.getImage(), null,null));
                                }

                                imageSlider.setImageList(selectedImages);
                            }
                        }

                        if (selectedImages.size() > 0) {
                            noPhotoDescription.setVisibility(View.GONE);
                            imageSlider.setVisibility(View.VISIBLE);

                        } else {
                            noPhotoDescription.setVisibility(View.VISIBLE);
                            imageSlider.setVisibility(View.GONE);
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
        boolean isEdit = ((CreateActivity)getActivity()).getIsEdit();
        recipeStepModels = recipeModel.getSteps();

        if(isEdit) {
            LinearLayout LLAddEditImages = (LinearLayout) view.findViewById(R.id.LLAddEditImages);
            LLAddEditImages.setVisibility(View.GONE);
        }

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

        noPhotoDescription = (LinearLayout) view.findViewById(R.id.NoPhotoDescription);

        imageSlider.setImageList(newSlideModel);
        if (newSlideModel.size() > 0) {
            noPhotoDescription.setVisibility(View.GONE);
            imageSlider.setVisibility(View.VISIBLE);

        } else {
            noPhotoDescription.setVisibility(View.VISIBLE);
            imageSlider.setVisibility(View.GONE);
        }

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
        ingredientRecycleView.setNestedScrollingEnabled(false);

        ingredientAdapter = new IngredientAdapter(context, ingredientModelList);
        ingredientRecycleView.setAdapter(ingredientAdapter);

        ingredientRecycleView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        context, ingredientRecycleView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        IngredientModel ingredientModel = ingredientAdapter.getIngredientList().get(position);
                        IngredientDetailDialogFragment dialog = new IngredientDetailDialogFragment(ingredientModel.getName(), ingredientModel.getIcon(),
                                ingredientModel.getQuantity(), ingredientModel.getWeight(), ingredientModel.getUnitQuantity(), ingredientModel.getUnitWeight(), ingredientModel.getMemo());
                        dialog.setDialogListener(new IngredientDetailDialogFragment.DialogListener(){

                            @Override
                            public void onFinishEditDialog(String quantity, String weight, String unitQuantity, String unitWeight, String memo) {
                                Log.d("ASDASDASD", "quantity" + quantity + quantity.isEmpty() + quantity.equals(""));
                                if(quantity != null && !quantity.isEmpty()){
                                    ingredientModel.setQuantity(Double.valueOf(quantity));
                                }

                                ingredientModel.setUnitQuantity(unitQuantity);

                                if(weight != null && !weight.isEmpty()){
                                    ingredientModel.setWeight(Double.valueOf(weight));
                                }
                                ingredientModel.setUnitWeight(unitWeight);
                                if(memo != null && !memo.isEmpty()){
                                    ingredientModel.setMemo(memo);
                                }
                                ingredientAdapter.getIngredientList().set(position, ingredientModel);
                                ingredientAdapter.notifyDataSetChanged();

                            }
                        });
                        dialog.show(((FragmentActivity)context).getSupportFragmentManager(),"dialog");

                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                }
                )
        );

        utensilModelList = recipeModel.getUtensils();
        utensilRecycleView = (RecyclerView) view.findViewById(R.id.activity_create_utensil_list);
        utensilRecycleView.setLayoutManager(linearLayoutManager1);
        utensilRecycleView.addItemDecoration(dividerItemDecoration);  //for divider
        utensilRecycleView.setNestedScrollingEnabled(false);
        utensilAdapter = new UtensilAdapter(context, utensilModelList);
        utensilRecycleView.setAdapter(utensilAdapter);

        // Initialize bottom sheet selected
        for (IngredientModel model : ingredientModelList) {
            if(model.getIcon() == -1) {
                //custom
                selCustomIngredients.add(model.getName());
            } else {
                selIngredientsItem.add(INGREDIENTS_ICON[model.getIcon()]);
            }
        }

        for (UtensilModel model : utensilModelList) {
            if(model.getIcon() == -1) {
                selCustomUtensils.add(model.getName());
            } else {
                selUtensilsItem.add(UTENSILS_ICON[model.getIcon()]);
            }
        }

        // check the number of ingredient and utensils
        checkNumberOfIngAndUt(view);

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
        editDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_white_background));

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
                    if (newSlideModel.size() > 0) {
                        noPhotoDescription.setVisibility(View.GONE);
                        imageSlider.setVisibility(View.VISIBLE);

                    } else {
                        noPhotoDescription.setVisibility(View.VISIBLE);
                        imageSlider.setVisibility(View.GONE);
                    }

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

        displayIngredientIcon = Arrays.stream(INGREDIENTS_ICON).boxed().collect(Collectors.toList());
        displayIngredientName = Arrays.stream(INGREDIENTS_NAME).boxed().collect(Collectors.toList());

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
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), INGREDIENTS_NAME, INGREDIENTS_ICON) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selIngredientsItem.contains(displayIngredientIcon.get(position))) {
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
                    if (selIngredientsItem.contains(displayIngredientIcon.get(position))) {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        selIngredientsItem.remove(Integer.valueOf(displayIngredientIcon.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(displayIngredientName.get(position)));
                        ingredientModel.setIcon(getIndex(INGREDIENTS_ICON, displayIngredientIcon.get(position)));
                        ingredientAdapter.removeIngredient(ingredientModel);

                        recipeModel.setIngredients(ingredientAdapter.getIngredientList());
                    } else {
                        viewPrev = (View) ingredientsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selIngredientsItem.add(displayIngredientIcon.get(position));
                        IngredientModel ingredientModel = new IngredientModel();
                        ingredientModel.setName(getString(displayIngredientName.get(position)));
                        ingredientModel.setIcon(getIndex(INGREDIENTS_ICON, displayIngredientIcon.get(position)));

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
                displayIngredientIcon.clear();
                displayIngredientName.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<INGREDIENTS_NAME.length; i++) {
                    if(getResources().getString(INGREDIENTS_NAME[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayIngredientIcon.add(INGREDIENTS_ICON[i]);
                        displayIngredientName.add(INGREDIENTS_NAME[i]);
                        searchName.add(INGREDIENTS_NAME[i]);

                        if (selIngredientsItem.contains(displayIngredientIcon.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayIngredientIcon.size() > 0) {
                    ingredientsGridView.setVisibility(View.VISIBLE);
                    ingredientLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayIngredientIcon.stream().mapToInt(i -> i).toArray();
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
                    IngredientModel ingredientModel = new IngredientModel();
                    ingredientModel.setName(chipItem);
                    ingredientAdapter.removeIngredient(ingredientModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkNumberOfIngAndUt(view);
            }
        });

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

        displayUtensilIcon = Arrays.stream(UTENSILS_ICON).boxed().collect(Collectors.toList());
        displayUtensilName = Arrays.stream(UTENSIL_NAME).boxed().collect(Collectors.toList());

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
        MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(view.getContext(), UTENSIL_NAME, UTENSILS_ICON) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = Color.TRANSPARENT; // Transparent
                if (selUtensilsItem.contains(displayUtensilIcon.get(position))) {
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
                    if (selUtensilsItem.contains(displayUtensilIcon.get(position))) {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        selUtensilsItem.remove(Integer.valueOf(displayUtensilIcon.get(position)));
                        viewPrev.setBackgroundColor(Color.TRANSPARENT);

                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(displayUtensilName.get(position)));
                        utensilModel.setIcon(getIndex(UTENSILS_ICON, displayUtensilIcon.get(position)));
                        utensilAdapter.removeUtensil(utensilModel);

                        recipeModel.setUtensils(utensilAdapter.getUtensilList());
                    } else {
                        viewPrev = (View) utensilsGridView.getChildAt(position);
                        view.setBackgroundColor(getResources().getColor(R.color.light_yellow));
                        selUtensilsItem.add(displayUtensilIcon.get(position));
                        UtensilModel utensilModel = new UtensilModel();
                        utensilModel.setName(getString(displayUtensilName.get(position)));
                        utensilModel.setIcon(getIndex(UTENSILS_ICON, displayUtensilIcon.get(position)));
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
                displayUtensilIcon.clear();
                displayUtensilName.clear();
                List<Integer> searchName = new ArrayList<>();
                List<Integer> selectedIndex = new ArrayList<>();

                int newIndex = 0;
                for(int i=0; i<UTENSIL_NAME.length; i++) {
                    if(getResources().getString(UTENSIL_NAME[i]).toLowerCase().contains(s.toString().toLowerCase())) {
                        displayUtensilIcon.add(UTENSILS_ICON[i]);
                        displayUtensilName.add(UTENSIL_NAME[i]);
                        searchName.add(UTENSIL_NAME[i]);

                        if (selUtensilsItem.contains(displayUtensilIcon.get(newIndex))) {
                            selectedIndex.add(newIndex);
                        }

                        newIndex++;
                    }
                }

                if (displayUtensilIcon.size() > 0) {
                    utensilsGridView.setVisibility(View.VISIBLE);
                    utensilLayoutCantFind.setVisibility(View.GONE);

                    int[] searchIconArray = displayUtensilIcon.stream().mapToInt(i -> i).toArray();
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
                    UtensilModel utensilModel = new UtensilModel();
                    utensilModel.setName(chipItem);
                    utensilAdapter.removeUtensil(utensilModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkNumberOfIngAndUt(view);
            }
        });

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
                    IngredientModel ingredientModel = new IngredientModel();
                    ingredientModel.setName(newItem);
                    ingredientModel.setIcon(-1);
                    ingredientAdapter.removeIngredient(ingredientModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomIngredients.add(newItem);
            IngredientModel ingredientModel = new IngredientModel();
            ingredientModel.setName(newItem);
            ingredientModel.setIcon(-1);
            ingredientAdapter.addIngredient(ingredientModel);
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
                    selCustomUtensils.remove(newItem);
                    UtensilModel utensilModel = new UtensilModel();
                    utensilModel.setName(newItem);
                    utensilModel.setIcon(-1);
                    utensilAdapter.removeUtensil(utensilModel);
                }
            });
            chip.setTextColor(getResources().getColor(R.color.black));

            chipGroup.addView(chip);
            selCustomUtensils.add(newItem);
            UtensilModel utensilModel = new UtensilModel();
            utensilModel.setName(newItem);
            utensilModel.setIcon(-1);
            utensilAdapter.addUtensil(utensilModel);
        }
    }

    private void checkNumberOfIngAndUt(View view){
        List<IngredientModel> ingredientModels = recipeModel.getIngredients();
        List<UtensilModel> utensilModels = recipeModel.getUtensils();

        TextView TVNoIngredientAdded = (TextView) view.findViewById(R.id.TVNoIngredientAdded);
        TextView TVNoUtensilAdded = (TextView) view.findViewById(R.id.TVNoUtensilAdded);
        TextView TVEditInstruction = (TextView) view.findViewById(R.id.TVEditInstruction);

        if (ingredientModels.size() > 0) {
            TVNoIngredientAdded.setVisibility(View.GONE);
            TVEditInstruction.setVisibility(View.VISIBLE);
        } else {
            TVNoIngredientAdded.setVisibility(View.VISIBLE);
            TVEditInstruction.setVisibility(View.GONE);
        }

        if (utensilModels.size() > 0) {
            TVNoUtensilAdded.setVisibility(View.GONE);
        } else {
            TVNoUtensilAdded.setVisibility(View.VISIBLE);
        }
    }

    private int getIndex(int[] list, int item) {
        for(int i=0; i<list.length; i++) {
            if (list[i] == item){
                return i;
            }
        }
        return -1;
    }
}