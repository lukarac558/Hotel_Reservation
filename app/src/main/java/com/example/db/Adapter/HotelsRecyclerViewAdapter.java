package com.example.db.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.Fragment.EditHotelFragment;
import com.example.db.R;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.db.Class.Hotel;
import com.example.db.Database.Database;

public class HotelsRecyclerViewAdapter extends RecyclerView.Adapter<HotelsRecyclerViewAdapter.HotelViewHolder> {

    private final ArrayList<Hotel> data;
    private final Context context;

    public HotelsRecyclerViewAdapter(Context context, ArrayList<Hotel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_row_item, parent, false);
        return new HotelViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelsRecyclerViewAdapter.HotelViewHolder holder, int position) {
        Hotel hotel = data.get(position);
        holder.hotel = data.get(position);
        holder.context = context;

        Bitmap bitmap = hotel.getImage().getBitmap();
        holder.hrHotelImageView.setImageBitmap(bitmap);
        holder.hrHotelNameTextView.setText(hotel.getName());
        holder.hrCountryTextView.setText(hotel.getCity().getCountry().getName());
        holder.hrCityTextView.setText(hotel.getCity().getName());
        holder.hrDescriptionTextView.setText(hotel.getDescription());
        holder.hrStarsRatingBar.setRating(hotel.getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        private final ImageView hrHotelImageView;
        private final TextView hrHotelNameTextView, hrCountryTextView, hrCityTextView, hrDescriptionTextView;
        private final RatingBar hrStarsRatingBar;
        private Context context;
        private Hotel hotel;
        private HotelsRecyclerViewAdapter adapter;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            hrHotelImageView = itemView.findViewById(R.id.hrHotelImageView);
            hrHotelNameTextView = itemView.findViewById(R.id.hrHotelNameTextView);
            hrCountryTextView = itemView.findViewById(R.id.hrCountryTextView);
            hrCityTextView = itemView.findViewById(R.id.hrCityTextView);
            hrDescriptionTextView = itemView.findViewById(R.id.hrDescriptionTextView);
            hrStarsRatingBar = itemView.findViewById(R.id.hrStarsRatingBar);

            itemView.findViewById(R.id.hrDeleteButton).setOnClickListener(view -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element z bazy wraz z jego powiązaniami?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok",
                        (dialog, id) -> {
                            hotel = adapter.data.get(getAdapterPosition());

                            try {
                                Database.deleteHotel(hotel.getId());
                                Toast.makeText(context, "Pomyślnie usunięto hotel", Toast.LENGTH_SHORT).show();
                            } catch (SQLException exception) {
                                Toast.makeText(context, "Usunięcie niemożliwe. Hotel jest w użyciu.", Toast.LENGTH_SHORT).show();
                            }

                            adapter.data.remove(getAdapterPosition());
                            adapter.notifyItemRemoved(getAdapterPosition());
                        });
                alertBuilder.setNegativeButton("Anuluj",
                        (dialog, id) -> dialog.cancel());

                AlertDialog hotelAlert = alertBuilder.create();
                hotelAlert.show();
            });

            itemView.findViewById(R.id.hrEditButton).setOnClickListener(view -> {
                hotel = adapter.data.get(getAdapterPosition());

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EditHotelFragment editHotelFragment = EditHotelFragment.newInstance(hotel.getId());
                fragmentTransaction.replace(R.id.hotelsLinearLayout, editHotelFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });


        }

        public HotelsRecyclerViewAdapter.HotelViewHolder linkAdapter(HotelsRecyclerViewAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }
}
