package com.example.cookwhat;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.model.IngredientModel;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IngredientModel> ingredientList;
    private Context context;

    IngredientAdapter(Context context, List<IngredientModel> ingredientList) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_layout, parent, false);
        return new ViewHolder(view, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String quantity = Double.toString(ingredientList.get(position).getQuantity()) + ingredientList.get(position).getUnit();
        ((ViewHolder) holder).textViewName.setText(ingredientList.get(position).getName());
        ((ViewHolder) holder).textViewDescription.setText(ingredientList.get(position).getDescription());
        ((ViewHolder)holder).textViewQuantity.setText(quantity);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDescription, textViewQuantity;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TVIngredientName);
            textViewQuantity = itemView.findViewById(R.id.TVQuantity);
            textViewDescription = itemView.findViewById(R.id.TVDescription);

        }
    }
}
