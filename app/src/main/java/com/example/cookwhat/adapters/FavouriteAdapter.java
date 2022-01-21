package com.example.cookwhat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.FavouriteFragment;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends ArrayAdapter<String> {
    ArrayList<String> recipeId;
    ArrayList<String>recipeName;
    ArrayList<String>recipeImg;
    ArrayList<List<String>>tags;
    UserModelDB userModelDB;
    ArrayList<RecipeModelDB> recipeModelDB;
    Context context;

    public FavouriteAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<String> recipeId, ArrayList<String>recipeName, ArrayList<String>recipeImg, ArrayList<List<String>>tags, UserModelDB userModelDB, ArrayList<RecipeModelDB> recipeModelDB) {
        super(context, 0,recipeId);
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImg = recipeImg;
        this.tags = tags;
        this.userModelDB = userModelDB;
        this.recipeModelDB = recipeModelDB;
        this.context = context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(int i, @Nullable View view, @Nullable ViewGroup viewGroup) {
        View view1 = view;
        ViewHolder holder = null;
        if(view1 == null){
            view1 = LayoutInflater.from(context).inflate(R.layout.favourite_item,viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.favImg = view1.findViewById(R.id.IV_FavouriteImage);
            viewHolder.tag1Icon = view1.findViewById(R.id.IV_Tag1);
            viewHolder.tag2Icon = view1.findViewById(R.id.IV_Tag2);
            viewHolder.tag3Icon = view1.findViewById(R.id.IV_Tag3);
            viewHolder.favName = view1.findViewById(R.id.TV_FavouriteName);
            viewHolder.tag1 = view1.findViewById(R.id.TV_Tag1);
            viewHolder.tag2 = view1.findViewById(R.id.TV_Tag2);
            viewHolder.tag3 = view1.findViewById(R.id.TV_Tag3);
            viewHolder.remove = view1.findViewById(R.id.Btn_Unfavourite);
            viewHolder.moveTo = view1.findViewById(R.id.Btn_Move);
            System.out.println(recipeName);

            view1.setTag(viewHolder);

            viewHolder.favName.setText(recipeName.get(i));
            Uri uri = Uri.parse(recipeImg.get(i));
            Picasso.get().load(uri).fit().into(viewHolder.favImg);

            if(tags.get(i)!= null){
                int numTags =  tags.get(i).size();

                if (numTags<3){
                    switch (numTags){
                        case 0:
                            viewHolder.tag1.setVisibility(View.GONE);
                            viewHolder.tag1Icon.setVisibility(View.GONE);
                            viewHolder.tag2.setVisibility(View.GONE);
                            viewHolder.tag2Icon.setVisibility(View.GONE);
                            viewHolder.tag3.setVisibility(View.GONE);
                            viewHolder.tag3Icon.setVisibility(View.GONE);
                            break;
                        case 1:
                            viewHolder.tag1.setText(tags.get(i).get(0));
                            viewHolder.tag1.setVisibility(View.VISIBLE);
                            viewHolder.tag1Icon.setVisibility(View.VISIBLE);
                            viewHolder.tag2.setVisibility(View.GONE);
                            viewHolder.tag2Icon.setVisibility(View.GONE);
                            viewHolder.tag3.setVisibility(View.GONE);
                            viewHolder.tag3Icon.setVisibility(View.GONE);
                            break;
                        case 2:
                            viewHolder.tag1.setText(tags.get(i).get(0));
                            viewHolder.tag2.setText(tags.get(i).get(1));
                            viewHolder.tag1.setVisibility(View.VISIBLE);
                            viewHolder.tag2.setVisibility(View.VISIBLE);
                            viewHolder.tag3.setVisibility(View.GONE);
                            viewHolder.tag1Icon.setVisibility(View.VISIBLE);
                            viewHolder.tag2Icon.setVisibility(View.VISIBLE);
                            viewHolder.tag3Icon.setVisibility(View.GONE);
                            break;

                    }
                }

                else{
                    viewHolder.tag1.setText(tags.get(i).get(0));
                    viewHolder.tag2.setText(tags.get(i).get(1));
                    viewHolder.tag3.setText(tags.get(i).get(2));
                    viewHolder.tag1.setVisibility(View.VISIBLE);
                    viewHolder.tag2.setVisibility(View.VISIBLE);
                    viewHolder.tag3.setVisibility(View.VISIBLE);
                    viewHolder.tag1Icon.setVisibility(View.VISIBLE);
                    viewHolder.tag2Icon.setVisibility(View.VISIBLE);
                    viewHolder.tag3Icon.setVisibility(View.VISIBLE);


                }

            }

            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavouriteFragment.removeItem(i);
                }
            });
            viewHolder.moveTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavouriteFragment.moveItem(context,userModelDB,recipeModelDB.get(i));
                }
            });



        }
        else{
            holder = (ViewHolder) view1.getTag();
        }

       //remove.setFocusable(false);
        //remove.setClickable(false);
        return view1;

    }

    public void removeItem(int i){


    }

    @Override
    public int getCount() {
        return this.recipeName.size();
    }

    public ArrayList<String> getRecipeId(){
        return recipeId;
    }

    public class ViewHolder{
        ImageView favImg ;
        ImageView tag1Icon ;
        ImageView tag2Icon ;
        ImageView tag3Icon ;
        TextView favName ;
        TextView tag1 ;
        TextView tag2 ;
        TextView tag3 ;
        Button remove;
        Button moveTo;
    }
}
