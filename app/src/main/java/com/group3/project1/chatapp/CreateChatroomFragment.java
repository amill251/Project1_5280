package com.group3.project1.chatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateChatroomFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_chatroom, container, false);
        getActivity().setTitle("Create Chatroom");
        return view;
    }
}