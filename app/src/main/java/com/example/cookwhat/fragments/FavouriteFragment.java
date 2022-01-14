package com.example.cookwhat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookwhat.OptionPopUp;
import com.example.cookwhat.R;
import com.example.cookwhat.activities.SearchActivity;
import com.example.cookwhat.adapters.FavouriteAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment implements OptionPopUp.passData  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<String> favouriteList;
    FavouriteAdapter favouriteAdapter;
    ListView listview;


    public FavouriteFragment() {
        /*Bundle bundle = getArguments();
        if(bundle != null){
            String categoryName = bundle.getString("categoryName");
            System.out.println(categoryName);

        }*/
        favouriteList = new ArrayList<>();
        favouriteList.add("One");
        favouriteList.add("Two");
        favouriteList.add("Three");
        favouriteList.add("Four");
        favouriteList.add("Five");
        favouriteList.add("Six");
        favouriteList.add("Seven");
        favouriteList.add("Eight");
        favouriteList.add("Nine");
        favouriteList.add("Ten");
        favouriteList.add("Eleven");
        favouriteList.add("Twelve");
        favouriteList.add("Thirteen");
        favouriteList.add("Fourteen");
        favouriteList.add("Fifteen");


        System.out.println("Next come to here?");
        //fetch favourite from category selected
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview =(ListView) view.findViewById(R.id.LV_FavouriteList);
        favouriteAdapter = new FavouriteAdapter(getContext(),favouriteList);
        listview.setAdapter(favouriteAdapter);
        System.out.println("In onviewcreated:"+favouriteAdapter.getCount());


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("action", "viewRecipe");
                startActivity(intent);

            }
        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()  {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                OptionPopUp optionPopUp = new OptionPopUp(i);
                optionPopUp.setTargetFragment(FavouriteFragment.this,1);
                optionPopUp.show(getFragmentManager(),"deleteOrMoveTo");

                return true;
            }
        });


    }

    @Override
    public void getOption(String option, Integer i) {
        if(option.equals("delete")){
            System.out.println("yes, i m entering delete processss");
            //favouriteList.remove(i);
            //favouriteAdapter.notifyDataSetChanged();
            favouriteAdapter.removeItem(i);
            System.out.println(i);
            System.out.println(favouriteAdapter.getCount());

            //System.out.println(favouriteAdapter.getCount());
            //listview.setAdapter(favouriteAdapter);

        }
        else if(option.equals("moveTo")){
            favouriteList.remove(i);
            //favouriteAdapter.removeItem(i);
            //listview.setAdapter(favouriteAdapter);
        }
    }
}



