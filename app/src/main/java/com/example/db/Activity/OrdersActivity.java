package com.example.db.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.db.Adapter.OrdersRecyclerViewAdapter;
import com.example.db.Class.Order;
import com.example.db.Database.Database;
import com.example.db.R;

public class OrdersActivity extends AppCompatActivity {

    private Intent intent;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Order> orderList;
        if (!Database.isAdmin)
            orderList = new ArrayList<>(Database.getUserOrders());
        else orderList = new ArrayList<>(Database.gettAllOrders());

        if (orderList.size() > 0) {
            setContentView(R.layout.activity_orders);
            RecyclerView ordersRecyclerView = findViewById(R.id.bookedRecyclerView);
            ordersRecyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            ordersRecyclerView.setLayoutManager(linearLayoutManager);

            OrdersRecyclerViewAdapter adapter = new OrdersRecyclerViewAdapter(this, orderList);
            ordersRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nie złożyłeś jeszcze żadnego zamówienia.", Toast.LENGTH_LONG).show();
            setContentView(R.layout.empty_recycler);

            Button filterButton = findViewById(R.id.searchEngineButton);
            TextView emptyTextView = findViewById(R.id.emptyTextView);

            if (!Database.isAdmin)
                emptyTextView.setText("Nie złożyłeś jeszcze żadnego zamówienia. Jeśli chcesz to zrobić, kliknij w poniższy przycisk. Zostaniesz wówczas przeniesiony do wyszukiwarki ofert.");
            else {
                filterButton.setVisibility(View.INVISIBLE);
                emptyTextView.setText("Nie ma obecnie żadnego złożonego zamówienia.");
            }

            filterButton.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
                startActivity(intent);
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!Database.isAdmin) {
            inflater.inflate(R.menu.user_menu, menu);
            MenuItem login_item = menu.findItem(R.id.login);
            login_item.setVisible(false);
        } else inflater.inflate(R.menu.admin_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (!Database.isAdmin) {

            if (id == R.id.showSearchEngine)
                intent = new Intent(this, SearchEngineActivity.class);
            else if (id == R.id.showFavourites)
                intent = new Intent(this, FavouriteOffersActivity.class);
            else if (id == R.id.uShowOrders)
                intent = new Intent(this, OrdersActivity.class);
            else if (id == R.id.uLogout) {
                Database.logOut();
                intent = new Intent(this, LoginActivity.class);
            }
        } else {

            if (id == R.id.showHotels)
                intent = new Intent(this, HotelsActivity.class);
            else if (id == R.id.showOffers)
                intent = new Intent(this, OffersActivity.class);
            else if (id == R.id.aShowOrders)
                intent = new Intent(this, OrdersActivity.class);
            else if (id == R.id.showConfiguration)
                intent = new Intent(this, ConfigurationActivity.class);
            else if (id == R.id.aLogout) {
                Database.logOut();
                intent = new Intent(this, LoginActivity.class);
            }
        }

        startActivity(intent);
        return true;
    }
}