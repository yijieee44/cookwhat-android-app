package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookwhat.R;
import com.example.cookwhat.adapters.CommentAdapter;
import com.example.cookwhat.adapters.EditProfilePicAdapter;
import com.example.cookwhat.models.RecipeModelDB;
import com.example.cookwhat.models.UserModelDB;
import com.example.cookwhat.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;


public class EditProfilePicDialogFragment extends DialogFragment {

    UserModelDB currentUser;
    RecyclerView rv;
    ImageView pfp;
    Button btn;
    int pos;
    FirebaseFirestore db;
    CollectionReference userdb;


    public EditProfilePicDialogFragment(UserModelDB currentUser) {
        this.currentUser = currentUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile_pic_dialog, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        userdb = db.collection("user");
        pos = currentUser.getProfilePic();
        Button btn = (Button) view.findViewById(R.id.BtnConnfirmm);
        pfp = (ImageView) getActivity().findViewById(R.id.IV_Profile);
        rv = (RecyclerView) view.findViewById(R.id.RVfavcat);
        LinearLayoutManager horizontalScroll = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(horizontalScroll);
        EditProfilePicAdapter editProfilePicAdapter = new EditProfilePicAdapter(currentUser, getContext(), new EditProfilePicAdapter.ConfirmListener() {
            @Override
            public void onItemClicked(int position) {
                pos = position;
            }
        });
        rv.setAdapter(editProfilePicAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pfp.setImageResource(Constants.PROFILE_PIC[pos]);
                currentUser.setProfilePic(pos);
                updateUser(currentUser);
                dismiss();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int)(metrics.widthPixels*0.90);
        int height = (int)(metrics.heightPixels*0.275);

        getDialog().getWindow().setLayout(width, height);
    }

    public void updateUser(UserModelDB userModelDB){
        userdb.document(userModelDB.getUserId()).set(userModelDB)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("SUCCESS", "DocumentSnapshot successfully written!");
                        } else {
                            Log.w("ERROR", "Error writing document", task.getException());
                        }
                    }
                });
    }
}