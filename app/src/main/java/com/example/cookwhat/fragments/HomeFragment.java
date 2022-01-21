package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.CreateActivity;
import com.example.cookwhat.activities.SearchKeywordResultActivity;
import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.activities.ViewProfileActivity;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.adapters.RecipeAdapter;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.RecipeModelSearch;
import com.example.cookwhat.models.UserModelDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.admin.v1.Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recipeList;
    public CollectionReference recipedb;
    public CollectionReference userdb;
    private FirebaseAuth mAuth;
    public ArrayList<RecipeModelDB> recipeModelsArray = new ArrayList<>();
    public ArrayList<UserModelDB> userModelsArray = new ArrayList<>();
    SearchView searchView;

    Dialog loadingDialog;

    ActivityResultLauncher<Intent> viewRecipeActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    }
                }
            });

    ActivityResultLauncher<Intent> viewUserActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                    }
                }
            });

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("CookWhat");
        loadingDialog = new Dialog(getContext());
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

        int width = (int)(getResources().getDisplayMetrics().widthPixels);
        int height = (int)(getResources().getDisplayMetrics().heightPixels);

        loadingDialog.getWindow().setLayout(width, height);
        loadingDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recipedb = db.collection("recipe");
        userdb = db.collection("user");

        searchView = view.findViewById(R.id.SearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>0){
                    search(query, new FirestoreCallback() {
                        @Override
                        public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList) {

                        }

                        @Override
                        public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList) {
                            Intent intent = new Intent(getContext(), SearchKeywordResultActivity.class);
                            intent.putExtra("recipeList", recipeModelArrayList);
                            intent.putExtra("userList", userModelArrayList);
                            intent.putExtra("recipeUserList", recipeUserModelArrayList);
                            viewRecipeActivityResultLauncher.launch(intent);
                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        recipeList = view.findViewById(R.id.recipeList);


        readData(new FirestoreCallback() {
            @Override
            public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList) {
                RecipeAdapter adapter = new RecipeAdapter(recipeModelArrayList, userModelArrayList, getContext());
                // set user on click listener
                // set recipe item on click listener
                adapter.setListener(new RecipeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
                        UserModelDB userModelDB = new UserModelDB();
                        intent.putExtra("recipeModel", recipeModelArrayList.get(position));
                        for (int i = 0; i < userModelArrayList.size(); i++) {
                            if (recipeModelArrayList.get(position).getUserId().equals(userModelArrayList.get(i).getUserId())) {
                                userModelDB = userModelArrayList.get(i);
                                break;
                            }
                        }

                        intent.putExtra("userModel", userModelDB);
                        viewRecipeActivityResultLauncher.launch(intent);
                    }

                    @Override
                    public void onUserClick(View view, int position) {
                        String userId = recipeModelArrayList.get(position).getUserId();
                        adapter.readCurrentUser(new RecipeAdapter.FirestoreCallback2() {
                            @Override
                            public void onCallBack(UserModelDB currentUser) {
                                Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
                                intent.putExtra("userId", userId);
                                intent.putExtra("userModel", currentUser);
                                viewUserActivityResultLauncher.launch(intent);
                            }
                        });
                    }
                });

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                recipeList.setLayoutManager(gridLayoutManager);
                recipeList.setAdapter(adapter);

            }

            @Override
            public void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList) {

            }
        });

        return view;
    }

    private void search(String query, FirestoreCallback firestoreCallback){
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

        int width = (int)(getResources().getDisplayMetrics().widthPixels);
        int height = (int)(getResources().getDisplayMetrics().heightPixels);

        loadingDialog.getWindow().setLayout(width, height);
        loadingDialog.show();
        final ObjectMapper mapper = new ObjectMapper();
//        recipedb.whereGreaterThanOrEqualTo("title", query).whereLessThanOrEqualTo("title",query+'\uf8ff').get()
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
                                                                        Toast.makeText(getContext(),
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
                                                        Toast.makeText(getContext(),
                                                                "Fail to search, please try again",
                                                                Toast.LENGTH_SHORT)
                                                                .show();

                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            loadingDialog.cancel();
                                            Toast.makeText(getContext(),
                                                    "Fail to search, please try again",
                                                    Toast.LENGTH_SHORT)
                                                    .show();

                                        }
                                    }
                                });
                            }
                            else{
                                loadingDialog.cancel();
                                Toast.makeText(getContext(),
                                        "Fail to search, please try again",
                                        Toast.LENGTH_SHORT)
                                        .show();

                            }
                        }
                    });
                }
                else{
                    loadingDialog.cancel();
                    Toast.makeText(getContext(),
                            "Fail to search, please try again",
                            Toast.LENGTH_SHORT)
                            .show();

                }
            }
        });

    }

    public void readData(FirestoreCallback firestoreCallback){


        recipedb.orderBy("createdTime", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    ArrayList<UserModelDB> tempUserModelArrayList = new ArrayList<>();
                    List<String> userids = new ArrayList<>();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                //TODO try to catch error here if fail to map value
                                RecipeModelDB recipeModelDB;
                                recipeModelDB = mapper.convertValue(document.getData(), RecipeModelDB.class);
                                recipeModelDB.setId(document.getId());
                                recipeModelsArray.add(recipeModelDB);
                            }
                            for (int i = 0; i < recipeModelsArray.size();i++){
                                String tempuserid = recipeModelsArray.get(i).getUserId();
                                if(!userids.contains(tempuserid)){
                                    userids.add(tempuserid);
                                }
                            }
                            userdb.whereIn("userId", userids)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                                    final ObjectMapper mapper = new ObjectMapper();
                                                    tempUserModelArrayList.add(mapper.convertValue(document.getData(), UserModelDB.class));
                                                }
                                                for (int y=0;y<userids.size();y++){
                                                    String userid = userids.get(y);
                                                    for (int z=0;z<tempUserModelArrayList.size();z++){
                                                        String userid2 = tempUserModelArrayList.get(z).getUserId();
                                                        if (userid.equals(userid2)){
                                                            userModelsArray.add(tempUserModelArrayList.get(z));
                                                            break;
                                                        }
                                                    }
                                                }
                                                loadingDialog.dismiss();
                                                firestoreCallback.onCallBack(recipeModelsArray, userModelsArray );
                                            } else {
                                                loadingDialog.cancel();
                                                Log.w("ERROR", "Error getting documents.", task.getException());
                                            }
                                        }
                                    });
                      } else {
                            loadingDialog.cancel();
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList);

        void onCallBack(ArrayList<RecipeModelDB> recipeModelArrayList, ArrayList<UserModelDB> userModelArrayList, ArrayList<UserModelDB> recipeUserModelArrayList);
    }
}