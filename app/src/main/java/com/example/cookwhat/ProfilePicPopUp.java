package com.example.cookwhat;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cookwhat.utils.Constants;

public class ProfilePicPopUp extends DialogFragment {

    int position;
    profilePicPopUpListener profilePicPopUpListener;

    public ProfilePicPopUp(int position, profilePicPopUpListener profilePicPopUpListener) {
        this.position = position;
        this.profilePicPopUpListener = profilePicPopUpListener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View profilePicPopUp = inflater.inflate(R.layout.pop_up_profile_pic,null);
        ImageView img = (ImageView) profilePicPopUp.findViewById(R.id.IV_ProfilePicEnlarge);
        img.setImageResource(Constants.PROFILE_PIC[position]);
        img.setVisibility(View.VISIBLE);


        Button confirm = profilePicPopUp.findViewById(R.id.Btn_Confirm);
        View.OnClickListener confirmOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePicPopUpListener.onConfirmClicked(true);
                dismiss();
            }
        };
        confirm.setOnClickListener(confirmOCL);


        return profilePicPopUp;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int)(metrics.widthPixels*0.80);
        int height = (int)(metrics.heightPixels*0.50);

        getDialog().getWindow().setLayout(width, height);
    }

    public interface profilePicPopUpListener {
        public void onConfirmClicked(boolean clicked);
    }

}
