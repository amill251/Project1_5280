package com.group3.project1.chatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group3.project1.chatapp.models.ChatroomSummary;

import java.util.ArrayList;

public class ChatroomsFragment extends Fragment {


    IListener mListener;
    ArrayList<ChatroomSummary> summaryList = new ArrayList<>();
    ChatroomsRecyclerAdapter chatroomsAdapter;

    interface IListener {
        public void signOut();
    }

    public ChatroomsFragment() {
        // Required empty public constructor
    }

    public static ChatroomsFragment newInstance(String param1, String param2) {
        ChatroomsFragment fragment = new ChatroomsFragment();
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

        if (context instanceof LoginFragment.IListener) {
            mListener = (IListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatrooms, container, false);
        getActivity().setTitle("Chatrooms");
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        summaryList.add(new ChatroomSummary("Chat group", "latest message"));
        RecyclerView chatroomsRecycleView = view.findViewById(R.id.chatrooms_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatroomsRecycleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        chatroomsRecycleView.addItemDecoration(dividerItemDecoration);
        chatroomsAdapter = new ChatroomsRecyclerAdapter(getContext(), summaryList);
        chatroomsRecycleView.setAdapter(chatroomsAdapter);

        btnSignOut(view);
        return view;
    }

    private void btnSignOut(final View view) {
        view.findViewById(R.id.btnSignOut).setOnClickListener(v -> mListener.signOut());
    }
}