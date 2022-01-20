package com.example.cookwhat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookwhat.R;

import java.util.ArrayList;

public class FavouriteCategoryAdapter extends BaseAdapter  {
    ArrayList<String>categoryName;
    ArrayList<Integer>categoryImg;

    public FavouriteCategoryAdapter(ArrayList<String> categoryName, ArrayList<Integer> categoryImg){
        this.categoryName = categoryName;
        this.categoryImg = categoryImg;
    }


    @Override
    public int getCount() {
        return categoryName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourite_category_item,null);

        ImageView img = view1.findViewById(R.id.IV_favCategory);
        TextView text = view1.findViewById(R.id.TV_favCategory);

        img.setImageResource(categoryImg.get(i));
        text.setText(categoryName.get(i));

        return view1;

    }
    public String getCategoryName(int i){
        return categoryName.get(i);
    }

    public void remove(int i){
        categoryName.remove(i);
        categoryImg.remove(i);
        notifyDataSetChanged();
    }
}
