package com.example.cookwhat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.RecipeModelSearch;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    ArrayList<RecipeModelDB> recipeModel;
    ArrayList<UserModelDB> userModel;
    List<RecipeModelSearch> recipeModelSearch;
    Context ctx;
    LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int type = 0;
    public CollectionReference recipedb =  db.collection("recipe");
    public CollectionReference userdb =  db.collection("user");
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public RecipeAdapter(ArrayList<RecipeModelDB> recipeModel, ArrayList<UserModelDB> userModel, Context ctx){
        this.recipeModel = recipeModel;
        this.userModel = userModel;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
    }

    public RecipeAdapter(ArrayList<RecipeModelSearch> recipeModel, ArrayList<UserModelDB> userModel, Context ctx, int type){
        this.recipeModelSearch = recipeModel;
        this.userModel = userModel;
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.type = 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String username = "";
        int profilePic = 0;


        if(type == 1){
            for (int i=0;i<userModel.size();i++){
                if(recipeModelSearch.get(position).getUserId().equals(userModel.get(i).getUserId())){
                    username = userModel.get(i).getUserName();
                    profilePic = userModel.get(i).getProfilePic();
                    break;
                }
            }

            holder.recipeName.setText(recipeModelSearch.get(position).getTitle());
            holder.tag.setText(recipeModelSearch.get(position).getTags().get(0));
            holder.numFav.setText(Integer.toString(recipeModelSearch.get(position).getNumFav()));
            holder.userName.setText(username);
            holder.userName.setCompoundDrawablesWithIntrinsicBounds(profilePic, 0, 0, 0);
            Picasso.get().load(recipeModelSearch.get(position).getSteps().get(0).getImage()).into(holder.recipeImage);

            RecipeModelSearch recipe = recipeModelSearch.get(position);
            String[] ingredientsName = new String[recipe.getNonMatchingIngredientIndex().size()];
            int[] ingredientsIcon = new int[recipe.getNonMatchingIngredientIndex().size()];
            int index = 0;
            for(int indexNonExist: recipe.getNonMatchingIngredientIndex()){
                ingredientsName[index] = recipe.getIngName().get(indexNonExist);
                int iconResourceId = R.drawable.i0067_others;
                if (recipe.getIngIcon().get(indexNonExist) >= 0  && recipe.getIngIcon().get(indexNonExist) < Constants.INGREDIENTS_ICON.length) {
                    iconResourceId = Constants.INGREDIENTS_ICON[recipe.getIngIcon().get(indexNonExist)];
                }
                ingredientsIcon[index] = iconResourceId;

                index ++;
            }

            String[] utensilsName = new String[recipe.getNonMatchingUtensilIndex().size()];
            int[] utensilsIcon = new int[recipe.getNonMatchingUtensilIndex().size()];


            index = 0;
            for(int indexNonExist: recipe.getNonMatchingUtensilIndex()){
                utensilsName[index] = recipe.getUtName().get(indexNonExist);
                int iconResourceId = R.drawable.i0067_others;
                if (recipe.getUtIcon().get(indexNonExist) >= 0  && recipe.getUtIcon().get(indexNonExist) < Constants.UTENSILS_ICON.length) {
                    iconResourceId = Constants.UTENSILS_ICON[recipe.getUtIcon().get(indexNonExist)];
                }
                utensilsIcon[index] = iconResourceId;
                index ++;
            }

            MarketIngredientAdapter marketIngredientAdapter = new MarketIngredientAdapter(inflater.getContext(), ingredientsName, ingredientsIcon, 1) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    int color = Color.TRANSPARENT; // Transparent
                    view.setBackgroundColor(color);
                    return view;
                }
            };

            MarketIngredientAdapter marketUtensilAdapter = new MarketIngredientAdapter(inflater.getContext(), utensilsName, utensilsIcon, 1) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    int color = Color.TRANSPARENT; // Transparent
                    view.setBackgroundColor(color);
                    return view;
                }
            };
            holder.expandButton.setVisibility(View.VISIBLE);
            holder.expandableLayout.setVisibility(View.GONE);
            holder.ingredientsGridView.setAdapter(marketIngredientAdapter);
            holder.utensilsGridView.setAdapter(marketUtensilAdapter);

            if(recipe.getNonMatchingUtensilIndex().size() <=0 && recipe.getNonMatchingIngredientIndex().size() <=0){
                holder.expandButton.setVisibility(View.GONE);
            }
            else if(recipe.getNonMatchingUtensilIndex().size() <=0){
                holder.utensilDontHave.setVisibility(View.GONE);
            }
            else if(recipe.getNonMatchingIngredientIndex().size() <=0){
                holder.ingredientDontHave.setVisibility(View.GONE);
            }

        }
        else{
            for (int i=0;i<userModel.size();i++){
                if(recipeModel.get(position).getUserId().equals(userModel.get(i).getUserId())){
                    username = userModel.get(i).getUserName();
                    profilePic = userModel.get(i).getProfilePic();
                    break;
                }
            }

            holder.recipeName.setText(recipeModel.get(position).getTitle());
            holder.tag.setText(recipeModel.get(position).getTags().get(0));
            holder.numFav.setText(Integer.toString(recipeModel.get(position).getNumFav()));
            holder.userName.setText(username);
            holder.userName.setCompoundDrawablesWithIntrinsicBounds(profilePic, 0, 0, 0);
            Picasso.get().load(recipeModel.get(position).getSteps().get(0).getImage()).into(holder.recipeImage);
            holder.expandButton.setVisibility(View.GONE);
            holder.expandableLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if(type == 1){
            return recipeModelSearch.size();
        }
        else{
            return recipeModel.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView recipeName;
        ImageView recipeImage;
        TextView numFav;
        Button userName;
        TextView tag;
        ImageButton expandButton;
        LinearLayout expandableLayout;
        CardView cardView;
        GridView ingredientsGridView;
        GridView utensilsGridView;
        TextView ingredientDontHave;
        TextView utensilDontHave;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.CardView);
            recipeName = (TextView) itemView.findViewById(R.id.TVrecipeName);
            recipeImage = (ImageView) itemView.findViewById(R.id.IVrecipeImage);
            numFav = (TextView) itemView.findViewById(R.id.TVNumFav);
            userName = (Button) itemView.findViewById(R.id.BtnUser1);
            tag = (TextView) itemView.findViewById(R.id.TVTag);
            expandButton = itemView.findViewById(R.id.BtnExpand);
            expandableLayout = itemView.findViewById(R.id.ExpandableLayout);
            ingredientsGridView = (GridView) itemView.findViewById(R.id.GVDontHave);
            utensilsGridView = (GridView) itemView.findViewById(R.id.GVDontHaveUtensil);
            ingredientDontHave = (TextView) itemView.findViewById(R.id.TVDontHaveIngredient);
            utensilDontHave = (TextView) itemView.findViewById(R.id.TVDontHaveUtensil);


            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // If the CardView is already expanded, set its visibility
                    //  to gone and change the expand less icon to expand more.
                    if (expandableLayout.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        //  by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
//                        TransitionManager.beginDelayedTransition(cardView,
//                                new AutoTransition());
                        expandableLayout.setVisibility(View.GONE);
                        expandButton.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    }

                    // If the CardView is not expanded, set its visibility
                    // to visible and change the expand more icon to expand less.
                    else {
                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        expandableLayout.setVisibility(View.VISIBLE);
                        expandButton.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    }
                }
            });

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId;
                    if(type == 1){
                        userId = recipeModelSearch.get(getAdapterPosition()).getUserId();
                    }
                    else{
                        userId = recipeModel.get(getAdapterPosition()).getUserId();
                    }
                    Intent intent = new Intent(itemView.getContext(), UserActivity.class);
                    intent.putExtra("fragmentname", "viewprofilefragment");
                    intent.putExtra("userId", userId);
                    itemView.getContext().startActivity(intent);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ViewRecipeActivity.class);

                    UserModelDB userModelDB = new UserModelDB();
                    if(type == 1){
                        intent.putExtra("recipeModel", new RecipeModelDB(recipeModelSearch.get(getAdapterPosition())));
                        for (int i=0;i<userModel.size();i++){
                            if(recipeModelSearch.get(getAdapterPosition()).getUserId().equals(userModel.get(i).getUserId())){
                                userModelDB = userModel.get(i);
                                break;
                            }
                        }
                    }
                    else{
                        intent.putExtra("recipeModel", recipeModel.get(getAdapterPosition()));
                        for (int i=0;i<userModel.size();i++){
                            if(recipeModel.get(getAdapterPosition()).getUserId().equals(userModel.get(i).getUserId())){
                                userModelDB = userModel.get(i);
                                break;
                            }
                        }
                    }

                    intent.putExtra("userModel", userModelDB);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}

