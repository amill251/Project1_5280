package com.group3.project1.chatapp.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group3.project1.chatapp.R;
import com.group3.project1.chatapp.models.User;
import java.util.ArrayList;

public class AllUsersRecyclerViewAdapter extends RecyclerView.Adapter<AllUsersRecyclerViewAdapter.ViewHolder> {
    ArrayList<User> users;
    IUsersRecycler mListener;
    FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public AllUsersRecyclerViewAdapter(ArrayList<User> users, IUsersRecycler mListener) {
        this.users = users;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();

        User user = users.get(position);
        holder.user = user;

        if (user.getImage_location() != null) {
            StorageReference imagesRef = storageRef.child(user.getImage_location());
            final long ONE_MEGABYTE = 1024 * 1024;
            imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.imageViewUserProfile.setImageBitmap(bmp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("myapp", "No Such file or Path found!!");
                }
            });
        } else {
            holder.imageViewUserProfile.setImageResource(R.drawable.ic_baseline_tag_faces_24);
        }

        holder.textViewUserName.setText(user.getFirst_name() + " " + user.getLast_name());
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewUserProfile;
        TextView textViewUserName;

        View rootView;
        int position;
        User user;
        IUsersRecycler mListener;

        public ViewHolder(@NonNull View itemView, IUsersRecycler mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;

            imageViewUserProfile = itemView.findViewById(R.id.imageViewUserProfile);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.gotoUserProfileDetailsFragment(user);
                }
            });
        }
    }

    interface IUsersRecycler {
        void gotoUserProfileDetailsFragment(User user);
    }
}
