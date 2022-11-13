package com.example.db.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.db.model.Offer;
import com.example.db.model.Order;
import com.example.db.database.Database;
import com.example.db.utils.MenuDirector;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;

public class OfferDetailsActivity extends AppCompatActivity {

    private Offer offer;
    private short peopleCount;
    private double totalPrice;
    private String stringTotalPrice;
    private NumberPicker peopleCountNumberPicker;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        ImageView hotelImageView = findViewById(R.id.dHotelImageView);
        TextView hotelNameTextView = findViewById(R.id.dHotelNameTextView);
        RatingBar starsRatingBar = findViewById(R.id.dStarsRatingBar);
        TextView countryTextView = findViewById(R.id.dCountryTextView);
        TextView cityTextView = findViewById(R.id.dCityTextView);
        TextView foodTextView = findViewById(R.id.dFoodTextView);
        TextView priceTextView = findViewById(R.id.dPriceTextView);
        TextView startDateTextView = findViewById(R.id.dStartDateTextView);
        TextView endDateTextView = findViewById(R.id.dEndDateTextView);
        TextView descriptionTextView = findViewById(R.id.dDescriptionTextView);
        peopleCountNumberPicker = findViewById(R.id.dPeopleCountNumberPicker);
        totalPriceTextView = findViewById(R.id.dTotalPriceTextView);

        hideBookButtonIfNotLogged(Database.userId);

        int offerId = (int) getIntent().getSerializableExtra("offerId");
        offer = Database.getOfferById(offerId);
        peopleCount = (short) getIntent().getSerializableExtra("peopleCount");

        peopleCountNumberPicker.setMinValue(1);
        setNumberPickerMaxValue(offer);

        Bitmap bitmap = offer.getHotel().getImage().getBitmap();
        String stringPrice = String.valueOf(offer.getPrice());
        totalPrice = offer.getPrice() * peopleCount;
        stringTotalPrice = String.valueOf(totalPrice);

        hotelImageView.setImageBitmap(bitmap);
        hotelNameTextView.setText(offer.getHotel().getName());
        starsRatingBar.setRating(offer.getHotel().getStarCount());
        countryTextView.setText(offer.getHotel().getCity().getCountry().getName());
        cityTextView.setText(offer.getHotel().getCity().getName());
        foodTextView.setText(offer.getFood().getType());
        priceTextView.setText(stringPrice);
        startDateTextView.setText(offer.getStartDate().toString());
        endDateTextView.setText(offer.getEndDate().toString());
        descriptionTextView.setText(offer.getHotel().getDescription());
        peopleCountNumberPicker.setValue(peopleCount);
        totalPriceTextView.setText(stringTotalPrice);

        peopleCountNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            peopleCount = (short) peopleCountNumberPicker.getValue();
            totalPrice = offer.getPrice() * peopleCount;

            double formattedCost = BigDecimal.valueOf(totalPrice)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            stringTotalPrice = String.valueOf(formattedCost);
            totalPriceTextView.setText(stringTotalPrice);
        });
    }

    public void addToFavourites(View view) {

        if (Database.userId > 0) {
            try {
                Database.addOfferToCart(offer.getId(), peopleCount);
                Toast.makeText(this, "Pomyślnie dodano przedmiot do koszyka", Toast.LENGTH_SHORT).show();
            } catch (SQLException exception) {
                Toast.makeText(this, "Przedmiot jest już w koszyku, dlatego nie można go dodać.", Toast.LENGTH_SHORT).show();
            }
        } else {
            addOfferSharedPreferences();
        }

        WindowDirector.changeActivity(this, FavouriteOffersActivity.class);
    }

    public void bookOffer(View view) {

        if (Database.userId > 0) {
            peopleCount = (short) peopleCountNumberPicker.getValue();
            double formattedCost = BigDecimal.valueOf(totalPrice)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            try {
                Database.makeOrder(new Order(0, formattedCost, peopleCount, offer.getId(), LocalDate.now(), Database.userId));
                Toast.makeText(this, "Pomyślnie złożono zamówienie", Toast.LENGTH_SHORT).show();
                WindowDirector.changeActivity(this, OrdersActivity.class);
            } catch (SQLException exception) {
                Toast.makeText(this, "Wystąpił problem przy składaniu zamówienia", Toast.LENGTH_SHORT).show();
            }

        } else
            Toast.makeText(this, "Zaloguj się, aby móc składać zamówienia", Toast.LENGTH_SHORT).show();
    }

    private void addOfferSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String offerId = "offer" + offer.getId();
        editor.putInt(offerId, peopleCount);
        editor.apply();
    }

    private void hideBookButtonIfNotLogged(int userId) {
        if (userId <= 0) {
            findViewById(R.id.dBookButton).setVisibility(View.INVISIBLE);
        }
    }

    private void setNumberPickerMaxValue(Offer offer) {
        if (offer.getPlacesNumber() >= 8) {
            peopleCountNumberPicker.setMaxValue(8);
        } else {
            peopleCountNumberPicker.setMaxValue(offer.getPlacesNumber());
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