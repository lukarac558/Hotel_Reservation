package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.db.Fragment.CityFragment;
import com.example.db.Fragment.CountryFragment;
import com.example.db.Database.Database;
import com.example.db.Fragment.FoodFragment;
import com.example.db.Fragment.HotelNameFragment;
import com.example.db.R;

public class ConfigurationActivity extends AppCompatActivity {

    private Intent intent;
    private CountryFragment countryFragment;
    private CityFragment cityFragment;
    private FoodFragment foodFragment;
    private HotelNameFragment hotelNameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
    }

    public void backToPanel(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(countryFragment != null)
            fragmentTransaction.remove(countryFragment);
        if(cityFragment != null)
            fragmentTransaction.remove(cityFragment);
        if(foodFragment != null)
            fragmentTransaction.remove(foodFragment);
        if(hotelNameFragment != null)
            fragmentTransaction.remove(hotelNameFragment);

        fragmentTransaction.commit();
        findViewById(R.id.countryImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.cityImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.foodImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.hotelNameImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
    }

    public void showCountryFragment(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        countryFragment = CountryFragment.newInstance();
        fragmentTransaction.replace(R.id.linearLayout,countryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        findViewById(R.id.backButton).setVisibility(View.VISIBLE);
    }

    public void showCityFragment(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        cityFragment = CityFragment.newInstance();
        fragmentTransaction.replace(R.id.linearLayout,cityFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        findViewById(R.id.backButton).setVisibility(View.VISIBLE);
    }

    public void showFoodFragment(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        foodFragment = FoodFragment.newInstance();
        fragmentTransaction.replace(R.id.linearLayout,foodFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        findViewById(R.id.backButton).setVisibility(View.VISIBLE);
    }

    public void showHotelNameFragment(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hotelNameFragment = HotelNameFragment.newInstance();
        fragmentTransaction.replace(R.id.linearLayout,hotelNameFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        findViewById(R.id.backButton).setVisibility(View.VISIBLE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(this, HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(this, OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(this, OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(this, ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}