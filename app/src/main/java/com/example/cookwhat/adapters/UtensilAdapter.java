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
import com.example.cookwhat.utils.Constants;

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
        UtensilModel utensilModel = utensilList.get(position);

        ((ViewHolder) holder).textViewName.setText(utensilModel.getName());
        if(utensilModel.getMemo() != null){
            ((ViewHolder) holder).textViewDescription.setText(utensilModel.getMemo());
        }

        int iconResourceId = R.drawable.i0067_others;
        if (utensilModel.getIcon() >= 0  && utensilModel.getIcon() < Constants.UTENSILS_ICON.length) {
            iconResourceId = Constants.UTENSILS_ICON[utensilModel.getIcon()];
        }
        ((ViewHolder)holder).icon.setImageResource(iconResourceId);
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
