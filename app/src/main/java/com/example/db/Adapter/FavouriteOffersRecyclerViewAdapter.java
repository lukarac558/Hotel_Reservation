package com.example.db.Adapter;

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
import com.example.db.Activity.OfferDetailsActivity;
import com.example.db.Activity.RegisterActivity;
import com.example.db.Class.CartItem;
import com.example.db.R;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.db.Class.Offer;
import com.example.db.Database.Database;

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

        holder.faHotelNameTextView.setText(offer.getHotel().getName());
        holder.faCountryTextView.setText(offer.getHotel().getCity().getCountry().getName()+",");
        holder.faCityTextView.setText(offer.getHotel().getCity().getName());
        holder.faStartDateTextView.setText(offer.getStartDate().toString());
        holder.faDaysTextView.setText("(" + days + " dni)");
        holder.faPriceTextView.setText(stringPrice);
        holder.faFoodTextView.setText(offer.getFood().getType());
        holder.faHotelImageView.setImageBitmap(bitmap);
        holder.faRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FavouriteOfferViewHolder extends RecyclerView.ViewHolder {
        private final TextView faHotelNameTextView, faCountryTextView, faCityTextView, faStartDateTextView, faDaysTextView, faPriceTextView, faFoodTextView;
        private final ImageView faHotelImageView;
        private final RatingBar faRatingBar;
        private CartItem cartItem;
        private Context context;
        private FavouriteOffersRecyclerViewAdapter adapter;

        public FavouriteOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            faHotelNameTextView = itemView.findViewById(R.id.faHotelNameTextView);
            faCountryTextView = itemView.findViewById(R.id.faCountryTextView);
            faCityTextView = itemView.findViewById(R.id.faCityTextView);
            faStartDateTextView = itemView.findViewById(R.id.faStartDateTextView);
            faDaysTextView = itemView.findViewById(R.id.faDaysTextView);
            faPriceTextView = itemView.findViewById(R.id.fapriceTextView);
            faFoodTextView = itemView.findViewById(R.id.faFoodTextView);
            faHotelImageView = itemView.findViewById(R.id.faHotelImageView);
            faRatingBar = itemView.findViewById(R.id.faStarsRatingBar);

            itemView.findViewById(R.id.faDeleteButton).setOnClickListener(view -> {

                cartItem = adapter.data.get(getAdapterPosition());

                if (Database.userId > 0) {
                    int id = cartItem.getOfferId();

                    try {
                        Database.deleteFromCart(id);
                        Toast.makeText(context, "Pomyślnie usunięto z koszyka", Toast.LENGTH_SHORT).show();
                    } catch (SQLException exception) {
                        Toast.makeText(context, "Napotkano błąd przy usuwaniu z koszyka.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String key = "offer" + cartItem.getOfferId();
                    editor.remove(key);
                    editor.apply();
                }

                adapter.data.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });


            itemView.findViewById(R.id.faDetailsButton).setOnClickListener(view -> {
                if (Database.userId > 0) {
                    Intent intent = new Intent(context, OfferDetailsActivity.class);
                    intent.putExtra("offer", cartItem.getOfferId());
                    intent.putExtra("peopleCount", cartItem.getPeopleCount());
                    context.startActivity(intent);
                }
                else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(itemView.getContext());
                    alertBuilder.setMessage("Musisz utworzyć konto, by móc składać zamówienia. Czy chcesz przejsć do rejestracji?");
                    alertBuilder.setCancelable(true);
                    alertBuilder.setPositiveButton("Ok",
                            (dialog, id) -> {
                                Intent intent = new Intent(context, RegisterActivity.class);
                                context.startActivity(intent);
                            });
                    alertBuilder.setNegativeButton("Anuluj",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog offerAlert = alertBuilder.create();
                    offerAlert.show();
                }
            });
        }

        public FavouriteOfferViewHolder linkAdapter(FavouriteOffersRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }
    }
}