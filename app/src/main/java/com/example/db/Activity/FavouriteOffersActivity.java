package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.Class.CartItem;
import com.example.db.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import com.example.db.Adapter.FavouriteOffersRecyclerViewAdapter;
import com.example.db.Class.Offer;
import com.example.db.Database.Database;

public class FavouriteOffersActivity extends AppCompatActivity {

    private Intent intent;
    private final ArrayList<CartItem> favouritesList = new ArrayList<>();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createList();
        if (favouritesList.size() > 0) {
            setContentView(R.layout.activity_favourite_offers);
            RecyclerView favouritesRecyclerView = findViewById(R.id.usersRecyclerView);
            favouritesRecyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            favouritesRecyclerView.setLayoutManager(linearLayoutManager);

            FavouriteOffersRecyclerViewAdapter adapter = new FavouriteOffersRecyclerViewAdapter(this, favouritesList);
            favouritesRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nie masz obecnie żadnych ulubionych ofert.", Toast.LENGTH_LONG).show();
            setContentView(R.layout.empty_recycler);

            Button filterButton = findViewById(R.id.searchEngineButton);
            TextView emptyTextView = findViewById(R.id.emptyTextView);

            emptyTextView.setText("Nie masz obecnie żadnych ulubionych ofert. Jeśli chcesz przeglądnąć aktualne oferty, kliknij w poniższy przycisk. Zostaniesz wówczas przeniesiony do wyszukiwarki ofert.");

            filterButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
                startActivity(intent);
            });
        }

    }

    private void createList() {
        if (Database.userId > 0) {
            favouritesList.addAll(Database.getAllFromCart());
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Map<String, ?> offerMap = sharedPreferences.getAll();

            for (Map.Entry<String, ?> entry : offerMap.entrySet()) {
                if (entry.getKey().contains("offer")) {
                    short peopleCount = (short) sharedPreferences.getInt(entry.getKey(), 1);
                    String offer = entry.getKey().replace("offer", "");
                    favouritesList.add(new CartItem(Integer.parseInt(offer), peopleCount));
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!Database.isAdmin) {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem login_item = menu.findItem(R.id.login);
            login_item.setVisible(false);
        } else {
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

        if (!Database.isAdmin) {

            if (id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if (id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if (id == R.id.uShowOrders)
                intent = new Intent(this, OrdersActivity.class);
            else if (id == R.id.uLogout) {
                Database.logOut();
                intent = new Intent(this, LoginActivity.class);
            }
        } else {

            if (id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if (id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if (id == R.id.login)
                intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }


}