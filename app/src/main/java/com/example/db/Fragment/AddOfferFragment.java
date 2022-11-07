package com.example.db.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.example.db.Class.City;
import com.example.db.Class.Country;
import com.example.db.Class.Food;
import com.example.db.Class.Offer;
import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Class.Regex;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class AddOfferFragment extends Fragment {

    private OffersActivity offersActivity;
    private Intent intent;
    private Spinner oCountrySpinner, oCitySpinner, oHotelNameSpinner, oFoodSpinner;
    private EditText oPriceEditText;
    private TextView oStartDateTextView;
    private TextView oEndDateTextView;
    private LocalDate oStartDate;
    private LocalDate oEndDate;
    private DatePickerDialog.OnDateSetListener oStartDateSetListener;
    private DatePickerDialog.OnDateSetListener oEndDateSetListener;
    private NumberPicker oPlacesNumberNumberPicker;
    private List<Country> countryList;
    private List<City> cityList;
    private List<String> hotelNameList;
    private List<Food> foodList;

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

        oCountrySpinner = view.findViewById(R.id.oCountrySpinner);
        oCitySpinner = view.findViewById(R.id.oCitySpinner);
        oHotelNameSpinner = view.findViewById(R.id.oHotelNameSpinner);
        oFoodSpinner = view.findViewById(R.id.oFoodSpinner);
        oPriceEditText = view.findViewById(R.id.oPriceEditText);
        oStartDateTextView = view.findViewById(R.id.oStartDateTextView);
        oEndDateTextView = view.findViewById(R.id.oEndDateTextView);
        oPlacesNumberNumberPicker = view.findViewById(R.id.oPlacesNumberNumberPicker);
        Button oAddOfferButton = view.findViewById(R.id.oAddOfferButton);

        countryList = Database.getCountriesInOffer();
        foodList = Database.getFoodTypes();

        setCountryAdapter();
        setFoodAdapter();

        oPlacesNumberNumberPicker.setMinValue(1);
        oPlacesNumberNumberPicker.setMaxValue(200);

        oCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        oCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) adapterView.getSelectedItem();

                hotelNameList = Database.getHotelNameByCityId(city.getId());
                Collections.sort(hotelNameList);
                setHotelNameAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        oStartDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar(oStartDateSetListener);
            }
        });

        oStartDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                setStartDate(oStartDateTextView, year, month, day);
            }
        };

        oEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar(oEndDateSetListener);
            }
        });

        oEndDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                setEndDate(oEndDateTextView, year, month, day);
            }
        };

        oPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = oPriceEditText.getText().toString();
                if (str.isEmpty()) return;
                String str2 = Regex.decimalPrecision(str, 5, 2);

                if (!str2.equals(str)) {
                    oPriceEditText.setText(str2);
                    oPriceEditText.setSelection(str2.length());
                }
            }
        });

        oAddOfferButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Country selectedCountry = (Country) oCountrySpinner.getSelectedItem();
                String selectedCity = oCitySpinner.getSelectedItem().toString();
                String selectedHotelName = oHotelNameSpinner.getSelectedItem().toString();
                String selectedFood = oFoodSpinner.getSelectedItem().toString();


                if (oPriceEditText.getText().toString().isEmpty()) {
                    Log.d("Price is empty", "Należy wprowadzić cenę za osobę");
                    Toast.makeText(getContext(), "Należy wprowadzić cenę za osobę", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (oStartDate == null || oEndDate == null) {
                    Log.d("Date is empty", "Należy wybrać początek i koniec oferty");
                    Toast.makeText(getContext(), "Należy wybrać początek i koniec oferty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (oStartDate.isAfter(oEndDate)) {
                    Log.d("Date error", "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu");
                    Toast.makeText(getContext(), "Początek musi mieć mniejszą wartość niż koniec zakresu dat urlopu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedCountry.toString().isEmpty()){
                    Log.d("Country is empty", "Należy wybrać państwo");
                    Toast.makeText(getContext(), "Należy wybrać państwo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedCity.isEmpty()){
                    Log.d("City is empty", "Należy wybrać miasto");
                    Toast.makeText(getContext(), "Należy wybrać miasto", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedHotelName.isEmpty()){
                    Log.d("Hotel name is empty", "Należy wybrać nazwę hotelu");
                    Toast.makeText(getContext(), "Należy wybrać nazwę hotelu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedFood.isEmpty()){
                    Log.d("Food is empty", "Należy wybrać typ wyżywienia");
                    Toast.makeText(getContext(), "Należy wybrać typ wyżywienia", Toast.LENGTH_SHORT).show();
                    return;
                }

                int hotelId = Database.getHotelByParams(selectedCountry.getName(),selectedCity,selectedHotelName);
                int fooId = Database.getFoodIdByType(selectedFood);

                short placesNumber = (short) oPlacesNumberNumberPicker.getValue();
                double price = Double.parseDouble(oPriceEditText.getText().toString());

                if (price > 20000.00) {
                    Toast.makeText(getContext(), "Maksymalna cena za osobę wynosi 20000.00zł", Toast.LENGTH_SHORT).show();
                    return;
                }

                Offer offer = new Offer(placesNumber, price, oStartDate, oEndDate, hotelId, fooId);

                Database.addOffer(offer);
                Toast.makeText(getContext(), "Pomyślnie dodano ofertę", Toast.LENGTH_SHORT).show();
                backToPanel();
            }
        });

        return view;
    }

    private void backToPanel() {
        Intent intent = new Intent(offersActivity.getApplicationContext(), OffersActivity.class);
        startActivity(intent);
    }

    private void setCountryAdapter() {
        ArrayAdapter<Country> countryAdapter = new ArrayAdapter<>(offersActivity.getApplicationContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oCountrySpinner.setAdapter(countryAdapter);
    }

    private void setFoodAdapter() {
        ArrayAdapter<Food> foodSpinner = new ArrayAdapter<>(offersActivity.getApplicationContext(), android.R.layout.simple_spinner_item, foodList);
        foodSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oFoodSpinner.setAdapter(foodSpinner);
    }

    private void setCityAdapter() {
        ArrayAdapter<City> citySpinner = new ArrayAdapter<>(offersActivity.getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
        citySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oCitySpinner.setAdapter(citySpinner);
    }

    private void setHotelNameAdapter() {
        ArrayAdapter<String> hotelNameSpinner = new ArrayAdapter<>(offersActivity.getApplicationContext(), android.R.layout.simple_spinner_item, hotelNameList);
        hotelNameSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oHotelNameSpinner.setAdapter(hotelNameSpinner);
    }

    private void showCalendar(DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                listener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }


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

        oStartDate = LocalDate.parse(stringDate, formatter);

        textView.setText(stringDate);
    }


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

        oEndDate = LocalDate.parse(stringDate, formatter);

        textView.setText(stringDate);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(offersActivity.getApplicationContext(), HotelsActivity.class);
        else if (id == R.id.showOffers)
            intent = new Intent(offersActivity.getApplicationContext(), OffersActivity.class);
        else if (id == R.id.aShowOrders)
            intent = new Intent(offersActivity.getApplicationContext(), OrdersActivity.class);
        else if (id == R.id.showConfiguration)
            intent = new Intent(offersActivity.getApplicationContext(), ConfigurationActivity.class);
        else if (id == R.id.aLogout) {
            Database.logOut();
            intent = new Intent(offersActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}