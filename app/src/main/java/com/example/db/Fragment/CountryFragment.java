package com.example.db.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class CountryFragment extends Fragment {

    ConfigurationActivity configurationActivity;
    Intent intent;
    Spinner allCountriesSpinner;
    ArrayAdapter<String> adapter;
    List countryList;
    List allCountriesList;
    List<String> stringCountryList;

    public CountryFragment() {
    }

    public static CountryFragment newInstance() {
        CountryFragment fragment = new CountryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        configurationActivity = (ConfigurationActivity)getActivity();
        configurationActivity.findViewById(R.id.countryImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.cityImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.foodImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.hotelNameImageButton).setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        allCountriesSpinner = view.findViewById(R.id.allCountriesSpinner);
        Button addCountryButton = view.findViewById(R.id.addCountryButton);
        Button deleteCountryButton = view.findViewById(R.id.deleteCountryButton);

        countryList = Database.getCountries();

        stringCountryList  = (List<String>) countryList.stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList());

        allCountriesList = Database.getAllWorldCountries();
        Collections.sort(allCountriesList);

        updateUsedCountries();
        setAdapter();

        addCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCountry = (String) allCountriesSpinner.getSelectedItem();
                if(selectedCountry.contains("[DOSTĘPNY W OFERCIE]"))
                    Log.d("Country used", "Państwo jest już dostępne w ofercie");
                else {
                    Database.addCountry(selectedCountry);
                    stringCountryList.add(selectedCountry);
                }

                updateUsedCountries();
                setAdapter();
            }
        });

        deleteCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCountry = (String) allCountriesSpinner.getSelectedItem();
                String country="";

                if(!selectedCountry.contains("[DOSTĘPNY W OFERCIE]"))
                    Log.d("Country not selected", "Państwo nie jest dostępne w ofercie");
                else {
                    country = selectedCountry.replace(" [DOSTĘPNY W OFERCIE]", "");
                    Database.deleteCountry(country);
                    stringCountryList.remove(country);
                }

                updateUnUsedCountries(country);
                setAdapter();
            }
        });

        return view;
    }

    private void setAdapter(){
        adapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, allCountriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allCountriesSpinner.setAdapter(adapter);
    }

    private void updateUsedCountries(){
        for(String country : stringCountryList){
            if(allCountriesList.contains(country)){
                int index = allCountriesList.indexOf(country);
                allCountriesList.set(index, country + " [DOSTĘPNY W OFERCIE]");
            }
        }
    }

    private void updateUnUsedCountries(String countryName){
        for(Object country : allCountriesList){
            if(allCountriesList.contains(countryName + " [DOSTĘPNY W OFERCIE]") && !stringCountryList.contains(countryName)){
                int index = allCountriesList.indexOf(country);
                String str = country.toString().replace(" [DOSTĘPNY W OFERCIE]", "");
                allCountriesList.set(index, str);
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(configurationActivity.getApplicationContext(), HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(configurationActivity.getApplicationContext(), OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(configurationActivity.getApplicationContext(), OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(configurationActivity.getApplicationContext(), ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(configurationActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}