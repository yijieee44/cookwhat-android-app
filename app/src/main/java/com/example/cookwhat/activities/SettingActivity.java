package com.example.cookwhat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.cookwhat.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Switch darkModeSwitch = (Switch) findViewById(R.id.SWDarkTheme);

        int nightModeFlags = getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkModeSwitch.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                darkModeSwitch.setChecked(false);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                darkModeSwitch.setChecked(false);
                break;
        }

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }
}