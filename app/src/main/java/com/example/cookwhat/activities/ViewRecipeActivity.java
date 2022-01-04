package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.MainActivity;
import com.example.cookwhat.adapters.CommentAdapter;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.example.cookwhat.models.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.cookwhat.utils.Utility;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
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

    private FirebaseAuth mAuth;
    public CollectionReference itemsRef0;
    public CollectionReference itemsRef1;
    public CollectionReference itemsRef2;
    public ArrayList<RecipeCommentModel> recipecomments = new ArrayList<>();
    public RecipeModel recipemodel = new RecipeModel();
    public List<String> usernames = new ArrayList<>();
    public int currImageIndex;
    public UserModel recipeowner = new UserModel();

    ImageSlider imageSlider;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        // Recipe Steps Pictures, need to get from cloud
        imageSlider = findViewById(R.id.image_slider);

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg",null));
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg",null));
        images.add(new SlideModel("https://upload.wikimedia.org/wikipedia/commons/9/99/IMG-20200708-WA0006.jpg",null));

        imageSlider.setImageList(images);

        ArrayList<RecipeStepModel> recipeStepModels = new ArrayList<>();
        RecipeStepModel model1 = new RecipeStepModel("xxx", "First egerugegiggggohigwr");
        RecipeStepModel model2 = new RecipeStepModel("xxxx", "Second egerugegiggggohigwr");
        recipeStepModels.add(model1);
        recipeStepModels.add(model2);

        ImageButton userPic = findViewById(R.id.IBOwnerPic);
        TextView recipeName = findViewById(R.id.TVRecipeName);
        TextView userName = findViewById(R.id.TVOwnerUsername);
        Button addFav = findViewById(R.id.BtnAddFav);
        Button ingredientandutensils = findViewById(R.id.BtnINU);

        ingredientandutensils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        EditText writecomment = findViewById(R.id.ETComment);
        TextView recipeCaption = findViewById(R.id.TVRecipeDesc);

        imageSlider.setItemChangeListener(new ItemChangeListener() {
            @Override
            public void onItemChanged(int position) {
                if (recipeStepModels.size() > position) {
                    currImageIndex = position;
                    String currImageCaption = recipeStepModels.get(currImageIndex).getStep();

                    if (currImageCaption.isEmpty()) {
                        for (int i = currImageIndex; i > 0; i--) {
                            if (!recipeStepModels.get(i).getStep().isEmpty()) {
                                currImageCaption = recipeStepModels.get(i).getStep();
                                break;
                            }
                        }
                    }

                    recipeCaption.setText(currImageCaption);
                }
            }
        });


        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.getDefault() )
                        .withZone( ZoneId.systemDefault() );


        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        itemsRef0 = db.collection("recipe");
        itemsRef1 = db.collection("recipecomment");
        itemsRef2 = db.collection("user");


        readData(new FirestoreCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onCallBack(RecipeModel recipeModel, UserModel userModel, ArrayList<RecipeCommentModel> recipecomments, List<String> usernames) {

                recipeName.setText(recipeModel.getTitle());
                userName.setText(userModel.getUserName());
                addFav.setText(String.valueOf(recipeModel.getNum_fav()));
                addFav.setOnClickListener(new View.OnClickListener() {
                    int clickcount = 0;
                    @Override
                    public void onClick(View v) {
                        clickcount++;
                        if (clickcount == 1) {
                            recipeModel.setNum_fav(recipeModel.getNum_fav() + 1);
                            writeData(recipeModel);
                            addFav.setText(String.valueOf(recipeModel.getNum_fav()));
                        }
                        else if (clickcount == 2){
                            recipeModel.setNum_fav(recipeModel.getNum_fav() - 1);
                            writeData(recipeModel);
                            addFav.setText(String.valueOf(recipeModel.getNum_fav()));
                            clickcount = 0;
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
                ListView listview = findViewById(R.id.LV1);
                listview.setAdapter(commentAdapter);
                Utility.setListViewHeightBasedOnChildren(listview);
                listview.setClickable(true);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ViewRecipeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }


    public void readData(FirestoreCallback firestoreCallback){

        itemsRef0.whereEqualTo("id", "1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                recipemodel = mapper.convertValue(document.getData(), RecipeModel.class);
                            }
                            itemsRef2.whereEqualTo("userId", recipemodel.getUserId()).get()
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
                            itemsRef1.whereEqualTo("recipeId", "1").get()
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
                                                    itemsRef2.whereIn("userId", userids)
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
        itemsRef0.whereEqualTo("id", "1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                itemsRef0.document(document.getId()).set(recipeModel);
                            }
                        }
                        else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
            }

    private interface FirestoreCallback {
        void onCallBack(RecipeModel recipeModel, UserModel userModel, ArrayList<RecipeCommentModel> recipecomments, List<String> usernames);
    }
}