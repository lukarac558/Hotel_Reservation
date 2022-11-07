package com.example.db.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.Activity.ConfigurationActivity;
import com.example.db.Activity.HotelsActivity;
import com.example.db.Activity.LoginActivity;
import com.example.db.Activity.OffersActivity;
import com.example.db.Activity.OrdersActivity;
import com.example.db.Class.Hotel;
import com.example.db.Database.Database;
import com.example.db.R;

import java.io.IOException;
import java.util.Objects;

public class EditHotelFragment extends Fragment {

    private int hotelId;
    private HotelsActivity hotelsActivity;
    private Intent intent;
    private ImageView hotelImageView;
    private EditText descriptionEditText;
    private Bitmap bitmap;
    private Uri imageUrl;
    private RadioButton oneStar, twoStars, threeStars, fourStars, fiveStars;
    private short starsCount = 1;

    public EditHotelFragment() { }

    public static EditHotelFragment newInstance() {
        return new EditHotelFragment();
    }

    public static EditHotelFragment newInstance(int hotelId) {
        EditHotelFragment fragment = new EditHotelFragment();
        Bundle args = new Bundle();
        args.putInt("hotelId", hotelId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hotelId = getArguments().getInt("hotelId");
        }

        setHasOptionsMenu(true);
        hotelsActivity = (HotelsActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_hotel, container, false);

        Hotel hotel = Database.getHotelById(hotelId);

        hotelImageView = view.findViewById(R.id.heHotelImageView);
        Button selectImageButton = view.findViewById(R.id.heSelectImageButton);
        Button editHotelButton = view.findViewById(R.id.editHotelButton);
        TextView hotelNameTextView = view.findViewById(R.id.heHotelNameTextView);
        TextView countryTextView = view.findViewById(R.id.heCountryTextView);
        TextView cityTextView = view.findViewById(R.id.heCityTextView);

        descriptionEditText = view.findViewById(R.id.heDescriptionEditText);
        RadioGroup heStarsRadioGroup = view.findViewById(R.id.heStarsRadioGroup);
        oneStar = view.findViewById(R.id.heOneStarRadioButton);
        twoStars = view.findViewById(R.id.heTwoStarsRadioButton);
        threeStars = view.findViewById(R.id.heThreeStarsRadioButton);
        fourStars = view.findViewById(R.id.heFourStarsRadioButton);
        fiveStars = view.findViewById(R.id.heFiveStarsRadioButton);

        hotelNameTextView.setText(hotel.getName());
        countryTextView.setText(hotel.getCity().getCountry().getName());
        cityTextView.setText(hotel.getCity().getName());
        hotelImageView.setImageBitmap(hotel.getImage().getBitmap());
        starsCount = hotel.getStarCount();
        checkActualStarsCount();

        heStarsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.heOneStarRadioButton) {
                    starsCount = 1;
                }
                if (radioGroup.getCheckedRadioButtonId() == R.id.heTwoStarsRadioButton) {
                    starsCount = 2;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.heThreeStarsRadioButton) {
                    starsCount = 3;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.heFourStarsRadioButton) {
                    starsCount = 4;
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.heFiveStarsRadioButton) {
                    starsCount = 5;
                }
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Wybierz zdjęcie"), 1);
            }
        });

        editHotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editHotel();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
            imageUrl = Objects.requireNonNull(data).getData();

        try {
            //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUrl);
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUrl);
            hotelImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editHotel() {
        AsyncTask.execute(() -> {
            if (bitmap == null) {
                showMessage("Należy wybrać zdjęcie hotelu", "Bitmap is null");
                return;
            }

            String description = descriptionEditText.getText().toString();

            Database.updateHotel(hotelId, starsCount, bitmap, description);
            showMessage("Pomyślnie edytowano hotel.", "Hotel edited");

            backToPanel();
        });
    }

    private void showMessage(String messageDescription, String logTag) {
        hotelsActivity.runOnUiThread(() -> {
            Toast.makeText(hotelsActivity.getApplicationContext(), messageDescription, Toast.LENGTH_LONG).show();
            Log.d(logTag, messageDescription);
        });
    }

    private void backToPanel() {
        Intent intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        startActivity(intent);
    }

    private void checkActualStarsCount(){
        switch(starsCount){
            case 1:
                oneStar.setChecked(true);
            case 2:
                twoStars.setChecked(true);
            case 3:
                threeStars.setChecked(true);
            case 4:
                fourStars.setChecked(true);
            case 5:
                fiveStars.setChecked(true);
        }
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showHotels)
            intent = new Intent(hotelsActivity.getApplicationContext(), HotelsActivity.class);
        else if (id == R.id.showOffers)
            intent = new Intent(hotelsActivity.getApplicationContext(), OffersActivity.class);
        else if (id == R.id.aShowOrders)
            intent = new Intent(hotelsActivity.getApplicationContext(), OrdersActivity.class);
        else if (id == R.id.showConfiguration)
            intent = new Intent(hotelsActivity.getApplicationContext(), ConfigurationActivity.class);
        else if (id == R.id.aLogout) {
            Database.logOut();
            intent = new Intent(hotelsActivity.getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        return true;
    }
}