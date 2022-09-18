package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.example.db.R;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Class.Food;
import com.example.db.Database.Database;

public class SearchEngineActivity extends AppCompatActivity {

    Intent intent;
    MultiSpinnerSearch countrySpinner;
    MultiSpinnerSearch citySpinner;
    MultiSpinnerSearch foodSpinner;
    NumberPicker peopleCountNumberPicker;
    RangeSlider priceRangeSlider;
    RadioGroup starsRadioGroup;
    List<Country> countryList;
    List<Country> selectedCountriesList = new ArrayList<>();
    List<KeyPairBoolData> countryListConverter;
    List<City> cityList;
    List<City> selectedCitiesList = new ArrayList<>();
    List<KeyPairBoolData> cityListConverter;
    List<Food> foodList;
    List<Food> selectedFoodTypesList = new ArrayList<>();
    List<KeyPairBoolData> foodListConverter;
    TextView startDateTextView;
    TextView endDateTextView;
    DatePickerDialog.OnDateSetListener startDateSetListener;
    DatePickerDialog.OnDateSetListener endDateSetListener;
    LocalDate startDate;
    LocalDate endDate;
    byte starsCount = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_engine);

        Button filterButton = findViewById(R.id.filterButton);
        countrySpinner = findViewById(R.id.countrySpinner);
        citySpinner = findViewById(R.id.citySpinner);
        foodSpinner = findViewById(R.id.foodSpinner);
        peopleCountNumberPicker = findViewById(R.id.peopleCountNumberPicker);
        priceRangeSlider = findViewById(R.id.priceRangeSlider);
        starsRadioGroup = findViewById(R.id.starsRadioGroup);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);

        setCountries();
        countrySpinner.setItems(countryListConverter, items -> {
        });

        setCities();
        citySpinner.setItems(cityListConverter, items -> {
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String countries = (String) adapterView.getSelectedItem();

                cityList.clear();

                if (countries.equals("Wybierz państwo"))
                    cityList = Database.getCities();
                else if (countries.contains(",")) {
                    String[] citiesArray = countries.split(", ");

                    for (String country : citiesArray) {
                        short countryId = Database.getCountryIdByName(country);
                        cityList.addAll(Objects.requireNonNull(Database.getCitiesByCountryId(countryId)));
                    }
                } else {
                    short countryId = Database.getCountryIdByName(countries);
                    cityList.addAll(Objects.requireNonNull(Database.getCitiesByCountryId(countryId)));
                }

                cityListConverter.clear();

                for (City c : cityList) {
                    KeyPairBoolData k = new KeyPairBoolData();
                    k.setId(c.getId());
                    k.setName(c.getName());
                    k.setSelected(false);
                    cityListConverter.add(k);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setFoodTypes();
        foodSpinner.setItems(foodListConverter, items -> {
        });

        peopleCountNumberPicker.setMinValue(1);
        peopleCountNumberPicker.setMaxValue(8);

        priceRangeSlider.setValueTo(20000.0f);
        priceRangeSlider.setValues(0.0f, 20000.0f);
        priceRangeSlider.setLabelBehavior(LabelFormatter.LABEL_VISIBLE);

        startDateTextView.setOnClickListener(view -> showCalendar(startDateSetListener));

        startDateSetListener = (datePicker, year, month, day) -> setStartDate(startDateTextView, year, month, day);


        endDateTextView.setOnClickListener(view -> showCalendar(endDateSetListener));

        endDateSetListener = (datePicker, year, month, day) -> setEndDate(endDateTextView, year, month, day);

        starsRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.twoStarsRadioButton) {
                starsCount = 2;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.threeStarsRadioButton) {
                starsCount = 3;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.fourStarsRadioButton) {
                starsCount = 4;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.fiveStarsRadioButton) {
                starsCount = 5;
            }
        });

        filterButton.setOnClickListener(view -> filter());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void filter() {
        AsyncTask.execute(() -> {
            short peopleCount = (short) peopleCountNumberPicker.getValue();

            List<Float> minMaxValues = priceRangeSlider.getValues();
            double minPrice = minMaxValues.get(0);
            double maxPrice = minMaxValues.get(1);

            setSelectedCountries();
            setSelectedCities();
            setSelectedFoodTypes();

            LocalDate dateNow = LocalDate.now();

            if (startDate == null)
                startDate = dateNow;

            if (endDate == null)
                endDate = dateNow.plusYears(2);

            if (startDate.isAfter(endDate)) {
                Toast.makeText(this, "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu", Toast.LENGTH_SHORT).show();
                Log.d("Date error", "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu");
                return;
            }

            //List<Offer> offerList = Database.filterOffers(peopleCount, minPrice, maxPrice, startDate, endDate, selectedCountriesList, selectedCitiesList, selectedFoodTypesList, starsCount);
            List<Integer> offersIdsList = Database.filterOffersIds(peopleCount, minPrice, maxPrice, startDate, endDate, selectedCountriesList, selectedCitiesList, selectedFoodTypesList, starsCount);
            //getOfferById

            if (offersIdsList != null && offersIdsList.size() > 0) {
                Intent intent = new Intent(getApplicationContext(), FilteredOffersActivity.class);
                ArrayList<Integer> adapterList = new ArrayList<>(offersIdsList);
                intent.putExtra("offerList", adapterList);
                intent.putExtra("peopleCount", peopleCount);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Nie znaleziono żadnych ofert. Zmień kryteria.", Toast.LENGTH_SHORT).show();
                Log.d("Searching error", "Nie znaleziono żadnych ofert. Zmień kryteria.");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (Database.permission.equalsIgnoreCase("user")) {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem login_item = menu.findItem(R.id.login);
            login_item.setVisible(false);
        } else {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem logout_item = menu.findItem(R.id.uLogout);
            logout_item.setVisible(false);
            MenuItem showOrders_item = menu.findItem(R.id.uShowOrders);
            showOrders_item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (Database.permission.equalsIgnoreCase("user")) {

            if (id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if (id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if (id == R.id.uShowOrders)
                intent = new Intent(this, OrdersActivity.class);
            else if (id == R.id.uLogout) {
                Database.logOut();
                intent = new Intent(this, LoginActivity.class);
            }
        } else {

            if (id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if (id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if (id == R.id.login)
                intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }

    private void setCountries() {
        countrySpinner.setSearchHint("Wpisz nazwę docelowego państwa");
        countrySpinner.setHintText("Wybierz państwo");
        countrySpinner.setEmptyTitle("Nie znaleziono takiego państwa");
        countrySpinner.setShowSelectAllButton(true);

        countryList = Database.getCountries();
        countryListConverter = new ArrayList<>();

        for (Country c : countryList) {
            KeyPairBoolData k = new KeyPairBoolData();
            k.setId(c.getId());
            k.setName(c.getName());
            k.setSelected(false);
            countryListConverter.add(k);
        }
    }

    private void setSelectedCountries() {
        if (selectedCountriesList.size() > 0)
            selectedCountriesList.clear();

        List<KeyPairBoolData> list = countrySpinner.getSelectedItems();

        if (list.size() > 0) {
            for (KeyPairBoolData k : list) {
                int id = (int) k.getId();
                String name = k.getName();
                selectedCountriesList.add(new Country(id, name));
            }
        } else
            selectedCountriesList.addAll(countryList);
    }

    private void setCities() {
        citySpinner.setSearchHint("Wpisz nazwę docelowego miasta");
        citySpinner.setHintText("Wybierz miasto");
        citySpinner.setEmptyTitle("Nie znaleziono takiego miasta");
        citySpinner.setShowSelectAllButton(true);

        cityList = Database.getCities();
        cityListConverter = new ArrayList<>();

        for (City c : cityList) {
            KeyPairBoolData k = new KeyPairBoolData();
            k.setId(c.getId());
            k.setName(c.getName());
            k.setSelected(false);
            cityListConverter.add(k);
        }
    }

    private void setSelectedCities() {
        if (selectedCitiesList != null)
            selectedCitiesList.clear();

        List<KeyPairBoolData> list = citySpinner.getSelectedItems();

        if (list.size() > 0) {
            for (KeyPairBoolData k : list) {
                int id = (int) k.getId();
                String name = k.getName();
                selectedCitiesList.add(new City(id, name, (short) 0));
            }
        } else
            selectedCitiesList.addAll(cityList);
    }

    private void setFoodTypes() {
        foodSpinner.setSearchHint("Wpisz nazwę docelowego wyżywienia");
        foodSpinner.setHintText("Wybierz typ wyżywienia");
        foodSpinner.setEmptyTitle("Nie znaleziono takiego typu wyżywienia");
        foodSpinner.setShowSelectAllButton(true);

        foodList = Database.getFoodTypes();
        foodListConverter = new ArrayList<>();

        for (Food f : foodList) {
            KeyPairBoolData k = new KeyPairBoolData();
            k.setId(f.getId());
            k.setName(f.getType());
            k.setSelected(false);
            foodListConverter.add(k);
        }
    }

    private void setSelectedFoodTypes() {
        if (selectedFoodTypesList != null)
            selectedFoodTypesList.clear();

        List<KeyPairBoolData> list = foodSpinner.getSelectedItems();

        if (list.size() > 0) {
            for (KeyPairBoolData k : list) {
                int id = (int) k.getId();
                String type = k.getName();
                selectedFoodTypesList.add(new Food(id, type));
            }
        } else
            selectedFoodTypesList.addAll(foodList);
    }

    private void showCalendar(DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SearchEngineActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                listener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setStartDate(TextView textView, int year, int month, int day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        month++;

        String stringDate = year + "-";
        char zero = '0';

        if (month < 10)
            stringDate += zero;

        stringDate += month + "-";

        if (day < 10)
            stringDate += zero;

        stringDate += day;

        startDate = LocalDate.parse(stringDate, formatter);

        textView.setText(stringDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEndDate(TextView textView, int year, int month, int day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        month++;

        String stringDate = year + "-";
        char zero = '0';

        if (month < 10)
            stringDate += zero;

        stringDate += month + "-";

        if (day < 10)
            stringDate += zero;

        stringDate += day;

        endDate = LocalDate.parse(stringDate, formatter);

        textView.setText(stringDate);
    }

}