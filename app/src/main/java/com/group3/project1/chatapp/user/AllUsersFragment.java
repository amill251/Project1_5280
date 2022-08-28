package com.group3.project1.chatapp.user;

import android.content.Context;
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
import com.group3.project1.chatapp.databinding.FragmentAllUsersBinding;
import com.group3.project1.chatapp.models.User;

import java.util.ArrayList;


public class AllUsersFragment extends Fragment implements AllUsersRecyclerViewAdapter.IUsersRecycler {
    FragmentAllUsersBinding binding;
    FirebaseAuth mAuth;
    ArrayList<User> users = new ArrayList<>();
    LinearLayoutManager layoutManager;
    AllUsersRecyclerViewAdapter adapter;

    public AllUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllUsersBinding.inflate(inflater, container, false);
        getActivity().setTitle("All Users");

        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new AllUsersRecyclerViewAdapter(users, this);
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
                .orderBy("last_name", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        users.clear();

                        for(QueryDocumentSnapshot document: value) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void gotoUserProfileDetailsFragment(User user) {
        mListener.gotoUserProfileDetailsFragment(user);
    }

    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (IListener) context;
    }

    public interface IListener {
        void gotoUserProfileDetailsFragment(User user);
    }
}