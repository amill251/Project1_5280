package com.group3.project1.chatapp.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.databinding.FragmentChatroomBinding;
import com.group3.project1.chatapp.databinding.FragmentCurrentChatRoomUsersBinding;
import com.group3.project1.chatapp.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CurrentChatRoomUsersFragment extends Fragment {
    private static final String CURRENT_CHAT_ROOM_USERS = "CurrentChatRoomUsers";

    FragmentCurrentChatRoomUsersBinding binding;

    ArrayList<String> currentChatRoomUsers;

    public CurrentChatRoomUsersFragment() {
        // Required empty public constructor
    }

    public static CurrentChatRoomUsersFragment newInstance(ArrayList<String> userIds) {
        CurrentChatRoomUsersFragment fragment = new CurrentChatRoomUsersFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_CHAT_ROOM_USERS, userIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentChatRoomUsers = (ArrayList<String>) getArguments().getSerializable(CURRENT_CHAT_ROOM_USERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentChatRoomUsersBinding.inflate(inflater, container, false);
        getActivity().setTitle("Current Chat Room Users");

        getUserFromDB();

        return binding.getRoot();
    }

    private void getUserFromDB() {
        final List<User> users = new ArrayList<>();
//        for (String userId : currentChatRoomUsers) {
            FirebaseFirestore.getInstance().collection("users")
                    .orderBy("last_name", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            for (QueryDocumentSnapshot document : value) {
                                User user = document.toObject(User.class);
                                user.setId(document.getId());
                                users.add(user);


                            }

                            for (User user : users) {
                                Log.d("myapp", user.getId() + "");
                                if (currentChatRoomUsers == null || !currentChatRoomUsers.contains(user.getId()))
                                    continue;
                                if (binding.textViewCurrentChatRoomUsers.getText().toString().isEmpty())
                                    binding.textViewCurrentChatRoomUsers.setText(user.getFirst_name() + " " + user.getLast_name());
                                else
                                    binding.textViewCurrentChatRoomUsers.setText(binding.textViewCurrentChatRoomUsers.getText().toString()
                                            + ", " + user.getFirst_name() + " " + user.getLast_name());
                            }
                        }
                    });

//        }
    }
}