package com.example.db.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.model.City;
import com.example.db.model.Country;
import com.example.db.database.Database;
import com.example.db.R;
import com.example.db.utils.Formatter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AddCityFragment extends Fragment {

    private ConfigurationActivity configurationActivity;
    private Spinner countriesSpinner;
    private Spinner citiesSpinner;
    private EditText cityEditText;
    private List<City> cities;

    public AddCityFragment() {
    }

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);

        configurationActivity = (ConfigurationActivity) getActivity();
        Button addCityButton = view.findViewById(R.id.acAddCityButton);
        countriesSpinner = view.findViewById(R.id.acCountriesSpinner);
        citiesSpinner = view.findViewById(R.id.acCitiesSpinner);
        cityEditText = view.findViewById(R.id.acCityEditText);

        List<Country> countries = Database.getAllCountries();
        Collections.sort(countries);
        Formatter.setAdapter(countriesSpinner, configurationActivity.getApplicationContext(), countries);

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getSelectedItem();
                cities = Database.getCitiesByCountryCode(country.getCode());
                Collections.sort(cities);
                Formatter.setAdapter(citiesSpinner, configurationActivity.getApplicationContext(), cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addCityButton.setOnClickListener(cityView -> addCity());

        return view;
    }

    private void addCity() {
        String cityName = cityEditText.getText().toString();
        Country selectedCountry = (Country) countriesSpinner.getSelectedItem();

        if (cityName.isEmpty()) {
            showMessage("Należy wprowadzić nazwę miasta", "Empty city name");
            return;
        }

        try {
            Database.addCity(new City(cityName, selectedCountry.getCode()));
            showMessage("Pomyślnie dodano nowe państwo.", "City added");
            cities = Database.getCitiesByCountryCode(selectedCountry.getCode());
            Collections.sort(cities);
            Formatter.setAdapter(citiesSpinner, configurationActivity.getApplicationContext(), cities);
        } catch (SQLException exception) {
            showMessage("Istnieje już takie miasto w zadanym państwie.", "City exists");
        }
    }

    private void showMessage(String messageDescription, String logTag) {
        configurationActivity.runOnUiThread(() -> {
            Toast.makeText(configurationActivity.getApplicationContext(), messageDescription, Toast.LENGTH_LONG).show();
            Log.d(logTag, messageDescription);
        });
    }


    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}