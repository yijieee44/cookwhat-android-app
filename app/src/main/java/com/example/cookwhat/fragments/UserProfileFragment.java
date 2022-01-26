package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cookwhat.ExpandableHeightGridView;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.EditAboutMeActivity;
import com.example.cookwhat.activities.FavouriteActivity;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.followPopUp;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.models.followData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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
    ArrayList<String> followerNameList = new ArrayList<>();
    ArrayList<String> followingNameList = new ArrayList<>();
    ArrayList<String> followerIDList = new ArrayList<>();
    ArrayList<String> followingIDList = new ArrayList<>();
    ArrayList<followData>follower = new ArrayList<>();
    ArrayList<followData>following = new ArrayList<>();
    ExpandableHeightGridView tabcontent;
    TextView textcontent;
    LinearLayout ll;
    ConstraintLayout cl, clmain;
    FirebaseAuth mAuth;
    Boolean showTabAboutMe = false;
    TextView description ;
    TextView email;
    TextView country ;
    TextView level ;
    TextView prefer1;
    TextView prefer2;
    TextView prefer3;
    ArrayList<Integer> profilepics;

    Dialog loadingDialog;


    ActivityResultLauncher<Intent> editActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        NavHostFragment host = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.NHFMain);
                        Bundle bundle = new Bundle();
                        bundle.putString("toAboutMe", "True");
                        host.getNavController().popBackStack();
                        host.getNavController().navigate(R.id.DestUserProfile, bundle);

                    }
                }
            });

    ActivityResultLauncher<Intent> favouriteActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 122) {
                        NavHostFragment host = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.NHFMain);
                        Bundle bundle = new Bundle();
                        host.getNavController().popBackStack();
                        host.getNavController().navigate(R.id.DestUserProfile, bundle);

                    }
                }
            });


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

        profilepics =  new ArrayList<>();
        for(int i =0 ; i<33; i++){
            String name = "ic_profile_pic_" + String.valueOf(i+1);
            int resourceId = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
            profilepics.add(resourceId);
        }

//        if (getArguments() != null) {
//            Bundle bundle = this.getArguments();
//            String action = bundle.getString("action");
//            if(action.equals("updated about me")){
//                showTabAboutMe = true;
//            }
//        }
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            if(bundle.getString("toAboutMe") != null){
                showTabAboutMe = true;
            }

        }
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        System.out.println("getUserid"+userID);;

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

        loadingDialog = new Dialog(getContext());
        readDataAndBind(view);


    }

    public void readDataAndBind(View view){
        readData(new FirestoreOnCallBack() {
            UserModelDB usermodel = new UserModelDB();
            @Override
            public void onCallBackUser(UserModelDB usermodel) {
                this.usermodel = usermodel;
            }

            Button favCategory = view.findViewById(R.id.Btn_Favourite);
            Button btnFollower = view.findViewById(R.id.Btn_Follower);
            Button btnFollowing = view.findViewById(R.id.Btn_Following);
            TextView tvUserName  = view.findViewById(R.id.TV_UserName);
            ImageView profilepic = view.findViewById(R.id.IV_Profile);


            int numFollowers;
            int numFollowings;

            @Override
            public void onCallBack(ArrayList<RecipeModelDB> createdRecipes, ArrayList<String>recipeImages) {

                tvUserName.setText(usermodel.getUserName());
                followerNameList = usermodel.getFollowersName();
                followerIDList = usermodel.getFollowersId();
                followingIDList = usermodel.getFollowingsId();
                followingNameList = usermodel.getFollowingsName();
                numFollowers = followerNameList.size();
                numFollowings = followingNameList.size();
                btnFollower.setText(Integer.toString(numFollowers));
                btnFollowing.setText(Integer.toString(numFollowings));
                profilepic.setImageResource(usermodel.getProfilePic());

                for(RecipeModelDB recipe : createdRecipes){
                    recipeName.add(recipe.getTitle());

                }

                View.OnClickListener profilepicOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditPicDialog(v, usermodel, profilepics);
                    }
                };

                profilepic.setOnClickListener(profilepicOCL);

                View.OnClickListener followerOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followPopUp showFollowList = new followPopUp(view, "follower", followerNameList, followerIDList, usermodel, followingIDList);
                        showFollowList.show(getActivity().getSupportFragmentManager(), "ProfilePicDialog");
                    }
                };

                View.OnClickListener followingOCL = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followPopUp showFollowList = new followPopUp(view, "following", followingNameList, followingIDList,usermodel, followingIDList);
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
                        intent.putExtra("usermodel", usermodel);
                        favouriteActivityResultLauncher.launch(intent);
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
                    CustomAdapter recipeAdapter = new CustomAdapter(recipeName, recipeImages, createdRecipes);
                    tabcontent.setExpanded(true);
                    tabcontent.setAdapter(recipeAdapter);
                    tabcontent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            RecipeModelDB recipeModelDB = (RecipeModelDB) recipeAdapter.getItem(i);
                            Intent intent = new Intent(getContext(), ViewRecipeActivity.class);
                            intent.putExtra("userModel", usermodel);
                            intent.putExtra("recipeModel",recipeModelDB);
                            startActivity(intent);

                        }
                    });
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
                                description = view.findViewById(R.id.TV_TabDescription);
                                email = view.findViewById(R.id.TV_TabEmail);
                                country = view.findViewById(R.id.TV_TabCountry);
                                level = view.findViewById(R.id.TV_TabLevel);
                                prefer1 = view.findViewById(R.id.TV_TabPreferences1);
                                prefer2 = view.findViewById(R.id.TV_TabPreferences2);
                                prefer3 = view.findViewById(R.id.TV_TabPreferences3);

                                description.setText(usermodel.getDescription());

                                if(usermodel.isShowEmail()){
                                    email.setText(usermodel.getEmailAddr());
                                }
                                else{
                                    email.setText("Email Address is not allowed to be shown");
                                }

                                if(usermodel.getCountry().equals("--Please select a country of your origin--")){
                                    country.setText("-");
                                }
                                else{
                                    country.setText(usermodel.getCountry());
                                }
                                if(usermodel.getLevel().equals("--Please select your cooking skill level--")){
                                    level.setText("-");
                                }
                                else{
                                    level.setText(usermodel.getLevel());
                                }

                                if(usermodel.isShowPreference()){
                                    prefer1.setText(usermodel.getPreference().get(0));

                                    if(usermodel.getPreference().size()>1){
                                        prefer2.setText(usermodel.getPreference().get(1));
                                        prefer2.setVisibility(View.VISIBLE);
                                    }

                                    if(usermodel.getPreference().size()>2){
                                        prefer3.setText(usermodel.getPreference().get(2));
                                        prefer3.setVisibility(View.VISIBLE);
                                    }
                                }
                                else{
                                    prefer1.setText("Preferences is not allowed to be shown");
                                }

                                View.OnClickListener editOCL = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), EditAboutMeActivity.class);

                                        intent.putExtra("usermodel", usermodel);
                                        System.out.println("Model in up fragment"+ usermodel.getUserName());
                                        System.out.println("Preferences in up"+usermodel.getPreference());
                                        editActivityResultLauncher.launch(intent);
