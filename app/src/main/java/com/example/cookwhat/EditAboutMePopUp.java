package com.example.cookwhat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Locale;

public class EditAboutMePopUp extends DialogFragment {

    private static final String TAG = "" ;
    passData mPassData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View popUpView = inflater.inflate(R.layout.pop_up_about_me, null);
        RadioGroup gender = popUpView.findViewById(R.id.RG_Gender);
        Button done = popUpView.findViewById(R.id.Btn_DoneEdit);
        EditText age = popUpView.findViewById(R.id.ET_Age);

        String[] ISOcountriesCodes = Locale.getISOCountries();
        ArrayList<String> countries = new ArrayList<>();
        for(String countrycode : ISOcountriesCodes){
            Locale locale = new Locale("",countrycode);
            countries.add(locale.getDisplayCountry());
        }
        Spinner countrylist = popUpView.findViewById(R.id.SPN_Country);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countrylist.setAdapter(countryAdapter);



        View.OnClickListener doneOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton genderSelected = popUpView.findViewById(gender.getCheckedRadioButtonId());
                String genderselec = (String)genderSelected.getText();
                Integer ageInt = Integer.parseInt( age.getText().toString());
                String country = countrylist.getSelectedItem().toString();
                mPassData.getData(genderselec, ageInt, country);
                dismiss();
            }

        };
        done.setOnClickListener(doneOCL);
        return popUpView;




    }


    public interface passData {
        void getData(String gender, Integer age, String country);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mPassData = (EditAboutMePopUp.passData) getTargetFragment();
        }
        catch(ClassCastException e){
            Log.e(TAG,"onAttach: ClassCastException:"+ e.getMessage());
        }

    }
}
