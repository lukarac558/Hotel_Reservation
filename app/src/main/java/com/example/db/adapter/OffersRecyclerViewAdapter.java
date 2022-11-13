package com.example.db.adapter;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.RecyclerView;
import com.example.db.R;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.db.model.Offer;
import com.example.db.database.Database;

public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<OffersRecyclerViewAdapter.OfferViewHolder> {

    private final ArrayList<Offer> data;
    private final Context context;

    public OffersRecyclerViewAdapter(Context context, ArrayList<Offer> data){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public OffersRecyclerViewAdapter.OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row_item,parent,false);
        return new OffersRecyclerViewAdapter.OfferViewHolder(view).linkAdapter(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OffersRecyclerViewAdapter.OfferViewHolder holder, int position) {
        Offer offer = data.get(position);
        holder.offer = data.get(position);
        holder.context = context;

        Bitmap bitmap = offer.getHotel().getImage().getBitmap();
        String placesNumber = String.valueOf(offer.getPlacesNumber());
        String price = String.valueOf(offer.getPrice());

        holder.placesNumberTextView.setText(placesNumber);
        holder.priceTextView.setText(price);
        holder.startDateTextView.setText(offer.getStartDate().toString());
        holder.endDateTextView.setText(offer.getEndDate().toString());
        holder.hotelNameTextView.setText(offer.getHotel().getName());
        holder.countryTextView.setText(offer.getHotel().getCity().getCountry().getName()+",");
        holder.cityTextView.setText(offer.getHotel().getCity().getName());
        holder.foodTextView.setText(offer.getFood().getType());
        holder.hotelImageView.setImageBitmap(bitmap);
        holder.starsRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private final TextView placesNumberTextView, priceTextView, startDateTextView, endDateTextView;
        private final TextView hotelNameTextView, countryTextView, cityTextView, foodTextView;
        private final ImageView hotelImageView;
        private final RatingBar starsRatingBar;
        private Context context;
        private Offer offer;
        private OffersRecyclerViewAdapter adapter;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            placesNumberTextView = itemView.findViewById(R.id.ofrPlacesNumberTextView);
            priceTextView = itemView.findViewById(R.id.ofrPriceTextView);
            startDateTextView = itemView.findViewById(R.id.ofrStartDateTextView);
            endDateTextView = itemView.findViewById(R.id.ofrEndDateTextView);
            hotelImageView = itemView.findViewById(R.id.ofrHotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.ofrHotelNameTextView);
            countryTextView = itemView.findViewById(R.id.ofrCountryTextView);
            cityTextView = itemView.findViewById(R.id.ofrCityTextView);
            foodTextView = itemView.findViewById(R.id.ofrFoodTextView);
            starsRatingBar = itemView.findViewById(R.id.ofrStarsRatingBar);

            itemView.findViewById(R.id.ofrDeleteButton).setOnClickListener(view -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybraną ofertę?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok",
                        (dialog, id) -> {
                            offer = adapter.data.get(getAdapterPosition());

                            try {
                                Database.deleteOffer(offer.getId());
                                Toast.makeText(context, "Pomyślnie usunięto ofertę", Toast.LENGTH_SHORT).show();
                            } catch (SQLException exception) {
                                Toast.makeText(context, "Usunięcie niemożliwe. Oferta w użyciu.", Toast.LENGTH_SHORT).show();
                            }

                            adapter.data.remove(getAdapterPosition());
                            adapter.notifyItemRemoved(getAdapterPosition());
                        });
                alertBuilder.setNegativeButton("Anuluj",
                        (dialog, id) -> dialog.cancel());

                AlertDialog offerAlert = alertBuilder.create();
                offerAlert.show();
            });
        }

        public OffersRecyclerViewAdapter.OfferViewHolder linkAdapter(OffersRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }

    }
}