package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.adapters.CommentAdapter;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.cookwhat.utils.Utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewRecipeActivity extends AppCompatActivity {

    public CollectionReference recipedb;
    public CollectionReference recipecommentdb;
    public CollectionReference userdb;
    public static ArrayList<RecipeCommentModel> recipecomments;
    public UserModel userModel1;
    public RecipeModel recipemodel;
    public static List<String> usernames;
    public int currImageIndex;
    public UserModel recipeowner;
    public String recipeId;
    public FirebaseFirestore db;
    public FirebaseUser user;

    ImageSlider imageSlider;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.getDefault() )
                        .withZone( ZoneId.systemDefault() );

        db = FirebaseFirestore.getInstance();
        recipedb = db.collection("recipe");
        recipecommentdb = db.collection("recipecomment");
        userdb = db.collection("user");
        user = FirebaseAuth.getInstance().getCurrentUser();


        userModel1 = new UserModel();
        recipeowner = new UserModel();
        recipemodel = new RecipeModel();
        usernames = new ArrayList<>();
        recipecomments = new ArrayList<>();
        recipeId = getIntent().getStringExtra("recipeId");

        imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> images = new ArrayList<>();

        ImageButton userPic = (ImageButton) findViewById(R.id.IBOwnerPic);
        TextView recipeName = (TextView) findViewById(R.id.TVRecipeName);
        TextView userName = (TextView) findViewById(R.id.TVOwnerUsername);
        Button addFav = (Button) findViewById(R.id.BtnAddFav);
        Button ingredientandutensils = (Button) findViewById(R.id.BtnINU);

        ingredientandutensils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        TextView recipeCaption = findViewById(R.id.TVRecipeDesc);
        ListView listview = findViewById(R.id.LV1);


        readData(new FirestoreCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onCallBack(RecipeModel recipeModel, UserModel userModel, ArrayList<RecipeCommentModel> recipecomments, List<String> usernames) {
                recipeName.setText(recipeModel.getTitle());
                userName.setText(userModel.getUserName());
                addFav.setText(String.valueOf(recipeModel.getNum_fav()));
                addFav.setOnClickListener(new View.OnClickListener() {
                    ArrayList<String> recipeids;
                    @Override
                    public void onClick(View v) {
                        readUser(new FirestoreCallback2() {
                            @Override
                            public void onCallBack(UserModel userModel1) {
                                if (userModel1.getFavouriteFood() == null){
                                    recipeids = new ArrayList<>();
                                }
                                else{
                                    recipeids = userModel1.getFavouriteFood();
                                }
                                if (recipeids.contains(recipeModel.getId())){
                                    recipeModel.setNum_fav(recipeModel.getNum_fav() - 1);
                                    recipeids.remove(recipeModel.getId());
                                    userModel1.setFavouriteFood(recipeids);
                                    writeData(recipeModel);
                                    writeUser(userModel1);
                                    addFav.setText(String.valueOf(recipeModel.getNum_fav()));
                                }
                                else{
                                    recipeModel.setNum_fav(recipeModel.getNum_fav() + 1);
                                    recipeids.add(recipeModel.getId());
                                    userModel1.setFavouriteFood(recipeids);
                                    writeData(recipeModel);
                                    writeUser(userModel1);
                                    addFav.setText(String.valueOf(recipeModel.getNum_fav()));
                                }
                            }
                        });
                    }
                });

                for (int z=0;z<recipeModel.getSteps().size();z++){
                    images.add(new SlideModel(recipeModel.getSteps().get(z).getImage(), null));
                }

                imageSlider.setImageList(images);

                recipeCaption.setText(recipeModel.getSteps().get(0).getStep());

                imageSlider.setItemChangeListener(new ItemChangeListener() {
                    @Override
                    public void onItemChanged(int position) {
                        if (recipeModel.getSteps().size() > position) {
                            currImageIndex = position;
                            String currImageCaption = recipeModel.getSteps().get(currImageIndex).getStep();

                            if (currImageCaption.isEmpty()) {
                                for (int i = currImageIndex; i > 0; i--) {
                                    if (!recipeModel.getSteps().get(i).getStep().isEmpty()) {
                                        currImageCaption = recipeModel.getSteps().get(i).getStep();
                                        break;
                                    }
                                }
                            }

                            recipeCaption.setText(currImageCaption);
                        }
                    }
                });

                userPic.setImageResource(userModel.getProfilePic());

                userPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                CommentAdapter commentAdapter = new CommentAdapter(ViewRecipeActivity.this, recipecomments, usernames);
                listview.setAdapter(commentAdapter);
                Utility.setListViewHeightBasedOnChildren(listview);
                listview.setClickable(true);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String userId = recipecomments.get(position).getUserId();
                        Intent intent = new Intent(ViewRecipeActivity.this, UserActivity.class);
                        intent.putExtra("fragmentname", "viewprofilefragment");
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                });
            }
        });

        EditText writecomment = findViewById(R.id.ETComment);

        writecomment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    RecipeCommentModel recipeCommentModel = new RecipeCommentModel();
                    recipeCommentModel.setComment(writecomment.getText().toString());
                    recipeCommentModel.setRecipeId(recipeId);
                    recipeCommentModel.setUserId(user.getUid());
                    recipeCommentModel.setCreatedTime(formatter.format(Instant.now()));
                    writeComment(recipeCommentModel);
                    writecomment.getText().clear();
                    recipecomments.add(recipeCommentModel);
                    usernames.add(user.getDisplayName());

                    CommentAdapter commentAdapter = new CommentAdapter(ViewRecipeActivity.this, recipecomments, usernames);
                    listview.setAdapter(commentAdapter);
                    Utility.setListViewHeightBasedOnChildren(listview);
                    listview.setClickable(true);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String userId = recipecomments.get(position).getUserId();
                            Intent intent = new Intent(ViewRecipeActivity.this, UserActivity.class);
                            intent.putExtra("fragmentname", "viewprofilefragment");
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    public void readData(FirestoreCallback firestoreCallback){

        recipedb.whereEqualTo("id", recipeId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                //TODO try to catch error here if fail to map value
                                recipemodel = mapper.convertValue(document.getData(), RecipeModel.class);
                            }
                            userdb.whereEqualTo("userId", recipemodel.getUserId()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                                    final ObjectMapper mapper = new ObjectMapper();
                                                    recipeowner = mapper.convertValue(document.getData(), UserModel.class);
                                                }
                                            } else {
                                                Log.w("ERROR", "Error getting documents.", task.getException());
                                            }
                                        }
                                    });
                            recipecommentdb.whereEqualTo("recipeId", recipeId).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        List<String> userids = new ArrayList<>();
                                        Map map=new HashMap();
                                        int counter = 0;
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    counter++;
                                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                                    final ObjectMapper mapper = new ObjectMapper();
                                                    final RecipeCommentModel recipeCommentModel = mapper.convertValue(document.getData(), RecipeCommentModel.class);
                                                    recipecomments.add(recipeCommentModel);
                                                }
                                                for (int i = 0; i < recipecomments.size();i++){
                                                    String temp = recipecomments.get(i).getUserId();
                                                    userids.add(temp);
                                                    if (!map.containsKey(temp)){
                                                        map.put(temp, "");
                                                    }
                                                }
                                                if (!userids.isEmpty()){
                                                    userdb.whereIn("userId", userids)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                                                            map.put(String.valueOf(document.getData().get("userId")), String.valueOf(document.getData().get("userName")));
                                                                        }
                                                                        for (int y=0;y<userids.size();y++){
                                                                            usernames.add(String.valueOf(map.get(userids.get(y))));
                                                                        }
                                                                        firestoreCallback.onCallBack(recipemodel, recipeowner, recipecomments, usernames);
                                                                    } else {
                                                                        Log.w("ERROR", "Error getting documents.", task.getException());
                                                                    }
                                                                }
                                                            });
                                                }
                                                else{
                                                    firestoreCallback.onCallBack(recipemodel, recipeowner, recipecomments, usernames);
                                                }
                                            } else {
                                                Log.w("ERROR", "Error getting documents.", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void writeData(RecipeModel recipeModel){
        recipedb.whereEqualTo("id", recipeId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                recipedb.document(document.getId()).set(recipeModel);
                            }
                        }
                        else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void writeComment(RecipeCommentModel recipeCommentModel){
        recipecommentdb.add(recipeCommentModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error adding document", e);
                    }
                });
    }

    public void writeUser(UserModel userModel){
        userdb.whereEqualTo("userId", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                userdb.document(document.getId()).set(userModel);
                            }
                        }
                        else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void readUser(FirestoreCallback2 firestoreCallback){
        userdb.whereEqualTo("userId", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                userModel1 = mapper.convertValue(document.getData(), UserModel.class);
                            }
                            firestoreCallback.onCallBack(userModel1);
                        } else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private interface FirestoreCallback {
        void onCallBack(RecipeModel recipeModel, UserModel userModel, ArrayList<RecipeCommentModel> recipecomments, List<String> usernames);
    }

    private interface FirestoreCallback2 {
        void onCallBack(UserModel userModel1);
    }
}