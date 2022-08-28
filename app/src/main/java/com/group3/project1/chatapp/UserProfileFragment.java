package com.group3.project1.chatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.group3.project1.chatapp.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    private static final String USER = "USER";

    User user;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Profile");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        TextView fName = view.findViewById(R.id.inputUserProfileFirstName);
        TextView lName = view.findViewById(R.id.inputUserProfileLastName);
        TextView city = view.findViewById(R.id.inputUserProfileCity);

        fName.setText(user.getFirstName());
        lName.setText(user.getLastName());
        city.setText(user.getCity());

        if (User.FEMALE.equalsIgnoreCase(user.getGender())) {
            RadioButton btn = view.findViewById(R.id.radioBtnUserProfileFemale);
            btn.setChecked(true);
        } else if (User.MALE.equalsIgnoreCase(user.getGender())) {
            RadioButton btn = view.findViewById(R.id.radioBtnUserProfileMale);
            btn.setChecked(true);
        }

        return view;
    }
}