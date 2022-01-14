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

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.cookwhat.ExpandableHeightGridView;
import com.example.cookwhat.R;
import com.example.cookwhat.followPopUp;
import com.google.android.material.tabs.TabLayout;

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
    String selectedUsername;
    Boolean haveFollowed = false;
    Boolean isFollowing = false;
    ArrayList<String> recipeName = new ArrayList<>();
    ArrayList<Integer> Img;
    ArrayList<String> followerList = new ArrayList<>();
    ArrayList<String> followingList = new ArrayList<>();
    ArrayList<String> followerIDList = new ArrayList<>();
    ArrayList<String> followingIDList = new ArrayList<>();

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



    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        System.out.println("bundle received"+ bundle);
        if(bundle != null){
            this.selectedUserID = bundle.getString("viewUserID");
            this.selectedUsername = bundle.getString("viewUsername");
            this.haveFollowed = bundle.getBoolean("isFollowing");
            System.out.println(haveFollowed);

        }
        else{
            Log.d("ErrorType","null details on selected user to view");
        }
        Button showFollow = view.findViewById(R.id.Btn_ShowFollow);
        System.out.println("ViewCreated:"+ haveFollowed);

        if(haveFollowed == true){
            showFollow.setText("Unfollow");
        }
        else{
            showFollow.setText("Follow");
        }

        View.OnClickListener showFollowOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String showFollowText = showFollow.getText().toString();

                if (showFollowText.equals("Unfollow")){
                    //remove existing following
                    showFollow.setText("Follow");
                }
                else{
                    //add new following
                    showFollow.setText("Unfollow");
                }
            }
        };

        showFollow.setOnClickListener(showFollowOCL);


        Button follower = view.findViewById(R.id.Btn_FollowFollower);
        Button following = view.findViewById(R.id.Btn_FollowFollowing);

        View.OnClickListener followerOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass followerArraylist and followernamelist
                followPopUp followList = new followPopUp(view, "follower",followerList, followerIDList,selectedUserID,followingIDList);
                followList.show(getActivity().getSupportFragmentManager(), "followerDialog");
            }
        };

        View.OnClickListener followingOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followPopUp followList = new followPopUp(view, "following", followingList,followingIDList,selectedUserID,followingIDList);
                followList.show(getActivity().getSupportFragmentManager(), "followingDialog");
            }
        };

        follower.setOnClickListener(followerOCL);
        following.setOnClickListener(followingOCL);

        if (isFollowing == true){
            follower.setEnabled(true);
            follower.setClickable(true);
            following.setEnabled(true);
            following.setClickable(true);
        }
        else{
            following.setEnabled(false);
            following.setClickable(false);
        }



        if (recipeName.isEmpty()){
            String noRecipe = "Nothing to show here.\nLet's get started on sharing your secret recipes!";
            TextView textcontent = view.findViewById(R.id.TV_Empty);
            textcontent.setText(noRecipe);

        }
        else{
            view.findViewById(R.id.TV_Empty).setVisibility(View.INVISIBLE);
            ExpandableHeightGridView tabcontent = view.findViewById(R.id.FollowTabContent);
            ViewProfileFragment.CustomAdapter recipeAdapter = new ViewProfileFragment.CustomAdapter();
            tabcontent.setExpanded(true);
            tabcontent.setAdapter(recipeAdapter);
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

                switch(tab.getPosition()){
                    case 0:
                    {
                        removeChild(ll);
                        addChild(cl, clmain);
                        cl.setVisibility(View.VISIBLE);
                        break;

                    }
                    case 1:
                    {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }
}