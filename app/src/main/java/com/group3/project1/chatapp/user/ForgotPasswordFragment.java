package com.group3.project1.chatapp.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.databinding.FragmentForgotPasswordBinding;
import com.group3.project1.chatapp.databinding.FragmentUserProfileBinding;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class ForgotPasswordFragment extends Fragment {
    FragmentForgotPasswordBinding binding;

    private FirebaseAuth mAuth;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    IListener mListener;
    public interface IListener {
        public void resetPasswordSuccess();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);

        getActivity().setTitle("Reset Password");

        mAuth = FirebaseAuth.getInstance();

        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] error = new String[1];

                mAuth.fetchSignInMethodsForEmail(binding.inputResetpasswordEmailAddress.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<SignInMethodQueryResult>() {
                            @Override
                            public void onSuccess(SignInMethodQueryResult signInMethodQueryResult) {
                                List<String> signInMethods = signInMethodQueryResult.getSignInMethods();
                                if (signInMethods.isEmpty())
                                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                                else {
                                    String email = binding.inputResetpasswordEmailAddress.getText().toString();
                                    mAuth.sendPasswordResetEmail(email).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("myapp", e.getMessage());
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            mListener.resetPasswordSuccess();
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Internal error validating email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return binding.getRoot();
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