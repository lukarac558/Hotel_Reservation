package com.example.db.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.db.utils.WindowDirector;
import com.example.db.utils.MenuDirector;
import com.example.db.R;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }

    public void showHotels(View view) {
        WindowDirector.changeActivity(this, HotelsActivity.class);
    }

    public void showOffers(View view) {
        WindowDirector.changeActivity(this, OffersActivity.class);
    }

    public void showOrders(View view) {
        WindowDirector.changeActivity(this, OrdersActivity.class);
    }

    public void showConfiguration(View view) {
        WindowDirector.changeActivity(this, ConfigurationActivity.class);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setAdminOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleAdminMenuItemSelected(this, item);
    }
}