package com.group3.project1.chatapp.models;


import android.provider.ContactsContract;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Objects;


public class Chatroom implements Serializable {

    String image_location;
    Boolean is_deleted = false;
    String name;
    DocumentReference owner;
    DocumentReference latest_message;

    @Exclude
    String id;
    @Exclude
    public static final String CURRENT_CHATROOM = "CURRENT_CHATROOM";

    public Chatroom(String image_location, Boolean is_deleted, String name, DocumentReference owner, DocumentReference latest_message, String id) {
        this.image_location = image_location;
        this.is_deleted = is_deleted;
        this.name = name;
        this.owner = owner;
        this.latest_message = latest_message;
        this.id = id;
    }

    public Chatroom(String name, String id) {
        this.id = id;
        this.name = name;
    }

    public Chatroom(String name) {
        this.name = name;
    }

    public Chatroom() {
        //Empty constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public DocumentReference getLatest_message() {
        return latest_message;
    }

    public void setLatest_message(DocumentReference latest_message) {
        this.latest_message = latest_message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chatroom chatroom = (Chatroom) o;
        return is_deleted.equals(chatroom.is_deleted) && name.equals(chatroom.name) && id.equals(chatroom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(is_deleted, name, id);
    }
}
