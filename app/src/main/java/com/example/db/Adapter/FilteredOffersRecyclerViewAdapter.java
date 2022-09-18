package com.example.db.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.Activity.OfferDetailsActivity;
import com.example.db.Database.Database;
import com.example.db.R;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.db.Class.Offer;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        holder.fHotelNameTextView.setText(offer.getHotel().getName().getName());
        holder.fCountryTextView.setText(offer.getHotel().getCountry().getName()+",");
        holder.fCityTextView.setText(offer.getHotel().getCity().getName());
        holder.fStartDateTextView.setText(offer.getStartDate().toString());
        holder.fDaysTextView.setText("(" + days + " dni)");
        holder.fPriceTextView.setText(stringPrice);
        holder.fFoodTextView.setText(offer.getHotel().getFood().getType());
        holder.fHotelImageView.setImageBitmap(bitmap);
        holder.fRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private final TextView fHotelNameTextView, fCountryTextView, fCityTextView, fStartDateTextView, fDaysTextView, fPriceTextView, fFoodTextView;
        private final ImageView fHotelImageView;
        private final RatingBar fRatingBar;
        private int offerId;
        private Context context;
        private short peopleCount;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            fHotelNameTextView = itemView.findViewById(R.id.fHotelNameTextView);
            fCountryTextView = itemView.findViewById(R.id.fCountryTextView);
            fCityTextView = itemView.findViewById(R.id.fCityTextView);
            fStartDateTextView = itemView.findViewById(R.id.fStartDateTextView);
            fDaysTextView = itemView.findViewById(R.id.fDaysTextView);
            fPriceTextView = itemView.findViewById(R.id.fPriceTextView);
            fFoodTextView = itemView.findViewById(R.id.fFoodTextView);
            fHotelImageView = itemView.findViewById(R.id.fHotelImageView);
            fRatingBar = itemView.findViewById(R.id.fRatingBar);

            itemView.findViewById(R.id.faDetailsButton).setOnClickListener(view -> getItem());
        }

        private void getItem(){
            Intent intent = new Intent(context, OfferDetailsActivity.class);
            intent.putExtra("offer", offerId);
            intent.putExtra("peopleCount", peopleCount);
            context.startActivity(intent);
        }
    }
}