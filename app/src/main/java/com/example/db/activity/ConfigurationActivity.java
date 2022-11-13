package com.example.db.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.db.utils.WindowDirector;
import com.example.db.fragment.CityFragment;
import com.example.db.fragment.CountryFragment;
import com.example.db.fragment.FoodFragment;
import com.example.db.fragment.UsersFragment;
import com.example.db.utils.MenuDirector;
import com.example.db.R;

public class ConfigurationActivity extends AppCompatActivity {

    private CountryFragment countriesFragment;
    private CityFragment citiesFragment;
    private FoodFragment foodFragment;
    private UsersFragment usersFragment;
    private FragmentManager fragmentManager;
    private final int configurationLayoutId = R.id.configurationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        countriesFragment = CountryFragment.newInstance();
        citiesFragment = CityFragment.newInstance();
        foodFragment = FoodFragment.newInstance();
        usersFragment = UsersFragment.newInstance();
    }

    public void backToPanel(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (countriesFragment != null)
            fragmentTransaction.remove(countriesFragment);
        if (citiesFragment != null)
            fragmentTransaction.remove(citiesFragment);
        if (foodFragment != null)
            fragmentTransaction.remove(foodFragment);
        if (usersFragment != null)
            fragmentTransaction.remove(usersFragment);

        fragmentTransaction.commit();
        findViewById(R.id.countryImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.cityImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.foodImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.userImageButton).setVisibility(View.VISIBLE);
        findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
    }

    public void showCountryFragment(View view) {
        WindowDirector.changeFragment(fragmentManager, countriesFragment, configurationLayoutId);
        showBackButton();
    }

    public void showCityFragment(View view) {
        WindowDirector.changeFragment(fragmentManager, citiesFragment, configurationLayoutId);
        showBackButton();
    }

    public void showFoodFragment(View view) {
        WindowDirector.changeFragment(fragmentManager, foodFragment, configurationLayoutId);
        showBackButton();
    }

    public void showUsersFragment(View view) {
        WindowDirector.changeFragment(fragmentManager, usersFragment, configurationLayoutId);
        showBackButton();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setAdminOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleAdminMenuItemSelected(this, item);
    }

    private void showBackButton(){
        findViewById(R.id.backButton).setVisibility(View.VISIBLE);
    }
}