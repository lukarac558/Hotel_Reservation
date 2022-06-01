package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.db.Database.Database;
import com.example.db.Fragment.AddHotelFragment;
import com.example.db.Fragment.DeleteHotelFragment;
import com.example.db.R;

public class HotelsActivity extends AppCompatActivity {

    Intent intent;
    AddHotelFragment addHotelFragment;
    DeleteHotelFragment deleteHotelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
    }

    public void addHotel(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        addHotelFragment = AddHotelFragment.newInstance();
        fragmentTransaction.replace(R.id.hotelsLinearLayout,addHotelFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void deleteHotel(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        deleteHotelFragment = DeleteHotelFragment.newInstance();
        fragmentTransaction.replace(R.id.hotelsLinearLayout,deleteHotelFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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