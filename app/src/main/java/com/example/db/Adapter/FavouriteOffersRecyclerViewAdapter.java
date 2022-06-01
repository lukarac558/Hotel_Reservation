package com.example.db.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.R;

import java.util.ArrayList;

import com.example.db.Class.Offer;
import com.example.db.Database.Database;

public class FavouriteOffersRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteOffersRecyclerViewAdapter.FavouriteOfferViewHolder> {

    private Context context;
    private ArrayList<Offer> data;

    public FavouriteOffersRecyclerViewAdapter(Context context, ArrayList<Offer> data) {
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

    @Override
    public void onBindViewHolder(@NonNull FavouriteOfferViewHolder holder, int position) {
        Offer offer = data.get(position);

        holder.context = context;
        holder.offer = offer;
        int id = offer.getId();
        holder.fOfferIdTextView.setText("Id: " + id);
        holder.fCountryNameTextView.setText(offer.getHotel().getName().getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FavouriteOfferViewHolder extends RecyclerView.ViewHolder {
        private TextView fOfferIdTextView;
        private TextView fCountryNameTextView;
        private Offer offer;
        private Context context;
        private FavouriteOffersRecyclerViewAdapter adapter;

        public FavouriteOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            fOfferIdTextView = itemView.findViewById(R.id.fOfferIdTextView);
            fCountryNameTextView = itemView.findViewById(R.id.fCountryNameTextView);

            itemView.findViewById(R.id.fDeleteButton).setOnClickListener( view -> {

                offer = adapter.data.get(getAdapterPosition());

                // zrobić obsługe do usuwania z SharedPreferences do niezalogowanych
                if(Database.userId > 0) {
                    int id = offer.getId();
                    Database.deleteFromCart(id);
                    Toast.makeText(context, "Pomyślnie usunięto z koszyka", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String key = "offer" + offer.getId();
                    editor.remove(key);
                    editor.apply();
                }

                adapter.data.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });

            // dodac obsluge "rezerwuj" -> jesli niezalogowany alertdialog z komunikatem i opcja przejscia do logowania

            //itemView.findViewById(R.id.fBookButton).setOnClickListener(view -> bookOffer());
        }

        public FavouriteOfferViewHolder linkAdapter(FavouriteOffersRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }
    }
}