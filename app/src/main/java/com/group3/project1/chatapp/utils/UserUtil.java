package com.group3.project1.chatapp.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group3.project1.chatapp.models.User;

public class UserUtil {
    // don't use this method because return can be before value is set
    private static User getUserFromDB(FirebaseFirestore db, FirebaseAuth mAuth, String userMAuthUId) {
        final User[] user = new User[1];
        DocumentReference docRef = db.collection("users").document(userMAuthUId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user[0] = new User(document.getString("email"), document.getString("first_name"),
                            document.getString("last_name"), document.getString("city"),
                            document.getString("gender"), document.getString("image_location"));
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        return user[0];
    }
}
