package com.example.db.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Database.Database;
import com.example.db.R;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DeleteCityFragment extends Fragment {

    private Intent intent;
    private ConfigurationActivity configurationActivity;
    private Spinner acSelectedCountriesSpinner;
    private Spinner acSelectedCitiesSpinner;
    private List<Country> countryList;
    private List<City> cityList;

    public DeleteCityFragment() {
    }

    public static DeleteCityFragment newInstance() {
        return new DeleteCityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_city, container, false);
        configurationActivity = (ConfigurationActivity) getActivity();

        Button acDeleteCityButton = view.findViewById(R.id.dcDeleteCityButton);
        acSelectedCountriesSpinner = view.findViewById(R.id.dcSelectedCountriesSpinner);
        acSelectedCitiesSpinner = view.findViewById(R.id.dcSelectedCitiesSpinner);

        countryList = Database.getCountriesInOffer();

        Collections.sort(countryList);
        setCountryAdapter();

        acSelectedCountriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getSelectedItem();

                cityList = Database.getCitiesByCountryCode(country.getCode());
                Collections.sort(cityList);
                setCityAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        acDeleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCity();
            }
        });

        return view;
    }

    private void deleteCity() {
            City selectedCity = (City) acSelectedCitiesSpinner.getSelectedItem();

            if (selectedCity == null) {
                showMessage("Należy wybrać miasto do usunięcia", "Empty city name");
                return;
            }

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setMessage("Czy na pewno chcesz usunąć miasto?");
            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Ok",
                    (dialog, id) -> {
                        try {
                            Database.deleteCity(selectedCity);
                            showMessage("Pomyślnie usunięto wybrane miasto.", "City deleted");
                            cityList = Database.getCitiesByCountryCode(selectedCity.getCountryCode());
                            Collections.sort(cityList);
                            setCityAdapter();
                        } catch (SQLException exception) {
                            showMessage("Usunięcie niemożliwe. Wybrane miasto jest w użyciu.", "City in use");
                        }
                    });
            alertBuilder.setNegativeButton("Anuluj",
                    (dialog, id) -> dialog.cancel());

            AlertDialog orderAlert = alertBuilder.create();
            orderAlert.show();
    }

    private void setCountryAdapter() {
        ArrayAdapter<Country> countryAdapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acSelectedCountriesSpinner.setAdapter(countryAdapter);
    }

    private void setCityAdapter() {
        ArrayAdapter<City> cityAdapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acSelectedCitiesSpinner.setAdapter(cityAdapter);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showMessage(String messageDescription, String logTag) {
        configurationActivity.runOnUiThread(() -> {
            Toast.makeText(configurationActivity.getApplicationContext(), messageDescription, Toast.LENGTH_LONG).show();
            Log.d(logTag, messageDescription);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(configurationActivity.getApplicationContext(), HotelsActivity.class);
        else if (id == R.id.showOffers)
            intent = new Intent(configurationActivity.getApplicationContext(), OffersActivity.class);
        else if (id == R.id.aShowOrders)
            intent = new Intent(configurationActivity.getApplicationContext(), OrdersActivity.class);
        else if (id == R.id.showConfiguration)
            intent = new Intent(configurationActivity.getApplicationContext(), ConfigurationActivity.class);
        else if (id == R.id.aLogout) {
            Database.logOut();
            intent = new Intent(configurationActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}