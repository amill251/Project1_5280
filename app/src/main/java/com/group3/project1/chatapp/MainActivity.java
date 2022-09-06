package com.group3.project1.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group3.project1.chatapp.chatroom.AllChatroomsFragment;
import com.group3.project1.chatapp.chatroom.ChatroomFragment;
import com.group3.project1.chatapp.chatroom.ChatroomsFragment;
import com.group3.project1.chatapp.chatroom.CreateChatroomFragment;
import com.group3.project1.chatapp.databinding.ActivityMainBinding;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;
import com.group3.project1.chatapp.models.User;
import com.group3.project1.chatapp.user.AllUsersFragment;
import com.group3.project1.chatapp.user.CurrentChatRoomUsersFragment;
import com.group3.project1.chatapp.user.ForgotPasswordFragment;
import com.group3.project1.chatapp.user.LoginFragment;
import com.group3.project1.chatapp.user.SignupFragment;
import com.group3.project1.chatapp.user.UserProfileFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.IListener, SignupFragment.IListener, ChatroomsFragment.IListener,
        SearchFragment.IListener, AllUsersFragment.IListener, CreateChatroomFragment.IListener,
        UserProfileFragment.IListener, FileChooserFragment.IListener, ForgotPasswordFragment.IListener,
        ChatroomFragment.IListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    ActivityMainBinding binding;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(mAuth.getCurrentUser() == null) {
            binding.bottomNavigationView.setVisibility(View.INVISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                    .commit();
        } else {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            replaceFragment(new ChatroomsFragment());
            mStorage = FirebaseStorage.getInstance();
            storageReference = mStorage.getReference();

            setUser();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.chatroomsFragment:
                    replaceFragment(new ChatroomsFragment());
                    break;
                case R.id.searchFragment:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.userProfileFragment:
                    replaceFragment(UserProfileFragment.newInstance(user));
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        setUser();

        String className = fragment.getClass().getName();
        if (fragment != null && fragment.getClass().getName().contains("UserProfileFragment")){
            gotoUserProfileDetailsFragment(user);
        } else  {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerview, fragment).commit();
        }
    }

    private void setUser() {
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user = new User(document.getId(), document.getString("email"), document.getString("first_name"),
                            document.getString("last_name"), document.getString("city"),
                            document.getString("gender"), document.getString("image_location"));
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .addToBackStack(null)
                .commit();
        mAuth.signOut();
        binding.bottomNavigationView.setVisibility(View.INVISIBLE);
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
    public void navChatroom(Chatroom chatroom) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, ChatroomFragment.newInstance(chatroom), "ChatroomFragment")
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
        setUser();
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new ChatroomsFragment(), "ChatroomsFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void forgotPassword() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new ForgotPasswordFragment(), "ForgotPasswordFragment")
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
        final StorageReference[] references = new StorageReference[1];
        if (imageLocalPath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving image...");
            progressDialog.show();

            references[0] = storageReference.child(UUID.randomUUID().toString());
            Bitmap image = BitmapFactory.decodeFile(imageLocalPath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,80,stream);
            byte[] byteArray = stream.toByteArray();
            references[0].putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Log.d("myapp", "wait failed while uploading image");
                    }

                    Log.d("myapp", "image saved successfully");
                    progressDialog.dismiss();

                    UserProfileFragment fragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag("UserProfileFragment");
                    if (references[0] != null)
                        fragment.updateProfileImage(references[0].getPath());

                    getSupportFragmentManager().popBackStack();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("myapp", "image save failed");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Saved " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    public void onClickProfileImageCancel() {
        UserProfileFragment fragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag("UserProfileFragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void resetPasswordSuccess() {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LoginFragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickCurrentUsers(List<String> currentUsers) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, CurrentChatRoomUsersFragment.newInstance((ArrayList)currentUsers), "CurrentChatRoomUsersFragment")
                .addToBackStack(null)
                .commit();
    }
}