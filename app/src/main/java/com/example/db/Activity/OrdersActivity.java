package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import com.example.db.Adapter.OrdersRecyclerViewAdapter;
import com.example.db.Class.Order;
import com.example.db.Database.Database;
import com.example.db.R;

public class OrdersActivity extends AppCompatActivity {

    Intent intent;
    ArrayList<Order> orderList;
    RecyclerView ordersRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersRecyclerView = findViewById(R.id.bookedRecyclerView);
        ordersRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);


        if(Database.permission.equalsIgnoreCase("user")) {
            orderList = (ArrayList<Order>) Database.getUserOrders();
        }
        else if(Database.permission.equalsIgnoreCase("admin"))
            orderList = (ArrayList<Order>) Database.gettAllOrders();

        OrdersRecyclerViewAdapter adapter = new OrdersRecyclerViewAdapter(this, orderList);
        ordersRecyclerView.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(Database.permission.equalsIgnoreCase("user")) {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem login_item = menu.findItem(R.id.login);
            login_item.setVisible(false);
        }
        else if(Database.permission.equalsIgnoreCase("admin"))
            inflater.inflate(R.menu.admin_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(Database.permission.equalsIgnoreCase("user")) {

            if(id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if(id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if(id == R.id.uShowOrders)
                intent = new Intent(this, OrdersActivity.class);
            else if(id == R.id.uLogout)
            {
                Database.logOut();
                intent = new Intent(this, LoginActivity.class);
            }
        }
        else if(Database.permission.equalsIgnoreCase("admin")){

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
        }

        startActivity(intent);
        return true;
    }
}