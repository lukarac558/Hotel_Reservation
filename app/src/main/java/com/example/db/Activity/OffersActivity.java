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
import com.example.db.Fragment.AddOfferFragment;
import com.example.db.Fragment.DeleteOfferFragment;
import com.example.db.R;

public class OffersActivity extends AppCompatActivity {

    Intent intent;
    AddOfferFragment addOfferFragment;
    DeleteOfferFragment deleteOfferFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
    }

    public void addOffer(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        addOfferFragment = AddOfferFragment.newInstance();
        fragmentTransaction.replace(R.id.offersLinearLayout,addOfferFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void deleteOffer(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        deleteOfferFragment = DeleteOfferFragment.newInstance();
        fragmentTransaction.replace(R.id.offersLinearLayout, deleteOfferFragment);
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