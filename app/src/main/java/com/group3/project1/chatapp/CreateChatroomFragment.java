package com.group3.project1.chatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.User;

public class CreateChatroomFragment extends Fragment {

    IListener mListener;
    public CreateChatroomFragment() {
        // Required empty public constructor
    }


    public static CreateChatroomFragment newInstance(String param1, String param2) {
        CreateChatroomFragment fragment = new CreateChatroomFragment();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IListener) {
            mListener = (IListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_chatroom, container, false);
        getActivity().setTitle("Create Chatroom");
        btnSubmitChatroom(view);
        return view;
    }

    private void btnSubmitChatroom(final View view) {
        view.findViewById(R.id.submit_new_chatroom).setOnClickListener(v -> {
                EditText nameField = view.findViewById(R.id.chatroom_name_field);
                Chatroom chatroom = new Chatroom(nameField.getText().toString());
                mListener.createChatroom(chatroom);
            }
        );
    }

    interface IListener {
        public void createChatroom(Chatroom chatroom);
    }
}