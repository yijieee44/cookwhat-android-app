package com.example.cookwhat.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.models.IngredientModel;

import java.util.List;

public class MarketIngredientAdapter extends BaseAdapter {

    Context context;
    int[] marketIngredientsName;
    int[] image;

    LayoutInflater inflater;

    public MarketIngredientAdapter(Context context, int[] marketIngredientsName, int[] image) {
        this.context = context;
        this.marketIngredientsName = marketIngredientsName;
        this.image = image;
    }

    @Override
    public int getCount() {
        return marketIngredientsName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.widget_market_ingredients, null);
        }

        ImageView imageView = convertView.findViewById(R.id.IVMarketIngredient);
        TextView textView = convertView.findViewById(R.id.TVMarketIngredient);

        imageView.setImageResource(image[position]);
        textView.setText(marketIngredientsName[position]);

        return convertView;
    }
}
