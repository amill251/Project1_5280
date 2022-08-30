package com.group3.project1.chatapp.chatroom;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Chatroom;
import com.group3.project1.chatapp.models.ChatroomUser;

import java.util.ArrayList;
import java.util.Map;

public class AllChatroomsRecyclerViewAdapter extends RecyclerView.Adapter<AllChatroomsRecyclerViewAdapter.ViewHolder> {
    ArrayList<Chatroom> chatrooms;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public AllChatroomsRecyclerViewAdapter(ArrayList<Chatroom> chatrooms) {
        this.chatrooms = chatrooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Chatroom chatroom = chatrooms.get(position);
        holder.chatroom = chatroom;
        holder.textViewChatroomName.setText(chatroom.getName());

        DocumentReference userRef;
        DocumentReference chatroomRef;

        userRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        chatroomRef = db.collection("chatrooms").document(chatroom.getId());

        db.collection("chatroom_users").document(chatroom.getId() +
                        "_" + mAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                Boolean bookmark = (Boolean) document.get("is_Bookmarked");

                                if (bookmark) {
                                    holder.imageViewBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                } else {
                                    holder.imageViewBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                                }
                            } catch (Exception e) {
                                //
                            }

                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("TAG", "No such document");
                        }

                    }
                });
    }


    @Override
    public int getItemCount() {
        return this.chatrooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewChatroom;
        TextView textViewChatroomName;
        ImageView imageViewBookmark;

        View rootView;
        int position;
        Chatroom chatroom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;

            textViewChatroomName = itemView.findViewById(R.id.textViewChatroomName);
            imageViewBookmark = itemView.findViewById(R.id.imageViewBookmark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference userRef;
                    DocumentReference chatroomRef;

                    userRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
                    chatroomRef = db.collection("chatrooms").document(chatroom.getId());

                    db.collection("chatroom_users").document(chatroom.getId() +
                                    "_" + mAuth.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        db.collection("chatroom_users")
                                                .document(chatroom.getId() +
                                                        "_" + mAuth.getUid())
                                                .delete();
                                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        db.collection("chatroom_users")
                                                .document(chatroom.getId() +
                                                        "_" + mAuth.getUid())
                                                .set(new ChatroomUser(userRef, chatroomRef));
                                        Log.d("TAG", "No such document");
                                    }
                                }
                            });
                }
            });
        }
    }
}
