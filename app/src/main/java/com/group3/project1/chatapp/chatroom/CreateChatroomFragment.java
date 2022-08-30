package com.group3.project1.chatapp.chatroom;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.databinding.FragmentAllUsersBinding;
import com.group3.project1.chatapp.databinding.FragmentCreateChatroomBinding;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;
import com.group3.project1.chatapp.models.User;
import com.group3.project1.chatapp.user.AllUsersFragment;
import com.group3.project1.chatapp.user.AllUsersRecyclerViewAdapter;

import java.util.ArrayList;

public class CreateChatroomFragment extends Fragment implements CreateChatroomAdapter.IUsersRecycler {
    FragmentCreateChatroomBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<User> addedUsers = new ArrayList<>();
    LinearLayoutManager layoutManager;
    CreateChatroomAdapter adapter;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateChatroomBinding.inflate(inflater, container, false);
        getActivity().setTitle("Create Chatroom");
        btnSubmitChatroom(binding.getRoot());

        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new CreateChatroomAdapter(users, addedUsers, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUsers();
    }

    private void getUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .orderBy("last_name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        users.clear();

                        for(QueryDocumentSnapshot document: value) {
                            User user = document.toObject(User.class);
                            if(!document.getId().equals(mAuth.getUid())) {
                                users.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void btnSubmitChatroom(final View view) {
        view.findViewById(R.id.submit_new_chatroom).setOnClickListener(v -> {
                EditText nameField = view.findViewById(R.id.chatroom_name_field);
                Chatroom chatroom = new Chatroom(nameField.getText().toString(), null);
                db = FirebaseFirestore.getInstance();
                chatroom.setOwner(db.collection("users")
                        .document(mAuth.getCurrentUser().getUid()));
                db.collection("chatrooms")
                        .add(chatroom)
                        .addOnSuccessListener(document-> {
                            db.collection("chatrooms").document(document.getId())
                                    .set(new Chatroom(nameField.getText().toString(), document.getId()));
                        })
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

    @Override
    public void addUserToChat(User user) {
        if(!user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
            addedUsers.add(user);
            adapter.notifyDataSetChanged();
        }
    }

    CreateChatroomFragment.IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateChatroomFragment.IListener) context;
    }

    public interface IListener {
        public void createChatroom();
    }

}