package com.group3.project1.chatapp.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.databinding.FragmentSignupBinding;
import com.group3.project1.chatapp.models.ChatroomUser;
import com.group3.project1.chatapp.models.User;


public class SignupFragment extends Fragment {
    FragmentSignupBinding binding;

    private FirebaseAuth mAuth;
    User user;

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
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create New Account");

        binding.btnRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.registerCancelled();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] error = new String[1];

                String firstName = binding.inputFirstName.getText().toString();
                String lastName = binding.inputLastName.getText().toString();
                String city = binding.inputCity.getText().toString();
                String email = binding.inputEmailAddress.getText().toString();
                String password = binding.inputPassword.getText().toString();

                int selectedRadioButton = binding.radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = binding.radioGroup.findViewById(selectedRadioButton);

                String gender = "";

                if (radioButton == binding.radioBtnUserProfileMale) {
                    gender = User.MALE;
                } else if (radioButton == binding.radioBtnUserProfileFemale) {
                    gender = User.FEMALE;
                }

                if (firstName.isEmpty()) {
                    Toast.makeText(getActivity(), "First name is required", Toast.LENGTH_LONG).show();
                    error[0] = "First name is required";
                    showAlert(error[0]);
                } else if (lastName.isEmpty()) {
                    Toast.makeText(getActivity(), "Last name is required", Toast.LENGTH_LONG).show();
                    error[0] = "Last name is required";
                    showAlert(error[0]);
                } else if (city.isEmpty()) {
                    Toast.makeText(getActivity(), "City is required", Toast.LENGTH_LONG).show();
                    error[0] = "City is required";
                    showAlert(error[0]);
                } else if (gender.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select a gender", Toast.LENGTH_LONG).show();
                    error[0] = "No gender selected";
                    showAlert(error[0]);
                } else if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_LONG).show();
                    error[0] = "Valid email required";
                    showAlert(error[0]);
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_LONG).show();
                    error[0] = "Password required";
                    showAlert(error[0]);
                } else {
                    FirebaseAuth mAuthLocal = FirebaseAuth.getInstance();
                    String finalGender = gender;
                    mAuthLocal.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("myapp", "Register successful");

                                        User newUser = new User(null, email, firstName, lastName, city, finalGender, null);

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("users").document(mAuthLocal.getUid())
                                                .set(newUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mListener.loginSuccess();
                                                    }
                                                });
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