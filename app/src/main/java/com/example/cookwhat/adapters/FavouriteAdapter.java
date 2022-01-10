package com.example.cookwhat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cookwhat.R;

import java.util.ArrayList;

public class FavouriteAdapter extends ArrayAdapter<String> {
    ArrayList<String> favouriteList;
    Context context;

    public FavouriteAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<String> list) {
        super(context, 0,list);
        this.favouriteList = list;
        this.context = context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(int i, @Nullable View view, @Nullable ViewGroup viewGroup) {
        View view1 = view;
        if(view1 == null){
            view1 = LayoutInflater.from(context).inflate(R.layout.favourite_item,viewGroup, false);
        }

        ImageView favImg = view1.findViewById(R.id.IV_FavouriteImage);
        TextView favName = view1.findViewById(R.id.TV_FavouriteName);
        TextView tag1 = view1.findViewById(R.id.TV_Tag1);
        TextView tag2 = view1.findViewById(R.id.TV_Tag2);
        TextView tag3 = view1.findViewById(R.id.TV_Tag3);

        //favImg.setImageResource(favouriteList.get(i).getImage);
        /*favName.setText(favouriteList.get(i).getTitle());
        tag1.setText(favouriteList.get(i).getTags().get(0));
        tag2.setText(favouriteList.get(i).getTags().get(1));
        tag3.setText(favouriteList.get(i).getTags().get(2));*/
        favName.setText("hi"+favouriteList.get(i));
        tag1.setText("hihi");
        tag2.setText("favouriteList.get(i)");
        tag3.setText("favouriteList.get(i)");


        return view1;


    }

    public void removeItem(int i){
        this.favouriteList.remove(i);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return this.favouriteList.size();
    }
}
