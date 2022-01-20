package com.example.cookwhat.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cookwhat.R;
import com.example.cookwhat.database.DatabaseHelper;
import com.example.cookwhat.models.RecipeModelDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DeleteRecipeDialogFragment extends DialogFragment {
    private DeleteRecipeDialogFragment.DialogListener listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference recipedb;

    RecipeModelDB recipeModelDB;

    public DeleteRecipeDialogFragment(RecipeModelDB recipeModelDB) {
        this.recipeModelDB = recipeModelDB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_background));
        setCancelable(true);

        recipedb = db.collection("recipe");

        return inflater.inflate(R.layout.fragment_delete_recipe_dialog, null);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button BtnDeleteRecipe = (Button) view.findViewById(R.id.BtnDeleteRecipe);
        Button BtnCancel = (Button) view.findViewById(R.id.BtnCancel);

        BtnDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipedb.document(recipeModelDB.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                if(listener != null){
                                    listener.onFinishEditDialog();
                                }
                                dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Fail to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setDialogListener(DeleteRecipeDialogFragment.DialogListener listener){
        this.listener = listener;
    }


    public interface DialogListener {
        void onFinishEditDialog();
    }

}
