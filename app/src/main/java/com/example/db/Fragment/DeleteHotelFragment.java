package com.example.db.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Adapter.HotelsRecyclerViewAdapter;
import com.example.db.Class.Hotel;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class DeleteHotelFragment extends Fragment {

    private HotelsActivity hotelsActivity;
    private Intent intent;

    public DeleteHotelFragment() {
    }

    public static DeleteHotelFragment newInstance() {
        return new DeleteHotelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        hotelsActivity = (HotelsActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_hotel, container, false);

        ArrayList<Hotel> hotelList = new ArrayList<>(Database.getAllHotels());
        RecyclerView hotelRecyclerView = view.findViewById(R.id.dHotelsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(hotelsActivity.getApplicationContext());
        hotelRecyclerView.setLayoutManager(linearLayoutManager);

        HotelsRecyclerViewAdapter hotelsRecyclerViewAdapter = new HotelsRecyclerViewAdapter(getContext(), hotelList);
        hotelRecyclerView.setAdapter(hotelsRecyclerViewAdapter);

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(hotelsActivity.getApplicationContext(), OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(hotelsActivity.getApplicationContext(), OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(hotelsActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}