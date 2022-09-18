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
import com.example.db.Class.HotelName;
import com.example.db.Database.Database;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.R;

public class HotelNameFragment extends Fragment {

    ConfigurationActivity configurationActivity;
    Intent intent;
    Spinner selectedHotelNameSpinner;
    EditText hotelNameEditText;
    ArrayAdapter<String> hotelNameAdapter;
    List<HotelName> hotelNameList;
    List<String> stringHotelNameList;

    public HotelNameFragment() {
    }

    public static HotelNameFragment newInstance() {
        return new HotelNameFragment();
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
        View view = inflater.inflate(R.layout.fragment_hotel_name, container, false);
        selectedHotelNameSpinner = view.findViewById(R.id.selectedHotelNameSpinner);
        hotelNameEditText = view.findViewById(R.id.hotelNameEditText);
        Button addHotelNameButton = view.findViewById(R.id.addHotelNameButton);
        Button deleteHotelNameButton = view.findViewById(R.id.deleteHotelNameButton);

        hotelNameList = Database.getHotelNames();

        stringHotelNameList = (List<String>) hotelNameList.stream().map(Objects::toString).collect(Collectors.toList());
        Collections.sort(stringHotelNameList);

        setHotelNameAdapter();

        addHotelNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hotelName = hotelNameEditText .getText().toString();

                if(!hotelName.isEmpty()) {
                    Database.addHotelName(hotelName);
                    Toast.makeText(getContext(), "Pomyślnie dodano nazwę hotelu", Toast.LENGTH_SHORT).show();

                    if (!stringHotelNameList.contains(hotelName))
                        stringHotelNameList.add(hotelName);

                    setHotelNameAdapter();
                }
                else
                    Log.d("Empty hotel name", "Należy wprowadzić nazwę hotelu");
            }
        });

        deleteHotelNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element z bazy wraz z jego powiązaniami?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String selectedHotelName = (String) selectedHotelNameSpinner.getSelectedItem();

                                Database.deleteHotelName(selectedHotelName);
                                stringHotelNameList.remove(selectedHotelName);
                                Toast.makeText(getContext(), "Pomyślnie usunięto nazwę hotelu", Toast.LENGTH_SHORT).show();
                                setHotelNameAdapter();
                            }
                        });
                alertBuilder.setNegativeButton("Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog countryAlert = alertBuilder.create();
                countryAlert.show();
            }
        });

        return view;
    }

    private void setHotelNameAdapter(){
        hotelNameAdapter = new ArrayAdapter<>(configurationActivity.getApplicationContext(), android.R.layout.simple_spinner_item, stringHotelNameList);
        hotelNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedHotelNameSpinner.setAdapter(hotelNameAdapter);
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
