package com.example.cookwhat;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookwhat.fragments.UserProfileFragment;

import java.util.ArrayList;

public class followPopUp extends Activity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String following_follower;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        following_follower = bundle.getString("message");

        setContentView(R.layout.pop_up_follow);
        TextView follow_title = findViewById(R.id.TV_FollowTitle);
        ListView follow_list = (ListView) findViewById(R.id.LV_Follow);
        ArrayList<String> follow_name = new ArrayList<>();

        if (following_follower.equalsIgnoreCase("follower")){
            follow_title.setText("Followers");
        }
        else{
            follow_title.setText("Followings");
        }

        for (int i=0; i<10; i++){
            follow_name.add(Integer.toString(i));
        }

        ArrayAdapter followAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,follow_name);
        follow_list.setAdapter(followAdapter);
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

       getWindow().setLayout((int)(width*.8),(int)(height*.6));



    }
    }


