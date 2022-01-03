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

import com.example.cookwhat.activities.MainActivity;
import com.example.cookwhat.models.UserModel;

public class ProfilePicPopUp extends DialogFragment {

    int position;
    boolean sendConfirm = false;

    public ProfilePicPopUp(int position) {
        this.position =position+1;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View profilePicPopUp = inflater.inflate(R.layout.pop_up_profile_pic,null);
        ImageView img = (ImageView) profilePicPopUp.findViewById(R.id.IV_ProfilePicEnlarge);
        String mDrawableName = "profile_pic_0"+ position;
        int resId = getResources().getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
        img.setImageResource(resId);
        img.setVisibility(View.VISIBLE);


        Button confirm = profilePicPopUp.findViewById(R.id.Btn_Confirm);
        View.OnClickListener confirmOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer oldprofile;
//                UserModel newUser = ((MainActivity)getActivity()).getUser();
//
//                oldprofile = newUser.getProfilePic();
//
//                if(oldprofile != null ){
//
//                }
//                newUser.setProfilePic(position);
//                System.out.println(newUser.getProfilePic());
//
//                View profilePicItem = inflater.inflate(R.layout.profile_pic_item,null);
//
//                newUser.setProfilePic(position+1);
//                ImageView img = profilePicItem.findViewById(R.id.IV_ProfilePic);
//                //tick.setVisibility(View.VISIBLE);
//                ((MainActivity)getActivity()).setUser(newUser);

                setConfirm(true);


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

    public void setConfirm(boolean sendConfirm){
        this.sendConfirm = sendConfirm;
    }

    public boolean getConfirm(){
        return this.sendConfirm;
    }

}
