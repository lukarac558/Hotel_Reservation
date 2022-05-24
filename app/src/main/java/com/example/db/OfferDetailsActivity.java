package com.example.db;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import Classess.Offer;
import Classess.Order;
import Database.Database;

public class OfferDetailsActivity extends AppCompatActivity {

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
        Database.addOfferToCart(offer.getId());
        Toast.makeText(this, "Pomyślnie dodano przedmiot do koszyka", Toast.LENGTH_SHORT).show();

        try {
            Thread.sleep(1000);
            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bookOffer(View view){
        //int id, double totalCost, short peopleCount, int hotelId, LocalDate orderDate, int userId

        double totalCost = offer.getPrice() * peopleCount;
        double formattedCost = BigDecimal.valueOf(totalCost)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        LocalDate dateNow = LocalDate.now();

        Database.makeOrder(new Order(0, formattedCost, peopleCount, offer.getHotelId(), dateNow, Database.userId));
        Toast.makeText(this, "Pomyślnie złożono zamówienie", Toast.LENGTH_SHORT);

        try {
            Thread.sleep(1000);
            Intent intent = new Intent(getApplicationContext(), FavouriteOffersActivity.class);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}