package com.example.cookwhat.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.FavouriteActivity;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.

 * Use the  factory method to* create an instance of this fragment.
 */
public class MoveFavouriteDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    UserModelDB userModelDB;
    RecipeModelDB recipeModelDB;
    String selectedCategory;
    String moveToCategory;
    Boolean onClick = false;
    Button BtnArabic, BtnChinese, BtnEuropean, BtnIndia, BtnJapan, BtnKorea, BtnMediterranean,
            BtnSEA, BtnOther;
    ArrayList<Button> buttonArrayList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoveFavouriteDialogFragment(UserModelDB userModelDB, RecipeModelDB recipeModelDB, String selectedCategory) {
        this.userModelDB = userModelDB;
        this.recipeModelDB = recipeModelDB;
        this.selectedCategory = selectedCategory;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_category_dialog, null);
        BtnArabic = (Button) view.findViewById(R.id.BtnArabic);
        BtnChinese = (Button) view.findViewById(R.id.BtnChinese);
        BtnEuropean = (Button) view.findViewById(R.id.BtnEuropean);
        BtnIndia = (Button) view.findViewById(R.id.BtnIndia);
        BtnJapan = (Button) view.findViewById(R.id.BtnJapan);
        BtnKorea = (Button) view.findViewById(R.id.BtnKorea);
        BtnMediterranean = (Button) view.findViewById(R.id.BtnMediterranean);
        BtnSEA = (Button) view.findViewById(R.id.BtnSEA);
        BtnOther = (Button) view.findViewById(R.id.BtnOther);
        buttonArrayList.add(BtnArabic);
        buttonArrayList.add(BtnChinese);
        buttonArrayList.add(BtnEuropean);
        buttonArrayList.add(BtnIndia);
        buttonArrayList.add(BtnJapan);
        buttonArrayList.add(BtnKorea);
        buttonArrayList.add(BtnMediterranean);
        buttonArrayList.add(BtnSEA);
        buttonArrayList.add(BtnOther);

        for (Button b : buttonArrayList) {
            b.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    onClick = true;
                    moveToCategory = (String) b.getText();
                    Map<String, ArrayList<String>> fav = userModelDB.getFavouriteCategory();
                    fav.get(selectedCategory).remove(recipeModelDB.getId());
                    fav.get(moveToCategory).add(recipeModelDB.getId());
                    userModelDB.setFavouriteCategory(fav);
                    updateFavCat(userModelDB);
                    dismiss();

                    FavouriteActivity activity = (FavouriteActivity) getActivity();
                    activity.startFavourite(userModelDB,selectedCategory);

                   /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                    FavouriteFragment fragment = new FavouriteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("usermodel",userModelDB);
                    bundle.putString("selectedCategoryName", selectedCategory);
                    fragment.setArguments(bundle);
                    ft.remove(getParentFragment());

                    ft.commit();*/

                }
            });
            if (b.getText().equals(selectedCategory)) {
                b.setClickable(false);
                b.setEnabled(false);
            }
        }


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateFavCat(UserModelDB userModelDB) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        System.out.println("Update"+userModelDB.getUserId());


        db.collection("user").document(userModelDB.getUserId()).update("favouriteCategory", userModelDB.getFavouriteCategory())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.e("MOVE", "The favourite category is successfully updated!");
                            //Toast.makeText(getContext(), "Saved recipe to " + moveToCategory, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}