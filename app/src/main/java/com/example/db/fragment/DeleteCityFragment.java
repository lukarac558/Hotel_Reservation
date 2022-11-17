package com.example.db.fragment;

import android.app.AlertDialog;
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

public class DeleteCityFragment extends Fragment {

    private ConfigurationActivity configurationActivity;
    private Spinner citySpinner;
    private List<City> cities;

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

        Button deleteCityButton = view.findViewById(R.id.dcDeleteCityButton);
        Spinner countrySpinner = view.findViewById(R.id.dcSelectedCountriesSpinner);
        citySpinner = view.findViewById(R.id.dcSelectedCitiesSpinner);

        List<Country> countries = Database.getCountriesInOffer();

        Collections.sort(countries);
        Formatter.setAdapter(countrySpinner, configurationActivity.getApplicationContext(), countries);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getSelectedItem();

                cities = Database.getCitiesByCountryCode(country.getCode());
                Collections.sort(cities);
                Formatter.setAdapter(citySpinner, configurationActivity.getApplicationContext(), cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deleteCityButton.setOnClickListener(deleteCityView -> deleteCity());

        return view;
    }

    private void deleteCity() {
            City selectedCity = (City) citySpinner.getSelectedItem();

            if (selectedCity == null) {
                showMessage("Należy wybrać miasto do usunięcia", "Empty city name");
                return;
            }

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrane miasto?");
            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Ok",
                    (dialog, id) -> {
                        try {
                            Database.deleteCity(selectedCity);
                            showMessage("Pomyślnie usunięto wybrane miasto.", "City deleted");
                            cities = Database.getCitiesByCountryCode(selectedCity.getCountryCode());
                            Collections.sort(cities);
                            Formatter.setAdapter(citySpinner, configurationActivity.getApplicationContext(), cities);
                        } catch (SQLException exception) {
                            showMessage("Usunięcie niemożliwe. Wybrane miasto jest w użyciu.", "City in use");
                        }
                    });
            alertBuilder.setNegativeButton("Anuluj",
                    (dialog, id) -> dialog.cancel());

            AlertDialog orderAlert = alertBuilder.create();
            orderAlert.show();
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
        return super.onOptionsItemSelected(item);
    }
}