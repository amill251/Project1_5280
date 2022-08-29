package com.group3.project1.chatapp.models;

import com.google.firebase.firestore.DocumentReference;

public class ChatroomUser {
    DocumentReference user;
    DocumentReference chatroom;
    Boolean is_viewing = false;

    public ChatroomUser(DocumentReference user, DocumentReference chatroom) {
        this.user = user;
        this.chatroom = chatroom;
    }

    public DocumentReference getChatroom() {
        return chatroom;
    }

    public void setChatroom(DocumentReference chatroom) {
        this.chatroom = chatroom;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }

    public Boolean getIs_viewing() {
        return is_viewing;
    }

    public void setIs_viewing(Boolean is_viewing) {
        this.is_viewing = is_viewing;
    }
}
