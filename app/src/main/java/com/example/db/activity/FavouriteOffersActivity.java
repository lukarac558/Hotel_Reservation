package com.example.db.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.adapter.FavouriteOffersRecyclerViewAdapter;
import com.example.db.model.CartItem;
import com.example.db.database.Database;
import com.example.db.utils.Formatter;
import com.example.db.utils.WindowDirector;
import com.example.db.utils.MenuDirector;
import com.example.db.R;

import java.util.ArrayList;
import java.util.Map;

public class FavouriteOffersActivity extends AppCompatActivity {

    private final ArrayList<CartItem> favouriteOffers = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialFavouriteOffers();
        if (favouriteOffers.size() > 0) {
            setContentView(R.layout.activity_favourite_offers);
            RecyclerView favouritesRecyclerView = findViewById(R.id.favouriteOffersRecyclerView);
            Formatter.setRecyclerView(favouritesRecyclerView, this);

            FavouriteOffersRecyclerViewAdapter adapter = new FavouriteOffersRecyclerViewAdapter(this, favouriteOffers);
            favouritesRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nie masz obecnie żadnych ulubionych ofert.", Toast.LENGTH_LONG).show();
            setContentView(R.layout.empty_recycler);

            TextView emptyTextView = findViewById(R.id.emptyTextView);
            emptyTextView.setText("Nie masz obecnie żadnych ulubionych ofert. Jeśli chcesz przeglądnąć aktualne oferty, kliknij w poniższy przycisk. Zostaniesz wówczas przeniesiony do wyszukiwarki ofert.");

            Button filterButton = findViewById(R.id.searchEngineButton);
            filterButton.setOnClickListener(view -> WindowDirector.changeActivity(this, SearchEngineActivity.class));
        }

    }

    private void initialFavouriteOffers() {
        if (Database.userId > 0) {
            favouriteOffers.addAll(Database.getAllFromCart());
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Map<String, ?> cartMap = sharedPreferences.getAll();

            for (Map.Entry<String, ?> cartItem : cartMap.entrySet()) {
                if (cartItem.getKey().contains("offer")) {
                    short peopleCount = (short) sharedPreferences.getInt(cartItem.getKey(), 1);
                    String offer = cartItem.getKey().replace("offer", "");
                    favouriteOffers.add(new CartItem(Integer.parseInt(offer), peopleCount));
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setUserOptionsMenu(Database.userId, this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleUserMenuItemSelected(Database.userId, this, item);
    }

}