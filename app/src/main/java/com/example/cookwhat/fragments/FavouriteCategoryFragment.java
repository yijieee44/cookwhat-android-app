package com.example.cookwhat.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.FavouriteCategoryAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<String> categoryName = new ArrayList<>();
    ArrayList<Integer> categoryImg = new ArrayList<>();

    public FavouriteCategoryFragment() {
        //getUserFavouriteCategory
        for (int i = 0; i < 10; i++) {
            categoryName.add(Integer.toString(i));
            categoryImg.add(R.drawable.addbutton);
        }


        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteCategoryFragment newInstance(String param1, String param2) {
        FavouriteCategoryFragment fragment = new FavouriteCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Favourite Category");


        GridView favCategory = (GridView)view.findViewById(R.id.GV_FavouriteCategory);
        FavouriteCategoryAdapter adapter = new FavouriteCategoryAdapter(categoryName, categoryImg);
        favCategory.setAdapter(adapter);

        favCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = adapter.getCategoryName(i);
                //fetch recipe based on categoryName
                //UserActivity activity = (UserActivity) getActivity();
                //activity.setCategoryName(categoryName);
                FavouriteFragment fragment = new FavouriteFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.NHFUser, fragment,"FavouriteFragment");
                transaction.commit();


                //Navigation.findNavController(view).navigate(R.id.DestFavourite);

            }

        });

        favCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("Are you sure you want to delete this?");
                alert.setCancelable(false);
                System.out.println("here");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(i);
                        adapter.notifyDataSetChanged();

                    }
                } );

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                alert.create();
                alert.show();

                return true;
            }


        });


    }
}