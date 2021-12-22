package com.example.cookwhat.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.models.UtensilModel;

import java.util.List;

public class UtensilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UtensilModel> utensilList;
    private Context context;

    public UtensilAdapter(Context context, List<UtensilModel> utensilList) {
        this.utensilList = utensilList;
        this.context = context;
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
        ((ViewHolder) holder).textViewDescription.setText(utensilList.get(position).getMemo());
    }

    @Override
    public int getItemCount() {
        return utensilList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDescription;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TVUtensilName);
            textViewDescription = itemView.findViewById(R.id.TVUtensilDescription);

        }
    }
}
