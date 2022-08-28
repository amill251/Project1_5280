package com.group3.project1.chatapp.chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Message;

import java.util.ArrayList;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.MessagesViewHolder> {

    ArrayList<Message> localMessagesList;
    private LayoutInflater inflater;

    MessagesRecyclerAdapter(Context context, ArrayList messages) {
        inflater = LayoutInflater.from(context);
        this.localMessagesList = messages;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chatroom_message, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        holder.setMessageText(localMessagesList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return localMessagesList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        public TextView getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText.setText(messageText);
        }
    }
}
