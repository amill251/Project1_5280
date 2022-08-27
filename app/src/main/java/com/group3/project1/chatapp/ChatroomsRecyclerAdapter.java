package com.group3.project1.chatapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.group3.project1.chatapp.models.ChatroomSummary;

import java.util.ArrayList;

public class ChatroomsRecyclerAdapter extends RecyclerView.Adapter<ChatroomsRecyclerAdapter.ChatroomsViewHolder> {

    ArrayList<ChatroomSummary> localSummaryList;
    private LayoutInflater inflater;

    ChatroomsRecyclerAdapter(Context context, ArrayList summaryList) {
        inflater = LayoutInflater.from(context);
        this.localSummaryList = summaryList;
    }

    @NonNull
    @Override
    public ChatroomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chatroom_summary, parent, false);
        return new ChatroomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomsViewHolder holder, int position) {
        holder.setTitle(localSummaryList.get(position).getTitle());
        holder.setLatestMessage(localSummaryList.get(position).getLatestMessage());
    }

    @Override
    public int getItemCount() {
        return localSummaryList.size();
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