//                                        startActivity(intent);
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
                if(showTabAboutMe){
                    TabLayout.Tab aboutMeTab = tabLayout.getTabAt(1);
                    aboutMeTab.select();
                }


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

            ImageView img = view1.findViewById(R.id.IV_SecretRecipe);
            TextView text = view1.findViewById(R.id.TV_SecretRecipe);
            Uri uri = Uri.parse(recipeImg.get(i));
            Picasso.get().load(uri).fit().into(img);
            //img.setImageURI(uri);
            text.setText(recipeName.get(i));

            return view1;
        }
    }

   public void readData(FirestoreOnCallBack firestoreOnCallBack, String userID){
       loadingDialog.setCancelable(false);
       loadingDialog.setContentView(R.layout.dialog_loading);
       loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

       int width = (int)(getResources().getDisplayMetrics().widthPixels);
       int height = (int)(getResources().getDisplayMetrics().heightPixels);

       loadingDialog.getWindow().setLayout(width, height);
       loadingDialog.show();

       FirebaseFirestore db = FirebaseFirestore.getInstance();
       CollectionReference userCollection = db.collection("user");
       CollectionReference recipeCollection = db.collection("recipe");
       System.out.println("Userid readData:"+ userID);



       userCollection.whereEqualTo("userId", userID).get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           ArrayList<RecipeModelDB> recipeModelList = new ArrayList<>();
                           ArrayList<String> recipeImageList = new ArrayList<>();
                           UserModelDB usermodel = new UserModelDB();
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                usermodel = queryDocumentSnapshot.toObject(UserModelDB.class);
                               //System.out.println(documentSnapshot.get("userId"));
                               System.out.println("Userprofile get favourite"+ usermodel.getFavouriteCategory());
                               firestoreOnCallBack.onCallBackUser(usermodel);

                               recipeCollection.whereEqualTo("userId", usermodel.getUserId()).get()
                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if(task.isSuccessful()){
                                                   for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                       RecipeModelDB recipe = queryDocumentSnapshot.toObject(RecipeModelDB.class);
                                                       recipeImageList.add(getResources().getString(R.string.recipe_image_uri)+recipe.getSteps().get(0).getImage()+"?alt=media");
                                                       recipeModelList.add(recipe);
                                                       System.out.println("yes here");
                                                   }
                                                   System.out.println("reading:"+ recipeModelList.size());
                                                   firestoreOnCallBack.onCallBack(recipeModelList, recipeImageList);

                                               }
                                           }
                                       });

                           }
                           loadingDialog.dismiss();
                       }
                       else{
                           loadingDialog.cancel();
                           Toast.makeText(getContext(),
                                   "Fail to load user data, please try again",
                                   Toast.LENGTH_SHORT)
                                   .show();
                       }
                   }
               });






   }

   public void reloadForEditedUserModel(UserModelDB editedUserModel){
       description.setText(editedUserModel.getDescription());

       if(editedUserModel.isShowEmail()){
           email.setText(editedUserModel.getEmailAddr());
       }
       else{
           email.setText("Email Address is not allowed to be shown");
       }

       country.setText(editedUserModel.getCountry());
       level.setText(editedUserModel.getLevel());
       if(editedUserModel.isShowPreference()){
           prefer1.setText(editedUserModel.getPreference().get(0));
       }
       else{
           prefer1.setText("Preferences is not allowed to be shown");
       }
   }

    private interface FirestoreOnCallBack{
        void onCallBack(ArrayList<RecipeModelDB>createdRecipe, ArrayList<String> recipeImage);
        void onCallBackUser(UserModelDB usermodel);
    }

    private void showEditPicDialog(View view, UserModelDB currentUser, ArrayList<Integer> profilepics) {
        EditProfilePicDialogFragment dialog = new EditProfilePicDialogFragment(profilepics, currentUser);
        dialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "dialog");
    }
}