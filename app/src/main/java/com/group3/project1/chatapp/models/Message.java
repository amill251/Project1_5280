package com.group3.project1.chatapp.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Message {
    String image_location;
    Boolean is_deleted = false;
    String text;
    Timestamp time_created;
    DocumentReference user_id;;
    int numberOfLikes = 0;
    String messageDocumentId;
    String chatRoomId;

    public Message() {

    }

    public Message(Message message) {
        if (message != null) {
            image_location = message.image_location;
            is_deleted = message.is_deleted;
            text = message.text;
            time_created = message.time_created;
            user_id = message.user_id;
            numberOfLikes = message.numberOfLikes;
            messageDocumentId = message.messageDocumentId;
            chatRoomId = message.chatRoomId;
        }
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime_created() {
        return time_created;
    }

    public void setTime_created(Timestamp time_created) {
        this.time_created = time_created;
    }

    public DocumentReference getUser_id() {
        return user_id;
    }

    public void setUser_id(DocumentReference user_id) {
        this.user_id = user_id;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public String getMessageDocumentId() {
        return messageDocumentId;
    }

    public void setMessageDocumentId(String messageDocumentId) {
        this.messageDocumentId = messageDocumentId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
