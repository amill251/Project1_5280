package com.group3.project1.chatapp.chatroom;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;

public class CreateChatroomFragment extends Fragment {

    IListener mListener;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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
        View view = inflater
                .inflate(R.layout.fragment_create_chatroom, container, false);
        getActivity().setTitle("Create Chatroom");
        btnSubmitChatroom(view);
        return view;
    }

    private void btnSubmitChatroom(final View view) {
        view.findViewById(R.id.submit_new_chatroom).setOnClickListener(v -> {
                EditText nameField = view.findViewById(R.id.chatroom_name_field);
                Chatroom chatroom = new Chatroom(nameField.getText().toString());
                db = FirebaseFirestore.getInstance();
                chatroom.setOwner(db.collection("users")
                        .document(mAuth.getCurrentUser().getUid()));
                db.collection("chatrooms")
                    .add(chatroom)
                    .addOnSuccessListener(documentReference -> {
                        db.collection("chatroom_users")
                                .document(documentReference.getId() +
                                        "_" + mAuth.getUid())
                                .set(new ChatroomUser(chatroom.getOwner(), documentReference));
                        mListener.createChatroom();
                    })
                    .addOnFailureListener(documentReference -> {
                        Log.e("ERROR", "btnSubmitChatroom: ",
                                documentReference.getCause());
                    });
            }
        );
    }

    public interface IListener {
        public void createChatroom();
    }
}