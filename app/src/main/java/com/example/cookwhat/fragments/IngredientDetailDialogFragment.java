package com.example.cookwhat.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.IngredientAdapter;
import com.example.cookwhat.utils.Constants;


public class IngredientDetailDialogFragment extends DialogFragment {
    private DialogListener listener;

    String ingredientName;
    int icon;
    String quantity;
    String weight;
    String unitQuantity;
    String unitWeight;
    String memo;

    public IngredientDetailDialogFragment(String ingredientName, int icon, Double quantity, Double weight,
                                          String unitQuantity, String unitWeight, String memo) {
        this.ingredientName = ingredientName;
        this.icon = icon;
        if(quantity != null){
            this.quantity = quantity.toString();
        }
        if(weight != null){
            this.weight = weight.toString();
        }

        this.unitQuantity = unitQuantity;
        this.unitWeight = unitWeight;
        this.memo = memo;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_white_background));


        return inflater.inflate(R.layout.fragment_ingredient_detail_dialog, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView ingredientNameTV = getView().findViewById(R.id.TVIngredientName);
        ingredientNameTV.setText(ingredientName);

        ImageView iconImage = getView().findViewById(R.id.IVIngredientIcon);
        int iconResourceId = R.drawable.i0067_others;
        if (icon >= 0  && icon < Constants.INGREDIENTS_ICON.length) {
            iconResourceId = Constants.INGREDIENTS_ICON[icon];
        }
        iconImage.setImageResource(iconResourceId);

        Spinner quantitySpinner = (Spinner) getView().findViewById(R.id.IngredientQuantitySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);

        Spinner weightSpinner = (Spinner) getView().findViewById(R.id.IngredientWeightSpinner);
        ArrayAdapter<CharSequence> adapterWeight = ArrayAdapter.createFromResource(view.getContext(),
                R.array.weight, android.R.layout.simple_spinner_item);
        adapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(adapterWeight);

        ImageButton submitButton = getView().findViewById(R.id.BtnIngredientDetailSubmit);
        submitButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        submit();
                    }
                }
        );

        EditText quantity = getView().findViewById(R.id.ETQuantity);
        EditText weight = getView().findViewById(R.id.ETWeight);
        EditText memo = getView().findViewById(R.id.ETIngredientMemo);

        if(this.quantity != null && !this.quantity.isEmpty()){
            quantity.setText(this.quantity);
        }

        if(this.weight != null && !this.weight.isEmpty()){
            weight.setText(this.weight);
        }

        if(this.memo != null && !this.memo.isEmpty()){
            memo.setText(this.memo);
        }

    }

    public void submit(){
        EditText quantity = getView().findViewById(R.id.ETQuantity);
        EditText weight = getView().findViewById(R.id.ETWeight);
        EditText memo = getView().findViewById(R.id.ETIngredientMemo);
        Spinner weightSpinner = (Spinner) getView().findViewById(R.id.IngredientWeightSpinner);
        Spinner quantitySpinner = (Spinner) getView().findViewById(R.id.IngredientQuantitySpinner);
        Log.d("WASDSA", "quantity:" + quantity.getText().toString() + weightSpinner.getSelectedItem().toString());
        if(listener != null){
            listener.onFinishEditDialog(quantity.getText().toString(),weight.getText().toString(),quantitySpinner.getSelectedItem().toString(),
                    weightSpinner.getSelectedItem().toString(), memo.getText().toString());
            this.dismiss();
        }


    }

    public void setDialogListener(DialogListener listener){
        this.listener = listener;
    }


    public interface DialogListener {
        void onFinishEditDialog(String quantity, String weight, String unitQuantity, String unitWeight, String memo);
    }

}