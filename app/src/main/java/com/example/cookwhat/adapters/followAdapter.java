package com.example.cookwhat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookwhat.R;
import com.example.cookwhat.models.followData;

import java.util.ArrayList;

public class followAdapter extends BaseAdapter implements Filterable {

    ArrayList<followData>origFollowList = new ArrayList<>();
    ArrayList<followData>tempFollowList = new ArrayList<>();
    Context context;
    CustomFilter customFilter;

    public followAdapter(Context context, ArrayList<followData> followList) {
        this.context = context;
        this.origFollowList = followList;
        this.tempFollowList = followList;
    }

    @Override
    public int getCount() {
        return origFollowList.size();
    }

    @Override
    public Object getItem(int i) {
        return origFollowList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.follow_list_item, null);

        TextView name = row.findViewById(R.id.TV_FollowName);
//        ImageView img = row.findViewById(R.id.IV_ProfileFollow);

//        img.setImageResource(R.drawable.ic_profile_pic_2);

        name.setText(origFollowList.get(i).getFollowName());

        return row;
    }

    @Override
    public Filter getFilter() {
        if(customFilter == null){
            customFilter = new CustomFilter();
        }
        return customFilter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if(charSequence != null && charSequence.length()>0 ){
                charSequence = charSequence.toString();
                ArrayList<followData> filterData = new ArrayList<>();
                for(followData data : tempFollowList){
                    if(data.getFollowName().contains(charSequence)){
                        followData dataToAdd =  new followData(data.getFollowName(),data.getFollowId());
                        filterData.add(dataToAdd);
                    }
                }
                results.count = filterData.size();
                results.values = filterData;

            }
            else{
                results.count = tempFollowList.size();
                results.values = tempFollowList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            origFollowList = (ArrayList<followData>) filterResults.values;
            notifyDataSetChanged();


        }
    }
}


