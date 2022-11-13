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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.activity.HotelsActivity;
import com.example.db.adapter.HotelsRecyclerViewAdapter;
import com.example.db.model.Hotel;
import com.example.db.database.Database;
import com.example.db.R;

import java.util.ArrayList;

public class DeleteHotelFragment extends Fragment {

    private HotelsActivity hotelsActivity;

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

        ArrayList<Hotel> hotels = new ArrayList<>(Database.getAllHotels());
        RecyclerView hotelRecyclerView = view.findViewById(R.id.dHotelsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(hotelsActivity.getApplicationContext());
        hotelRecyclerView.setLayoutManager(linearLayoutManager);

        HotelsRecyclerViewAdapter hotelsRecyclerViewAdapter = new HotelsRecyclerViewAdapter(getContext(), hotels);
        hotelRecyclerView.setAdapter(hotelsRecyclerViewAdapter);

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