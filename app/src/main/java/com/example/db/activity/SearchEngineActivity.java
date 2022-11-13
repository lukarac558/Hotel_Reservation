package com.example.db.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.example.db.model.City;
import com.example.db.model.Country;
import com.example.db.model.Food;
import com.example.db.utils.Formatter;
import com.example.db.database.Database;
import com.example.db.utils.MenuDirector;
import com.example.db.R;
import com.example.db.utils.WindowDirector;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class SearchEngineActivity extends AppCompatActivity {

    private MultiSpinnerSearch countrySpinner;
    private MultiSpinnerSearch citySpinner;
    private MultiSpinnerSearch foodSpinner;
    private NumberPicker peopleCountNumberPicker;
    private RangeSlider priceRangeSlider;
    private List<Country> countries;
    private final List<Country> selectedCountries = new ArrayList<>();
    private List<KeyPairBoolData> countriesConverter;
    private Map<Long, String> countryCodes;
    private List<City> cities;
    private final List<City> selectedCities = new ArrayList<>();
    private List<KeyPairBoolData> citiesConverter;
    private List<Food> foodTypes;
    private final List<Food> selectedFoodTypes = new ArrayList<>();
    private List<KeyPairBoolData> foodTypesConverter;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private LocalDate startDate;
    private LocalDate endDate;
    private byte starsCount = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        RadioGroup starsRadioGroup = findViewById(R.id.starsRadioGroup);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);

        setCountrySpinner();
        countrySpinner.setItems(countriesConverter, items -> {
        });

        setCitySpinner();
        citySpinner.setItems(citiesConverter, items -> {
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCountriesString = (String) adapterView.getSelectedItem();
                updateCities(selectedCountriesString);
                updateCitySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        setFoodSpinner();
        foodSpinner.setItems(foodTypesConverter, items -> {
        });

        peopleCountNumberPicker.setMinValue(1);
        peopleCountNumberPicker.setMaxValue(8);

        priceRangeSlider.setValueTo(20000.0f);
        priceRangeSlider.setValues(0.0f, 20000.0f);
        priceRangeSlider.setLabelBehavior(LabelFormatter.LABEL_VISIBLE);

        startDateTextView.setOnClickListener(view -> WindowDirector.showCalendar(startDateSetListener, this));
        startDateSetListener = (datePicker, year, month, day) -> {
            String stringDate = Formatter.getFormattedDate(year, month, day);
            startDate = LocalDate.parse(stringDate, formatter);
            startDateTextView.setText(stringDate);
        };

        endDateTextView.setOnClickListener(view -> WindowDirector.showCalendar(endDateSetListener, this));
        endDateSetListener = (datePicker, year, month, day) -> {
            String stringDate = Formatter.getFormattedDate(year, month, day);
            endDate = LocalDate.parse(stringDate, formatter);
            endDateTextView.setText(stringDate);
        };

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


    public void filter() {
        short peopleCount = (short) peopleCountNumberPicker.getValue();

        List<Float> minMaxValues = priceRangeSlider.getValues();
        double minPrice = minMaxValues.get(0);
        double maxPrice = minMaxValues.get(1);

        setSelectedCountries();
        setSelectedCities();
        setSelectedFoodTypes();

        if (startDate == null) {
            startDate = LocalDate.now();
        }

        if (endDate == null) {
            endDate = LocalDate.now().plusYears(2);
        }

        if (startDate.isAfter(endDate)) {
            Toast.makeText(this, "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu", Toast.LENGTH_SHORT).show();
            Log.d("Date error", "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu");
            return;
        }

        List<Integer> offersIds = Database.filterOffersIds(peopleCount, minPrice, maxPrice, startDate, endDate, selectedCountries, selectedCities, selectedFoodTypes, starsCount);

        if (offersIds.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), FilteredOffersActivity.class);
            intent.putExtra("filteredOffersIds", new ArrayList<>(offersIds));
            intent.putExtra("peopleCount", peopleCount);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nie znaleziono żadnych ofert. Zmień kryteria.", Toast.LENGTH_SHORT).show();
            Log.d("Searching error", "Nie znaleziono żadnych ofert. Zmień kryteria.");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setUserOptionsMenu(Database.userId, this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleUserMenuItemSelected(Database.userId, this, item);
    }

    private void setCountrySpinner() {
        countrySpinner.setSearchHint("Wpisz nazwę docelowego państwa");
        countrySpinner.setHintText("Wybierz państwo");
        countrySpinner.setEmptyTitle("Nie znaleziono takiego państwa");
        countrySpinner.setShowSelectAllButton(true);

        countries = Database.getCountriesInOffer();
        countryCodes = new HashMap<>();
        long key = 1;

        for (Country c : countries) {
            countryCodes.put(key, c.getCode());
            key++;
        }

        countriesConverter = new ArrayList<>();
        fillCountriesConverter();
    }

    private void fillCountriesConverter(){
        for (Country c : countries) {
            KeyPairBoolData k = new KeyPairBoolData();
            k.setId(getKeyByValue(c.getCode()));
            k.setName(c.getName());
            k.setSelected(false);
            countriesConverter.add(k);
        }
    }

    private long getKeyByValue(String code) {
        AtomicLong id = new AtomicLong(0);
        countryCodes.forEach((key, value) -> {
            if (value.equals(code))
                id.set(key);
        });

        return id.get();
    }

    private void setSelectedCountries() {
        if (selectedCountries.size() > 0) {
            selectedCountries.clear();
        }

        List<KeyPairBoolData> list = countrySpinner.getSelectedItems();

        if (list.size() > 0) {
            for (KeyPairBoolData key : list) {
                long id = key.getId();
                String name = key.getName();
                selectedCountries.add(new Country(countryCodes.get(id), name));
            }
        } else {
            selectedCountries.addAll(countries);
        }
    }

    private void updateCities(String selectedCountriesString) {
        cities.clear();

        if (selectedCountriesString.equals("Wybierz państwo")) {
            cities = Database.getAllCities();
        } else if (selectedCountriesString.contains(",")) {
            String[] countriesArray = selectedCountriesString.split(", ");

            for (String country : countriesArray) {
                String countryCode = Database.getCountryCodeByName(country);
                cities.addAll(Database.getCitiesByCountryCode(countryCode));
            }
        } else {
            String countryCode = Database.getCountryCodeByName(selectedCountriesString);
            cities = Database.getCitiesByCountryCode(countryCode);
        }
    }

    private void setCitySpinner() {
        citySpinner.setSearchHint("Wpisz nazwę docelowego miasta");
        citySpinner.setHintText("Wybierz miasto");
        citySpinner.setEmptyTitle("Nie znaleziono takiego miasta");
        citySpinner.setShowSelectAllButton(true);

        cities = Database.getAllCities();
        citiesConverter = new ArrayList<>();
        fillCitiesConverter();
    }

    private void updateCitySpinner(){
        citiesConverter.clear();
        fillCitiesConverter();
        citySpinner.setItems(citiesConverter, items -> {
        });
    }

    private void fillCitiesConverter(){
        for (City city : cities) {
            KeyPairBoolData key = new KeyPairBoolData();
            key.setId(city.getId());
            key.setName(city.getName());
            key.setSelected(false);
            citiesConverter.add(key);
        }
    }

    private void setSelectedCities() {
        if (selectedCities != null) {
            selectedCities.clear();
        }

        List<KeyPairBoolData> list = citySpinner.getSelectedItems();

        if (list.size() > 0) {
            for (KeyPairBoolData key : list) {
                int id = (int) key.getId();
                String name = key.getName();
                Objects.requireNonNull(selectedCities).add(new City(id, name));
            }
        } else {
            Objects.requireNonNull(selectedCities).addAll(cities);
        }
    }

    private void setFoodSpinner() {
        foodSpinner.setSearchHint("Wpisz nazwę docelowego wyżywienia");
        foodSpinner.setHintText("Wybierz typ wyżywienia");
        foodSpinner.setEmptyTitle("Nie znaleziono takiego typu wyżywienia");
        foodSpinner.setShowSelectAllButton(true);

        foodTypes = Database.getFoodTypes();
        foodTypesConverter = new ArrayList<>();
        fillFoodConverter();
    }

    private void fillFoodConverter(){
        for (Food food : foodTypes) {
            KeyPairBoolData key = new KeyPairBoolData();
            key.setId(food.getId());
            key.setName(food.getType());
            key.setSelected(false);
            foodTypesConverter.add(key);
        }
    }

    private void setSelectedFoodTypes() {
        if (selectedFoodTypes != null) {
            selectedFoodTypes.clear();
        }

        List<KeyPairBoolData> list = foodSpinner.getSelectedItems();

        if (list.size() > 0) {
            for (KeyPairBoolData key : list) {
                int id = (int) key.getId();
                String type = key.getName();
                Objects.requireNonNull(selectedFoodTypes).add(new Food(id, type));
            }
        } else {
            Objects.requireNonNull(selectedFoodTypes).addAll(foodTypes);
        }
    }
}