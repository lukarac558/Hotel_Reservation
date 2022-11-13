package com.example.db.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

public class CityFragment extends Fragment {

    private AddCityFragment addCityFragment;
    private DeleteCityFragment deleteHotelFragment;
    private FragmentManager fragmentManager;
    private final int citiesLayoutId = R.id.citiesLayout;

    public CityFragment() {
    }

    public static CityFragment newInstance() {
        return new CityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ConfigurationActivity configurationActivity = (ConfigurationActivity) getActivity();
        fragmentManager = getFragmentManager();
        addCityFragment = AddCityFragment.newInstance();
        deleteHotelFragment = DeleteCityFragment.newInstance();
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

    public void addCity() {
        WindowDirector.changeFragment(fragmentManager, addCityFragment, citiesLayoutId);
    }

    public void deleteCity() {
        WindowDirector.changeFragment(fragmentManager, deleteHotelFragment, citiesLayoutId);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}