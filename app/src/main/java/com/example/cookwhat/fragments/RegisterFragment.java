package com.example.cookwhat.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cookwhat.R;
import com.example.cookwhat.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public RegisterFragment() {
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("WARNING", "Login already" + currentUser.getUid());
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        container.removeAllViews();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Button BtnNext = view.findViewById(R.id.button3);
        View.OnClickListener OCLNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextUsername = view.findViewById(R.id.ETRegisterUsername);
                String username = editTextUsername.getText().toString();
                EditText editTextEmail = view.findViewById(R.id.ETRegisterEmail);
                String email = editTextEmail.getText().toString();
                EditText editTextPassword = view.findViewById(R.id.ETRegisterPassword);
                String password = editTextPassword.getText().toString();
                System.out.println("password here"+password);

                readData(new FirestoreCallback(){

                    @Override
                    public void onCallBackCheckExisting(boolean isExisting) {

                        if (username.equals("")){
                            editTextUsername.setHint("Username must be filled!");
                            //editTextUsername.setHintTextColor(Integer.parseInt("@color/red"));
                        }

                        if (email.equals("")){
                            editTextEmail.setHint("Email must be filled!");
                            //editTextEmail.setHintTextColor(Integer.parseInt("@color/red"));
                        }

                        if (password.equals("")){
                            editTextPassword.setHint("Username must be filled!");
                           //editTextPassword.setHintTextColor(Integer.parseInt("@color/red"));
                        }
                        else  {
                            if (isExisting){

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setMessage(R.string.register_warning);
                                alert.setCancelable(false);
                                alert.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Navigation.findNavController(view).navigate(R.id.DestLogin);

                                    }
                                });
                                alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                alert.create();
                                alert.show();

                            }
                            else{

                                String[] details = {username, email, password};
                                LoginActivity activity = (LoginActivity) getActivity();
                                activity.setRegisterDetails(details);

                                
                            }

                        }

                    }
                }, email );






            }
        };

        BtnNext.setOnClickListener(OCLNext);

    }

    public void readData(FirestoreCallback firestoreCallback, String emailAddr){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("readData and check");
        db.collection("user")
                .whereEqualTo("emailAddr", emailAddr)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    boolean isExisting = false;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                isExisting = true;
                                System.out.println(isExisting);
                            }
                            firestoreCallback.onCallBackCheckExisting(isExisting);
                        }
                        else{
                            System.out.println("Not enter"+isExisting);
                        }

                    }
                });
    }

    private interface FirestoreCallback {
        void onCallBackCheckExisting(boolean isExisting);
    }
}