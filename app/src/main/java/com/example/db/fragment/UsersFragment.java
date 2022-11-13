package com.example.db.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.adapter.UsersRecyclerViewAdapter;
import com.example.db.model.User;
import com.example.db.database.Database;
import com.example.db.R;
import com.example.db.utils.Formatter;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private ConfigurationActivity configurationActivity;

    public UsersFragment() {
    }

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        configurationActivity = (ConfigurationActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        ArrayList<User> users = (ArrayList<User>) Database.getAllUsers();

        RecyclerView usersRecyclerView = view.findViewById(R.id.usersRecyclerView);
        Formatter.setRecyclerView(usersRecyclerView, configurationActivity.getApplicationContext());

        UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(getContext(), users);
        usersRecyclerView.setAdapter(adapter);

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}