package com.example.db.Adapter;

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

import com.example.db.Class.Offer;
import com.example.db.Database.Database;

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

        holder.ofrPlacesNumberTextView.setText(placesNumber);
        holder.ofrPriceTextView.setText(price);
        holder.ofrStartDateTextView.setText(offer.getStartDate().toString());
        holder.ofrEndDateTextView.setText(offer.getEndDate().toString());
        holder.ofrHotelNameTextView.setText(offer.getHotel().getName());
        holder.ofrCountryTextView.setText(offer.getHotel().getCity().getCountry().getName()+",");
        holder.ofrCityTextView.setText(offer.getHotel().getCity().getName());
        holder.ofrFoodTextView.setText(offer.getFood().getType());
        holder.ofrHotelImageView.setImageBitmap(bitmap);
        holder.ofrStarsRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private final TextView ofrPlacesNumberTextView, ofrPriceTextView, ofrStartDateTextView, ofrEndDateTextView;
        private final TextView ofrHotelNameTextView, ofrCountryTextView, ofrCityTextView, ofrFoodTextView;
        private final ImageView ofrHotelImageView;
        private final RatingBar ofrStarsRatingBar;
        private Context context;
        private Offer offer;
        private OffersRecyclerViewAdapter adapter;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            ofrPlacesNumberTextView = itemView.findViewById(R.id.ofrPlacesNumberTextView);
            ofrPriceTextView = itemView.findViewById(R.id.ofrPriceTextView);
            ofrStartDateTextView = itemView.findViewById(R.id.ofrStartDateTextView);
            ofrEndDateTextView = itemView.findViewById(R.id.ofrEndDateTextView);
            ofrHotelImageView = itemView.findViewById(R.id.ofrHotelImageView);
            ofrHotelNameTextView = itemView.findViewById(R.id.ofrHotelNameTextView);
            ofrCountryTextView = itemView.findViewById(R.id.ofrCountryTextView);
            ofrCityTextView = itemView.findViewById(R.id.ofrCityTextView);
            ofrFoodTextView = itemView.findViewById(R.id.ofrFoodTextView);
            ofrStarsRatingBar = itemView.findViewById(R.id.ofrStarsRatingBar);

            itemView.findViewById(R.id.userDeleteButton).setOnClickListener(view -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element z bazy wraz z jego powiązaniami?");
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