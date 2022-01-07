package com.example.cookwhat.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.models.IngredientModel;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IngredientModel> ingredientList;
    private Context context;


    public IngredientAdapter(Context context, List<IngredientModel> ingredientList) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    public List<IngredientModel> getIngredientList() {
        return ingredientList;
    }

    public void addIngredient(IngredientModel ingredient){
        ingredientList.add(ingredient);
        this.notifyDataSetChanged();
    }

    public void removeIngredient(IngredientModel ingredient){
        int index = -1;
        for(IngredientModel ingredientModel: ingredientList){
            if(ingredientModel.getName().equals(ingredient.getName())){
                index = ingredientList.indexOf(ingredientModel);
                break;
            }
        }
        if(index != -1){
            ingredientList.remove(index);
        }
        this.notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_layout, parent, false);
        return new ViewHolder(view, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IngredientModel ingredientModel = ingredientList.get(position);
        ((ViewHolder) holder).textViewName.setText(ingredientModel.getName());
        ((ViewHolder)holder).icon.setImageResource(ingredientModel.getIcon());
        if(ingredientModel.getMemo() != null){
            ((ViewHolder) holder).textViewDescription.setText(ingredientModel.getMemo());
        }

        if(ingredientModel.getQuantity() == null){
            ((ViewHolder)holder).multiplySymbol.setVisibility(View.INVISIBLE);
        }
        else{
            ((ViewHolder)holder).multiplySymbol.setVisibility(View.VISIBLE);
            String quantity = ingredientModel.getQuantity().toString();
            if(ingredientModel.getUnit() != null) {
                quantity += ingredientModel.getUnit();
            }
            ((ViewHolder)holder).textViewQuantity.setText(quantity);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDescription, textViewQuantity, multiplySymbol;
        ImageView icon;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            icon = itemView.findViewById(R.id.IVIcon);
            textViewName = itemView.findViewById(R.id.TVIngredientName);
            textViewQuantity = itemView.findViewById(R.id.TVQuantity);
            textViewDescription = itemView.findViewById(R.id.TVDescription);
            multiplySymbol = itemView.findViewById(R.id.TVMultiply);


        }
    }
}
