package com.group3.project1.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group3.project1.chatapp.chatroom.AllChatroomsFragment;
import com.group3.project1.chatapp.chatroom.ChatroomsFragment;
import com.group3.project1.chatapp.chatroom.CreateChatroomFragment;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;
import com.group3.project1.chatapp.models.User;
import com.group3.project1.chatapp.user.AllUsersFragment;
import com.group3.project1.chatapp.user.LoginFragment;
import com.group3.project1.chatapp.user.SignupFragment;
import com.group3.project1.chatapp.user.UserProfileFragment;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.IListener, SignupFragment.IListener, ChatroomsFragment.IListener, SearchFragment.IListener, AllUsersFragment.IListener, CreateChatroomFragment.IListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            setContentView(R.layout.activity_main);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                    .commit();
        } else {
            setContentView(R.layout.activity_main);
            loginSuccess();
        }
    }

    @Override
    public void signup() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new SignupFragment(), "SignupFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void signOut() {
        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        mAuth.signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .commit();
    }

    @Override
    public void navCreateChatroom(Chatroom chatroom) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new CreateChatroomFragment(), "CreateChatroom")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createChatroom(Chatroom newChatroom) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        newChatroom.setOwner(db.collection("users").document(mAuth.getCurrentUser().getUid()));
        db.collection("chatrooms")
                .add(newChatroom)
                .addOnSuccessListener(documentReference -> {
                    documentReference.collection("chatroom_users").add(new ChatroomUser(newChatroom.getOwner()));
                     getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(documentReference -> {
                    Toast.makeText(this, documentReference.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void settings(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, UserProfileFragment.newInstance(user), "UserProfileFragment")
                .commit();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void registerCancelled() {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LoginFragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void loginSuccess() {
        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new ChatroomsFragment(), "ChatroomsFragment")

                .commit();
    }

    @Override
    public void gotoAllUsersFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new AllUsersFragment(), "AllUsersFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoAllChatroomsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new AllChatroomsFragment(), "AllChatroomsFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoUserProfileDetailsFragment(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new UserProfileFragment(), "UserProfileFragment")
                .addToBackStack(null)
                .commit();
    }
}