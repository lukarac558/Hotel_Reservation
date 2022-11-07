package com.example.db.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class CityFragment extends Fragment {

    private ConfigurationActivity configurationActivity;
    private Intent intent;

    public CityFragment() {
    }

    public static CityFragment newInstance() {
        return new CityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        configurationActivity = (ConfigurationActivity) getActivity();
        configurationActivity.findViewById(R.id.countryImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.cityImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.foodImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.userImageButton).setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        Button addCityButton = view.findViewById(R.id.cAddCityButton);
        Button deleteCityButton = view.findViewById(R.id.cDeleteCityButton);

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });

        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCity();
            }
        });

        return view;
    }

    public void addCity(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddCityFragment addCityFragment = AddCityFragment.newInstance();
        fragmentTransaction.replace(R.id.citiesLinearLayout, addCityFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void deleteCity(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DeleteCityFragment deleteHotelFragment = DeleteCityFragment.newInstance();
        fragmentTransaction.replace(R.id.citiesLinearLayout, deleteHotelFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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