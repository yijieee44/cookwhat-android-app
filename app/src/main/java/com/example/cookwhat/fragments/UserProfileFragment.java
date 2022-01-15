package com.example.cookwhat.fragments;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.cookwhat.ExpandableHeightGridView;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.FavouriteActivity;
import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.followPopUp;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

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
    ArrayList<String> followerNameList = new ArrayList<>();
    ArrayList<String> followingNameList = new ArrayList<>();
    ArrayList<String> followerIDList = new ArrayList<>();
    ArrayList<String> followingIDList = new ArrayList<>();
    ExpandableHeightGridView tabcontent;
    TextView textcontent;
    LinearLayout ll;
    ConstraintLayout cl, clmain;
    FirebaseAuth mAuth;
    Boolean showTabAboutMe = false;



    public UserProfileFragment() {
        //throw arraylist of usermodels of following and followers from db
    }

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
            Bundle bundle = this.getArguments();
            String action = bundle.getString("action");
            if(action.equals("updated about me")){
                showTabAboutMe = true;
            }
        }
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        System.out.println("getUserid"+userID);;

        /*for(int i=0; i<recipeName.size(); i++){
            recipeName.add(Integer.toString(i));
            Img.add(R.drawable.addbutton);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Profile");
        System.out.println("getUserIdin on view created:"+userID);

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
                tvUserName.setText(usermodel.getUserName());
                followerNameList = usermodel.getFollowersName();
                followerIDList = usermodel.getFollowersId();
                followingIDList = usermodel.getFollowingsId();
                followingNameList = usermodel.getFollowingsName();
                numFollowers = followerNameList.size();
                numFollowings = followingNameList.size();
                btnFollower.setText(Integer.toString(numFollowers));
                btnFollowing.setText(Integer.toString(numFollowings));

                for(int i =0; i< createdRecipes.size(); i++){
                    recipeName.add(createdRecipes.get(i).getTitle());
                }

                View.OnClickListener followerOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followPopUp showFollowList = new followPopUp(view, "follower", followerNameList, followerIDList, userID, followingIDList);
                        showFollowList.show(getActivity().getSupportFragmentManager(), "ProfilePicDialog");
                    }
                };
                View.OnClickListener followingOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followPopUp showFollowList = new followPopUp(view, "following", followingNameList, followingIDList,userID, followingIDList);
                        showFollowList.show(getActivity().getSupportFragmentManager(), "ProfilePicDialog");
                    }
                };

                btnFollower.setOnClickListener(followerOCL);
                btnFollowing.setOnClickListener(followingOCL);

                View.OnClickListener favCategoryOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Map<String,ArrayList<String>> favMap = usermodel.getFavouriteCategory();
                        //ArrayList<ArrayList<String>> favourite = new ArrayList<ArrayList<String>>(favMap.values());
                        Intent intent = new Intent(getActivity(),FavouriteActivity.class);
                        intent.putExtra("userId", usermodel.getUserId());
                        startActivity(intent);
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



                TabLayout tabLayout = view.findViewById(R.id.TL_ProfileTab);
                cl = view.findViewById(R.id.CL_SecretRecipe);
                cl.setVisibility(View.VISIBLE);

                ll = view.findViewById(R.id.LL_AboutMe);
                clmain = view.findViewById(R.id.CS_Main);

                removeChild(ll);


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
                                TextView description = view.findViewById(R.id.TV_TabDescription);
                                TextView email = view.findViewById(R.id.TV_TabEmail);
                                TextView country = view.findViewById(R.id.TV_TabCountry);
                                TextView level = view.findViewById(R.id.TV_TabLevel);
                                TextView prefer = view.findViewById(R.id.TV_TabPreferences);
                                description.setText(usermodel.getDescription());

                                if(usermodel.getShowEmail()){
                                    email.setText(usermodel.getEmailAddr());
                                }
                                else{
                                    email.setText("Email Address is not allowed to be shown");
                                }

                                country.setText(usermodel.getCountry());
                                level.setText(usermodel.getLevel());
                                if(usermodel.getShowPreferences()){
                                    prefer.setText(usermodel.getPreferences().get(0));
                                }
                                else{
                                    prefer.setText("Preferences is not allowed to be shown");
                                }

                                View.OnClickListener editOCL = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), UserActivity.class);
                                        intent.putExtra("fragmentname", "EditAboutMe");
                                        intent.putExtra("usermodel", usermodel);
                                        System.out.println("Model in up fragment"+ usermodel.getUserName());
                                        System.out.println("Preferences in up"+usermodel.getPreferences());
                                        startActivity(intent);
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
       System.out.println("Userid readData:"+ userID);
       userCollection.whereEqualTo("userId", userID).get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           ArrayList<RecipeModel> recipeModelList = new ArrayList<>();
                           UserModel usermodel = new UserModel();
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                usermodel = queryDocumentSnapshot.toObject(UserModel.class);
                               //System.out.println(documentSnapshot.get("userId"));
                               System.out.println(usermodel.getUserId());

                           }
                           recipeCollection.whereEqualTo("userId", usermodel.getUserId()).get()
                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                       @Override
                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                           if(task.isSuccessful()){
                                               for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                   recipeModelList.add(queryDocumentSnapshot.toObject(RecipeModel.class));
                                               }
                                           }
                                       }
                                   });

                           firestoreOnCallBack.onCallBack(usermodel,recipeModelList);
                       }
                   }
               });





   }

    private interface FirestoreOnCallBack{

        void onCallBack(UserModel usermodel, ArrayList<RecipeModel>recipeModels);

    }
}