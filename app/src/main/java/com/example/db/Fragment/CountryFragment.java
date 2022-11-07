package com.example.db.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Class.Country;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class CountryFragment extends Fragment {

    private ConfigurationActivity configurationActivity;
    private Intent intent;
    private Spinner selectedCountrySpinner;
    private EditText countryCodeEditText, countryNameEditText;
    private List<Country> allCountriesList;

    public CountryFragment() {
    }

    public static CountryFragment newInstance() {
        return new CountryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        configurationActivity = (ConfigurationActivity)getActivity();
        configurationActivity.findViewById(R.id.countryImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.cityImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.foodImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.userImageButton).setVisibility(View.INVISIBLE);
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        selectedCountrySpinner = view.findViewById(R.id.selectedCountrySpinner);
        countryCodeEditText = view.findViewById(R.id.countryCodeEditText);
        countryNameEditText = view.findViewById(R.id.countryNameEditText);
        Button addCountryButton = view.findViewById(R.id.addCountryButton);
        Button deleteCountryButton = view.findViewById(R.id.deleteCountryButton);

        allCountriesList = Database.getAllCountries();
        Collections.sort(allCountriesList);
        setAdapter();

        addCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = countryCodeEditText.getText().toString();
                String name = countryNameEditText.getText().toString();

                if(code.isEmpty()) {
                    Toast.makeText(getContext(), "Należy wprowadzić kod państwa.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.isEmpty()) {
                    Toast.makeText(getContext(), "Należy wprowadzić nazwę państwa.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Database.addCountry(new Country(code, name));
                    Toast.makeText(getContext(), "Pomyślnie dodano nowe państwo.", Toast.LENGTH_SHORT).show();

                    allCountriesList = Database.getAllCountries();
                    Collections.sort(allCountriesList);
                    setAdapter();
                } catch (SQLException exception) {
                    Toast.makeText(getContext(), "Istnieje już państwo o takim kodzie.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Country selectedCountry = (Country) selectedCountrySpinner.getSelectedItem();

                                try {
                                    Database.deleteCountry(selectedCountry.getCode());
                                    Toast.makeText(getContext(), "Pomyślnie usunięto państwo.", Toast.LENGTH_SHORT).show();

                                    allCountriesList = Database.getAllCountries();
                                    Collections.sort(allCountriesList);
                                    setAdapter();
                                } catch (SQLException exception) {
                                    Toast.makeText(getContext(), "Usunięcie niemożliwe. Wybrane państwo jest w użyciu.", Toast.LENGTH_SHORT).show();
                                }

                                allCountriesList = Database.getAllCountries();
                                Collections.sort(allCountriesList);
                                setAdapter();
                            }
                        });
                alertBuilder.setNegativeButton("Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog countryAlert = alertBuilder.create();
                countryAlert.show();
            }
        });

        return view;
    }

    private void setAdapter(){
        ArrayAdapter<Country> adapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, allCountriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedCountrySpinner.setAdapter(adapter);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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