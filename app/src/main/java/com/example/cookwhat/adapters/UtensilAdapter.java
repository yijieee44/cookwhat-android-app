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
import com.example.cookwhat.models.UtensilModel;

import java.util.List;

public class UtensilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UtensilModel> utensilList;
    private Context context;

    public UtensilAdapter(Context context, List<UtensilModel> utensilList) {
        this.utensilList = utensilList;
        this.context = context;
    }

    public List<UtensilModel> getUtensilList() {
        return utensilList;
    }

    public void addUtensil(UtensilModel utensil){
        utensilList.add(utensil);
        this.notifyDataSetChanged();
    }

    public void removeUtensil(UtensilModel utensil){
        int index = -1;
        for(UtensilModel utensilModel: utensilList){
            if(utensilModel.getName().equals(utensil.getName())){
                index = utensilList.indexOf(utensilModel);
                break;
            }
        }
        if(index != -1){
            utensilList.remove(index);
        }
        this.notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utensil_layout, parent, false);
        return new ViewHolder(view, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textViewName.setText(utensilList.get(position).getName());
        if(utensilList.get(position).getMemo() != null){
            ((ViewHolder) holder).textViewDescription.setText(utensilList.get(position).getMemo());
        }
        ((ViewHolder)holder).icon.setImageResource(utensilList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return utensilList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDescription;
        ImageView icon;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TVUtensilName);
            textViewDescription = itemView.findViewById(R.id.TVUtensilDescription);
            icon = itemView.findViewById(R.id.IVIconUtensil);

        }
    }
}
