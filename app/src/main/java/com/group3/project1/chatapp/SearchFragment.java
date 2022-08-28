package com.group3.project1.chatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.group3.project1.chatapp.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment {
    FragmentSearchBinding binding;

    ArrayList<String> options = new ArrayList<>(Arrays.asList("Browse Users", "Browse Chatrooms"));
    ArrayAdapter<String> adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        getActivity().setTitle("Search");

        adapter = new SearchAdapter(getContext(), R.layout.search_row_item, options);
        binding.listView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.gotoSearchOption(options.get(position));
            }
        });
    }

    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof IListener) {
            mListener = (IListener) context;
        } else {
            throw new RuntimeException((context.toString() + "must implement IListener"));
        }
    }

    public interface IListener {
        void gotoSearchOption(String option);
    }
}