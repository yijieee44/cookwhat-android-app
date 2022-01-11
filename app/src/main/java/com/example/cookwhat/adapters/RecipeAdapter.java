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

import com.example.cookwhat.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.activities.ViewRecipeActivity;
import com.example.cookwhat.models.RecipeModel;
import com.example.cookwhat.models.UserModel;
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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    ArrayList<RecipeModel> recipeModel;
    ArrayList<UserModel> userModel;
    Context ctx;
    LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference recipedb =  db.collection("recipe");
    public CollectionReference userdb =  db.collection("user");
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public UserModel userModel1 = new UserModel();

    public RecipeAdapter(ArrayList<RecipeModel> recipeModel, ArrayList<UserModel> userModel, Context ctx){
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
        holder.recipeName.setText(recipeModel.get(position).getTitle());
        holder.tag.setText(recipeModel.get(position).getTags().get(0));
        holder.numFav.setText(Integer.toString(recipeModel.get(position).getNum_fav()));
        holder.userName.setText(userModel.get(position).getUserName());
        holder.userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.avatar_winter_custome_23_svgrepo_com, 0, 0, 0);
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


            numFav.setOnClickListener(new View.OnClickListener() {
                ArrayList<String> recipeids;
                @Override
                public void onClick(View v) {
                    readUser(new FirestoreCallback2() {
                        @Override
                        public void onCallBack(UserModel userModel1) {
                            if (userModel1.getFavouriteFood() == null){
                                recipeids = new ArrayList<>();
                            }
                            else{
                                recipeids = userModel1.getFavouriteFood();
                            }
                            if (recipeids.contains(recipeModel.get(getAdapterPosition()).getId())){
                                recipeModel.get(getAdapterPosition()).setNum_fav(recipeModel.get(getAdapterPosition()).getNum_fav() - 1);
                                recipeids.remove(recipeModel.get(getAdapterPosition()).getId());
                                userModel1.setFavouriteFood(recipeids);
                                writeData(recipeModel.get(getAdapterPosition()), recipeModel.get(getAdapterPosition()).getId());
                                writeUser(userModel1);
                                numFav.setText(String.valueOf(recipeModel.get(getAdapterPosition()).getNum_fav()));
                            }
                            else{
                                recipeModel.get(getAdapterPosition()).setNum_fav(recipeModel.get(getAdapterPosition()).getNum_fav() + 1);
                                recipeids.add(recipeModel.get(getAdapterPosition()).getId());
                                userModel1.setFavouriteFood(recipeids);
                                writeData(recipeModel.get(getAdapterPosition()), recipeModel.get(getAdapterPosition()).getId());
                                writeUser(userModel1);
                                numFav.setText(String.valueOf(recipeModel.get(getAdapterPosition()).getNum_fav()));
                            }
                        }
                    });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ViewRecipeActivity.class);
                    intent.putExtra("recipeId", recipeModel.get(getAdapterPosition()).getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public void readUser(FirestoreCallback2 firestoreCallback){
        userdb.whereEqualTo("userId", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                final ObjectMapper mapper = new ObjectMapper();
                                userModel1 = mapper.convertValue(document.getData(), UserModel.class);
                            }
                            firestoreCallback.onCallBack(userModel1);
                        } else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void writeData(RecipeModel recipeModel, String recipeId){
        recipedb.whereEqualTo("id", recipeId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                recipedb.document(document.getId()).set(recipeModel);
                            }
                        }
                        else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void writeUser(UserModel userModel){
        userdb.whereEqualTo("userId", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                userdb.document(document.getId()).set(userModel);
                            }
                        }
                        else {
                            Log.w("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private interface FirestoreCallback2 {
        void onCallBack(UserModel userModel1);
    }
}

