package com.example.cookwhat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookwhat.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    ArrayList<RecipeModel> recipeModel;
    ArrayList<UserModel> userModel;
    Context ctx;
    LayoutInflater inflater;

    public RecipeAdapter(ArrayList<RecipeModel> recipeModel, ArrayList<UserModel> userModel, Context ctx){
        this.recipeModel = recipeModel;
        this.userModel = userModel;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeName.setText(recipeModel.get(position).getTitle());
        holder.tag.setText(recipeModel.get(position).getTags().get(0));
        holder.numFav.setText(Integer.toString(recipeModel.get(position).getNum_fav()));
        holder.userName.setText(userModel.get(position).getUserName());
        Picasso.get().load(recipeModel.get(position).getSteps().get(0).getImage()).into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return recipeModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView recipeName;
        ImageView recipeImage;
        Button numFav;
        Button userName;
        TextView tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.TVrecipeName);
            recipeImage = itemView.findViewById(R.id.IVrecipeImage);
            numFav = itemView.findViewById(R.id.BtnFav);
            userName = itemView.findViewById(R.id.BtnUser1);
            tag = itemView.findViewById(R.id.TVTag);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ViewRecipeActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}

