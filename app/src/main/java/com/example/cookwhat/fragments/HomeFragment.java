package com.example.cookwhat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.RecipeAdapter;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.example.cookwhat.models.UserModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recipeList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recipeList = view.findViewById(R.id.recipeList);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Vegan");
        tags.add("Organic");

        RecipeStepModel recipeStepModel1 = new RecipeStepModel("https://img.jakpost.net/c/2020/03/02/2020_03_02_87967_1583122954._large.jpg", "step1 of recipe");
        RecipeStepModel recipeStepModel2 = new RecipeStepModel("IMG URL2", "step2 of recipe");

        ArrayList<RecipeStepModel> steps1 = new ArrayList<>();
        steps1.add(recipeStepModel1);
        steps1.add(recipeStepModel2);

        RecipeStepModel recipeStepModel3 = new RecipeStepModel("https://www.healthyeating.org/images/default-source/home-0.0/nutrition-topics-2.0/milk-dairy/2-1-3-1dairyfoods_cheese_detailfeature.jpg?sfvrsn=a580dd8c_4", "step1 of recipe");
        ArrayList<RecipeStepModel> steps2 = new ArrayList<>();
        steps2.add(recipeStepModel3);

        RecipeModel recipeModel1 = new RecipeModel();
        recipeModel1.setTitle("Turkey Sucks");
        recipeModel1.setTags(tags);
        recipeModel1.setNum_fav(6);
        recipeModel1.setId(9999111);
        recipeModel1.setUserId(12345);
        recipeModel1.setSteps(steps1);

        RecipeModel recipeModel2 = new RecipeModel();
        recipeModel2.setTitle("Turkey Yums");
        recipeModel2.setTags(tags);
        recipeModel2.setNum_fav(10);
        recipeModel2.setId(9999111);
        recipeModel2.setUserId(12345);
        recipeModel2.setSteps(steps2);

        ArrayList<RecipeModel> recipeModelsArray = new ArrayList<>();
        recipeModelsArray.add(recipeModel1);
        recipeModelsArray.add(recipeModel2);

        UserModel userModel1 = new UserModel();
        userModel1.setUserName("PJasmine2328u4u23");

        UserModel userModel2 = new UserModel();
        userModel2.setUserName("PAladdin");

        ArrayList<UserModel> userModelsArray = new ArrayList<>();
        userModelsArray.add(userModel1);
        userModelsArray.add(userModel2);

        RecipeAdapter adapter = new RecipeAdapter(recipeModelsArray, userModelsArray, this.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
        recipeList.setLayoutManager(gridLayoutManager);
        recipeList.setAdapter(adapter);

        return view;
    }
}