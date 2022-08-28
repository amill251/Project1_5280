package com.group3.project1.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.group3.project1.chatapp.models.User;
import java.util.ArrayList;

public class AllUsersRecyclerViewAdapter extends RecyclerView.Adapter<AllUsersRecyclerViewAdapter.ViewHolder> {
    ArrayList<User> users;
    IUsersRecycler mListener;
    FirebaseAuth mAuth;

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

        holder.imageViewUserProfile.setImageResource(R.drawable.ic_launcher_background);
        holder.textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
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
