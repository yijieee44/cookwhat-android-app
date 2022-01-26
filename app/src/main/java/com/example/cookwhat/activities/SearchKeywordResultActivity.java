package com.example.cookwhat.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.SearchResultAdapter;
import com.example.cookwhat.fragments.HomeFragment;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchKeywordResultActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String currentUserId;
    ArrayList<RecipeModelDB> recipeList;
    ArrayList<UserModelDB> recipeUserList ;
    ArrayList<UserModelDB> userList;
    Context context;
    String query;

    public CollectionReference recipedb;
    public CollectionReference userdb;

    Dialog loadingDialog;

    TextView recipeResult;
    TextView userResult;
    TextView notFound;
    View recipeDivider;
    View userDivider;

    RecyclerView userRecyclerView;
    SearchResultAdapter userAdapter;
    RecyclerView recipeRecyclerView;
    SearchResultAdapter recipeAdapter;

    UserModelDB currentUser;
    ActivityResultLauncher<Intent> viewActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        updateAdapter();
                    }
                }
            });







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword_result);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recipedb = db.collection("recipe");
        userdb = db.collection("user");
        loadingDialog = new Dialog(this);


        recipeResult = findViewById(R.id.TVRecipeSearchResult);
        userResult = findViewById(R.id.TVUserSearchResult);
        notFound = findViewById(R.id.TVKeywordNotFound);
        notFound.setVisibility(View.GONE);

        recipeDivider = findViewById(R.id.RecipeDivider);
        userDivider = findViewById(R.id.UserDivider);

        setInvisible();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        query = (String)getIntent().getSerializableExtra("query");

        context = this;

        readData(currentUserId, new FirestoreOnCallBack() {
            @Override
            public void onCallBack(UserModelDB userModelDB) {
                currentUser = userModelDB;
                search(query, new FirestoreOnCallBack(){
                    @Override
                    public void onCallBack(UserModelDB userModelDB) {

                    }

                    @Override
                    public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList) {
                        recipeList =  recipeModelArrayList;
                        recipeUserList =  recipeUserModelArrayList;
                        userList =  userModelArrayList;
                        createView();
                    }
                });
            }

            @Override
            public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList) {

            }
        });


    }

    private void updateAdapter(){
        setInvisible();
        search(query, new FirestoreOnCallBack(){
            @Override
            public void onCallBack(UserModelDB userModelDB) {

            }

            @Override
            public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList) {
                userAdapter.updateAdapter(userModelArrayList, currentUser);
                userAdapter.setListener(new SearchResultAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String userId = userModelArrayList.get(position).getUserId();
                        Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("userModel", currentUser);
                        viewActivityResultLauncher.launch(intent);
                    }
                });
                recipeAdapter.updateAdapter(recipeModelArrayList, recipeUserModelArrayList);
                recipeAdapter.setListener(new SearchResultAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);

                        UserModelDB userModelDB = new UserModelDB();
                        intent.putExtra("recipeModel", recipeModelArrayList.get(position));
                        for (int i=0;i<recipeUserModelArrayList.size();i++){
                            if(recipeModelArrayList.get(position).getUserId().equals(recipeUserModelArrayList.get(i).getUserId())){
                                userModelDB = recipeUserModelArrayList.get(i);
                                Log.d("GOT USER", "wasd");
                                break;
                            }
                        }
                        intent.putExtra("userModel", userModelDB);
                        viewActivityResultLauncher.launch(intent);
                    }
                });
                setVisibility();
            }
        });
    }

    private void createView(){

        int index = -1;
        for(UserModelDB user: userList){
            if(user.getUserId().equals(currentUser.getUserId())){
                index = userList.indexOf(user);
                break;
            }
        }
        if(index >-1){
            userList.remove(index);
        }



        System.out.println("InsearchKeyword, getcurrentUser"+ currentUser.getUserId());

        setVisibility();


        userRecyclerView = findViewById(R.id.RVUserKeywordResult);
        LinearLayoutManager userLinearLayoutManager = new LinearLayoutManager(context);
        userRecyclerView.setLayoutManager(userLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(userRecyclerView.getContext(), userLinearLayoutManager.getOrientation());
        userRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider
        userAdapter = new SearchResultAdapter(userList, context, currentUser);
        userAdapter.setListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String userId = userList.get(position).getUserId();
                Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userModel", currentUser);
                viewActivityResultLauncher.launch(intent);
            }
        });
        userRecyclerView.setAdapter(userAdapter);

        LinearLayoutManager recipeLinearLayoutManager = new LinearLayoutManager(context);
        recipeRecyclerView = findViewById(R.id.RVRecipeKeywordResult);
        recipeRecyclerView.setLayoutManager(recipeLinearLayoutManager);
        DividerItemDecoration recipeDividerItemDecoration = new DividerItemDecoration(recipeRecyclerView.getContext(), recipeLinearLayoutManager.getOrientation());
        recipeRecyclerView.addItemDecoration(recipeDividerItemDecoration);  //for divider
        recipeAdapter = new SearchResultAdapter(recipeList, recipeUserList, context);

        recipeAdapter.setListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);

                UserModelDB userModelDB = new UserModelDB();
                intent.putExtra("recipeModel", recipeList.get(position));
                for (int i=0;i<recipeUserList.size();i++){
                    if(recipeList.get(position).getUserId().equals(recipeUserList.get(i).getUserId())){
                        userModelDB = recipeUserList.get(i);
                        Log.d("GOT USER", "wasd");
                        break;
                    }
                }
                intent.putExtra("userModel", userModelDB);
                viewActivityResultLauncher.launch(intent);
            }
        });
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    private void setInvisible(){
        notFound.setVisibility(View.GONE);
        userResult.setVisibility(View.GONE);
        userDivider.setVisibility(View.GONE);
        recipeResult.setVisibility(View.GONE);
        recipeDivider.setVisibility(View.GONE);
    }

    private void setVisibility(){
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
    }

    private void search(String query, FirestoreOnCallBack firestoreCallback){
        Log.d("SEARCHING", "HEHE");
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

        int width = (int)(getResources().getDisplayMetrics().widthPixels);
        int height = (int)(getResources().getDisplayMetrics().heightPixels);

        loadingDialog.getWindow().setLayout(width, height);
        loadingDialog.show();
        final ObjectMapper mapper = new ObjectMapper();
        recipedb.orderBy("title").startAt(query).endAt(query+'\uf8ff').get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<RecipeModelDB> resultFromRecipeTitle = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                RecipeModelDB recipeModelDB;
                                recipeModelDB = mapper.convertValue(document.getData(), RecipeModelDB.class);
                                recipeModelDB.setId(document.getId());
                                resultFromRecipeTitle.add(recipeModelDB);
                            }
                            recipedb.whereArrayContainsAny("tags", Arrays.asList(query)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        List<RecipeModelDB> resultFromRecipeTag = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            RecipeModelDB recipeModelDB;
                                            recipeModelDB = mapper.convertValue(document.getData(), RecipeModelDB.class);
                                            recipeModelDB.setId(document.getId());
                                            resultFromRecipeTag.add(recipeModelDB);
                                        }

                                        userdb.orderBy("userName").startAt(query).endAt(query+'\uf8ff').get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    List<UserModelDB> resultFromUserName = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : task.getResult()){
                                                        UserModelDB userModelDB;
                                                        userModelDB = mapper.convertValue(document.getData(), UserModelDB.class);
                                                        resultFromUserName.add(userModelDB);
                                                    }
                                                    userdb.orderBy("emailAddr").startAt(query).endAt(query+'\uf8ff').get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                List<UserModelDB> resultFromUserEmail = new ArrayList<>();
                                                                for (QueryDocumentSnapshot document : task.getResult()){
                                                                    UserModelDB userModelDB;
                                                                    userModelDB = mapper.convertValue(document.getData(), UserModelDB.class);
                                                                    resultFromUserEmail.add(userModelDB);
                                                                }

                                                                List<RecipeModelDB> finalRecipeModelDB;
                                                                if(resultFromRecipeTag.size() <=0){
                                                                    finalRecipeModelDB = resultFromRecipeTitle;
                                                                }
                                                                else if(resultFromRecipeTitle.size() <=0){
                                                                    finalRecipeModelDB = resultFromRecipeTag;
                                                                }
                                                                else{
                                                                    List<RecipeModelDB> allRecipeModelDB = new ArrayList<>();
                                                                    allRecipeModelDB.addAll(resultFromRecipeTitle);
                                                                    allRecipeModelDB.addAll(resultFromRecipeTag);

                                                                    Map<String, RecipeModelDB> map = new HashMap<String, RecipeModelDB>();
                                                                    for (RecipeModelDB recipe : allRecipeModelDB) {
                                                                        if (!map.containsKey(recipe.getId())) {
                                                                            map.put(recipe.getId(), recipe);
                                                                        }
                                                                    }
                                                                    finalRecipeModelDB = new ArrayList<RecipeModelDB>(map.values());
                                                                }

                                                                List<UserModelDB> finalUserModelDB;

                                                                if(resultFromUserEmail.size() <=0){
                                                                    finalUserModelDB = resultFromUserName;
                                                                }
                                                                else if(resultFromUserName.size() <=0){
                                                                    finalUserModelDB = resultFromUserEmail;
                                                                }
                                                                else{
                                                                    List<UserModelDB> allUserModelDB = new ArrayList<>();
                                                                    allUserModelDB.addAll(resultFromUserEmail);
                                                                    allUserModelDB.addAll(resultFromUserName);

                                                                    Map<String, UserModelDB> mapUser = new HashMap<String, UserModelDB>();
                                                                    for (UserModelDB user : allUserModelDB) {
                                                                        if (!mapUser.containsKey(user.getUserId())) {
                                                                            mapUser.put(user.getUserId(), user);
                                                                        }
                                                                    }
                                                                    finalUserModelDB = new ArrayList<UserModelDB>(mapUser.values());
                                                                }

                                                                if(finalRecipeModelDB.size()<=0){
                                                                    List<UserModelDB> emptyList = new ArrayList<>();
                                                                    firestoreCallback.onCallBack(new ArrayList<>(finalRecipeModelDB), new ArrayList<>(finalUserModelDB), new ArrayList<>(emptyList));
                                                                    loadingDialog.dismiss();
                                                                }
                                                                else{
                                                                    List<String> userids = new ArrayList<>();

                                                                    for (RecipeModelDB recipe: finalRecipeModelDB){
                                                                        String tempuserid = recipe.getUserId();
                                                                        if(!userids.contains(tempuserid)){
                                                                            userids.add(tempuserid);
                                                                        }
                                                                    }

                                                                    userdb.whereIn("userId", userids)
                                                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if(task.isSuccessful()){
                                                                                List<UserModelDB> recipeUserArray = new ArrayList<>();
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                                                                    final ObjectMapper mapper = new ObjectMapper();
                                                                                    recipeUserArray.add(mapper.convertValue(document.getData(), UserModelDB.class));
                                                                                }
                                                                                List<UserModelDB> recipeUserModelList = new ArrayList<>();
                                                                                for (String userId: userids){
                                                                                    for (UserModelDB user: recipeUserArray){
                                                                                        if (userId.equals(user.getUserId())){
                                                                                            recipeUserModelList.add(user);
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                firestoreCallback.onCallBack(new ArrayList<>(finalRecipeModelDB), new ArrayList<>(finalUserModelDB), new ArrayList<>(recipeUserModelList));
                                                                                loadingDialog.dismiss();
                                                                            }
                                                                            else{
                                                                                loadingDialog.cancel();
                                                                                Toast.makeText(SearchKeywordResultActivity.this,
                                                                                        "Fail to search, please try again",
                                                                                        Toast.LENGTH_SHORT)
                                                                                        .show();

                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                            else{
                                                                loadingDialog.cancel();
                                                                Toast.makeText(SearchKeywordResultActivity.this,
                                                                        "Fail to search, please try again",
                                                                        Toast.LENGTH_SHORT)
                                                                        .show();

                                                            }
                                                        }
                                                    });
                                                }
                                                else{
                                                    loadingDialog.cancel();
                                                    Toast.makeText(SearchKeywordResultActivity.this,
                                                            "Fail to search, please try again",
                                                            Toast.LENGTH_SHORT)
                                                            .show();

                                                }
                                            }
                                        });
                                    }
                                    else{
                                        loadingDialog.cancel();
                                        Toast.makeText(SearchKeywordResultActivity.this,
                                                "Fail to search, please try again",
                                                Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                }
                            });
                        }
                        else{
                            loadingDialog.cancel();
                            Toast.makeText(SearchKeywordResultActivity.this,
                                    "Fail to search, please try again",
                                    Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                });

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

    private interface FirestoreOnCallBack{
        void onCallBack(UserModelDB userModelDB);

        void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList);
    }

}