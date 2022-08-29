package com.group3.project1.chatapp.chatroom;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.User;

import java.util.ArrayList;
import java.util.Map;

public class ChatroomsFragment extends Fragment {


    IListener mListener;
    ArrayList<Chatroom> chatroomList = new ArrayList<>();
    ChatroomsRecyclerAdapter chatroomsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    // set at successful login
    User user;

    public interface IListener {
        public void signOut();
        public void navCreateChatroom(Chatroom chatroom);
        public void settings(User user);
        public void navChatroom(Chatroom chatroom);
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
        if (context instanceof ChatroomsFragment.IListener) {
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
        chatroomList.clear();
        setUser();
        setupRecyclerView(view);
        loadChatrooms(view);
        btnSignOut(view);
        btnNavCreateChatroom(view);
        btnSettings(view);

        return view;
    }



    private void setupRecyclerView(View view) {
        RecyclerView chatroomsRecycleView = view.findViewById(R.id.chatrooms_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatroomsRecycleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        chatroomsRecycleView.addItemDecoration(dividerItemDecoration);
        chatroomsAdapter = new ChatroomsRecyclerAdapter(getContext(), chatroomList, mListener);
        chatroomsRecycleView.setAdapter(chatroomsAdapter);
    }

    private void loadChatrooms(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chatrooms").addSnapshotListener((value, error) -> {
            for(QueryDocumentSnapshot documentSnapshot: value) {
                Map map = documentSnapshot.getData();
                Chatroom chatroom = null;
                try {
                    chatroom = new Chatroom(
                            (String) map.get("image_location"),
                            (Boolean) map.get("is_deleted"),
                            (String) map.get("name"),
                            (DocumentReference) map.get("owner"),
                            (DocumentReference) map.get("latest_message"),
                            (String) documentSnapshot.getId()
                    );
                } catch (Exception e) {
                    Log.e("ERROR", "loadChatrooms: ", e);
                }

                chatroomList.add(chatroom);
            }
            chatroomsAdapter.notifyDataSetChanged();
        });
    }

    private void btnSignOut(final View view) {
        view.findViewById(R.id.btnSignOut).setOnClickListener(v -> mListener.signOut());
    }

    private void btnNavCreateChatroom(final View view) {
        view.findViewById(R.id.btnCreateGroup).setOnClickListener(v -> mListener.navCreateChatroom(new Chatroom("Chatroom test")));
    }

    private void btnSettings(final View view) {
        view.findViewById(R.id.imageSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.settings(user);
            }
        });
    }

    private void setUser() {
        mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user = new User(document.getString("email"), document.getString("first_name"), document.getString("last_name"), document.getString("city"), document.getString("gender"));
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}