package com.example.cookwhat.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

    GridView favCatView;

    public FavouriteCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteCategory.
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        Logintest login = new Logintest();
        UserData userData = login.userData;

        CustomAdapter categoryAdapter = new CustomAdapter(userData);
        favCatView.setAdapter(categoryAdapter);
    }

    private class CustomAdapter extends BaseAdapter {

        private String[] categoryName;
        private int[] categoryImg;

        private CustomAdapter(UserData User){
            this.categoryName = new String[User.getFavouriteCategory().size()];
            this.categoryName = User.getFavouriteCategory().toArray(categoryName);
            this.categoryImg = new int[User.getFavouriteCategory().size()];

            for(int i=0; i<User.getFavouriteCategory().size(); i++){

                if(this.categoryName[i].equalsIgnoreCase("Japan"))
                    this.categoryImg[i]= R.drawable.japan;

                if(this.categoryName[i].equalsIgnoreCase("Korea"))
                    this.categoryImg[i]=R.drawable.korea;

                /*if(this.categoryName[i].equalsIgnoreCase("Thailand"))
                    this.categoryImg[i]=R.drawable.Thailand;

                if(this.categoryName[i].equalsIgnoreCase("Malaysia"))
                    this.categoryImg[i]=R.drawable.Malaysia;

                if(this.categoryName[i].equalsIgnoreCase("Malaysia"))
                    this.categoryImg[i]=R.drawable.Malaysia; */
            }
        }

        @Override
        public int getCount() {
            return categoryName.length;
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
            View view1 = getLayoutInflater().inflate(R.layout.row_data_favcategory,null);

            ImageView img = view1.findViewById(R.id.TV_favCategory);
            TextView text = view1.findViewById(R.id.IV_favCategory);

            img.setImageResource(this.categoryImg[i]);
            text.setText(this.categoryName[i]);

            return view1;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_category, container, false);
    }
}