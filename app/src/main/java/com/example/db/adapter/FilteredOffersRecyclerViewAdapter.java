package com.example.db.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.activity.OfferDetailsActivity;
import com.example.db.database.Database;
import com.example.db.R;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.db.model.Offer;

public class FilteredOffersRecyclerViewAdapter extends RecyclerView.Adapter<FilteredOffersRecyclerViewAdapter.OfferViewHolder> {

    private final ArrayList<Integer> data;
    private final Context context;
    private final short peopleCount;

    public FilteredOffersRecyclerViewAdapter(Context context, ArrayList<Integer> data, short peopleCount){
        this.data = data;
        this.context = context;
        this.peopleCount = peopleCount;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtered_offer_row_item,parent,false);
        return new OfferViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    
    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        int id = data.get(position);
        Offer offer = Database.getOfferById(id);
        holder.context = context;

        Bitmap bitmap = offer.getHotel().getImage().getBitmap();
        String stringPrice = String.valueOf(offer.getPrice());

        long days = ChronoUnit.DAYS.between(offer.getStartDate(), offer.getEndDate());

        holder.peopleCount = peopleCount;
        holder.offerId = id;
        holder.hotelNameTextView.setText(offer.getHotel().getName());
        holder.countryTextView.setText(offer.getHotel().getCity().getCountry().getName()+",");
        holder.cityTextView.setText(offer.getHotel().getCity().getName());
        holder.startDateTextView.setText(offer.getStartDate().toString());
        holder.daysTextView.setText("(" + days + " dni)");
        holder.priceTextView.setText(stringPrice);
        holder.foodTextView.setText(offer.getFood().getType());
        holder.hotelImageView.setImageBitmap(bitmap);
        holder.starsRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private final TextView hotelNameTextView, countryTextView, cityTextView, startDateTextView, daysTextView, priceTextView, foodTextView;
        private final ImageView hotelImageView;
        private final RatingBar starsRatingBar;
        private int offerId;
        private Context context;
        private short peopleCount;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.fHotelNameTextView);
            countryTextView = itemView.findViewById(R.id.fCountryTextView);
            cityTextView = itemView.findViewById(R.id.fCityTextView);
            startDateTextView = itemView.findViewById(R.id.fStartDateTextView);
            daysTextView = itemView.findViewById(R.id.fDaysTextView);
            priceTextView = itemView.findViewById(R.id.fPriceTextView);
            foodTextView = itemView.findViewById(R.id.fFoodTextView);
            hotelImageView = itemView.findViewById(R.id.fHotelImageView);
            starsRatingBar = itemView.findViewById(R.id.fRatingBar);

            itemView.findViewById(R.id.fDetailsButton).setOnClickListener(view -> getItem());
        }

        private void getItem(){
            Intent intent = new Intent(context, OfferDetailsActivity.class);
            intent.putExtra("offerId", offerId);
            intent.putExtra("peopleCount", peopleCount);
            context.startActivity(intent);
        }
    }
}