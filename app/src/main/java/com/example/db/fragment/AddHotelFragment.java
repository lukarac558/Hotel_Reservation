package com.example.db.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.activity.HotelsActivity;
import com.example.db.model.City;
import com.example.db.model.Country;
import com.example.db.model.Hotel;
import com.example.db.database.Database;
import com.example.db.utils.Formatter;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AddHotelFragment extends Fragment {

    private HotelsActivity hotelsActivity;
    private Spinner citySpinner;
    private ImageView hotelImageView;
    private EditText descriptionEditText, hotelNameEditText;
    private Bitmap bitmap;
    private Uri imageUrl;
    private short starsCount = 1;
    private List<Country> countries;
    private List<City> cities;

    public AddHotelFragment() {
    }

    public static AddHotelFragment newInstance() {
        return new AddHotelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        hotelsActivity = (HotelsActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_hotel, container, false);

        hotelImageView = view.findViewById(R.id.hotelImageView);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button addHotelButton = view.findViewById(R.id.adAddHotelButton);
        ImageButton addCountryImageButton = view.findViewById(R.id.addCountryImageButton);
        ImageButton addCityImageButton = view.findViewById(R.id.addCityImageButton);
        hotelNameEditText = view.findViewById(R.id.hHotelNameEditText);
        Spinner countrySpinner = view.findViewById(R.id.hAllCountriesSpinner);
        citySpinner = view.findViewById(R.id.hAllCitiesSpinner);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        RadioGroup hStarsRadioGroup = view.findViewById(R.id.hStarsRadioGroup);

        countries = Database.getCountriesInOffer();
        Collections.sort(countries);
        Formatter.setAdapter(countrySpinner, hotelsActivity.getApplicationContext(), countries);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getSelectedItem();

                cities = Database.getCitiesByCountryCode(country.getCode());
                Collections.sort(cities);
                Formatter.setAdapter(citySpinner, hotelsActivity.getApplicationContext(), cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        view.findViewById(R.id.hOneStarRadioButton).setSelected(true);
        hStarsRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.hOneStarRadioButton) {
                starsCount = 1;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.hTwoStarsRadioButton) {
                starsCount = 2;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.hThreeStarsRadioButton) {
                starsCount = 3;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.hFourStarsRadioButton) {
                starsCount = 4;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.hFiveStarsRadioButton) {
                starsCount = 5;
            }
        });

        selectImageButton.setOnClickListener(selectImageView -> {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Wybierz zdjęcie"), 1);
        });

        addHotelButton.setOnClickListener(addHotelView -> addHotel());

        addCountryImageButton.setOnClickListener(addCountryView -> goToConfiguration());

        addCityImageButton.setOnClickListener(addCityView -> goToConfiguration());

        return view;
    }

    private void goToConfiguration() {
        WindowDirector.changeActivity(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
            imageUrl = Objects.requireNonNull(data).getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUrl);
            hotelImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHotel() {
        AsyncTask.execute(() -> {
            if (countries.size() == 0) {
                showMessage("Należy dodać państwa do bazy.", "There is no country");
                return;
            }

            if (cities.size() == 0) {
                showMessage("Należy dodać najpierw miasta do bazy.", "There is no city");
                return;
            }

            if (hotelNameEditText.getText().toString().isEmpty()) {
                showMessage("Należy dodać najpierw wprowadzić nazwę hotelu.", "There is no hn");
                return;
            }

            if (bitmap == null) {
                showMessage("Należy wybrać zdjęcie hotelu", "Bitmap is null");
                return;
            }

            City selectedCity = (City) citySpinner.getSelectedItem();
            String hotelName = hotelNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            int cityId = Database.getCityIdByName(selectedCity.getName());

            Hotel hotel = new Hotel(cityId, starsCount, description, hotelName);

            try {
                Database.addHotel(hotel, bitmap);
                showMessage("Pomyślnie utworzono hotel.", "Hotel added");
            } catch (SQLException exception) {
                showMessage("Istnieje już hotel o takiej kombinacji państwa, miasta i nazwy.", "Hotel in use");
            }

            backToPanel();
        });
    }

    private void showMessage(String messageDescription, String logTag) {
        hotelsActivity.runOnUiThread(() -> {
            Toast.makeText(hotelsActivity.getApplicationContext(), messageDescription, Toast.LENGTH_LONG).show();
            Log.d(logTag, messageDescription);
        });
    }

    private void backToPanel() {
        WindowDirector.changeActivity(hotelsActivity.getApplicationContext(), HotelsActivity.class);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}