package com.example.cookwhat.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.adapters.CommentAdapter;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.utils.Utility;
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
import com.google.firebase.firestore.auth.User;

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
    public CollectionReference userdb;
    public int currImageIndex;
    public FirebaseFirestore db;
    public FirebaseUser user;
    public ArrayList<UserModelDB> userModelDBArrayList;
    public ArrayList<String> userIds;
    ImageSlider imageSlider;
    TextView recipeName, userName, recipeCaption; // need to add timecreated TV
    Button addFav, ingredientandutensils;
    ImageButton userPic, enterButton;
    ListView listView;
    EditText writeComment;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(Locale.getDefault())
                        .withZone(ZoneId.systemDefault());

        db = FirebaseFirestore.getInstance();
        recipedb = db.collection("recipe");
        userdb = db.collection("user");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userModelDBArrayList = new ArrayList<>();
        userIds = new ArrayList<>();

        UserModelDB userModelDB = (UserModelDB) getIntent().getSerializableExtra("userModel");
        RecipeModelDB recipeModelDB = (RecipeModelDB) getIntent().getSerializableExtra("recipeModel");

        imageSlider = (ImageSlider) findViewById(R.id.image_slider);
        ArrayList<SlideModel> images = new ArrayList<>();
        recipeName = (TextView) findViewById(R.id.TVRecipeName);
        userName = (TextView) findViewById(R.id.TVOwnerUsername);
        recipeCaption = (TextView) findViewById(R.id.TVRecipeDesc);
        // add one more for timeCreated
        // one more for chips (tags)
        addFav = (Button) findViewById(R.id.BtnAddFav);
        ingredientandutensils = (Button) findViewById(R.id.BtnINU);
        userPic = (ImageButton) findViewById(R.id.IBOwnerPic);
        enterButton = (ImageButton) findViewById(R.id.IBEnterButton);
        listView = (ListView) findViewById(R.id.LV1);
        writeComment = (EditText) findViewById(R.id.ETComment);


        for (int z = 0; z < recipeModelDB.getSteps().size(); z++) {
            images.add(new SlideModel(recipeModelDB.getSteps().get(z).getImage(), null));
        }

        imageSlider.setImageList(images);

        recipeCaption.setText(recipeModelDB.getSteps().get(0).getStep());

        imageSlider.setItemChangeListener(new ItemChangeListener() {
            @Override
            public void onItemChanged(int position) {
                if (recipeModelDB.getSteps().size() > position) {
                    currImageIndex = position;
                    String currImageCaption = recipeModelDB.getSteps().get(currImageIndex).getStep();

                    if (currImageCaption.isEmpty()) {
                        for (int i = currImageIndex; i > 0; i--) {
                            if (!recipeModelDB.getSteps().get(i).getStep().isEmpty()) {
                                currImageCaption = recipeModelDB.getSteps().get(i).getStep();
                                break;
                            }
                        }
                    }

                    recipeCaption.setText(currImageCaption);
                }
            }
        });

        recipeName.setText(recipeModelDB.getTitle());
        userName.setText(userModelDB.getUserName());

        addFav.setText(String.valueOf(recipeModelDB.getNumFav()));
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ingredientandutensils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        userPic.setImageResource(userModelDB.getProfilePic());

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserModel.class);
                intent.putExtra("fragmentname", "viewprofilefragment");
                startActivity(intent);
            }
        });

        if (recipeModelDB.getComments() != null && !recipeModelDB.getComments().isEmpty()){
            for (int a = 0; a < recipeModelDB.getComments().size(); a++) {
                String userid = recipeModelDB.getComments().get(a).getUserId();
                userIds.add(userid);
            }
            readUser(new FirestoreCallback() {
                @Override
                public void onCallBack(ArrayList<UserModelDB> userModelDBArrayList) {
                    CommentAdapter commentAdapter = new CommentAdapter(ViewRecipeActivity.this, recipeModelDB.getComments(), userModelDBArrayList);
                    listView.setAdapter(commentAdapter);
                    Utility.setListViewHeightBasedOnChildren(listView);
                    listView.setClickable(true);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String userId = recipeModelDB.getComments().get(position).getUserId();
                            Intent intent = new Intent(ViewRecipeActivity.this, UserActivity.class);
                            intent.putExtra("fragmentname", "viewprofilefragment");
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
        else {
            CommentAdapter commentAdapter = new CommentAdapter(ViewRecipeActivity.this, new ArrayList<>(), new ArrayList<>());
            listView.setAdapter(commentAdapter);
            Utility.setListViewHeightBasedOnChildren(listView);
        }


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!writeComment.getText().toString().isEmpty()) {
                    RecipeCommentModel recipeCommentModel = new RecipeCommentModel();
                    recipeCommentModel.setComment(writeComment.getText().toString());
                    recipeCommentModel.setRecipeId("");
                    recipeCommentModel.setUserId(user.getUid());
                    recipeCommentModel.setCreatedTime(formatter.format(Instant.now()));
                    recipeModelDB.getComments().add(recipeCommentModel);
                    updateComments(recipeModelDB);
                    writeComment.getText().clear();

                    CommentAdapter commentAdapter = new CommentAdapter(ViewRecipeActivity.this, recipeModelDB.getComments(), userModelDBArrayList);
                    listView.setAdapter(commentAdapter);
                    Utility.setListViewHeightBasedOnChildren(listView);
                    listView.setClickable(true);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String userId = recipeModelDB.getComments().get(position).getUserId();
                            Intent intent = new Intent(ViewRecipeActivity.this, UserActivity.class);
                            intent.putExtra("fragmentname", "viewprofilefragment");
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    });

                    readUser(new FirestoreCallback() {
                        @Override
                        public void onCallBack(ArrayList<UserModelDB> userModelDBArrayList) {
                            CommentAdapter commentAdapter = new CommentAdapter(ViewRecipeActivity.this, recipeModelDB.getComments(), userModelDBArrayList);
                            listView.setAdapter(commentAdapter);
                            Utility.setListViewHeightBasedOnChildren(listView);
                            listView.setClickable(true);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String userId = recipeModelDB.getComments().get(position).getUserId();
                                    Intent intent = new Intent(ViewRecipeActivity.this, UserActivity.class);
                                    intent.putExtra("fragmentname", "viewprofilefragment");
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void readUser(FirestoreCallback firestoreCallback) {
        userdb.whereIn("userId", userIds).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                userModelDBArrayList.add(mapper.convertValue(document.getData(), UserModelDB.class));
                            }
                            firestoreCallback.onCallBack(userModelDBArrayList);
                        } else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void updateComments(RecipeModelDB recipeModelDB){
        recipedb.document(recipeModelDB.getId()).set(recipeModelDB)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("SUCCESS", "DocumentSnapshot successfully written!");
                        }
                        else {
                            Log.w("ERROR", "Error writing document", task.getException());
                        }
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(ArrayList<UserModelDB> userModelDBArrayList);
    }
}