package com.group3.project1.chatapp.models;


import android.provider.ContactsContract;

import com.google.firebase.firestore.DocumentReference;


public class Chatroom {
    String image_location;
    Boolean is_deleted = false;
    String name;
    DocumentReference owner;


    public Chatroom(String image_location, String name) {
        this.image_location = image_location;
        this.name = name;
    }

    public Chatroom(String name) {
        this.name = name;
    }

    public DocumentReference getOwner() {
        return owner;
    }

    public void setOwner(DocumentReference owner) {
        this.owner = owner;
    }

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
