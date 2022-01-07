package com.example.cookwhat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;

public class followPopUp extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String following_follower;
    View parentview;
    ArrayList<String> followList = new ArrayList<>();


    public followPopUp(View parentview, String following_follower, ArrayList<String> followList) {
        this.following_follower = following_follower;
        this.parentview = parentview;
        this.followList = followList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View followPopUp = inflater.inflate(R.layout.pop_up_follow,null);
        TextView follow_title = followPopUp.findViewById(R.id.TV_FollowTitle);
        ListView follow_list = (ListView) followPopUp.findViewById(R.id.LV_Follow);

        if (following_follower.equalsIgnoreCase("follower")){
            follow_title.setText("Followers");
        }
        else{
            follow_title.setText("Followings");
        }

        // followname = passed arg


        ArrayAdapter followAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1,followList);
        follow_list.setAdapter(followAdapter);

        AdapterView.OnItemClickListener followListOCL = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int i, long l) {
                String name = followAdapter.getItem(i).toString();
                //setSelectedUserID


                Navigation.findNavController(parentview).navigate(R.id.DestViewProfile);
                dismiss();
            }


        };

        follow_list.setOnItemClickListener(followListOCL);
        return followPopUp;
    }

    @Override
    public void onResume() {

        super.onResume();
        getDialog().getWindow().setLayout(900,1000);
    }


    }


