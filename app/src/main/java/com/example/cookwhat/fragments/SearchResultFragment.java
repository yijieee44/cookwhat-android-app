package com.example.cookwhat.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.SearchActivity;
import com.example.cookwhat.adapters.RecipeAdapter;
import com.example.cookwhat.models.RecipeModelSearch;
import com.example.cookwhat.models.UserModelDB;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {

    List<RecipeModelSearch> recipeSearchResult;
    List<UserModelDB>  userList;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        if(((SearchActivity)getActivity()).getUserList() != null && ((SearchActivity)getActivity()).getRecipeSearchResult() != null){
            userList = ((SearchActivity)getActivity()).getUserList();
            recipeSearchResult = ((SearchActivity)getActivity()).getRecipeSearchResult();
            TextView noResult = view.findViewById(R.id.TVNoResult);
            if(recipeSearchResult.size() <=0){

                noResult.setVisibility(View.VISIBLE);
            }
            else{
                noResult.setVisibility(View.GONE);
                RecyclerView recipeList = view.findViewById(R.id.RVSearchResult);
                RecipeAdapter adapter = new RecipeAdapter(new ArrayList<RecipeModelSearch>(recipeSearchResult), new ArrayList<UserModelDB>(userList), getContext(), 1);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                recipeList.setLayoutManager(gridLayoutManager);
                recipeList.setAdapter(adapter);
            }
        }
        else{
            ((SearchActivity)getActivity()).toIngredient();
        }




        return view;
    }
}