package com.example.cookwhat.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.adapters.UtensilAdapter;
import com.example.cookwhat.models.IngredientModel;
import com.example.cookwhat.models.UtensilModel;

import java.util.List;

public class IngredientAndUtensilDialogFragment extends DialogFragment {
    private IngredientAndUtensilDialogFragment.DialogListener listener;

    List<IngredientModel> ingredientModels;
    List<UtensilModel> utensilModels;

    public IngredientAndUtensilDialogFragment(List<IngredientModel> ingredientModels, List<UtensilModel> utensilModels) {
        this.ingredientModels = ingredientModels;
        this.utensilModels = utensilModels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_white_background));
        setCancelable(true);

        return inflater.inflate(R.layout.fragment_inu_dialog, null);
    }

    @Override
    public void onResume(){
        super.onResume();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int)(metrics.widthPixels*0.90);
        int height = (int)(metrics.heightPixels*0.75);

        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ingredientModels.size()>0) {
            RecyclerView ingredientsRecyclerView = (RecyclerView) getDialog().findViewById(R.id.RVIngredients);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
            ingredientsRecyclerView.setAdapter(new IngredientAdapter(getContext(), ingredientModels));

        } else {
            TextView noIngredientTextView = (TextView) getDialog().findViewById(R.id.TVNoIngredients);
            noIngredientTextView.setVisibility(View.VISIBLE);
        }

        if(utensilModels.size()>0) {
            RecyclerView utensilsRecyclerView = (RecyclerView) getDialog().findViewById(R.id.RVUtensils);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            utensilsRecyclerView.setLayoutManager(linearLayoutManager);
            utensilsRecyclerView.setAdapter(new UtensilAdapter(getContext(), utensilModels));

        } else {
            TextView noUtensilsTextView = (TextView) getDialog().findViewById(R.id.TVNoUtensils);
            noUtensilsTextView.setVisibility(View.VISIBLE);
        }

    }

    public void setDialogListener(IngredientAndUtensilDialogFragment.DialogListener listener){
        this.listener = listener;
    }


    public interface DialogListener {
        void onFinishEditDialog();
    }

}
