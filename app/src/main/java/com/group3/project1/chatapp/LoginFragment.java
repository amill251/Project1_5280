package com.group3.project1.chatapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    IListener mListener;
    interface IListener {
        public void signup();
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

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        getActivity().setTitle("Login");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnSignin(view);

        btnCreateAccount(view);

        return view;
    }

    private void btnCreateAccount(final View view) {
        view.findViewById(R.id.btnCreateAccount).setOnClickListener(v -> mListener.signup());
    }

    private void btnSignin(final View view) {

        EditText inputAddress = view.findViewById(R.id.inputAddress);
        EditText inputPassword = view.findViewById(R.id.inputPassword);

        view.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                final String[] error = new String[1];
                if (inputAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Email Address", Toast.LENGTH_LONG).show();
                    error[0] = "Email required";
                    showAlert(error[0]);
                } else if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_LONG).show();
                    error[0] = "Password required";
                    showAlert(error[0]);
                } else {
                    mAuth.signInWithEmailAndPassword(inputAddress.getText().toString(), inputPassword.getText().toString())
                            .addOnCompleteListener(getActivity(), task -> {
                                if (task.isSuccessful()) {
                                    Log.d("myapp", "Login successful");
                                    mListener.loginSuccess();
                                } else {
                                    Log.d("myapp", "Login failed");
                                    Log.d("myapp", task.getException().getMessage());
                                    error[0] = task.getException().getMessage() + "";
                                }

                                showAlert(error[0]);
                            });
                }
            }
        });
    }

    private void showAlert(String error) {
        if (error != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error")
                    .setMessage(error)
                    .setPositiveButton("OK", (dialog, which) -> {

                    }).show();
        }
    }

}