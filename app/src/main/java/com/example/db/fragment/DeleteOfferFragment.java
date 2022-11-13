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

import com.example.db.activity.OffersActivity;
import com.example.db.adapter.OffersRecyclerViewAdapter;
import com.example.db.model.Offer;
import com.example.db.database.Database;
import com.example.db.R;

import java.util.ArrayList;

public class DeleteOfferFragment extends Fragment {

    private OffersActivity offersActivity;

    public DeleteOfferFragment() {
    }

    public static DeleteOfferFragment newInstance() {
        return new DeleteOfferFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offersActivity = (OffersActivity) getActivity();
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_offer, container, false);

        ArrayList<Offer> offers = new ArrayList<>(Database.getAllOffers());
        RecyclerView offerRecyclerView = view.findViewById(R.id.oOffersRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(offersActivity.getApplicationContext());
        offerRecyclerView.setLayoutManager(linearLayoutManager);

        OffersRecyclerViewAdapter offersRecyclerViewAdapter = new OffersRecyclerViewAdapter(getContext(), offers);
        offerRecyclerView.setAdapter(offersRecyclerViewAdapter);

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