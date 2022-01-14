package com.example.cookwhat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cookwhat.EditAboutMePopUp;
import com.example.cookwhat.ExpandableHeightGridView;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.followPopUp;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment implements EditAboutMePopUp.passData {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userID;
    ArrayList<String> recipeName =new ArrayList<>();
    ArrayList<Integer> Img = new ArrayList<>();
    ArrayList<String> followerList = new ArrayList<>();
    ArrayList<String> followingList = new ArrayList<>();
    ArrayList<String> followerIDList = new ArrayList<>();
    ArrayList<String> followingIDList = new ArrayList<>();
    ExpandableHeightGridView tabcontent;
    TextView textcontent;
    LinearLayout ll;
    ConstraintLayout cl, clmain;



    public UserProfileFragment() {


        //throw arraylist of usermodels of following and followers from db



    }
    //method to create arraylist of follower and following namelist

   /* private void getSecretRecipe(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipe")
                .whereEqualTo("username", "yuan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                recipeName.add(document.getData().get("recipe_name").toString());

                            }
                    }

                    }
                });

        System.out.println(recipeName.size());

    }*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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

        UserActivity activity = (UserActivity)  getActivity();
        userID = activity.getUserID();


        /*for(int i=0; i<recipeName.size(); i++){
            recipeName.add(Integer.toString(i));
            Img.add(R.drawable.addbutton);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Profile");
        readData(new FirestoreOnCallBack() {
            Button favCategory = view.findViewById(R.id.Btn_Favourite);
            Button btnFollower = view.findViewById(R.id.Btn_Follower);
            Button btnFollowing = view.findViewById(R.id.Btn_Following);
            TextView tvUserName  = view.findViewById(R.id.TV_UserName);

            UserModel usermodel = new UserModel();
            int numFollowers;
            int numFollowings;

            @Override
            public void onCallBack(UserModel usermodel,  ArrayList<RecipeModel> createdRecipes) {
                this.usermodel = usermodel;
                numFollowers = usermodel.getFollowersNumber();
                numFollowings = usermodel.getFollowingsNumber();
                btnFollower.setText(Integer.toString(numFollowers));
                btnFollowing.setText(Integer.toString(numFollowings));
                tvUserName.setText(usermodel.getUserName());
                followingIDList = usermodel.getFollowingsID();

                View.OnClickListener followerOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //pass followerArraylist and followernamelist
                        followerList = usermodel.getFollowers();
                        followerIDList = usermodel.getFollowersID();
                        System.out.println(followerIDList);
                        followPopUp showFollowList = new followPopUp(view, "follower", followerList, followerIDList, userID, followingIDList);
                        showFollowList.show(getActivity().getSupportFragmentManager(), "ProfilePicDialog");
                    }
                };
                View.OnClickListener followingOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followingList = usermodel.getFollowings();
                        followPopUp showFollowList = new followPopUp(view, "following", followingList, followingIDList,userID, followingIDList);
                        showFollowList.show(getActivity().getSupportFragmentManager(), "ProfilePicDialog");
                    }

                };

                btnFollower.setOnClickListener(followerOCL);
                btnFollowing.setOnClickListener(followingOCL);

                View.OnClickListener favCategoryOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //fetch favourite category

                        Navigation.findNavController(view).navigate(R.id.DestFavouriteCategory);
                    }
                };

                favCategory.setOnClickListener(favCategoryOCL);

                if (recipeName.isEmpty()) {
                    String noRecipe = "Nothing to show here.\nLet's get started on sharing your secret recipes!";
                    textcontent = view.findViewById(R.id.TV_Empty);
                    textcontent.setText(noRecipe);

                } else {
                    view.findViewById(R.id.TV_Empty).setVisibility(View.INVISIBLE);
                    tabcontent = view.findViewById(R.id.tabcontent);
                    CustomAdapter recipeAdapter = new CustomAdapter();
                    tabcontent.setExpanded(true);
                    tabcontent.setAdapter(recipeAdapter);
                }

                cl = view.findViewById(R.id.CL_SecretRecipe);
                cl.setVisibility(View.VISIBLE);

                ll = view.findViewById(R.id.LL_AboutMe);
                clmain = view.findViewById(R.id.CS_Main);

                removeChild(ll);

                TabLayout tabLayout = view.findViewById(R.id.TL_ProfileTab);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        switch (tab.getPosition()) {
                            case 0: {
                                removeChild(ll);
                                addChild(cl, clmain);
                                cl.setVisibility(View.VISIBLE);
                                break;

                            }
                            case 1: {
                                removeChild(cl);
                                addChild(ll, clmain);
                                ll.setVisibility(View.VISIBLE);
                                Button edit = view.findViewById(R.id.Btn_Edit);
                                View.OnClickListener editOCL = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        EditAboutMePopUp editAboutMePopUp = new EditAboutMePopUp();
                                        editAboutMePopUp.setTargetFragment(UserProfileFragment.this,1);
                                        editAboutMePopUp.show(getFragmentManager(),"deleteOrMoveTo");
                                    }
                                };
                                edit.setOnClickListener(editOCL);
                                break;
                            }
                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });


            }


        }, userID);


    }



    public void removeChild(ViewGroup viewgroup){
        ((ViewGroup)viewgroup.getParent()).removeView(viewgroup);

    }

    public void addChild(ViewGroup child, ViewGroup parent){
        parent.addView(child);
    }

    @Override
    public void getData(String gender, Integer age, String country) {
        System.out.println("Gender: "+gender+"age: "+ age+"Country: "+country);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return recipeName.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);

            ImageView img = view1.findViewById(R.id.IV_favCategory);
            TextView text = view1.findViewById(R.id.TV_favCategory);

            img.setImageResource(Img.get(i));
            text.setText(recipeName.get(i));

            return view1;

        }
    }

   public void readData(FirestoreOnCallBack firestoreOnCallBack, String userID){
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       CollectionReference userCollection = db.collection("user");
       CollectionReference recipeCollection = db.collection("recipe");

       ArrayList<RecipeModel> createdRecipes = new ArrayList<>();
       UserModel usermodel = new UserModel();

       userCollection.document(userID).get()
               .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {

                       System.out.println(documentSnapshot.get("userName"));
                       usermodel.setUserName((String) documentSnapshot.get("userName"));
                       ArrayList<String> followingsID = (ArrayList<String>) documentSnapshot.get("followings");
                       ArrayList<String> followings = new ArrayList<>();
                       for(int i =0; i<followingsID.size();i++){
                           System.out.println(followingsID.get(i));
                           usermodel.setFollowingsID(followingsID.get(i));

                       }
                       if(followingsID != null){
                           for(int i =0; i< followingsID.size(); i++){
                               userCollection.document(followingsID.get(i)).get()
                                       .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                           @Override
                                           public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                               usermodel.setFollowings((String) documentSnapshot.get("userName"));
                                           }
                                       });
                           }
                       }

                       userCollection.whereArrayContains("followings",userID).get()
                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                       if(task.isSuccessful()){
                                           for(QueryDocumentSnapshot document : task.getResult()){
                                               usermodel.setFollowersID(document.getId());
                                               usermodel.setFollowers((String) document.get("userName"));


                                           }
                                           recipeCollection.whereEqualTo("userID", userID).get()
                                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                           if(task.isSuccessful()){
                                                               for(QueryDocumentSnapshot document : task.getResult()){
                                                                   RecipeModel recipeModel = new RecipeModel();
                                                                   recipeModel = document.toObject(RecipeModel.class);
                                                                   createdRecipes.add(recipeModel);
                                                               }
                                                               firestoreOnCallBack.onCallBack(usermodel,  createdRecipes);
                                                           }
                                                           else{
                                                               Log.w("ERROR", "Error getting created recipe.", task.getException());
                                                           }
                                                       }
                                                   });

                                       }
                                       else{
                                           Log.w("ERROR", "Error getting documents.", task.getException());
                                       }
                                   }
                               });


                   }
               });








   }

    private interface FirestoreOnCallBack{


        void onCallBack(UserModel usermodel, ArrayList<RecipeModel>createdRecipes);


    }
}