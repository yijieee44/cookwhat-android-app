package com.example.cookwhat.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    String userID;
    boolean cond1 = false;
    boolean cond2 = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Dialog loadingDialog;

    public LoginFragment() {
        mAuth = FirebaseAuth.getInstance();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadingDialog = new Dialog(getContext());
        // Inflate the layout for this fragment
        container.removeAllViews();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button BtnLogin = view.findViewById(R.id.BtnGo);
        EditText editTextEmail = view.findViewById(R.id.ETLoginEmailAddress);
        EditText editTextPassword = view.findViewById(R.id.ETLoginPassword);
        View.OnClickListener OCLLogin = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();
                if (email.trim().equalsIgnoreCase("")) {
                    editTextEmail.setError("This field can not be blank");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Invalid email address");
                }
                else{
                    cond1 = true;
                }

                String password = editTextPassword.getText().toString();
                if (password.trim().equalsIgnoreCase("")) {
                    editTextPassword.setError("This field can not be blank");
                }
                else if (password.length()<6){
                    editTextPassword.setError("Password must contain at least 6 characters");
                }
                else{
                    cond2 = true;
                }

                if(cond1 && cond2){
                    loadingDialog.setCancelable(false);
                    loadingDialog.setContentView(R.layout.dialog_loading);
                    loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.black_transparent_background));

                    int width = (int)(getResources().getDisplayMetrics().widthPixels);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels);

                    loadingDialog.getWindow().setLayout(width, height);
                    loadingDialog.show();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SUCCESS", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();


                                        Intent intentMainActivity = new Intent(getActivity(), MainActivity.class);
                                        intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        loadingDialog.dismiss();
                                        startActivity(intentMainActivity);



                                    } else {
                                        // If sign in fails, display a message to the user.
                                        loadingDialog.cancel();
                                        Log.w("ERROR", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Failed to log in. Incorrect email address or password.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        };

        BtnLogin.setOnClickListener(OCLLogin);


    }

}