package com.example.cookwhat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.adapters.followAdapter;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.models.followData;

import java.util.ArrayList;

public class followPopUp extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String following_follower;
    View parentview;
    ArrayList<String> followList = new ArrayList<>();
    ArrayList<String> followIDList = new ArrayList<>();
    ArrayList<String> checkFollowIDList = new ArrayList<>();
    Boolean isFollowing = false;
    UserModelDB userModel;
    followAdapter adapter;
    ArrayList<followData> followDataList = new ArrayList<>();
    Context context;


    public followPopUp(View parentview, String following_follower, ArrayList<String> followList, ArrayList<String> followIDList, UserModelDB userModel, ArrayList<String>checkFollowIDList) {
        this.following_follower = following_follower;
        this.parentview = parentview;
        this.followList = followList;
        this.followIDList = followIDList;
        this.userModel = userModel;
        this.checkFollowIDList = checkFollowIDList;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View followPopUp = inflater.inflate(R.layout.pop_up_follow,null);
        EditText followSearchBar = followPopUp.findViewById(R.id.ET_FollowSearchBar);
        ListView follow_list = (ListView) followPopUp.findViewById(R.id.LV_Follow);

        if (following_follower.equalsIgnoreCase("follower")){
            followSearchBar.setHint("Search your "+ following_follower);
            isFollowing = false;
        }
        else{
            followSearchBar.setHint("Search your "+ following_follower);
            isFollowing = true;
        }


        for(int i =0; i<followList.size(); i++){
            followData follow = new followData(followList.get(i), followIDList.get(i));
            followDataList.add(follow);
        }

        adapter = new followAdapter(this.getActivity(),followDataList);

        follow_list.setAdapter(adapter);

        followSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        AdapterView.OnItemClickListener followListOCL = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int i, long l) {
                followData selectedFollow = (followData)adapter.getItem(i);
                String userIdToView = selectedFollow.getFollowId();
                System.out.println(selectedFollow.getFollowId());

                if (following_follower.equals("follower")) {
                    if (checkFollowIDList.contains(userIdToView)){
                        isFollowing = true;
                    }
                }
                Intent intent = new Intent(getContext(), UserActivity.class);
                //ViewProfileFragment viewProfileFragment = new ViewProfileFragment();
               //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userIdToView);
                bundle.putSerializable("userModel", userModel);

                intent.putExtras(bundle);
                startActivity(intent);
                dismiss();
            }


        };
        follow_list.setOnItemClickListener(followListOCL);
        return followPopUp;

    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int)(metrics.widthPixels*0.90);
        int height = (int)(metrics.heightPixels*0.75);

        getDialog().getWindow().setLayout(width, height);
    }



    }


