package com.example.db.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.activity.OfferDetailsActivity;
import com.example.db.activity.RegisterActivity;
import com.example.db.model.CartItem;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.db.model.Offer;
import com.example.db.database.Database;

public class FavouriteOffersRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteOffersRecyclerViewAdapter.FavouriteOfferViewHolder> {

    private final Context context;
    private final ArrayList<CartItem> data;

    public FavouriteOffersRecyclerViewAdapter(Context context, ArrayList<CartItem> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_offer_row_item, parent, false);
        return new FavouriteOfferViewHolder(view).linkAdapter(this);
    }

    @SuppressLint("SetTextI18n")

    @Override
    public void onBindViewHolder(@NonNull FavouriteOfferViewHolder holder, int position) {
        CartItem cartItem = data.get(position);
        holder.context = context;
        holder.cartItem = cartItem;
        Offer offer = Database.getOfferById(cartItem.getOfferId());

        Bitmap bitmap = offer.getHotel().getImage().getBitmap();
        String stringPrice = String.valueOf(offer.getPrice());

        long days = ChronoUnit.DAYS.between(offer.getStartDate(), offer.getEndDate());

        holder.hotelNameTextView.setText(offer.getHotel().getName());
        holder.countryTextView.setText(offer.getHotel().getCity().getCountry().getName() + ",");
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

    public static class FavouriteOfferViewHolder extends RecyclerView.ViewHolder {
        private final TextView hotelNameTextView, countryTextView, cityTextView, startDateTextView, daysTextView, priceTextView, foodTextView;
        private final ImageView hotelImageView;
        private final RatingBar starsRatingBar;
        private CartItem cartItem;
        private Context context;
        private FavouriteOffersRecyclerViewAdapter adapter;

        public FavouriteOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.faHotelNameTextView);
            countryTextView = itemView.findViewById(R.id.faCountryTextView);
            cityTextView = itemView.findViewById(R.id.faCityTextView);
            startDateTextView = itemView.findViewById(R.id.faStartDateTextView);
            daysTextView = itemView.findViewById(R.id.faDaysTextView);
            priceTextView = itemView.findViewById(R.id.fapriceTextView);
            foodTextView = itemView.findViewById(R.id.faFoodTextView);
            hotelImageView = itemView.findViewById(R.id.faHotelImageView);
            starsRatingBar = itemView.findViewById(R.id.faStarsRatingBar);

            itemView.findViewById(R.id.faDeleteButton).setOnClickListener(view -> {

                cartItem = adapter.data.get(getAdapterPosition());

                if (Database.userId > 0) {
                    try {
                        Database.deleteFromCart(cartItem.getOfferId());
                        Toast.makeText(context, "Pomyślnie usunięto z koszyka", Toast.LENGTH_SHORT).show();
                    } catch (SQLException exception) {
                        Toast.makeText(context, "Napotkano błąd przy usuwaniu z koszyka.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    deleteFromSharedPreferences();
                }

                adapter.data.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });

            itemView.findViewById(R.id.faDetailsButton).setOnClickListener(view -> {
                if (Database.userId > 0) {
                    Intent intent = new Intent(context, OfferDetailsActivity.class);
                    intent.putExtra("offerId", cartItem.getOfferId());
                    intent.putExtra("peopleCount", cartItem.getPeopleCount());
                    context.startActivity(intent);
                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                    alertBuilder.setMessage("Musisz utworzyć konto, by móc składać zamówienia. Czy chcesz przejsć do rejestracji?");
                    alertBuilder.setCancelable(true);
                    alertBuilder.setPositiveButton("Ok",
                            (dialog, id) -> WindowDirector.changeActivity(context, RegisterActivity.class));

                    alertBuilder.setNegativeButton("Anuluj",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog offerAlert = alertBuilder.create();
                    offerAlert.show();
                }
            });
        }

        public FavouriteOfferViewHolder linkAdapter(FavouriteOffersRecyclerViewAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        private void deleteFromSharedPreferences() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String key = "offer" + cartItem.getOfferId();
            editor.remove(key);
            editor.apply();
            Toast.makeText(context, "Pomyślnie usunięto z koszyka", Toast.LENGTH_SHORT).show();
        }
    }
}