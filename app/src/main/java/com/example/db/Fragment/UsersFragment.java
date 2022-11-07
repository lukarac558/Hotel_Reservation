package com.example.db.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.Adapter.UsersRecyclerViewAdapter;
import com.example.db.Class.User;
import com.example.db.Database.Database;
import com.example.db.R;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private ConfigurationActivity configurationActivity;
    private Intent intent;

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
        usersRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(configurationActivity.getApplicationContext());
        usersRecyclerView.setLayoutManager(linearLayoutManager);
        UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(getContext(), users);
        usersRecyclerView.setAdapter(adapter);

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(configurationActivity.getApplicationContext(), HotelsActivity.class);
        else if (id == R.id.showOffers)
            intent = new Intent(configurationActivity.getApplicationContext(), OffersActivity.class);
        else if (id == R.id.aShowOrders)
            intent = new Intent(configurationActivity.getApplicationContext(), OrdersActivity.class);
        else if (id == R.id.showConfiguration)
            intent = new Intent(configurationActivity.getApplicationContext(), ConfigurationActivity.class);
        else if (id == R.id.aLogout) {
            Database.logOut();
            intent = new Intent(configurationActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}