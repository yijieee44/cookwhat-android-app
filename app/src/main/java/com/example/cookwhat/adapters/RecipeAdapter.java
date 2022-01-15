package com.example.cookwhat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.UserActivity;
import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.models.RecipeCommentModel;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModel;
import com.example.cookwhat.models.UserModelDB;
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
    Context ctx;
    LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference recipedb =  db.collection("recipe");
    public CollectionReference userdb =  db.collection("user");
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public RecipeAdapter(ArrayList<RecipeModelDB> recipeModel, ArrayList<UserModelDB> userModel, Context ctx){
        this.recipeModel = recipeModel;
        this.userModel = userModel;
        this.ctx = ctx;
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

        String username = "";
        int profilePic = 0;

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
    }

    @Override
    public int getItemCount() {
        return recipeModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView recipeName;
        ImageView recipeImage;
        TextView numFav;
        Button userName;
        TextView tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.TVrecipeName);
            recipeImage = (ImageView) itemView.findViewById(R.id.IVrecipeImage);
            numFav = (TextView) itemView.findViewById(R.id.TVNumFav);
            userName = (Button) itemView.findViewById(R.id.BtnUser1);
            tag = (TextView) itemView.findViewById(R.id.TVTag);

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = recipeModel.get(getAdapterPosition()).getUserId();
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
                    intent.putExtra("recipeModel", recipeModel.get(getAdapterPosition()));
                    UserModelDB userModelDB = new UserModelDB();
                    for (int i=0;i<userModel.size();i++){
                        if(recipeModel.get(getAdapterPosition()).getUserId().equals(userModel.get(i).getUserId())){
                            userModelDB = userModel.get(i);
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

