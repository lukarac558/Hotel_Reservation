package com.example.db.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class FoodFragment extends Fragment {

    ConfigurationActivity configurationActivity;
    Intent intent;
    Spinner selectedFoodTypesSpinner;
    EditText foodEditText;
    ArrayAdapter<String> foodAdapter;
    List foodList;
    List<String> stringFoodList;

    public FoodFragment() {
    }


    public static FoodFragment newInstance() {
        return new FoodFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        configurationActivity = (ConfigurationActivity)getActivity();
        configurationActivity.findViewById(R.id.countryImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.cityImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.foodImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.hotelNameImageButton).setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food, container, false);
        selectedFoodTypesSpinner = view.findViewById(R.id.selectedFoodTypesSpinner);
        foodEditText = view.findViewById(R.id.foodEditText);
        Button addFoodButton = view.findViewById(R.id.addFoodButton);
        Button deleteFoodButton = view.findViewById(R.id.deleteFoodButton);

        foodList = Database.getFoodTypes();

        stringFoodList  = (List<String>) foodList.stream().map(object -> Objects.toString(object)).collect(Collectors.toList());

        Collections.sort(stringFoodList);

        setFoodAdapter();

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodType = foodEditText.getText().toString();

                if(!foodType.isEmpty()) {
                    Database.addFoodType(foodType);
                    Toast.makeText(getContext(), "Pomyślnie dodano wyżywienie", Toast.LENGTH_SHORT).show();

                    if (!stringFoodList.contains(foodType))
                        stringFoodList.add(foodType);

                    setFoodAdapter();
                }
                else
                    Log.d("Empty food type", "Należy wprowadzić typ wyżywienia");
            }
        });

        deleteFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element z bazy wraz z jego powiązaniami?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String selectedFood = (String) selectedFoodTypesSpinner.getSelectedItem();

                                Database.deleteFoodType(selectedFood);
                                stringFoodList.remove(selectedFood);
                                Toast.makeText(getContext(), "Pomyślnie usunięto wyżywienie", Toast.LENGTH_SHORT).show();
                                setFoodAdapter();
                            }
                        });
                alertBuilder.setNegativeButton("Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog foodAlert = alertBuilder.create();
                foodAlert.show();
            }
        });

        return view;
    }

    private void setFoodAdapter(){
        foodAdapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, stringFoodList);
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedFoodTypesSpinner.setAdapter(foodAdapter);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(configurationActivity.getApplicationContext(), HotelsActivity.class);
        else if(id == R.id.showOffers)
            intent = new Intent(configurationActivity.getApplicationContext(), OffersActivity.class);
        else if(id == R.id.aShowOrders)
            intent = new Intent(configurationActivity.getApplicationContext(), OrdersActivity.class);
        else if(id == R.id.showConfiguration)
            intent = new Intent(configurationActivity.getApplicationContext(), ConfigurationActivity.class);
        else if(id == R.id.aLogout)
        {
            Database.logOut();
            intent = new Intent(configurationActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}