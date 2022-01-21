package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.adapters.FavouriteAdapter;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment   {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static ArrayList<String> recipeIdList = new ArrayList<>();
    static FavouriteAdapter favouriteAdapter;
    static ListView listview;
    static UserModelDB userModelDB;
    static String selectedCategoryName;
    String userId;
    Dialog loadingDialog;
    static ArrayList<String> recipeName = new ArrayList<>();
    static ArrayList<String> recipeImage = new ArrayList<>();
    static ArrayList<List<String>> tags = new ArrayList<>();




    public FavouriteFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new Dialog(getContext());
        recipeIdList = (ArrayList<String>) userModelDB.getFavouriteCategory().get(selectedCategoryName);
        listview =(ListView) view.findViewById(R.id.LV_FavouriteList);
        System.out.println("checkrecipeId"+ userModelDB.getFavouriteCategory());
        userId = userModelDB.getUserId();
        readData(new FirestoreOnCallBack(){
            @Override
            public void onCallBackRecipe(ArrayList<RecipeModelDB> recipeModel, ArrayList<String> recipeNames, ArrayList<String> recipeImages, ArrayList<List<String>> tag) {
                recipeName = recipeNames;
                recipeImage = recipeImages;
                tags = tag;
                System.out.println("recipeSize"+recipeName.size());
                favouriteAdapter = new FavouriteAdapter(getContext(),recipeIdList, recipeName, recipeImage, tags, userModelDB, recipeModel);
                listview.setAdapter(favouriteAdapter);
                System.out.println("In onviewcreated:"+favouriteAdapter.getCount());


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            Intent intent = new Intent(getActivity(), ViewRecipeActivity.class);
                            intent.putExtra("recipeModel", recipeModel.get(i));
                            intent.putExtra("userModel", userModelDB);
                            startActivity(intent);}


                });


            }

        }, recipeIdList);




    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            userModelDB = (UserModelDB) this.getArguments().getSerializable("usermodel");
            selectedCategoryName = this.getArguments().getString("selectedCategoryName");
            System.out.println("favouritelistfragment"+selectedCategoryName);
        }
    }




    public static void removeItem(int pos){
        String id = recipeIdList.get(pos);
        recipeIdList.remove(pos);
        recipeName.remove(pos);
        recipeImage.remove(pos);
        tags.remove(pos);
        listview.setAdapter(favouriteAdapter);
        System.out.println("RecipeNames:"+recipeName);
        updateData(id);
    }

    public static void moveItem(Context context, UserModelDB usermodelDB, RecipeModelDB recipeModelDB){
        System.out.println("here");
        FragmentActivity activity = (FragmentActivity)context;
        MoveFavouriteDialogFragment favouriteCategoryDialogFragment = new MoveFavouriteDialogFragment(usermodelDB, recipeModelDB, selectedCategoryName);
        favouriteCategoryDialogFragment.show(activity.getSupportFragmentManager(), "FavCatDialog");

    }




    public void readData(FirestoreOnCallBack firestoreOnCallBack, ArrayList<String>recipeId){
        TextView noFavourite = getView().findViewById(R.id.TVNoFavouriteRecipe);
        noFavourite.setVisibility(View.GONE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String>recipeName = new ArrayList<>();
        ArrayList<String>recipeImage =  new ArrayList<>();
        ArrayList<List<String>>recipeTags = new ArrayList<>();
        ArrayList<RecipeModelDB>recipeModel = new ArrayList<>();
//        int recipeIdSize = recipeId.size();
//        String lastRecipeID = recipeId.get(recipeIdSize-1);
        if(recipeId.size()>0){
            loadingDialog.setCancelable(false);
            loadingDialog.setContentView(R.layout.dialog_loading);
            loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

            int width = (int)(getResources().getDisplayMetrics().widthPixels);
            int height = (int)(getResources().getDisplayMetrics().heightPixels);

            loadingDialog.getWindow().setLayout(width, height);
            loadingDialog.show();
            db.collection("recipe").whereIn(FieldPath.documentId(), recipeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.d("SUCCESS", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            final ObjectMapper mapper = new ObjectMapper();
                            RecipeModelDB recipemodel = new RecipeModelDB();
                            recipemodel = documentSnapshot.toObject(RecipeModelDB.class);
                            recipeModel.add(recipemodel);
                            recipeName.add((String) documentSnapshot.get("title"));
                            recipeImage.add(getResources().getString(R.string.recipe_image_uri) + recipemodel.getSteps().get(0).getImage()+"?alt=media");
                            recipeTags.add(recipemodel.getTags());
                        }
                        firestoreOnCallBack.onCallBackRecipe(recipeModel, recipeName, recipeImage,recipeTags);
                        loadingDialog.dismiss();
                    }
                    else{
                        loadingDialog.cancel();
                    }
                }
            });
        }
        else{
            noFavourite.setVisibility(View.VISIBLE);
        }

    }

    public static void updateData(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,ArrayList<String>> favouriteFood = userModelDB.getFavouriteCategory() ;
        favouriteFood.get(selectedCategoryName).remove(id);
        System.out.println("Update DB:"+favouriteFood.get(selectedCategoryName));
        System.out.println("Size:"+favouriteFood.get(selectedCategoryName).size());
        userModelDB.setFavouriteCategory(favouriteFood);
        db.collection("user").document(userModelDB.getUserId()).update("favouriteCategory", favouriteFood);


    }

    public interface FirestoreOnCallBack{
        void onCallBackRecipe(ArrayList<RecipeModelDB>recipeModel, ArrayList<String>recipeName, ArrayList<String>recipeImage, ArrayList<List<String>>tags);
    }




}



