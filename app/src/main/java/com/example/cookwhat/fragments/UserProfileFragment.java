package com.example.cookwhat.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookwhat.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    GridView tabcontent;

    public UserProfileFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        TabItem secretRecipe = view.findViewById(R.id.TL_SecretRecipe);
        TabItem aboutMe = view.findViewById(R.id.TL_AboutMe);

        Button favCategory = view.findViewById(R.id.Btn_Favourite);

        View.OnClickListener favCategoryOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to fav category
            }
        };

        favCategory.setOnClickListener(favCategoryOCL);

        View.OnClickListener secretRecipeOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tabcontent = view.findViewById(R.id.tabcontent);


                //CustomAdapter tabAdapter = new CustomAdapter(userData);   pass userID return recipeID, arg: recipe class
                tabcontent.setAdapter(tabAdapter);

                //method of click btn of tabcontent
            }
        };

        secretRecipe.setOnClickListener(secretRecipeOCL);

    }

    private class CustomAdapter extends BaseAdapter{



        private CustomAdapter(RecipeModel  Recipe){

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
}