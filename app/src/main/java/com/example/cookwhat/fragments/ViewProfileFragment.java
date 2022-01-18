package com.example.cookwhat.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String selectedUserID;
    UserModelDB currentUser;

    Boolean haveFollowed = false;
    Boolean isFollowing = false;
    ArrayList<String> recipeName = new ArrayList<>();
    ArrayList<Integer> Img;
    ArrayList<String> followerNameList = new ArrayList<>();
    ArrayList<String> followingNameList = new ArrayList<>();
    ArrayList<String> followerIDList = new ArrayList<>();
    ArrayList<String> followingIDList = new ArrayList<>();
    int numFollowers = 0;
    int numFollowings = 0;

    public ViewProfileFragment() {
        //Fetch selectedUserID info
        //Fetch follower of selectedUserID
        //Fetch following of selectedID
        //If else to set havefollowed
        //If else to set isfollowing

        //Fetch selectedUserID created recipe
        //set recipeName
        //set Image
        //set recipe model list

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getActivity().getIntent()!= null) {

            this.selectedUserID = getActivity().getIntent().getStringExtra("userId");
            this.currentUser = (UserModelDB) getActivity().getIntent().getSerializableExtra("currentUserModel");

        } else {
            Log.d("USERID::", "null user id");
        }
        System.out.println("selectedUserId" + selectedUserID);



    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        readData(new FirestoreOnCallBack() {
            UserModelDB selectedUser = new UserModelDB();

            @Override
            public void onCallBackUser(UserModelDB usermodel) {
                System.out.println("In oncallback"+selectedUserID);
                selectedUser = usermodel;

            }

            Button btnFollower = view.findViewById(R.id.Btn_FollowFollower);
            Button btnFollowing = view.findViewById(R.id.Btn_FollowFollowing);
            ImageView profilepic = view.findViewById(R.id.IV_FollowProfile);
            TextView profileName = view.findViewById(R.id.TV_FollowProfileName);
            @Override
            public void onCallBack(ArrayList<RecipeModelDB> createdRecipe, ArrayList<String> recipeImage) {

                profileName.setText(selectedUser.getUserName());
                followerNameList = selectedUser.getFollowersName();
                followerIDList = selectedUser.getFollowersId();
                followingIDList = selectedUser.getFollowingsId();
                followingNameList = selectedUser.getFollowingsName();
                numFollowers = followerNameList.size();
                numFollowings = followingNameList.size();
                btnFollower.setText(Integer.toString(numFollowers));
                btnFollowing.setText(Integer.toString(numFollowings));
                profilepic.setImageResource(selectedUser.getProfilePic());
                Button showFollow = view.findViewById(R.id.Btn_ShowFollow);

                if(selectedUser.getFollowersId().contains(currentUser.getUserId())){
                    showFollow.setText("Unfollow");
                }

                else{
                    showFollow.setText("Follow");
                }

                View.OnClickListener showFollowOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String showFollowText = showFollow.getText().toString();

                        if (showFollowText.equals("Unfollow")) {
                            //remove existing following
                            showFollow.setText("Follow");
                        } else {
                            //add new following
                            showFollow.setText("Unfollow");
                        }
                    }
                };

                showFollow.setOnClickListener(showFollowOCL);

                View.OnClickListener followerOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //pass followerArraylist and followernamelist
                        //followPopUp followList = new followPopUp(view, "follower",followerList, followerIDList,selectedUserID,followingIDList);
                        // followList.show(getActivity().getSupportFragmentManager(), "followerDialog");
                    }
                };

                View.OnClickListener followingOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // followPopUp followList = new followPopUp(view, "following", followingList,followingIDList,selectedUserID,followingIDList);
                        //followList.show(getActivity().getSupportFragmentManager(), "followingDialog");
                    }
                };

                btnFollower.setOnClickListener(followerOCL);
                btnFollowing.setOnClickListener(followingOCL);

                if (isFollowing == true) {
                    btnFollower.setEnabled(true);
                    btnFollower.setClickable(true);
                    btnFollowing.setEnabled(true);
                    btnFollowing.setClickable(true);
                } else {
                    btnFollowing.setEnabled(false);
                    btnFollowing.setClickable(false);
                }

                for(RecipeModelDB recipe : createdRecipe){
                    recipeName.add(recipe.getTitle());

                }

                if (recipeName == null) {
                    String noRecipe = "Nothing to show here.\nLet's get started on sharing your secret recipes!";
                    TextView textcontent = view.findViewById(R.id.TV_Empty);
                    textcontent.setText(noRecipe);

                } else {
                    view.findViewById(R.id.TV_Empty).setVisibility(View.INVISIBLE);
                    ExpandableHeightGridView tabcontent = view.findViewById(R.id.FollowTabContent);
                    CustomAdapter recipeAdapter = new CustomAdapter(recipeName, recipeImage, createdRecipe);
                    tabcontent.setExpanded(true);
                    tabcontent.setAdapter(recipeAdapter);
                    tabcontent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            RecipeModelDB recipeModelDB = (RecipeModelDB) recipeAdapter.getItem(i);
                            Intent intent = new Intent(getContext(), ViewRecipeActivity.class);
                            intent.putExtra("userModel", selectedUser);
                            intent.putExtra("recipeModel",recipeModelDB);
                            startActivity(intent);

                        }
                    });
                }


                ConstraintLayout cl = view.findViewById(R.id.CL_FollowSecretRecipe);
                cl.setVisibility(View.VISIBLE);

                LinearLayout ll = view.findViewById(R.id.LL_FollowAboutMe);
                ConstraintLayout clmain = view.findViewById(R.id.CS_Follow);

                removeChild(ll);

                TabLayout tabLayout = view.findViewById(R.id.TL_FollowProfileTab);
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


        }, selectedUserID);


    }

    public void removeChild(ViewGroup viewgroup) {
        ((ViewGroup) viewgroup.getParent()).removeView(viewgroup);

    }

    public void addChild(ViewGroup child, ViewGroup parent) {
        parent.addView(child);
    }

    private class CustomAdapter extends BaseAdapter {

        ArrayList<String> recipeName = new ArrayList<>();
        ArrayList<String> recipeImg = new ArrayList<>();
        ArrayList<RecipeModelDB> recipeModel = new ArrayList<>();
        public CustomAdapter(ArrayList<String> recipeName, ArrayList<String> recipeImg,ArrayList<RecipeModelDB>recipeModel) {
            this.recipeName = recipeName;
            this.recipeImg = recipeImg;
            this.recipeModel = recipeModel;
        }

        @Override
        public int getCount() {
            return recipeName.size();
        }

        @Override
        public Object getItem(int i) {
            return recipeModel.get(i);
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
            Uri uri = Uri.parse(recipeImg.get(i));
            img.setImageURI(uri);
            text.setText(recipeName.get(i));

            return view1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    public void readData(FirestoreOnCallBack firestoreOnCallBack, String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection("user");
        CollectionReference recipeCollection = db.collection("recipe");
        System.out.println("Userid readData:" + userID);

        userCollection.whereEqualTo("userId", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RecipeModelDB> recipeModelList = new ArrayList<>();
                            ArrayList<String> recipeImageList = new ArrayList<>();
                            UserModelDB usermodel = new UserModelDB();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                usermodel = queryDocumentSnapshot.toObject(UserModelDB.class);
                                //System.out.println(documentSnapshot.get("userId"));
                                System.out.println("in task result:"+usermodel.getUserId());
                                firestoreOnCallBack.onCallBackUser(usermodel);

                                recipeCollection.whereEqualTo("userId", usermodel.getUserId()).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                        RecipeModelDB recipe = queryDocumentSnapshot.toObject(RecipeModelDB.class);
                                                        recipeImageList.add(getResources().getString(R.string.recipe_image_uri) + recipe.getSteps().get(0).getImage() + "?alt=media");
                                                        recipeModelList.add(recipe);
                                                        System.out.println("yes here");
                                                    }
                                                    System.out.println("reading:" + recipeModelList.size());
                                                    firestoreOnCallBack.onCallBack(recipeModelList, recipeImageList);

                                                }
                                            }
                                        });


                            }


                        }
                    }
                });

    }

    private interface FirestoreOnCallBack{

        void onCallBack(ArrayList<RecipeModelDB>createdRecipe, ArrayList<String> recipeImage);
        void onCallBackUser(UserModelDB usermodel);

    }
}