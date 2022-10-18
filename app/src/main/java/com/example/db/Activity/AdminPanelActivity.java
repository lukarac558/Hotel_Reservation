package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.db.Database.Database;
import com.example.db.R;

public class AdminPanelActivity extends AppCompatActivity {

    private Intent intent;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }

    public void showHotels(View view){
        intent = new Intent(getApplicationContext(), HotelsActivity.class);
        startActivity(intent);
    }

    public void showOffers(View view){
        intent = new Intent(getApplicationContext(), OffersActivity.class);
        startActivity(intent);
    }

    public void showOrders(View view){
        intent = new Intent(getApplicationContext(), OrdersActivity.class);
        startActivity(intent);
    }


    public void showConfiguration(View view){
        intent = new Intent(getApplicationContext(), ConfigurationActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(this, HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(this, OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(this, OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(this, ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}