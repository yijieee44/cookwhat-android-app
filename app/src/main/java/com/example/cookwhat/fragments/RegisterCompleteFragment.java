package com.example.cookwhat.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.LoginActivity;
import com.example.cookwhat.adapters.ProfilePicAdapter;
import com.example.cookwhat.models.UserModelDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterCompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterCompleteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String username;
    String password;
    String email;
    ArrayList<Integer> profilepic ;
    int prof_pic = 0;
    boolean cond1 = false;
    boolean cond2 = false;
    Dialog loadingDialog;


    public RegisterCompleteFragment() {



    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterCompleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterCompleteFragment newInstance(String param1, String param2) {
        RegisterCompleteFragment fragment = new RegisterCompleteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        System.out.println("bundle received"+ bundle);
        if(bundle != null){
            username = bundle.getString("name");
            email = bundle.getString("email");
            password = bundle.getString("password");

        }
        else{
            Log.d("ErrorType","null details on first page of register");
        }

        profilepic =  new ArrayList<>();
        for(int i =0 ; i<33; i++){
            String name = "ic_profile_pic_" + String.valueOf(i+1);
            int resourceId = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
            profilepic.add(resourceId);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();

        return inflater.inflate(R.layout.fragment_register_complete, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new Dialog(getContext());
        Log.d("CHECKING", "email password username" + email + password + username);
        RecyclerView profilePicRV = view.findViewById(R.id.RV_ProfilePic);
        LinearLayoutManager recycleViewLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        profilePicRV.setLayoutManager(recycleViewLayoutManager);

        ProfilePicAdapter adapter = new ProfilePicAdapter(profilepic, new ProfilePicAdapter.ProfileClickListener2() {
            @Override
            public void onItemClicked(int position) {
                prof_pic = profilepic.get(position);
            }
        });

        LinearLayoutManager HorizontalScroll = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        profilePicRV.setLayoutManager(HorizontalScroll);
        profilePicRV.setAdapter(adapter);



        UserModelDB usermodel = new UserModelDB();

        usermodel.setUserName(username);
        usermodel.setEmailAddr(email);


        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

        Button BtnRegister = view.findViewById(R.id.Btn_Register);
        ChipGroup chipGroup = view.findViewById(R.id.CG_Preferences);

        View.OnClickListener OCLRegister = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (prof_pic == 0){
                    Toast.makeText(getContext(), "Please select a profile picture", Toast.LENGTH_SHORT).show();
                }
                else if (profilepic.contains(prof_pic)){
                    cond1 = true;
                    usermodel.setProfilePic(prof_pic);
                }

                List<Integer> chips = chipGroup.getCheckedChipIds();
                ArrayList<String> chipsSelected = new ArrayList<>();
                for(int i =0; i<chips.size(); i++){
                    Chip chip = view.findViewById(chips.get(i));
                    chipsSelected.add((String) chip.getText());
                }



                if (chipsSelected.isEmpty()){
                    Toast.makeText(getContext(), "Please select at least 1 preference", Toast.LENGTH_SHORT).show();
                }
                else{
                    cond2 = true;
                    usermodel.setPreference(chipsSelected);
                }


                if (cond1 && cond2){
                    loadingDialog.setCancelable(false);
                    loadingDialog.setContentView(R.layout.dialog_loading);
                    loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

                    int width = (int)(getResources().getDisplayMetrics().widthPixels);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels);

                    loadingDialog.getWindow().setLayout(width, height);
                    loadingDialog.show();

                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SUCCESS", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();
                                        usermodel.setUserId(userId);


                                        firestoreDb.collection("user").document(userId)
                                                .set(usermodel);

                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("SUCCESS", "Email sent.");
                                                            LoginActivity activity = (LoginActivity) getActivity();
                                                            activity.onRegisterSuccessful();
                                                        }
                                                    }
                                                });

                                        Toast.makeText(getActivity(), "Successfully registered!!",
                                                Toast.LENGTH_SHORT).show();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build();

                                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("SUCCESS", "Updated profile");
                                                }
                                            }
                                        });
                                        loadingDialog.dismiss();

                                    } else {
                                        loadingDialog.cancel();
                                        Log.w("ERROR", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }};
        BtnRegister.setOnClickListener(OCLRegister);
    }
}