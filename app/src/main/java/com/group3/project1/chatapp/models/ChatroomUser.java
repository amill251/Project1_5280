package com.group3.project1.chatapp.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class ChatroomUser {
    DocumentReference user;
    DocumentReference chatroom;
    Boolean is_viewing = false;
    Boolean is_Bookmarked = false;
    ArrayList<String> userDocIds;

    public ChatroomUser(DocumentReference user, DocumentReference chatroom, ArrayList<String> userDocIds) {
        this.user = user;
        this.chatroom = chatroom;
        this.userDocIds = userDocIds;
    }

    public ChatroomUser(DocumentReference user, DocumentReference chatroom, Boolean is_Bookmarked) {
        this.user = user;
        this.chatroom = chatroom;
        this.is_Bookmarked = is_Bookmarked;
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

    public Boolean getIs_Bookmarked() {
        return is_Bookmarked;
    }

    public void setIs_Bookmarked(Boolean is_Bookmarked) {
        this.is_Bookmarked = is_Bookmarked;
    }

    public ArrayList<String> getUserDocIds() {
        return userDocIds;
    }

    public void setUserDocIds(ArrayList<String> userDocIds) {
        this.userDocIds = userDocIds;
    }
}
