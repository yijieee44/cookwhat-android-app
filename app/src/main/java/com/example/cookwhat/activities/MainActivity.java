package com.example.cookwhat.activities;

import static android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.cookwhat.R;
import com.example.cookwhat.models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    UserModel user = new UserModel();

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    private FirebaseAuth mAuth;
    private boolean isLoggedIn;
    private static final int MENU_ADD = Menu.FIRST;
    private static final int MENU_LOGIN = Menu.FIRST + 1;
    private static final int MENU_LOGOUT = Menu.FIRST + 2;

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
        menu.clear();
        menu.add(0,MENU_ADD, Menu.NONE,"Create Recipe").setIcon(R.drawable.ic_baseline_add_circle_outline_24).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);

        if(!isLoggedIn) {
            menu.add(0, MENU_LOGIN, Menu.NONE, "Login").setIcon(R.drawable.ic_baseline_login_24).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
        }
        else{
            menu.add(0, MENU_LOGOUT, Menu.NONE, "Logout").setIcon(R.drawable.ic_baseline_logout_24).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.action_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ADD) {
            // direct to create activity
            Intent intentCreateActivity = new Intent(this, CreateActivity.class);
            startActivity(intentCreateActivity);
            return true;
        }
        else if(id == MENU_LOGIN){
            Intent intentLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(intentLoginActivity);
            return true;
        }
        else if(id == MENU_LOGOUT){
            FirebaseAuth.getInstance().signOut();
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
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