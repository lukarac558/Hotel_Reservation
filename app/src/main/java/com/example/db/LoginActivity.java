package com.example.db;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Classess.City;
import Classess.Country;
import Classess.Food;
import Classess.Hotel;
import Classess.HotelName;
import Classess.Offer;
import Database.Database;

public class LoginActivity extends AppCompatActivity {

    EditText loginEditText;
    EditText passwordEditText;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.errorTextView);
    }

    public void login(View view){

        errorTextView.clearComposingText();

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (login.isEmpty())
            loginEditText.setError("Należy wprowadzić login");

        if (password.isEmpty())
            loginEditText.setError("Należy wprowadzić hasło");

        Database.login(login, password);

        if (Database.userId == -1)
            errorTextView.setText("Wprowadzono niepoprawne dane.");
        else {
            // check permission admin/user 2 other activities
            Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
            startActivity(intent);
        }
    }

    public void continueWithoutLogin(View view){
        Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void addCity(View view){
        Database.addCity("Warszawa");
        //Database.addCity("Paryż");

        List<City> cityList = Database.getCities();

        /*
        String s="";

        for(City city : cityList){
            s += city.toString() + " ";
        }

        citiesTextView.setText(s);

         */

    }

    public void deleteCity(View view){
        Database.deleteCity("Warszawa");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void filter(View view){
        /*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date1 = LocalDate.parse("2023-07-07", formatter); // użytkownik będzie wprowadzał date
        LocalDate date2 = LocalDate.parse("2023-09-15", formatter); // użytkownik będzie wprowadzał date

        Hotel hotel = new Hotel(0,new Country(0,"XXX"), new City(0,"Warszawa"), new Food(0,"ZZZ"), (short) 4, "bla bla bla", new HotelName(0,"Golebiewski"));

        List<String> countryList = new ArrayList<>();
        //countryList.add("Polska");
       // countryList.add("Niemcy");
        List<String> cityList = new ArrayList<>();
        //cityList.add("Paryz");
        List<String> foodList = new ArrayList<>();
        foodList.add("all inclusive");


        List<Offer> offerList = Database.filterOffers((short) 2,0,10000,date1, date2, countryList, cityList, foodList, (short)2);
        */
        /*
        String s="";

        if(offerList != null) {
            for (Offer offer : offerList) {
                s += offer.getPrice() + " ";
            }
            citiesTextView.setText(s);
        }

         */
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addOffer(View view){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date1 = LocalDate.parse("2023-07-07", formatter);
        LocalDate date2 = LocalDate.parse("2023-07-31", formatter);

        // places_number,price,start_date,end_date,hotel_id)
        Offer offer = new Offer((short)10, 2000.00, date1, date2, 2);
        Database.addOffer(offer);
    }

}