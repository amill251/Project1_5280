package com.group3.project1.chatapp.models;

public class ChatroomSummary {
    String title;
    String latestMessage;

    public ChatroomSummary(String title, String latestMessage) {
        this.title = title;
        this.latestMessage = latestMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }
}
