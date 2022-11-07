package com.example.db.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Class.Hotel;
import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class AddHotelFragment extends Fragment {

    private HotelsActivity hotelsActivity;
    private Intent intent;
    private Spinner hAllCountriesSpinner;
    private Spinner hAllCitiesSpinner;
    private ImageView hotelImageView;
    private EditText descriptionEditText, hotelNameEditText;
    private Bitmap bitmap;
    private Uri imageUrl;
    private short starsCount = 1;
    private List<Country> countryList;
    private List<City> cityList;

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
        Button addHotelButton = view.findViewById(R.id.addHotelButton);
        ImageButton addCountryImageButton = view.findViewById(R.id.addCountryImageButton);
        ImageButton addCityImageButton = view.findViewById(R.id.addCityImageButton);
        hotelNameEditText = view.findViewById(R.id.hHotelNameEditText);
        hAllCountriesSpinner = view.findViewById(R.id.hAllCountriesSpinner);
        hAllCitiesSpinner = view.findViewById(R.id.hAllCitiesSpinner);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        RadioGroup hStarsRadioGroup = view.findViewById(R.id.hStarsRadioGroup);

        countryList = Database.getCountriesInOffer();

        Collections.sort(countryList);

        setCountryAdapter();

        hAllCountriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        view.findViewById(R.id.hOneStarRadioButton).setSelected(true);
        hStarsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.hOneStarRadioButton) {
                    starsCount = 1;
                }
                if (radioGroup.getCheckedRadioButtonId() == R.id.hTwoStarsRadioButton) {
                    starsCount = 2;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.hThreeStarsRadioButton) {
                    starsCount = 3;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.hFourStarsRadioButton) {
                    starsCount = 4;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.hFiveStarsRadioButton) {
                    starsCount = 5;
                }
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Wybierz zdjęcie"), 1);
            }
        });

        addHotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHotel();
            }
        });

        addCountryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConfiguration();
            }
        });

        addCityImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConfiguration();
            }
        });

        return view;
    }

    private void goToConfiguration() {
        Intent intent = new Intent(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
            imageUrl = Objects.requireNonNull(data).getData();

        try {
            //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUrl);
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUrl);
            hotelImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHotel() {
        AsyncTask.execute(() -> {

            if (countryList.size() == 0) {
                showMessage("Należy dodać państwa do bazy.", "There is no country");
                return;
            }

            if (cityList.size() == 0) {
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

            City selectedCity = (City) hAllCitiesSpinner.getSelectedItem();
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
        Intent intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        startActivity(intent);
    }


    private void setCountryAdapter() {
        ArrayAdapter<Country> countryAdapter = new ArrayAdapter<>(hotelsActivity.getApplicationContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hAllCountriesSpinner.setAdapter(countryAdapter);
    }

    private void setCityAdapter() {
        ArrayAdapter<City> cityAdapter = new ArrayAdapter<>(hotelsActivity.getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hAllCitiesSpinner.setAdapter(cityAdapter);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        else if (id == R.id.showOffers)
            intent = new Intent(hotelsActivity.getApplicationContext(), OffersActivity.class);
        else if (id == R.id.aShowOrders)
            intent = new Intent(hotelsActivity.getApplicationContext(), OrdersActivity.class);
        else if (id == R.id.showConfiguration)
            intent = new Intent(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
        else if (id == R.id.aLogout) {
            Database.logOut();
            intent = new Intent(hotelsActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}