package com.example.cookwhat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookwhat.R;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EditAboutMeActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userId;
    UserModelDB userModel;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about_me);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit About Me");

        Intent intent = getIntent();
        userModel = (UserModelDB) intent.getSerializableExtra("usermodel");


        String[] ISOcountriesCodes = Locale.getISOCountries();
        ArrayList<String> countries = new ArrayList<>();
        for(String countrycode : ISOcountriesCodes){
            Locale locale = new Locale("",countrycode);
            countries.add(locale.getDisplayCountry());
        }
        Spinner countryList = findViewById(R.id.SPN_Country);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countryList.setAdapter(countryAdapter);

        String[] levelOfExpertArr = getResources().getStringArray(R.array.level);
        List<String> levelOfExpert =  Arrays.asList(levelOfExpertArr);
        Spinner levelList = findViewById(R.id.SPN_Level);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levelOfExpert);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        levelList.setAdapter(levelAdapter);

        EditText description = findViewById(R.id.ET_Description);
        TextView email = findViewById(R.id.TV_AbtMeEmail);
        CheckBox isShowEmail = findViewById(R.id.CB_ShowEmail);
        ChipGroup preferences = findViewById(R.id.CG_Preferences);
        CheckBox isShowPreferences = findViewById(R.id.CB_ShowPreferences);
        Button done =findViewById(R.id.Btn_DoneEdit);

        description.setText(userModel.getDescription());
        email.setText(userModel.getEmailAddr());
        isShowEmail.setChecked(userModel.isShowEmail());

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
        isShowPreferences.setChecked(userModel.isShowPreference());


        int numPreferences = preferences.getChildCount();
        List<String> preferencesList = userModel.getPreference();
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
                userModel.setShowPreference(isShowPreferences.isChecked());
                List<Integer> preferencesId = preferences.getCheckedChipIds();
                ArrayList<String>prefer = new ArrayList<>();
                for(Integer id : preferencesId){
                    Chip chip = findViewById(id);
                    prefer.add(chip.getText().toString());
                }

                userModel.setPreference(prefer);
                updateAboutMe(userModel);
                Intent intent = new Intent();
                setResult(122, intent);
                finish();
//                Intent intent = new Intent(EditAboutMeActivity.this, MainActivity.class);
//                intent.putExtra("usermodel",userModel);
//                intent.putExtra("action", "updated about me");
//                startActivity(intent);


            }

        };
        done.setOnClickListener(doneOCL);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAboutMe( UserModelDB userModel){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = userModel.getUserId();
        db.collection("user").document(userId)
                .set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                Toast.makeText(getApplicationContext(), "You have successfully updated your information!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
