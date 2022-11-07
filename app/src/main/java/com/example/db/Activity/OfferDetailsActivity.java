package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.R;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;

import com.example.db.Class.Offer;
import com.example.db.Class.Order;
import com.example.db.Database.Database;

public class OfferDetailsActivity extends AppCompatActivity {

    private Intent intent;
    private Offer offer;
    private short peopleCount;
    private double totalPrice;
    private String stringTotalPrice;
    private NumberPicker dPeopleCountNumberPicker;
    private TextView dTotalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        ImageView dHotelImageView = findViewById(R.id.dHotelImageView);
        TextView dHotelNameTextView = findViewById(R.id.dHotelNameTextView);
        RatingBar dStarsRatingBar = findViewById(R.id.dStarsRatingBar);
        TextView dCountryTextView = findViewById(R.id.dCountryTextView);
        TextView dCityTextView = findViewById(R.id.dCityTextView);
        TextView dFoodTextView = findViewById(R.id.dFoodTextView);
        TextView dPriceTextView = findViewById(R.id.dPriceTextView);
        TextView dStartDateTextView = findViewById(R.id.dStartDateTextView);
        TextView dEndDateTextView = findViewById(R.id.dEndDateTextView);
        TextView dDescriptionTextView = findViewById(R.id.dDescriptionTextView);
        dPeopleCountNumberPicker = findViewById(R.id.dPeopleCountNumberPicker);
        dTotalPriceTextView = findViewById(R.id.dTotalPriceTextView);

        if(Database.userId <= 0)
            findViewById(R.id.dBookButton).setVisibility(View.INVISIBLE);

        int offerId = (int) getIntent().getSerializableExtra("offer");
        offer = Database.getOfferById(offerId);
        peopleCount = (short) getIntent().getSerializableExtra("peopleCount");

        dPeopleCountNumberPicker.setMinValue(1);

        if(offer.getPlacesNumber() >= 8)
            dPeopleCountNumberPicker.setMaxValue(8);
        else
            dPeopleCountNumberPicker.setMaxValue(offer.getPlacesNumber());

        Bitmap bitmap = offer.getHotel().getImage().getBitmap();
        String stringPrice = String.valueOf(offer.getPrice());
        totalPrice = offer.getPrice() * peopleCount;
        stringTotalPrice = String.valueOf(totalPrice);

        dHotelImageView.setImageBitmap(bitmap);
        dHotelNameTextView.setText(offer.getHotel().getName());
        dStarsRatingBar.setRating(offer.getHotel().getStarCount());
        dCountryTextView.setText(offer.getHotel().getCity().getCountry().getName());
        dCityTextView.setText(offer.getHotel().getCity().getName());
        dFoodTextView.setText(offer.getFood().getType());
        dPriceTextView.setText(stringPrice);
        dStartDateTextView.setText(offer.getStartDate().toString());
        dEndDateTextView.setText(offer.getEndDate().toString());
        dDescriptionTextView.setText(offer.getHotel().getDescription());
        dPeopleCountNumberPicker.setValue(peopleCount);
        dTotalPriceTextView.setText(stringTotalPrice);

        dPeopleCountNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            peopleCount = (short) dPeopleCountNumberPicker.getValue();
            totalPrice = offer.getPrice() * peopleCount;

            double formattedCost = BigDecimal.valueOf(totalPrice)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            stringTotalPrice = String.valueOf(formattedCost);
            dTotalPriceTextView.setText(stringTotalPrice);
        });
    }

    public void addToFavourites(View view){

        if(Database.userId > 0) {
            int id = offer.getId();

            try {
                Database.addOfferToCart(id , peopleCount);
                Toast.makeText(this, "Pomyślnie dodano przedmiot do koszyka", Toast.LENGTH_SHORT).show();
            } catch (SQLException exception) {
                Toast.makeText(this, "Przedmiot jest już w koszyku, dlatego nie można go dodać.", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        }
        else
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String offerId = "offer" + offer.getId();
            editor.putInt(offerId, peopleCount);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        }
    }

    public void bookOffer(View view){

        if(Database.userId > 0 ) {

            peopleCount = (short) dPeopleCountNumberPicker.getValue();
            double formattedCost = BigDecimal.valueOf(totalPrice)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            LocalDate dateNow = LocalDate.now();

            int id = offer.getId();
            Database.makeOrder(new Order(0, formattedCost, peopleCount, id , dateNow, Database.userId));
            Toast.makeText(this, "Pomyślnie złożono zamówienie", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Zaloguj się by składać zamówienia", Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(!Database.isAdmin) {
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

        if(!Database.isAdmin) {

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