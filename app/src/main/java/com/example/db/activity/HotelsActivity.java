package com.example.db.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.db.utils.WindowDirector;
import com.example.db.fragment.AddHotelFragment;
import com.example.db.fragment.DeleteHotelFragment;
import com.example.db.utils.MenuDirector;
import com.example.db.R;

public class HotelsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private AddHotelFragment addHotelFragment;
    private DeleteHotelFragment deleteHotelFragment;
    private final int hotelsLayoutId = R.id.hotelsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        fragmentManager = getSupportFragmentManager();
        addHotelFragment = AddHotelFragment.newInstance();
        deleteHotelFragment = DeleteHotelFragment.newInstance();
    }

    public void addHotel(View view){
        WindowDirector.changeFragment(fragmentManager, addHotelFragment, hotelsLayoutId);
    }

    public void editOrDeleteHotel(View view){
        WindowDirector.changeFragment(fragmentManager, deleteHotelFragment, hotelsLayoutId);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setAdminOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleAdminMenuItemSelected(this, item);
    }
}