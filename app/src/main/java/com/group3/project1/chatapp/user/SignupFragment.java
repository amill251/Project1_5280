package com.group3.project1.chatapp.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.group3.project1.chatapp.R;


public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;

    public SignupFragment() {
        // Required empty public constructor
    }

    IListener mListener;
    public interface IListener {
        public void registerCancelled();
        public void loginSuccess();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IListener) {
            mListener = (IListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    public static SignupFragment newInstance(String adminUserEmail, String adminUserPassword) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Create New Account");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        EditText inputEmailAdress = view.findViewById(R.id.inputEmailAddress);
        EditText inputPassword = view.findViewById(R.id.inputPassword);
        EditText inputName = view.findViewById(R.id.inputFirstName);

        view.findViewById(R.id.btnRegisterCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.registerCancelled();
            }
        });

        view.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] error = new String[1];
                if (inputName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Name is required", Toast.LENGTH_LONG).show();
                    error[0] = "Name is required";
                    showAlert(error[0]);
                } else if (inputEmailAdress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_LONG).show();
                    error[0] = "Valid email required";
                    showAlert(error[0]);
                } else if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_LONG).show();
                    error[0] = "Password required";
                    showAlert(error[0]);
                } else {
                    FirebaseAuth mAuthLocal = FirebaseAuth.getInstance();
                    mAuthLocal.createUserWithEmailAndPassword(inputEmailAdress.getText().toString(),
                                    inputPassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("myapp", "Register successful");



                                        mListener.loginSuccess();
                                    } else {
                                        Log.d("myapp", "Register failed");
                                        Log.d("myapp", task.getException().getMessage());
                                        error[0] = task.getException().getMessage() + "";
                                    }

                                    showAlert(error[0]);
                                }
                            });
                }

            }
        });

        return view;
    }

    private void showAlert(String error) {
        if (error != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error")
                    .setMessage(error)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }
}