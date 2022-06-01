package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.db.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import com.example.db.Adapter.FavouriteOffersRecyclerViewAdapter;
import com.example.db.Class.Offer;
import com.example.db.Database.Database;
public class FavouriteOffersActivity extends AppCompatActivity {

    Intent intent;
    ArrayList<Offer> favouritesList = new ArrayList<>();
    RecyclerView favouritesRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_offers);

        favouritesRecyclerView = findViewById(R.id.favouriteOffersRecyclerView);
        favouritesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        favouritesRecyclerView.setLayoutManager(linearLayoutManager);
        createList();
        FavouriteOffersRecyclerViewAdapter adapter = new FavouriteOffersRecyclerViewAdapter(this, favouritesList);
        favouritesRecyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createList() {
        if(Database.userId > 0){
             favouritesList = (ArrayList<Offer>) Database.getOffersFromCart();
        }
        else{
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Map<String, ?> offerMap = sharedPreferences.getAll();

            for (Map.Entry<String, ?> entry : offerMap.entrySet()) {
                if(entry.getKey().contains("offer")){
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString(entry.getKey(), "");
                    Offer offer = gson.fromJson(json, Offer.class);
                    favouritesList.add(offer);
                }
            }
        }
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