package com.example.db.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.db.activity.ConfigurationActivity;
import com.example.db.model.Food;
import com.example.db.database.Database;
import com.example.db.R;
import com.example.db.utils.Formatter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FoodFragment extends Fragment {

    private Spinner foodSpinner;
    private EditText foodEditText;
    private List<String> stringFoodTypes;

    public FoodFragment() {
    }


    public static FoodFragment newInstance() {
        return new FoodFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ConfigurationActivity configurationActivity = (ConfigurationActivity) getActivity();
        configurationActivity.findViewById(R.id.countryImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.cityImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.foodImageButton).setVisibility(View.INVISIBLE);
        configurationActivity.findViewById(R.id.userImageButton).setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food, container, false);
        foodSpinner = view.findViewById(R.id.selectedFoodTypesSpinner);
        foodEditText = view.findViewById(R.id.foodEditText);
        Button addFoodButton = view.findViewById(R.id.addFoodButton);
        Button deleteFoodButton = view.findViewById(R.id.deleteFoodButton);

        List<Food> foodTypes = Database.getFoodTypes();
        stringFoodTypes = foodTypes.stream().map(Objects::toString).collect(Collectors.toList());

        Collections.sort(stringFoodTypes);
        Formatter.setAdapter(foodSpinner, getContext(), foodTypes);

        addFoodButton.setOnClickListener(addFoddView -> {
            String foodType = foodEditText.getText().toString();

            if (!foodType.isEmpty()) {
                try {
                    Database.addFoodType(foodType);
                    Toast.makeText(getContext(), "Pomyślnie dodano wyżywienie", Toast.LENGTH_SHORT).show();
                } catch (SQLException exception) {
                    Toast.makeText(getContext(), "Podany typ istnieje już w bazie", Toast.LENGTH_SHORT).show();
                }

                if (!stringFoodTypes.contains(foodType))
                    stringFoodTypes.add(foodType);

                Formatter.setAdapter(foodSpinner, getContext(), foodTypes);
            } else
                Log.d("Empty food type", "Należy wprowadzić typ wyżywienia");
        });

        deleteFoodButton.setOnClickListener(deleteFoodView -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element z bazy?");
            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Ok",
                    (dialog, id) -> {
                        Food selectedFood = (Food) foodSpinner.getSelectedItem();

                        try {
                            Database.deleteFoodType(selectedFood.getType());
                            stringFoodTypes.remove(selectedFood.getType());
                            Toast.makeText(getContext(), "Pomyślnie usunięto wyżywienie", Toast.LENGTH_SHORT).show();
                            Formatter.setAdapter(foodSpinner, getContext(), foodTypes);
                        } catch (SQLException exception) {
                            Toast.makeText(getContext(), "Usunięcie niemożliwe. Zadany typ wyżywienia w użyciu.", Toast.LENGTH_SHORT).show();
                        }
                    });
            alertBuilder.setNegativeButton("Anuluj",
                    (dialog, id) -> dialog.cancel());

            AlertDialog foodAlert = alertBuilder.create();
            foodAlert.show();
        });

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}