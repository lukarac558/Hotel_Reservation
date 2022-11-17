package com.example.db.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.model.Country;
import com.example.db.database.Database;
import com.example.db.R;
import com.example.db.utils.Formatter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CountryFragment extends Fragment {

    private Spinner countrySpinner;
    private EditText countryCodeEditText, countryNameEditText;
    private List<Country> countries;

    public CountryFragment() {
    }

    public static CountryFragment newInstance() {
        return new CountryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ConfigurationActivity configurationActivity = (ConfigurationActivity) getActivity();
        configurationActivity.findViewById(R.id.countryImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.cityImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.foodImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.userImageButton).setVisibility(View.INVISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        countrySpinner = view.findViewById(R.id.selectedCountrySpinner);
        countryCodeEditText = view.findViewById(R.id.countryCodeEditText);
        countryNameEditText = view.findViewById(R.id.countryNameEditText);
        Button addCountryButton = view.findViewById(R.id.addCountryButton);
        Button deleteCountryButton = view.findViewById(R.id.deleteCountryButton);

        countries = Database.getAllCountries();
        Collections.sort(countries);
        Formatter.setAdapter(countrySpinner, getContext(), countries);

        addCountryButton.setOnClickListener(addCountryView -> {

            String code = countryCodeEditText.getText().toString();
            String name = countryNameEditText.getText().toString();

            if (code.length() != 2) {
                Toast.makeText(getContext(), "Kod państwa musi zawierać 2 znaki.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.length() < 4) {
                Toast.makeText(getContext(), "Nazwa państwa musi zawierać co najmniej 4 znaki.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Database.addCountry(new Country(code, name));
                Toast.makeText(getContext(), "Pomyślnie dodano nowe państwo.", Toast.LENGTH_SHORT).show();

                countries = Database.getAllCountries();
                Collections.sort(countries);
                Formatter.setAdapter(countrySpinner, getContext(), countries);
            } catch (SQLException exception) {
                Toast.makeText(getContext(), "Istnieje już państwo o takim kodzie.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteCountryButton.setOnClickListener(deleteCountryView -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrane państwo?");
            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Ok",
                    (dialog, id) -> {
                        Country selectedCountry = (Country) countrySpinner.getSelectedItem();

                        try {
                            Database.deleteCountry(selectedCountry.getCode());
                            Toast.makeText(getContext(), "Pomyślnie usunięto państwo.", Toast.LENGTH_SHORT).show();

                            countries = Database.getAllCountries();
                            Collections.sort(countries);
                            Formatter.setAdapter(countrySpinner, getContext(), countries);
                        } catch (SQLException exception) {
                            Toast.makeText(getContext(), "Usunięcie niemożliwe. Wybrane państwo jest w użyciu.", Toast.LENGTH_SHORT).show();
                        }

                        countries = Database.getAllCountries();
                        Collections.sort(countries);
                        Formatter.setAdapter(countrySpinner, getContext(), countries);
                    });
            alertBuilder.setNegativeButton("Anuluj",
                    (dialog, id) -> dialog.cancel());

            AlertDialog countryAlert = alertBuilder.create();
            countryAlert.show();
        });

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}