package com.group3.project1.chatapp.chatroom;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;
import com.group3.project1.chatapp.models.Message;
import com.group3.project1.chatapp.models.User;
import com.group3.project1.chatapp.utils.UserUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;


public class ChatroomFragment extends Fragment {

    ArrayList<Message> messagesList = new ArrayList<>();
    MessagesRecyclerAdapter messagesAdapter;
    Chatroom currentChatroom;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    User user = null;

    public ChatroomFragment() {
        // Required empty public constructor
    }

    public static ChatroomFragment newInstance(Chatroom chatroom) {
        ChatroomFragment fragment = new ChatroomFragment();
        Bundle args = new Bundle();
        args.putSerializable(Chatroom.CURRENT_CHATROOM, chatroom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            currentChatroom = (Chatroom) getArguments().getSerializable(Chatroom.CURRENT_CHATROOM);
        }

        setUserFromDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);
        getActivity().setTitle(currentChatroom.getName());
        messagesList.clear();
        setupRecyclerView(view);
        btnSendMessage(view);
        loadMessages();
        return view;
    }

    private void btnSendMessage(View view) {
        view.findViewById(R.id.btnSendMessage).setOnClickListener(v -> {
            EditText messageInput = view.findViewById(R.id.message_input);
            Message newMessage = new Message();
            newMessage.setText(messageInput.getText().toString());
            newMessage.setTime_created(Timestamp.now());
            newMessage.setImage_location(user.getImage_location());
            newMessage.setUser_id(db.collection("users").document(mAuth.getCurrentUser().getUid()));
            db.collection("chatrooms").document(currentChatroom.getId()).collection("messages")
                    .add(newMessage)
                    .addOnSuccessListener(documentReference -> {
                        db.document("chatrooms/" + currentChatroom.getId()).update("latest_message", documentReference);
                        messageInput.setText("");
                    })
                    .addOnFailureListener(documentReference -> {
                        Log.e("ERROR", "btnSendMessage: ", documentReference.getCause());
                    });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadMessages() {
        db.collection("chatrooms").document(currentChatroom.getId()).collection("messages").addSnapshotListener((value, error) -> {
            value.getDocumentChanges();
            for(DocumentChange documentChange: value.getDocumentChanges()) {
                if(documentChange.getType().equals(DocumentChange.Type.REMOVED)) {
                    //TODO find message and delete
                } else if(documentChange.getType().equals(DocumentChange.Type.ADDED)){
                    Message message = documentChange.getDocument().toObject(Message.class);
                    messagesList.add(message);
                } else if(documentChange.getType().equals(DocumentChange.Type.MODIFIED)) {
                    //TODO check to see what was modified on message
                }
            }
            messagesList.sort(Comparator.comparing(Message::getTime_created));
            messagesAdapter.notifyDataSetChanged();
        });
    }

    private void setupRecyclerView(View view) {
        RecyclerView chatroomRecycleView = view.findViewById(R.id.chatroom_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatroomRecycleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        chatroomRecycleView.addItemDecoration(dividerItemDecoration);
        messagesAdapter = new MessagesRecyclerAdapter(getContext(), messagesList);
        chatroomRecycleView.setAdapter(messagesAdapter);
    }

    private void setUserFromDB() {
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user = new User(document.getString("email"), document.getString("first_name"),
                            document.getString("last_name"), document.getString("city"),
                            document.getString("gender"), document.getString("image_location"));
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}