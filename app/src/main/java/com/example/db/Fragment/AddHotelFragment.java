package com.example.db.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Class.Food;
import com.example.db.Class.Hotel;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Class.HotelName;
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
    private Spinner hAllHotelNamesSpinner;
    private Spinner hAlLFoodTypesSpinner;
    private ImageView hotelImageView;
    private EditText descriptionEditText;
    private RadioGroup hStarsRadioGroup;
    private Bitmap bitmap;
    private Uri imageUrl;
    private short starsCount = 1;
    private ArrayAdapter<Country> countryAdapter;
    private ArrayAdapter<City> cityAdapter;
    private ArrayAdapter<Food> foodAdapter;
    private ArrayAdapter<HotelName> hotelNameAdapter;
    private List countryList, cityList, foodList, hotelNameList;

    public AddHotelFragment() {
    }

    public static AddHotelFragment newInstance() {
        AddHotelFragment fragment = new AddHotelFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        hotelsActivity = (HotelsActivity)getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_hotel, container, false);

        hotelImageView = view.findViewById(R.id.hotelImageView);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button addHotelButton = view.findViewById(R.id.addHotelButton);
        Button configurationButton = view.findViewById(R.id.configurationButton);
        hAllCountriesSpinner = view.findViewById(R.id.hAllCountriesSpinner);
        hAllCitiesSpinner = view.findViewById(R.id.hAllCitiesSpinner);
        hAllHotelNamesSpinner = view.findViewById(R.id.hAllHotelNamesSpinner);
        hAlLFoodTypesSpinner = view.findViewById(R.id.hAlLFoodTypesSpinner);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        hStarsRadioGroup = view.findViewById(R.id.hStarsRadioGroup);

        countryList = Database.getCountries();
        foodList = Database.getFoodTypes();
        hotelNameList = Database.getHotelNames();

        Collections.sort(countryList);
        Collections.sort(foodList);
        Collections.sort(hotelNameList);

        setCountryAdapter();
        setFoodAdapter();
        setHotelNameAdapter();

        hAllCountriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getSelectedItem();
                cityList = Database.getCitiesByCountryId((short)country.getId());
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
                if(radioGroup.getCheckedRadioButtonId() == R.id.hOneStarRadioButton){
                    starsCount = 1;
                }
                if(radioGroup.getCheckedRadioButtonId() == R.id.hTwoStarsRadioButton){
                    starsCount = 2;
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.hThreeStarsRadioButton){
                    starsCount = 3;
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.hFourStarsRadioButton){
                    starsCount = 4;
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.hFiveStarsRadioButton){
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
                startActivityForResult(Intent.createChooser(gallery, "Wybierz zdjęcie"),1);
            }
        });

        addHotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHotel();
            }
        });

        configurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConfiguration();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK)
            imageUrl = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUrl);
                hotelImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void goToConfiguration(){
        Intent intent = new Intent(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
        startActivity(intent);
    }

    private void addHotel(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                if(bitmap == null)
                {
                    showImageError();
                    return;
                }

                String selectedCountry = (String) hAllCountriesSpinner.getSelectedItem();
                String selectedCity = (String) hAllCitiesSpinner.getSelectedItem();
                String selectedFood = (String) hAlLFoodTypesSpinner.getSelectedItem();
                String selectedHotelName = (String) hAllHotelNamesSpinner.getSelectedItem();
                String description = descriptionEditText.getText().toString();

                short countryId = Database.getCountryIdByName(selectedCountry);
                int cityId = Database.getCityIdByName(selectedCity);
                int foodId = Database.getFoodIdByType(selectedFood);
                int hotelNameId = Database.getHotelNameIdByType(selectedHotelName);


                Hotel hotel = new Hotel(0, countryId, cityId, foodId, starsCount, description, hotelNameId);
                Database.addHotel(hotel, bitmap);
                backToPanel();
            }
        });
    }

    private void showImageError(){
        hotelsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(hotelsActivity.getApplicationContext(), "Należy wybrać zdjęcie hotelu", Toast.LENGTH_LONG).show();
                Log.d("Bitmap is null", "Należy wybrać zdjęcie hotelu");
            }
        });
    }

    private void backToPanel(){
        Intent intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        startActivity(intent);
    }


    private void setCountryAdapter(){
        countryAdapter = new ArrayAdapter<>(hotelsActivity.getApplicationContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hAllCountriesSpinner.setAdapter(countryAdapter);
    }

    private void setCityAdapter(){
        cityAdapter = new ArrayAdapter<>(hotelsActivity.getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hAllCitiesSpinner.setAdapter(cityAdapter);
    }

    private void setFoodAdapter(){
        foodAdapter = new ArrayAdapter<>(hotelsActivity.getApplicationContext(), android.R.layout.simple_spinner_item, foodList);
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hAlLFoodTypesSpinner.setAdapter(foodAdapter);
    }

    private void setHotelNameAdapter(){
        hotelNameAdapter = new ArrayAdapter<>(hotelsActivity.getApplicationContext(), android.R.layout.simple_spinner_item, hotelNameList);
        hotelNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hAllHotelNamesSpinner.setAdapter(hotelNameAdapter);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(hotelsActivity.getApplicationContext(), OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(hotelsActivity.getApplicationContext(), OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(hotelsActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}