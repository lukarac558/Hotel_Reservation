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
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class CityFragment extends Fragment {

    ConfigurationActivity configurationActivity;
    Intent intent;
    Spinner selectedCountriesSpinner;
    Spinner selectedCitiesSpinner;
    EditText cityEditText;
    ArrayAdapter<Country> countryAdapter;
    ArrayAdapter<City> cityAdapter;
    List countryList, cityList;

    public CityFragment() {
    }

    public static CityFragment newInstance() {
        CityFragment fragment = new CityFragment();
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

        View view =  inflater.inflate(R.layout.fragment_city, container, false);
        selectedCountriesSpinner = view.findViewById(R.id.selectedCountriesSpinner);
        selectedCitiesSpinner = view.findViewById(R.id.selectedCitiesSpinner);
        cityEditText = view.findViewById(R.id.cityEditText);
        Button addCityButton = view.findViewById(R.id.addCityButton);
        Button deleteCityButton = view.findViewById(R.id.deleteCityButton);

        countryList = Database.getCountries();
        cityList = Database.getCities();

        Collections.sort(countryList);
        Collections.sort(cityList);

        setCountryAdapter();
        setCityAdapter();

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityEditText.getText().toString();

                if(!cityName.isEmpty()) {
                    Country selectedCountry = (Country) selectedCountriesSpinner.getSelectedItem();
                    short countryId = (short) selectedCountry.getId();

                    City city = new City(0, cityName, countryId);

                    Database.addCity(city);

                    if (!cityList.contains(cityName)) {
                        cityList.add(city);
                        Collections.sort(cityList);
                    }

                    setCityAdapter();
                }
                else
                    Log.d("Empty city name", "Należy wprowadzić nazwę miasta");
            }
        });

        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                City selectedCity = (City) selectedCitiesSpinner.getSelectedItem();

                Database.deleteCity(selectedCity.getName());
                cityList.remove(selectedCity);

                setCityAdapter();
            }
        });

        return view;
    }

    private void setCountryAdapter(){
        countryAdapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedCountriesSpinner.setAdapter(countryAdapter);
    }

    private void setCityAdapter(){
        cityAdapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedCitiesSpinner.setAdapter(cityAdapter);
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