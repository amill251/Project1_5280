package com.group3.project1.chatapp.chatroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Chatroom;

import java.util.ArrayList;

public class AllChatroomsRecyclerViewAdapter extends RecyclerView.Adapter<AllChatroomsRecyclerViewAdapter.ViewHolder> {
    ArrayList<Chatroom> chatrooms;
    FirebaseAuth mAuth;

    public AllChatroomsRecyclerViewAdapter(ArrayList<Chatroom> chatrooms) {
        this.chatrooms = chatrooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();

        Chatroom chatroom = chatrooms.get(position);
        holder.chatroom = chatroom;
        holder.textViewChatroomName.setText(chatroom.getName());
        holder.imageViewBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
    }

    @Override
    public int getItemCount() {
        return this.chatrooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewChatroom;
        TextView textViewChatroomName;
        ImageView imageViewBookmark;

        View rootView;
        int position;
        Chatroom chatroom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;

            textViewChatroomName = itemView.findViewById(R.id.textViewChatroomName);
            imageViewBookmark = itemView.findViewById(R.id.imageViewBookmark);

        }
    }

}
