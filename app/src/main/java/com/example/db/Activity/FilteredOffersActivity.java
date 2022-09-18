package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import com.example.db.Adapter.FilteredOffersRecyclerViewAdapter;
import com.example.db.Database.Database;
import com.example.db.R;

public class FilteredOffersActivity extends AppCompatActivity {

    Intent intent;
    ArrayList<Integer> offersIdsList;
    RecyclerView offersRecyclerView;
    short peopleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_offers);

        peopleCount = (short) getIntent().getSerializableExtra("peopleCount");
        offersIdsList = (ArrayList<Integer>) getIntent().getSerializableExtra("offerList");
        offersRecyclerView = findViewById(R.id.filteredOffersRecyclerView);
        offersRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        offersRecyclerView.setLayoutManager(linearLayoutManager);
        FilteredOffersRecyclerViewAdapter adapter = new FilteredOffersRecyclerViewAdapter(this, offersIdsList, peopleCount);
        offersRecyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(Database.permission.equalsIgnoreCase("user")) {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem login_item = menu.findItem(R.id.login);
            login_item.setVisible(false);
        }
        else {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem logout_item = menu.findItem(R.id.uLogout);
            logout_item.setVisible(false);
            MenuItem showOrders_item = menu.findItem(R.id.uShowOrders);
            showOrders_item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(Database.permission.equalsIgnoreCase("user")) {

            if(id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if(id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if(id == R.id.uShowOrders)
                intent = new Intent(this, OrdersActivity.class);
            else if(id == R.id.uLogout)
            {
                Database.logOut();
                intent = new Intent(this, LoginActivity.class);
            }
        }
        else {

            if(id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if(id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if(id == R.id.login)
                intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}