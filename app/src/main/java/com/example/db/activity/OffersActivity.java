package com.example.db.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.db.utils.WindowDirector;
import com.example.db.fragment.AddOfferFragment;
import com.example.db.fragment.DeleteOfferFragment;
import com.example.db.utils.MenuDirector;
import com.example.db.R;

public class OffersActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private AddOfferFragment addOfferFragment;
    private DeleteOfferFragment deleteOfferFragment;
    private final int offersLayoutId = R.id.offersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        fragmentManager = getSupportFragmentManager();
        addOfferFragment = AddOfferFragment.newInstance();
        deleteOfferFragment = DeleteOfferFragment.newInstance();
    }

    public void addOffer(View view) {
        WindowDirector.changeFragment(fragmentManager, addOfferFragment, offersLayoutId);
    }

    public void deleteOffer(View view) {
        WindowDirector.changeFragment(fragmentManager, deleteOfferFragment, offersLayoutId);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setAdminOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleAdminMenuItemSelected(this, item);
    }
}