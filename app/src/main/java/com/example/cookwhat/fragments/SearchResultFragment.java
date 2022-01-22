package com.example.cookwhat.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.cookwhat.activities.ViewProfileActivity;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.adapters.RecipeAdapter;
import com.example.cookwhat.models.RecipeModelSearch;
import com.example.cookwhat.models.UserModelDB;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {

    List<RecipeModelSearch> recipeSearchResult;
    List<UserModelDB>  userList;
    RecipeAdapter adapter;
    RecyclerView recipeList;
    TextView noResult;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    ActivityResultLauncher<Intent> viewActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        updateRecipeAdapter();
                    }
                }
            });

    private void updateRecipeAdapter(){
        ((SearchActivity)getActivity()).search(new SearchActivity.SearchFirestoreCallback() {
            @Override
            public void onCallBack(List<RecipeModelSearch> recipeModelArrayList, List<UserModelDB> userModelArrayList) {
                if(recipeModelArrayList.size()<=0){
                    noResult.setVisibility(View.VISIBLE);
                }
                else{
                    noResult.setVisibility(View.GONE);
                }
                adapter.updateAdapter(new ArrayList<>(recipeModelArrayList), new ArrayList<>(userModelArrayList), 1);
                adapter.setListener(new RecipeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                        UserModelDB userModelDB = new UserModelDB();
                        intent.putExtra("recipeModel", recipeModelArrayList.get(position));
                        for (int i = 0; i < userModelArrayList.size(); i++) {
                            if (recipeModelArrayList.get(position).getUserId().equals(userModelArrayList.get(i).getUserId())) {
                                userModelDB = userModelArrayList.get(i);
                                break;
                            }
                        }

                        intent.putExtra("userModel", userModelDB);
                        viewActivityResultLauncher.launch(intent);
                    }

                    @Override
                    public void onUserClick(View view, int position) {
                        String userId = recipeModelArrayList.get(position).getUserId();
                        adapter.readCurrentUser(new RecipeAdapter.FirestoreCallback2() {
                            @Override
                            public void onCallBack(UserModelDB currentUser) {
                                if(currentUser.getUserId().equals(userId)){

                                }
                                else{
                                    Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("userModel", currentUser);
                                    viewActivityResultLauncher.launch(intent);
                                }
                            }
                        });
                    }
                });
            }
        });
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
            noResult = view.findViewById(R.id.TVNoResult);
            if(recipeSearchResult.size() <=0){

                noResult.setVisibility(View.VISIBLE);
            }
            else{
                noResult.setVisibility(View.GONE);
                recipeList = view.findViewById(R.id.RVSearchResult);
                adapter = new RecipeAdapter(new ArrayList<RecipeModelSearch>(recipeSearchResult), new ArrayList<UserModelDB>(userList), getContext(), 1);
                adapter.setListener(new RecipeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                        UserModelDB userModelDB = new UserModelDB();
                        intent.putExtra("recipeModel", recipeSearchResult.get(position));
                        for (int i = 0; i < userList.size(); i++) {
                            if (recipeSearchResult.get(position).getUserId().equals(userList.get(i).getUserId())) {
                                userModelDB = userList.get(i);
                                break;
                            }
                        }

                        intent.putExtra("userModel", userModelDB);
                        viewActivityResultLauncher.launch(intent);
                    }

                    @Override
                    public void onUserClick(View view, int position) {
                        String userId = recipeSearchResult.get(position).getUserId();
                        adapter.readCurrentUser(new RecipeAdapter.FirestoreCallback2() {
                            @Override
                            public void onCallBack(UserModelDB currentUser) {
                                if(currentUser.getUserId().equals(userId)){

                                }
                                else{
                                    Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("userModel", currentUser);
                                    viewActivityResultLauncher.launch(intent);
                                }

                            }
                        });
                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                recipeList.setLayoutManager(gridLayoutManager);
                recipeList.setAdapter(adapter);
            }
        }
        else{
            ((SearchActivity)getActivity()).onBackPressed();
        }




        return view;
    }
}