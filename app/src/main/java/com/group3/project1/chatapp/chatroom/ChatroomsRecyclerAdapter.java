package com.group3.project1.chatapp.chatroom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.Message;

import java.util.ArrayList;

public class ChatroomsRecyclerAdapter extends RecyclerView.Adapter<ChatroomsRecyclerAdapter.ChatroomsViewHolder> {

    ArrayList<Chatroom> localChatroomsList;
    private LayoutInflater inflater;
    ChatroomsFragment.IListener mListener;

    ChatroomsRecyclerAdapter(Context context, ArrayList chatrooms, ChatroomsFragment.IListener listener) {
        inflater = LayoutInflater.from(context);
        this.localChatroomsList = chatrooms;
        mListener = listener;
    }

    @NonNull
    @Override
    public ChatroomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chatroom_summary, parent, false);
        return new ChatroomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomsViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.setTitle(localChatroomsList.get(position).getName());
        DocumentReference refLatestMessage = localChatroomsList.get(position).getLatest_message();
        if(refLatestMessage != null) {
            db.document(refLatestMessage.getPath())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        try {
                            Message message = documentSnapshot.toObject(Message.class);
                            holder.setLatestMessage(message.getText());
                        } catch(Exception e) {

                        }
                    })
                    .addOnFailureListener(e -> Log.e("ERROR", "onFailure: ", e));
        }
        holder.itemView.setOnClickListener(view -> mListener.navChatroom(localChatroomsList.get(position)));
    }

    @Override
    public int getItemCount() {
        return localChatroomsList.size();
    }

    public class ChatroomsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView latestMessage;
        public ChatroomsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Chatroom_Title);
            latestMessage = itemView.findViewById(R.id.Latest_Message);

        }

        public String getTitle() {
            return title.getText().toString();
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public String getLatestMessage() {
            return latestMessage.getText().toString();
        }

        public void setLatestMessage(String latestMessage) {
            this.latestMessage.setText(latestMessage);
        }
    }
}
