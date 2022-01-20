package com.example.cookwhat.activities;

import static android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.cookwhat.R;
import com.example.cookwhat.database.DatabaseHelper;
import com.example.cookwhat.database.UserTableContract;
import com.example.cookwhat.fragments.UserProfileFragment;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    UserModel user = new UserModel();
    NavController navController;

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

    ActivityResultLauncher<Intent> createActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            });

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

        if(currentUser != null){
            isLoggedIn = true;
            String selection = UserTableContract.UserTable._ID + " = ?";
            String[] selectionArgs = { "1" };
            int deletedRows = db.delete(UserTableContract.UserTable.TABLE_NAME, selection, selectionArgs);

            ContentValues values = new ContentValues();
            values.put(UserTableContract.UserTable._ID, 1);
            values.put(UserTableContract.UserTable.COLUMN_NAME_DISPLAY_NAME, currentUser.getDisplayName());
            long newRowId = db.insert(UserTableContract.UserTable.TABLE_NAME, null, values);
            Log.d("SUCCESS", "New Row Id:: =>" + newRowId);
            firestoreDb.collection("user")
                    .whereEqualTo("userId", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                    ArrayList<UserModel> followers = (ArrayList<UserModel>)document.getData().get("followers");
                                    ArrayList<UserModel> followings = (ArrayList<UserModel>)document.getData().get("followings");

                                    int following = 0;
                                    int follower = 0;
                                    if (followers != null){
                                        follower = followers.size();
                                    }
                                    if (followings != null){
                                        following = followings.size();
                                    }
                                    values.put(UserTableContract.UserTable.COLUMN_NAME_FOLLOWER, follower);
                                    values.put(UserTableContract.UserTable.COLUMN_NAME_FOLLOWING, following);
//                                    int count = db.update(
//                                            UserTableContract.UserTable.TABLE_NAME,
//                                            values,
//                                            selection,
//                                            selectionArgs);

                                }
                            } else {
                                Log.w("ERROR", "Error getting documents.", task.getException());
                            }
                        }
                    });




        }
        else{
            isLoggedIn = false;
            String selection = UserTableContract.UserTable._ID + " = ?";
            String[] selectionArgs = { "1" };
            int deletedRows = db.delete(UserTableContract.UserTable.TABLE_NAME, selection, selectionArgs);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String action = getIntent().getStringExtra("action");
        Boolean isFromFavourite = getIntent().getBooleanExtra("isFromFavourite",false);
        if(isFromFavourite){
            startFragment();
        }


        if(action != null){
            if(action.equals("updated about me")){
                UserModelDB userModelDB = (UserModelDB) getIntent().getSerializableExtra("usermodel");
                startFragment();
            }
        }



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
        navController = host.getNavController();

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
            createActivityResultLauncher.launch(intentCreateActivity);
            return true;
        }
        else if(id == MENU_LOGIN){
            Intent intentLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(intentLoginActivity);
            return true;
        }
        else if(id == MENU_LOGOUT){
            FirebaseAuth.getInstance().signOut();
            String selection = UserTableContract.UserTable._ID + " = ?";
            String[] selectionArgs = { "1" };
            int deletedRows = db.delete(UserTableContract.UserTable.TABLE_NAME, selection, selectionArgs);
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

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    public void startFragment(){
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //bundle.putString("action",action);
        //userProfileFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.NHFMain, userProfileFragment);
        fragmentTransaction.commit();

    }

}