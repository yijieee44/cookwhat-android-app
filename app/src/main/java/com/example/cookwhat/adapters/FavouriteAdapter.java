package com.example.cookwhat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends ArrayAdapter<String> {
    ArrayList<String> recipeId;
    ArrayList<String>recipeName;
    ArrayList<String>recipeImg;
    ArrayList<List<String>>tags;
    Context context;

    public FavouriteAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<String> recipeId, ArrayList<String>recipeName, ArrayList<String>recipeImg, ArrayList<List<String>>tags) {
        super(context, 0,recipeId);
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImg = recipeImg;
        this.tags = tags;
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
        ImageView tag1Icon = view1.findViewById(R.id.IV_Tag1);
        ImageView tag2Icon = view1.findViewById(R.id.IV_Tag3);
        ImageView tag3Icon = view1.findViewById(R.id.IV_Tag2);
        TextView favName = view1.findViewById(R.id.TV_FavouriteName);
        TextView tag1 = view1.findViewById(R.id.TV_Tag1);
        TextView tag2 = view1.findViewById(R.id.TV_Tag2);
        TextView tag3 = view1.findViewById(R.id.TV_Tag3);

        //favImg.setImageResource(favouriteList.get(i).getImage);
        /*favName.setText(favouriteList.get(i).getTitle());
        tag1.setText(favouriteList.get(i).getTags().get(0));
        tag2.setText(favouriteList.get(i).getTags().get(1));
        tag3.setText(favouriteList.get(i).getTags().get(2));*/
        favName.setText(recipeName.get(i));
        Uri uri = Uri.parse(recipeImg.get(i));
        Picasso.get().load(uri).fit().into(favImg);

        if(tags.get(i)!= null){
            int numTags =  tags.get(i).size();

            if (numTags<3){
                switch (numTags){
                    case 1:
                        tag1.setText(tags.get(i).get(0));
                        tag1.setVisibility(View.VISIBLE);
                        tag1Icon.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        tag1.setText(tags.get(i).get(0));
                        tag1.setText(tags.get(i).get(1));
                        tag1.setVisibility(View.VISIBLE);
                        tag2.setVisibility(View.VISIBLE);
                        tag1Icon.setVisibility(View.VISIBLE);
                        tag2Icon.setVisibility(View.VISIBLE);

                        break;

                }
            }

            else{
                tag1.setText(tags.get(i).get(0));
                tag2.setText(tags.get(i).get(1));
                tag3.setText(tags.get(i).get(2));
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.VISIBLE);
                tag3.setVisibility(View.VISIBLE);
                tag1Icon.setVisibility(View.VISIBLE);
                tag2Icon.setVisibility(View.VISIBLE);
                tag3Icon.setVisibility(View.VISIBLE);


            }


        }

        return view1;


    }

    public void removeItem(int i){
        this.recipeId.remove(i);
        this.recipeName.remove(i);
        this.recipeImg.remove(i);
        this.tags.remove(i);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return this.recipeName.size();
    }

    public ArrayList<String> getRecipeId(){
        return recipeId;
    }
}
