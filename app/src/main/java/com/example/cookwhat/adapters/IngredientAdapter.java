package com.example.cookwhat.adapters;

import android.content.Context;
import android.os.Build;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.fragments.IngredientDetailDialogFragment;
import com.example.cookwhat.models.IngredientModel;

import java.text.DecimalFormat;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IngredientModel> ingredientList;
    private Context context;

    public IngredientAdapter(Context context, List<IngredientModel> ingredientList) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    public void addIngredient(IngredientModel ingredient){
        ingredientList.add(ingredient);
        this.notifyDataSetChanged();
    }

    public List<IngredientModel> getIngredientList() {
        return ingredientList;
    }

    public void editIngredient(int position, IngredientModel ingredientModel){
        ingredientList.set(position, ingredientModel);
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
        DecimalFormat format = new DecimalFormat("0.#");
        IngredientModel ingredientModel = ingredientList.get(position);
        ((ViewHolder) holder).textViewName.setText(ingredientModel.getName());
        ((ViewHolder)holder).icon.setImageResource(ingredientModel.getIcon());
        if(ingredientModel.getMemo() != null){
            String text = ingredientModel.getMemo();
            if(ingredientModel.getMemo().length() > 25){
                text = ingredientModel.getMemo().substring(0, 25)+"...";
            }

            ((ViewHolder) holder).textViewDescription.setText(text);
        }

        if(ingredientModel.getQuantity() == null && ingredientModel.getWeight() == null){
            ((ViewHolder)holder).multiplySymbol.setVisibility(View.INVISIBLE);
        }
        else if(ingredientModel.getQuantity() != null){
            ((ViewHolder)holder).multiplySymbol.setVisibility(View.VISIBLE);
            String quantity = format.format(ingredientModel.getQuantity()) + " ";
            if(ingredientModel.getUnitQuantity() != null) {
                quantity += ingredientModel.getUnitQuantity();
            }
            ((ViewHolder)holder).textViewQuantity.setText(quantity);
        }
        else if( ingredientModel.getWeight() != null){
            ((ViewHolder)holder).multiplySymbol.setVisibility(View.VISIBLE);
            String weight = format.format(ingredientModel.getWeight()) + " ";
            if(ingredientModel.getUnitWeight() != null) {
                weight += ingredientModel.getUnitWeight();
            }
            ((ViewHolder)holder).textViewQuantity.setText(weight);
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
