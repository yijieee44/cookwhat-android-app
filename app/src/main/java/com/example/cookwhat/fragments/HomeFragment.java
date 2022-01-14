package com.example.cookwhat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.adapters.RecipeAdapter;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeStepModel;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.utils.CustomComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    public CollectionReference recipestepdb;
    private FirebaseAuth mAuth;
    public ArrayList<RecipeModel> recipeModelsArray = new ArrayList<>();
    public ArrayList<UserModel> userModelsArray = new ArrayList<>();
    public ArrayList<RecipeStepModel> recipeStepModelsArray = new ArrayList<>();

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
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        recipedb = db.collection("recipe");
//        recipestepdb = db.collection("recipestep");
//        userdb = db.collection("user");
//
//        recipeList = view.findViewById(R.id.recipeList);
//
//        readData(new FirestoreCallback() {
//            @Override
//            public void onCallBack(ArrayList<RecipeModel> recipeModelArrayList, ArrayList<RecipeStepModel> recipeStepModelArrayList, ArrayList<UserModel> userModelArrayList) {
//                Collections.sort(recipeStepModelArrayList, new CustomComparator());
//                for (int i=0;i<recipeModelArrayList.size();i++){
//                    ArrayList<RecipeStepModel> temp = new ArrayList<>();
//                    String recipeId = recipeModelArrayList.get(i).getId();
//                    for (int m=0;m<recipeStepModelArrayList.size();m++){
//                        String recipeId2 = recipeStepModelArrayList.get(m).getRecipeId();
//                        if (recipeId.equals(recipeId2)){
//                            temp.add(recipeStepModelArrayList.get(m));
//                        }
//                    }
//                    recipeModelArrayList.get(i).setSteps(temp);
//                }
//                for (int n=0;n<recipeModelArrayList.size();n++){
//                    writeData(recipeModelArrayList.get(n), recipeModelArrayList.get(n).getId());
//                }
//                RecipeAdapter adapter = new RecipeAdapter(recipeModelArrayList, userModelArrayList, getContext());
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
//                recipeList.setLayoutManager(gridLayoutManager);
//                recipeList.setAdapter(adapter);
//
//            }
//        });
//
        return view;
    }

    public void readData(FirestoreCallback firestoreCallback){

        recipedb.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    ArrayList<UserModel> tempUserModelArrayList = new ArrayList<>();
                    List<String> userids = new ArrayList<>();
                    List<String> recipeids = new ArrayList<>();
                    Map mapuserid=new HashMap();
                    Map maprecipeid=new HashMap();
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                //TODO try to catch error here if fail to map value
                                recipeModelsArray.add(mapper.convertValue(document.getData(), RecipeModel.class));
                            }
                            for (int i = 0; i < recipeModelsArray.size();i++){
                                String tempuserid = recipeModelsArray.get(i).getUserId();
                                String temprecipeid = recipeModelsArray.get(i).getId();
                                userids.add(tempuserid);
                                recipeids.add(temprecipeid);
                                if (!mapuserid.containsKey(tempuserid)){
                                    mapuserid.put(tempuserid, "");
                                }
                                if (!maprecipeid.containsKey(temprecipeid)){
                                    maprecipeid.put(temprecipeid, "");
                                }
                            }
                            recipestepdb.whereIn("recipeId", recipeids).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                                    final ObjectMapper mapper = new ObjectMapper();
                                                    recipeStepModelsArray.add(mapper.convertValue(document.getData(), RecipeStepModel.class));
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
                                                                        tempUserModelArrayList.add(mapper.convertValue(document.getData(), UserModel.class));
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
                                                                    firestoreCallback.onCallBack(recipeModelsArray, recipeStepModelsArray, userModelsArray);
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
                      } else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void writeData(RecipeModel recipeModel, String recipeId){
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

    private interface FirestoreCallback {
        void onCallBack(ArrayList<RecipeModel> recipeModelArrayList, ArrayList<RecipeStepModel> recipeStepModelArrayList, ArrayList<UserModel> userModelArrayList);
    }
}