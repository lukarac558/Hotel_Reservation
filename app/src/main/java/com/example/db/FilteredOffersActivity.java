package com.example.db;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import Adapter.OfferRecyclerViewAdapter;
import Classess.Offer;

public class FilteredOffersActivity extends AppCompatActivity {

    ArrayList<Offer> offerList;
    RecyclerView offersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_offers);

        offerList = (ArrayList<Offer>) getIntent().getSerializableExtra("offerList");
        offersRecyclerView = findViewById(R.id.offersRecyclerView);
        offersRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        offersRecyclerView.setLayoutManager(linearLayoutManager);
        OfferRecyclerViewAdapter adapter = new OfferRecyclerViewAdapter(this, offerList);
        offersRecyclerView.setAdapter(adapter);
    }
}