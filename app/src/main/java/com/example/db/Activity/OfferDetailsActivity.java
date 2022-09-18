package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
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
import java.time.LocalDate;

import com.example.db.Class.Offer;
import com.example.db.Class.Order;
import com.example.db.Database.Database;

public class OfferDetailsActivity extends AppCompatActivity {

    Intent intent;
    int offerId;
    Offer offer;
    short peopleCount;
    double totalPrice;
    String stringTotalPrice;
    ImageView dHotelImageView;
    RatingBar dStarsRatingBar;
    TextView dHotelNameTextView;
    TextView dCountryTextView;
    TextView dCityTextView;
    TextView dFoodTextView;
    TextView dPriceTextView;
    TextView dStartDateTextView;
    TextView dEndDateTextView;
    TextView dDescriptionTextView;
    NumberPicker dPeopleCountNumberPicker;
    TextView dTotalPriceTextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        dHotelImageView = findViewById(R.id.dHotelImageView);
        dHotelNameTextView = findViewById(R.id.dHotelNameTextView);
        dStarsRatingBar = findViewById(R.id.dStarsRatingBar);
        dCountryTextView = findViewById(R.id.dCountryTextView);
        dCityTextView = findViewById(R.id.dCityTextView);
        dFoodTextView = findViewById(R.id.dFoodTextView);
        dPriceTextView = findViewById(R.id.dPriceTextView);
        dStartDateTextView = findViewById(R.id.dStartDateTextView);
        dEndDateTextView = findViewById(R.id.dEndDateTextView);
        dDescriptionTextView = findViewById(R.id.dDescriptionTextView);
        dPeopleCountNumberPicker = findViewById(R.id.dPeopleCountNumberPicker);
        dTotalPriceTextView = findViewById(R.id.dTotalPriceTextView);

        if(Database.userId <= 0)
            findViewById(R.id.dBookButton).setVisibility(View.INVISIBLE);

        offerId = (int) getIntent().getSerializableExtra("offer");
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
        dHotelNameTextView.setText(offer.getHotel().getName().getName());
        dStarsRatingBar.setRating(offer.getHotel().getStarCount());
        dCountryTextView.setText(offer.getHotel().getCountry().getName());
        dCityTextView.setText(offer.getHotel().getCity().getName());
        dFoodTextView.setText(offer.getHotel().getFood().getType());
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
            Database.addOfferToCart(id);
            Toast.makeText(this, "Pomyślnie dodano przedmiot do koszyka", Toast.LENGTH_SHORT).show();
            Log.d("To cart added", "Pomyślnie dodano przedmiot do koszyka");

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
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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