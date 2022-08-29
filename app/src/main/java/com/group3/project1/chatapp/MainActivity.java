package com.group3.project1.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group3.project1.chatapp.chatroom.AllChatroomsFragment;
import com.group3.project1.chatapp.chatroom.ChatroomFragment;
import com.group3.project1.chatapp.chatroom.ChatroomsFragment;
import com.group3.project1.chatapp.chatroom.CreateChatroomFragment;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;
import com.group3.project1.chatapp.models.User;
import com.group3.project1.chatapp.user.AllUsersFragment;
import com.group3.project1.chatapp.user.LoginFragment;
import com.group3.project1.chatapp.user.SignupFragment;
import com.group3.project1.chatapp.user.UserProfileFragment;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.IListener, SignupFragment.IListener, ChatroomsFragment.IListener,
        SearchFragment.IListener, AllUsersFragment.IListener, CreateChatroomFragment.IListener,
        UserProfileFragment.IListener, FileChooserFragment.IListener {

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

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

            mStorage = FirebaseStorage.getInstance();
            storageReference = mStorage.getReference();

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
                .addToBackStack(null)
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
    public void createChatroom() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void settings(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, UserProfileFragment.newInstance(user), "UserProfileFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navChatroom(Chatroom chatroom) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, ChatroomFragment.newInstance(chatroom), "ChatroomFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navAllChatrooms() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new AllChatroomsFragment(), "AllChatroomsFragment")
                .addToBackStack(null)
                .commit();
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
                .addToBackStack(null)
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
                .replace(R.id.containerview, UserProfileFragment.newInstance(user), "UserProfileFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void chooseProfileImage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new FileChooserFragment(), "FileChooserFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickProfileImageSave(String imageLocalPath) {
        StorageReference reference = null;
        if (imageLocalPath != null) {
            reference = storageReference.child(UUID.randomUUID().toString());
            Bitmap image = BitmapFactory.decodeFile(imageLocalPath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,80,stream);
            byte[] byteArray = stream.toByteArray();
            reference.putBytes(byteArray);
        }

        UserProfileFragment fragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag("UserProfileFragment");
        if (reference != null)
            fragment.updateProfileImage(reference.getPath());

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickProfileImageCancel() {
        UserProfileFragment fragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag("UserProfileFragment");
        getSupportFragmentManager().popBackStack();
    }
}