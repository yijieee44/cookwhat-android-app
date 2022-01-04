package com.example.cookwhat.activities;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.MainActivity;
import com.example.cookwhat.adapters.CommentAdapter;
import com.example.cookwhat.databinding.ActivityViewRecipeBinding;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.UserModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.cookwhat.utils.Utility;

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
    public CollectionReference itemsRef1;
    public CollectionReference itemsRef2;
    public ArrayList<RecipeCommentModel> recipecomments = new ArrayList<>();
    public List<String> usernames = new ArrayList<>();

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

        ArrayList<UserModel> usermodels = new ArrayList<>();


        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.getDefault() )
                        .withZone( ZoneId.systemDefault() );

//        RecipeCommentModel model = new RecipeCommentModel(123456,5,formatter.format(Instant.now()),"YOU GOT THAT YUMMYYUMYUMMYUM");
        RecipeCommentModel model = new RecipeCommentModel();
        model.setRecipeId(5);
        model.setUserId(12346);
        model.setComment("YOU GOT THAT YUMMYYUMYUMMYUM");
        model.setCreatedTime(formatter.format(Instant.now()));
        Map<String, Object> comments = new HashMap<>();
        comments.put("recipeId", model.getRecipeId());
        comments.put("userId", model.getUserId());
        comments.put("comment", model.getComment());
        comments.put("createdTime", model.getCreatedTime());

        UserModel usermodel = new UserModel("selenagomez666", "sgrocks4ever@gmail.com", "1234");
        usermodel.setUserId(12346);
        Map<String, Object> user = new HashMap<>();
        user.put("userName", usermodel.getUserName());
        user.put("emailAddr", usermodel.getEmailAddr());
        user.put("userId", usermodel.getUserId());


        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        itemsRef1 = db.collection("recipecomment");
        itemsRef2 = db.collection("user");

        readData(new FirestoreCallback() {
            @Override
            public void onCallBack(ArrayList<RecipeCommentModel> recipecomments, List<String> usernames) {
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

        itemsRef1.whereEqualTo("recipeId", 5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    List<Integer> userids = new ArrayList<>();
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
                                for (int i = 0; i < recipecomments.size();i++){
                                    userids.add(recipecomments.get(i).getUserId());
                                }
                                if (counter == 1) {
                                    userids.remove(0);
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
                                                        usernames.add(String.valueOf(document.getData().get("userName")));
                                                    }
                                                    firestoreCallback.onCallBack(recipecomments, usernames);
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
    }

    private interface FirestoreCallback {
        void onCallBack(ArrayList<RecipeCommentModel> recipecomments, List<String> usernames);
    }
}