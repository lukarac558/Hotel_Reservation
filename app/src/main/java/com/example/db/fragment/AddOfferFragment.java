package com.example.db.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.db.activity.OffersActivity;
import com.example.db.model.City;
import com.example.db.model.Country;
import com.example.db.model.Food;
import com.example.db.model.Offer;
import com.example.db.utils.Formatter;
import com.example.db.database.Database;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class AddOfferFragment extends Fragment {

    private OffersActivity offersActivity;
    private Spinner countrySpinner, citySpinner, hotelNameSpinner, foodSpinner;
    private EditText priceEditText;
    private TextView startDateTextView, endDateTextView;
    private LocalDate startDate, endDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener, endDateSetListener;
    private NumberPicker placesNumberNumberPicker;
    private List<City> cities;
    private List<String> hotelNames;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AddOfferFragment() {
    }

    public static AddOfferFragment newInstance() {
        return new AddOfferFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        offersActivity = (OffersActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_offer, container, false);

        countrySpinner = view.findViewById(R.id.oCountrySpinner);
        citySpinner = view.findViewById(R.id.oCitySpinner);
        hotelNameSpinner = view.findViewById(R.id.oHotelNameSpinner);
        foodSpinner = view.findViewById(R.id.oFoodSpinner);
        priceEditText = view.findViewById(R.id.oPriceEditText);
        startDateTextView = view.findViewById(R.id.oStartDateTextView);
        endDateTextView = view.findViewById(R.id.oEndDateTextView);
        placesNumberNumberPicker = view.findViewById(R.id.oPlacesNumberNumberPicker);
        Button addOfferButton = view.findViewById(R.id.oAddOfferButton);

        List<Country> countries = Database.getCountriesInOffer();
        List<Food> foodTypes = Database.getFoodTypes();

        Formatter.setAdapter(countrySpinner, offersActivity.getApplicationContext(), countries);
        Formatter.setAdapter(foodSpinner, offersActivity.getApplicationContext(), foodTypes);

        placesNumberNumberPicker.setMinValue(1);
        placesNumberNumberPicker.setMaxValue(200);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getSelectedItem();
                cities = Database.getCitiesByCountryCode(country.getCode());
                Collections.sort(cities);
                Formatter.setAdapter(citySpinner, offersActivity.getApplicationContext(), cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) adapterView.getSelectedItem();
                hotelNames = Database.getHotelNameByCityId(city.getId());
                Collections.sort(hotelNames);
                Formatter.setAdapter(hotelNameSpinner, offersActivity.getApplicationContext(), hotelNames);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        startDateTextView.setOnClickListener(startDateView -> WindowDirector.showCalendar(startDateSetListener, getActivity()));

        startDateSetListener = (datePicker, year, month, day) -> {
            String stringDate = Formatter.getFormattedDate(year, month, day);
            startDate = LocalDate.parse(stringDate, formatter);
            startDateTextView.setText(stringDate);
        };

        endDateTextView.setOnClickListener(endDateView -> WindowDirector.showCalendar(endDateSetListener, getActivity()));

        endDateSetListener = (datePicker, year, month, day) -> {
            String stringDate = Formatter.getFormattedDate(year, month, day);
            endDate = LocalDate.parse(stringDate, formatter);
            endDateTextView.setText(stringDate);
        };

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = priceEditText.getText().toString();
                if (str.isEmpty()) return;
                String str2 = Formatter.getCorrectDecimalPrecision(str, 5, 2);

                if (!str2.equals(str)) {
                    priceEditText.setText(str2);
                    priceEditText.setSelection(str2.length());
                }
            }
        });

        addOfferButton.setOnClickListener(addOfferView -> {
            Country selectedCountry = (Country) countrySpinner.getSelectedItem();
            String selectedCity = "", selectedHotelName = "", selectedFood = "";

            if (citySpinner.getSelectedItem() != null) {
                selectedCity = citySpinner.getSelectedItem().toString();
            }

            if (hotelNameSpinner.getSelectedItem() != null) {
                selectedHotelName = hotelNameSpinner.getSelectedItem().toString();
            }

            if (foodSpinner.getSelectedItem() != null) {
                selectedFood = foodSpinner.getSelectedItem().toString();
            }


            if (priceEditText.getText().toString().isEmpty()) {
                Log.d("Price is empty", "Należy wprowadzić cenę za osobę");
                Toast.makeText(getContext(), "Należy wprowadzić cenę za osobę", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startDate == null || endDate == null) {
                Log.d("Date is empty", "Należy wybrać początek i koniec oferty");
                Toast.makeText(getContext(), "Należy wybrać początek i koniec oferty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startDate.isAfter(endDate)) {
                Log.d("Date error", "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu");
                Toast.makeText(getContext(), "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedCountry.toString().isEmpty()) {
                Log.d("Country is empty", "Należy wybrać państwo");
                Toast.makeText(getContext(), "Należy wybrać państwo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedCity.isEmpty()) {
                Log.d("City is empty", "Należy wybrać miasto");
                Toast.makeText(getContext(), "Należy wybrać miasto", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedHotelName.isEmpty()) {
                Log.d("Hotel name is empty", "Należy wybrać nazwę hotelu");
                Toast.makeText(getContext(), "Należy wybrać nazwę hotelu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedFood.isEmpty()) {
                Log.d("Food is empty", "Należy wybrać typ wyżywienia");
                Toast.makeText(getContext(), "Należy wybrać typ wyżywienia", Toast.LENGTH_SHORT).show();
                return;
            }

            int hotelId = Database.getHotelIdByParams(selectedCountry.getName(), selectedCity, selectedHotelName);
            int fooId = Database.getFoodIdByType(selectedFood);

            short placesNumber = (short) placesNumberNumberPicker.getValue();
            double price = Double.parseDouble(priceEditText.getText().toString());

            if (price > 20000.00) {
                Toast.makeText(getContext(), "Maksymalna cena za osobę wynosi 20000.00zł", Toast.LENGTH_SHORT).show();
                return;
            }

            Offer offer = new Offer(placesNumber, price, startDate, endDate, hotelId, fooId);

            Database.addOffer(offer);
            Toast.makeText(getContext(), "Pomyślnie dodano ofertę", Toast.LENGTH_SHORT).show();
            backToPanel();
        });

        return view;
    }

    private void backToPanel() {
        WindowDirector.changeActivity(offersActivity.getApplicationContext(), OffersActivity.class);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}