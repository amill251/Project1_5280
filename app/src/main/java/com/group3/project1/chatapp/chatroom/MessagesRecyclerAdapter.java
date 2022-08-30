package com.group3.project1.chatapp.chatroom;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.Message;
import com.group3.project1.chatapp.models.User;
import com.group3.project1.chatapp.utils.DateUtil;
import com.group3.project1.chatapp.utils.ImageUtil;
import com.group3.project1.chatapp.utils.UserUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.MessagesViewHolder> {

    ArrayList<Message> localMessagesList;
    private LayoutInflater inflater;

    MessagesRecyclerAdapter(Context context, ArrayList messages) {
        inflater = LayoutInflater.from(context);
        this.localMessagesList = messages;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chatroom_message, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        Message currentMsg = localMessagesList.get(position);

        holder.setMessageText(currentMsg.getText());
        holder.setMsgLikes(currentMsg.getNumberOfLikes() + "");

        // set msg owner name
        setMsgOwnerName(holder.getMsgOwnerName(), currentMsg.getUser_id().getId());

        String dateStr = DateUtil.firebaseTimestampToString(currentMsg.getTime_created());
        holder.setMessageDateTime(dateStr);

        // set the current msg's owner image
        ImageUtil.downloadAndSetImage(currentMsg.getImage_location(),
                FirebaseStorage.getInstance().getReference(), holder.getMsgOwnerImage());
    }

    @Override
    public int getItemCount() {
        return localMessagesList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageDateTime;
        TextView msgLikes;
        TextView msgOwnerName;
        ImageView msgOwnerImage;


        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageDateTime = itemView.findViewById(R.id.textViewMsgDateTime);
            msgLikes = itemView.findViewById(R.id.textViewMsgLikes);
            msgOwnerName = itemView.findViewById(R.id.textViewChatMsgOwnerName);
            msgOwnerImage = itemView.findViewById(R.id.imageViewMsgOwner);
        }

        public TextView getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText.setText(messageText);
        }

        public TextView getMessageDateTime() {return messageDateTime;};

        public void setMessageDateTime(String msgDateTime) {this.messageDateTime.setText(msgDateTime);}

        public TextView getMsgLikes() {return msgLikes;}

        public void setMsgLikes(String msgLikes) {this.msgLikes.setText(msgLikes);}

        public TextView getMsgOwnerName() {return msgOwnerName;}

        public void setMsgOwnerName(String msgOwnerName) {this.msgOwnerName.setText(msgOwnerName);}

        public ImageView getMsgOwnerImage() {return msgOwnerImage;}

        public void setMsgOwnerImage(ImageView msgOwnerImage) {this.msgOwnerImage = msgOwnerImage;}
    }

    private void setMsgOwnerName(TextView textViewOwnerName, String recordId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String curRecordId = document.getId();
                    if (curRecordId.equalsIgnoreCase(recordId)) {
                        textViewOwnerName.setText("" + document.getData().get("first_name") + " " +
                                document.getData().get("last_name"));
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("myapp", "failed to get user record");
                    }
                });
    }
}
