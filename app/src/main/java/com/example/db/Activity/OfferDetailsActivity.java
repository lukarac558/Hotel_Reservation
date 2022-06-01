package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.R;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.example.db.Class.Offer;
import com.example.db.Class.Order;
import com.example.db.Database.Database;

public class OfferDetailsActivity extends AppCompatActivity {

    Intent intent;
    TextView offerTextView;
    Offer offer;
    short peopleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        offerTextView = findViewById(R.id.offerTextView);

        offer = (Offer) getIntent().getSerializableExtra("offer");
        peopleCount = (short) getIntent().getSerializableExtra("peopleCount");

        offerTextView.setText("Id: " + offer.getId());
    }

    public void addToFavourites(View view){

        if(Database.userId > 0) {
            int id = offer.getId();
            Database.addOfferToCart(id);
            Toast.makeText(this, "Pomyślnie dodano przedmiot do koszyka", Toast.LENGTH_SHORT).show();
            Log.d("Ux", "Dodane oferte, userId=" + Database.userId);

            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        }
        else
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(offer);
            String offerId = "offer" + offer.getId();
            editor.putString(offerId, json);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bookOffer(View view){

        if(Database.userId > 0 ) {

            double totalCost = offer.getPrice() * peopleCount;
            double formattedCost = BigDecimal.valueOf(totalCost)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            LocalDate dateNow = LocalDate.now();

            int id = offer.getHotel().getId();
            Database.makeOrder(new Order(offer.getId(), formattedCost, peopleCount, id , dateNow, Database.userId));
            Toast.makeText(this, "Pomyślnie złożono zamówienie", Toast.LENGTH_SHORT);

            Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Zaloguj się by składać zamówienia", Toast.LENGTH_SHORT).show();
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