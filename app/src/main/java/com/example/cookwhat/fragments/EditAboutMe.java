package com.example.cookwhat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.MainActivity;
import com.example.cookwhat.models.UserModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAboutMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAboutMe extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userId;
    UserModel userModel;

    public EditAboutMe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAboutMe.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAboutMe newInstance(String param1, String param2) {
        EditAboutMe fragment = new EditAboutMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] ISOcountriesCodes = Locale.getISOCountries();
        ArrayList<String> countries = new ArrayList<>();
        for(String countrycode : ISOcountriesCodes){
            Locale locale = new Locale("",countrycode);
            countries.add(locale.getDisplayCountry());
        }
        Spinner countryList = view.findViewById(R.id.SPN_Country);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countryList.setAdapter(countryAdapter);

        String[] levelOfExpertArr = getResources().getStringArray(R.array.level);
        List<String> levelOfExpert =  Arrays.asList(levelOfExpertArr);
        Spinner levelList = view.findViewById(R.id.SPN_Level);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, levelOfExpert);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        levelList.setAdapter(levelAdapter);

        EditText description = view.findViewById(R.id.ET_Description);
        TextView email = view.findViewById(R.id.TV_AbtMeEmail);
        CheckBox isShowEmail = view.findViewById(R.id.CB_ShowEmail);
        ChipGroup preferences = view.findViewById(R.id.CG_Preferences);
        CheckBox isShowPreferences = view.findViewById(R.id.CB_ShowPreferences);
        Button done = view.findViewById(R.id.Btn_DoneEdit);

        description.setText(userModel.getDescription());
        email.setText(userModel.getEmailAddr());
        isShowEmail.setChecked(userModel.getShowEmail());

        String countryFrmModel = userModel.getCountry();
        String levelFrmModel = userModel.getLevel();

        if(countryFrmModel != null){
            if (countries.contains(countryFrmModel)){
                countryList.setSelection(countries.indexOf(countryFrmModel));
            }
        }
        if(levelFrmModel != null){
            if(levelOfExpert.contains(levelFrmModel)){
                levelList.setSelection(levelOfExpert.indexOf(levelFrmModel));
            }
        }
        isShowPreferences.setChecked(userModel.getShowPreferences());


        int numPreferences = preferences.getChildCount();
        List<String> preferencesList = userModel.getPreferences();
        System.out.println("get preferences:"+ preferencesList);
        for(int i =0; i<numPreferences; i++){
            int chipId = preferences.getChildAt(i).getId();
            String tag = (String) ((Chip) preferences.getChildAt(i)).getText();

            if (preferencesList.contains(tag)){
                System.out.println("Tag"+ tag);
                preferences.check(chipId);
            }
        }


        View.OnClickListener doneOCL = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setDescription(description.getText().toString());
                userModel.setShowEmail(isShowEmail.isChecked());
                userModel.setCountry(countryList.getSelectedItem().toString());
                userModel.setLevel(levelList.getSelectedItem().toString());
                userModel.setShowPreferences(isShowPreferences.isChecked());
                List<Integer> preferencesId = preferences.getCheckedChipIds();
                ArrayList<String>prefer = new ArrayList<>();
                for(Integer id : preferencesId){
                    Chip chip = view.findViewById(id);
                    prefer.add(chip.getText().toString());
                }

                userModel.setPreferences(prefer);
                updateAboutMe(userModel);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("action","updated about me");
                startActivity(intent);


            }

        };
        done.setOnClickListener(doneOCL);

    }

   public void updateAboutMe( UserModel userModel){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = userModel.getUserId();
        db.collection("user").document(userId)
                .set(userModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            userModel = bundle.getParcelable("usermodel");
            System.out.println("onCreate"+ userModel.getUserName());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        return inflater.inflate(R.layout.fragment_edit_about_me, container, false);
    }


}