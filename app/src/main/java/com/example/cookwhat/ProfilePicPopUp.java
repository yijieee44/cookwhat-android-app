package com.example.cookwhat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProfilePicPopUp extends DialogFragment {

    int position;
    profilePicPopUpListener profilePicPopUpListener;

    public ProfilePicPopUp(int position, profilePicPopUpListener profilePicPopUpListener) {
        this.position = position+1;
        this.profilePicPopUpListener = profilePicPopUpListener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View profilePicPopUp = inflater.inflate(R.layout.pop_up_profile_pic,null);
        ImageView img = (ImageView) profilePicPopUp.findViewById(R.id.IV_ProfilePicEnlarge);
        String mDrawableName = "ic_profile_pic_"+ position;
        int resId = getResources().getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
        img.setImageResource(resId);
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
        getDialog().getWindow().setLayout(900,1000);
    }

    public interface profilePicPopUpListener {
        public void onConfirmClicked(boolean clicked);
    }

}
