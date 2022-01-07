package com.example.cookwhat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cookwhat.adapters.FavouriteAdapter;

public class OptionPopUp extends DialogFragment{
    private static final String TAG = "" ;
    FavouriteAdapter adapter;
    int inx;
    public passData mPassData;


    public OptionPopUp(int inx){
        this.inx = inx;
    }
    public interface passData {
        void getOption(String option, Integer i);

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mPassData = (passData) getTargetFragment();
        }
        catch(ClassCastException e){
            Log.e(TAG,"onAttach: ClassCastException:"+ e.getMessage());
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View popUpView = inflater.inflate(R.layout.pop_up_favourite_delete, null);
        Button delete =popUpView.findViewById(R.id.Btn_Delete);
        Button moveTo = popUpView.findViewById(R.id.Btn_MoveTo);

        View.OnClickListener deleteOCL = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPassData.getOption("delete", inx);
                dismiss();

            }

        };

        View.OnClickListener moveToOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        delete.setOnClickListener(deleteOCL);
        moveTo.setOnClickListener(moveToOCL);



        return popUpView;
    }



}



