package com.group3.project1.chatapp.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group3.project1.chatapp.databinding.FragmentAllChatroomsBinding;
import com.group3.project1.chatapp.models.Chatroom;

import java.util.ArrayList;

public class  AllChatroomsFragment extends Fragment {
    FragmentAllChatroomsBinding binding;
    FirebaseAuth mAuth;
    ArrayList<Chatroom> chatrooms = new ArrayList<>();
    LinearLayoutManager layoutManager;
    AllChatroomsRecyclerViewAdapter adapter;

    public AllChatroomsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllChatroomsBinding.inflate(inflater, container, false);
        getActivity().setTitle("All Chatrooms");

        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new AllChatroomsRecyclerViewAdapter(chatrooms);
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

        db.collection("chatrooms")
                .orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        chatrooms.clear();

                        for(QueryDocumentSnapshot document: value) {
                            //Chatroom chatroom = document.toObject(Chatroom.class);
                            chatrooms.add(new Chatroom(document.getString("image_location"), document.getString("name")));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}