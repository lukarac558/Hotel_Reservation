package com.example.db.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.example.db.Adapter.OffersRecyclerViewAdapter;

import com.example.db.Class.Offer;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class DeleteOfferFragment extends Fragment {

    OffersActivity offersActivity;
    Intent intent;
    ArrayList<Offer> offerList;
    RecyclerView offerRecyclerView;
    OffersRecyclerViewAdapter offersRecyclerViewAdapter;

    public DeleteOfferFragment() {
    }

    public static DeleteOfferFragment newInstance() {
        DeleteOfferFragment fragment = new DeleteOfferFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offersActivity = (OffersActivity) getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_offer, container, false);

        offerList = (ArrayList<Offer>) Database.getAllOffers();
        offerRecyclerView = view.findViewById(R.id.oOffersRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(offersActivity.getApplicationContext());
        offerRecyclerView.setLayoutManager(linearLayoutManager);

        offersRecyclerViewAdapter = new OffersRecyclerViewAdapter(offersActivity.getApplicationContext(), offerList);
        offerRecyclerView.setAdapter(offersRecyclerViewAdapter);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(offersActivity.getApplicationContext(), HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(offersActivity.getApplicationContext(), OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(offersActivity.getApplicationContext(), OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(offersActivity.getApplicationContext(), ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(offersActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }

}