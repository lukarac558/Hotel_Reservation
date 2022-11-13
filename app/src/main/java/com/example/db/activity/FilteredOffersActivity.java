package com.example.db.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.adapter.FilteredOffersRecyclerViewAdapter;
import com.example.db.database.Database;
import com.example.db.utils.Formatter;
import com.example.db.utils.MenuDirector;
import com.example.db.R;

import java.util.ArrayList;

public class FilteredOffersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_offers);

        short peopleCount = (short) getIntent().getSerializableExtra("peopleCount");
        ArrayList<Integer> filteredOffersIds = (ArrayList<Integer>) getIntent().getSerializableExtra("filteredOffersIds");
        RecyclerView offersRecyclerView = findViewById(R.id.filteredOffersRecyclerView);
        Formatter.setRecyclerView(offersRecyclerView, this);

        FilteredOffersRecyclerViewAdapter adapter = new FilteredOffersRecyclerViewAdapter(this, filteredOffersIds, peopleCount);
        offersRecyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setUserOptionsMenu(Database.userId, this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleUserMenuItemSelected(Database.userId, this, item);
    }

}