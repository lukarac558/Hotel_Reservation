package com.example.db.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.utils.WindowDirector;
import com.example.db.fragment.EditHotelFragment;
import com.example.db.R;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.db.model.Hotel;
import com.example.db.database.Database;

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
        holder.hotelImageView.setImageBitmap(bitmap);
        holder.hotelNameTextView.setText(hotel.getName());
        holder.countryTextView.setText(hotel.getCity().getCountry().getName());
        holder.cityTextView.setText(hotel.getCity().getName());
        holder.descriptionTextView.setText(hotel.getDescription());
        holder.starsRatingBar.setRating(hotel.getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        private final ImageView hotelImageView;
        private final TextView hotelNameTextView, countryTextView, cityTextView, descriptionTextView;
        private final RatingBar starsRatingBar;
        private Context context;
        private Hotel hotel;
        private HotelsRecyclerViewAdapter adapter;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            hotelImageView = itemView.findViewById(R.id.hrHotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hrHotelNameTextView);
            countryTextView = itemView.findViewById(R.id.hrCountryTextView);
            cityTextView = itemView.findViewById(R.id.hrCityTextView);
            descriptionTextView = itemView.findViewById(R.id.hrDescriptionTextView);
            starsRatingBar = itemView.findViewById(R.id.hrStarsRatingBar);

            itemView.findViewById(R.id.hrDeleteButton).setOnClickListener(view -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany hotel?");
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
                EditHotelFragment editHotelFragment = EditHotelFragment.newInstance(hotel.getId());

                WindowDirector.changeFragment(fragmentManager, editHotelFragment, R.id.hotelsLayout);

            });
        }

        public HotelsRecyclerViewAdapter.HotelViewHolder linkAdapter(HotelsRecyclerViewAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }
}
