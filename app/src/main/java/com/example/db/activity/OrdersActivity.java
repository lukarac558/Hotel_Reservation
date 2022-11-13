package com.example.db.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.adapter.OrdersRecyclerViewAdapter;
import com.example.db.model.Order;
import com.example.db.database.Database;
import com.example.db.utils.Formatter;
import com.example.db.utils.MenuDirector;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private ArrayList<Order> orders;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialOrders();

        if (orders.size() > 0) {
            setContentView(R.layout.activity_orders);
            RecyclerView ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
            Formatter.setRecyclerView(ordersRecyclerView, this);

            OrdersRecyclerViewAdapter adapter = new OrdersRecyclerViewAdapter(this, orders);
            ordersRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nie złożyłeś jeszcze żadnego zamówienia.", Toast.LENGTH_LONG).show();
            setContentView(R.layout.empty_recycler);

            Button filterButton = findViewById(R.id.searchEngineButton);
            TextView emptyTextView = findViewById(R.id.emptyTextView);

            if (!Database.isAdmin) {
                emptyTextView.setText("Nie złożyłeś jeszcze żadnego zamówienia. Jeśli chcesz to zrobić, kliknij w poniższy przycisk. Zostaniesz wówczas przeniesiony do wyszukiwarki ofert.");
            } else {
                filterButton.setVisibility(View.INVISIBLE);
                emptyTextView.setText("Nie ma obecnie żadnego złożonego zamówienia.");
            }

            filterButton.setOnClickListener(view -> WindowDirector.changeActivity(this, SearchEngineActivity.class));
        }
    }

    private void initialOrders() {
        if (!Database.isAdmin) {
            orders = new ArrayList<>(Database.getUserOrders());
        } else {
            orders = new ArrayList<>(Database.gettAllOrders());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return MenuDirector.setOptionsMenuByPermission(Database.isAdmin, this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return MenuDirector.handleMenuItemSelectedByPermission(Database.isAdmin, this, item);
    }
}