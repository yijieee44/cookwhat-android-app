package com.example.cookwhat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    LayoutInflater inflater;
    int type = 0;
    ArrayList<UserModelDB> userModel;
    ArrayList<RecipeModelDB> recipeModel;
    Context ctx;
    UserModelDB currentUserModel;

    public SearchResultAdapter(ArrayList<UserModelDB> userList, Context ctx, UserModelDB currentUserModel){
        type = 0;
        this.userModel = userList;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.currentUserModel = currentUserModel;
    }

    public SearchResultAdapter(ArrayList<RecipeModelDB> recipeList, ArrayList<UserModelDB> userList, Context ctx){
        type = 1;
        this.recipeModel = recipeList;
        this.userModel = userList;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_result_layout, parent, false);
        return new SearchResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(type == 0){
            holder.searchResultTitle.setText(userModel.get(position).getUserName());
            holder.image.setImageResource(userModel.get(position).getProfilePic());
            holder.chipGroup.setVisibility(View.GONE);
            holder.favourite.setVisibility(View.GONE);
        }
        else{
            int profilePic = 0;
            RecipeModelDB recipe = recipeModel.get(position);
            holder.searchResultTitle.setText(recipe.getTitle());

//            for (int i=0;i<userModel.size();i++){
//                if(recipe.getUserId().equals(userModel.get(i).getUserId())){
//                    profilePic = userModel.get(i).getProfilePic();
//                    break;
//                }
//            }
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/cookwhat.appspot.com/o/images%2F" + recipeModel.get(position).getSteps().get(0).getImage() + "?alt=media")
                    .into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) holder.image.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(holder.image.getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.min(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            holder.image.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError(Exception e) {
//                            holder.image.setImageResource(R.drawable.default_image);
                        }
                    });
            int index = 0;
            for(String tag: recipe.getTags()){
                if(index<3){
                    Chip chip = new Chip(holder.chipGroup.getContext());
                    chip.setText(tag);
                    Drawable img = holder.chipGroup.getContext().getResources().getDrawable(R.drawable.ic_tag_svgrepo_com);
                    chip.setChipIcon(img);
                    chip.setChipBackgroundColorResource(R.color.transparent);
                    holder.chipGroup.addView(chip);
                }
                else{
                    break;
                }
                index ++;
            }
            holder.favourite.setText(Integer.toString(recipeModel.get(position).getNumFav()));
        }
    }

    @Override
    public int getItemCount() {
        if(type == 0){
            return userModel.size();
        }
        else{
            return recipeModel.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView searchResultTitle;
        ImageView image;
        ChipGroup chipGroup;
        TextView favourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            searchResultTitle = itemView.findViewById(R.id.TVSearchResultTitle);
            image = itemView.findViewById(R.id.IVSearchResultImage);
            chipGroup = itemView.findViewById(R.id.CGTags);
            favourite = itemView.findViewById(R.id.TVFavourite);

            //View User
            if(type == 0){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = userModel.get(getAdapterPosition()).getUserId();
                        Intent intent = new Intent(itemView.getContext(), UserActivity.class);
                        intent.putExtra("fragmentname", "viewprofilefragment");
                        intent.putExtra("userId", userId);
                        intent.putExtra("userModel",currentUserModel );
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

            //View Recipe
            if(type == 1){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), ViewRecipeActivity.class);

                        UserModelDB userModelDB = new UserModelDB();
                        intent.putExtra("recipeModel", recipeModel.get(getAdapterPosition()));
                        for (int i=0;i<userModel.size();i++){
                            if(recipeModel.get(getAdapterPosition()).getUserId().equals(userModel.get(i).getUserId())){
                                userModelDB = userModel.get(i);
                                Log.d("GOT USER", "wasd");
                                break;
                            }
                        }
                        intent.putExtra("userModel", userModelDB);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

        }
    }
}
