package com.example.cookwhat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.SearchResultAdapter;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchKeywordResultActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String currentUserId;
    ArrayList<RecipeModelDB> recipeList;
    ArrayList<UserModelDB> recipeUserList ;
    ArrayList<UserModelDB> userList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword_result);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        recipeList =  (ArrayList<RecipeModelDB>)getIntent().getSerializableExtra("recipeList");
        recipeUserList =  (ArrayList<UserModelDB>)getIntent().getSerializableExtra("recipeUserList");
        userList =  (ArrayList<UserModelDB>)getIntent().getSerializableExtra("userList");
        context = this;

        readData(currentUserId,new FirestoreOnCallBack() {
            @Override
            public void onCallBack(UserModelDB userModelDB) {
                TextView recipeResult = findViewById(R.id.TVRecipeSearchResult);
                TextView userResult = findViewById(R.id.TVUserSearchResult);
                TextView notFound = findViewById(R.id.TVKeywordNotFound);
                notFound.setVisibility(View.GONE);

                View recipeDivider = findViewById(R.id.RecipeDivider);
                View userDivider = findViewById(R.id.UserDivider);

                int index = -1;
                for(UserModelDB user: userList){
                    if(user.getUserId().equals(userModelDB.getUserId())){
                        index = userList.indexOf(user);
                        break;
                    }
                }
                if(index >-1){
                    userList.remove(index);
                }



                System.out.println("InsearchKeyword, getcurrentUser"+ userModelDB.getUserId());

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
                LinearLayoutManager userLinearLayoutManager = new LinearLayoutManager(context);
                userRecyclerView.setLayoutManager(userLinearLayoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(userRecyclerView.getContext(), userLinearLayoutManager.getOrientation());
                userRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider
                SearchResultAdapter userAdapter = new SearchResultAdapter(userList, context, userModelDB);
                userRecyclerView.setAdapter(userAdapter);

                LinearLayoutManager recipeLinearLayoutManager = new LinearLayoutManager(context);
                RecyclerView recipeRecyclerView = findViewById(R.id.RVRecipeKeywordResult);
                recipeRecyclerView.setLayoutManager(recipeLinearLayoutManager);
                DividerItemDecoration recipeDividerItemDecoration = new DividerItemDecoration(recipeRecyclerView.getContext(), recipeLinearLayoutManager.getOrientation());
                recipeRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider
                SearchResultAdapter recipeAdapter = new SearchResultAdapter(recipeList, recipeUserList, context);
                recipeRecyclerView.setAdapter(recipeAdapter);

            }
        });



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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(122, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(122, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void readData(String currentUserId, FirestoreOnCallBack firestoreOnCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").document(currentUserId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        UserModelDB userModelDB = new UserModelDB();
                        userModelDB = documentSnapshot.toObject(UserModelDB.class);
                        Log.d("SUCCESS", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        firestoreOnCallBack.onCallBack(userModelDB);
                    }
                });
    }

    interface FirestoreOnCallBack{
        void onCallBack(UserModelDB userModelDB);
    }
}