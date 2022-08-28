package com.group3.project1.chatapp;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.group3.project1.chatapp.databinding.SearchRowItemBinding;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<String> {
    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            SearchRowItemBinding binding = SearchRowItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }

        String option = getItem(position);
        SearchRowItemBinding binding = (SearchRowItemBinding) convertView.getTag();

        binding.textViewSearchOption.setText(option);

        if(position == 0) {
            binding.imageViewSearchOption.setImageResource(R.drawable.ic_baseline_group_24);
        } else if (position == 1) {
            binding.imageViewSearchOption.setImageResource(R.drawable.ic_baseline_chat_bubble_24);
        }

        return convertView;
    }
}
