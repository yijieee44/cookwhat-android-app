package com.example.cookwhat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.SearchResultAdapter;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.models.UserModelDB;

import java.util.ArrayList;

public class SearchKeywordResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword_result);

        TextView recipeResult = findViewById(R.id.TVRecipeSearchResult);
        TextView userResult = findViewById(R.id.TVUserSearchResult);
        TextView notFound = findViewById(R.id.TVKeywordNotFound);
        notFound.setVisibility(View.GONE);

        View recipeDivider = findViewById(R.id.RecipeDivider);
        View userDivider = findViewById(R.id.UserDivider);

        ArrayList<RecipeModelDB> recipeList =  (ArrayList<RecipeModelDB>)getIntent().getSerializableExtra("recipeList");
        ArrayList<UserModelDB> recipeUserList =  (ArrayList<UserModelDB>)getIntent().getSerializableExtra("recipeUserList");
        ArrayList<UserModelDB> userList =  (ArrayList<UserModelDB>)getIntent().getSerializableExtra("userList");

        if(userList.size() <= 0 && recipeList.size()<=0){
            notFound.setVisibility(View.VISIBLE);
        }
        if(userList.size()<=0){
            userResult.setVisibility(View.GONE);
            userDivider.setVisibility(View.GONE);
        }
        else{
            userResult.setVisibility(View.VISIBLE);
            userDivider.setVisibility(View.VISIBLE);
        }

        if(recipeList.size()<=0){
            recipeResult.setVisibility(View.GONE);
            recipeDivider.setVisibility(View.GONE);
        }
        else{
            recipeResult.setVisibility(View.VISIBLE);
            recipeDivider.setVisibility(View.VISIBLE);
        }


        RecyclerView userRecyclerView = findViewById(R.id.RVUserKeywordResult);
        LinearLayoutManager userLinearLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(userLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(userRecyclerView.getContext(), userLinearLayoutManager.getOrientation());
        userRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider
        SearchResultAdapter userAdapter = new SearchResultAdapter(userList, this);
        userRecyclerView.setAdapter(userAdapter);

        LinearLayoutManager recipeLinearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recipeRecyclerView = findViewById(R.id.RVRecipeKeywordResult);
        recipeRecyclerView.setLayoutManager(recipeLinearLayoutManager);
        DividerItemDecoration recipeDividerItemDecoration = new DividerItemDecoration(recipeRecyclerView.getContext(), recipeLinearLayoutManager.getOrientation());
        recipeRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider
        SearchResultAdapter recipeAdapter = new SearchResultAdapter(recipeList, recipeUserList, this);
        recipeRecyclerView.setAdapter(recipeAdapter);


//        for(UserModelDB userModelDB: userList){
//            Log.d("USERLIST::", userModelDB.getUserName());
//        }
//
//        for(RecipeModelDB recipeModelDB: recipeList){
//            Log.d("RECIPELISTTITLE::", recipeModelDB.getTitle());
//            for(String tag: recipeModelDB.getTags()){
//                Log.d("RECIPELISTTAG::", tag);
//            }
//
//        }

    }
}