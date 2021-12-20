package com.example.cookwhat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cookwhat.R;
import com.example.cookwhat.models.IngredientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private boolean isLoggedIn;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            isLoggedIn = true;
        }
        else{
            isLoggedIn = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DB Code
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        IngredientModel model = new IngredientModel();
//        model.setMemo("Hello im testing");
//        model.setName("HEHEHEH");
//        model.setUnit("kg");
//        model.setQuantity(1);
//        model.setIcon("wasdwasdwasd");
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Alan");
//        user.put("middle", "Mathison");
//        user.put("last", "Turing");
//        user.put("born", 1912);
//        ArrayList<String> list = new ArrayList<>();
//        list.add("wasd");
//        list.add("asdasd");
//        user.put("list", list);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        db.collection("recipe")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("ERROR", "Error adding document", e);
//                    }
//                });
//
//        db.collection("recipe")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
//                                ArrayList<String> anotherList = (ArrayList<String>) document.getData().get("list");
//                            }
//                        } else {
//                            Log.w("ERROR", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

        // bind NavHostFragment with NavController
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFMain);
        NavController navController = host.getNavController();

        setupBottomNavMenu(navController);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(isLoggedIn) {
            menu.add(0, 0, Menu.NONE, "Logout").setIcon(R.drawable.ic_baseline_logout_24);
        }
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu
//        getMenuInflater().inflate(R.menu.action_nav_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_create) {
            // direct to create activity
            Intent intentCreateActivity = new Intent(this, CreateActivity.class);
            startActivity(intentCreateActivity);
            return true;
        }
        else if(id == R.id.action_login){
            Intent intentCreateActivity = new Intent(this, LoginActivity.class);
            startActivity(intentCreateActivity);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // disable the middle empty button
        bottomNav.getMenu().getItem(1).setEnabled(false);
    }

    public void BtnSearchRecipe(View v){
        Intent intentSearchActivity = new Intent(this, SearchActivity.class);
        startActivity(intentSearchActivity);
    }

}