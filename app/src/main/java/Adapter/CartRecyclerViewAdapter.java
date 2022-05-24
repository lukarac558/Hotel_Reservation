package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.OfferDetailsActivity;
import com.example.db.R;

import java.util.ArrayList;

import Classess.Offer;
import Database.Database;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder> {

    private final ArrayList<Offer> data;
    private Context context;

    public CartRecyclerViewAdapter(Context context, ArrayList<Offer> data){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_row_item,parent,false);
        return new CartRecyclerViewAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.CartViewHolder holder, int position) {
        Offer offer = data.get(position);
        holder.context = context;
        int id = offer.getId();
        String countryName = offer.getHotel().getCountry().getName();

        holder.offer = offer;
        holder.fOfferIdTextView.setText("Identyfikator oferty: " + id);
        holder.fCountryNameTextView.setText(countryName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        private TextView fOfferIdTextView;
        private TextView fCountryNameTextView;
        private Offer offer;
        private Context context;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            fOfferIdTextView = itemView.findViewById(R.id.fOfferIdTextView);
            fCountryNameTextView = itemView.findViewById(R.id.fCountryNameTextView);

            itemView.findViewById(R.id.fDeleteButton).setOnClickListener(view -> deleteFromCart());
            itemView.findViewById(R.id.fBookButton).setOnClickListener(view -> bookOffer());
        }

        private void deleteFromCart(){
            Database.deleteFromCart(offer.getId());
            Toast.makeText(context, "Pomyślnie usunięto z koszyka", Toast.LENGTH_SHORT).show();
        }

        private void bookOffer(){
            // zrobić fragment w którym użytkownik wybierze liczbę osób
        }
    }
}
