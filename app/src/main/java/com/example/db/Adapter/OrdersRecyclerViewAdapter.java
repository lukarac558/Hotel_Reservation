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
import com.example.db.Class.Offer;
import com.example.db.R;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import com.example.db.Class.Order;
import com.example.db.Database.Database;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.OrderViewHolder> {

    private final ArrayList<Order> data;
    private final Context context;

    public OrdersRecyclerViewAdapter(Context context, ArrayList<Order> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_item,parent,false);
        if(!Database.isAdmin)
            view.findViewById(R.id.orDeleteButton).setVisibility(View.INVISIBLE);
        return new OrderViewHolder(view).linkAdapter(this);
    }

    @SuppressLint("SetTextI18n")
    
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = data.get(position);
        holder.order = data.get(position);
        holder.context = context;
        Offer offer = Database.getOfferById(order.getOfferId());

        Bitmap bitmap = Objects.requireNonNull(offer).getHotel().getImage().getBitmap();
        String stringTotalCost = String.valueOf(order.getTotalCost());
        String orderId = String.valueOf(order.getId());
        String peopleCount = String.valueOf(order.getPeopleCount());

        long days = ChronoUnit.DAYS.between(offer.getStartDate(), offer.getEndDate());

        holder.orOrderIdTextView.setText(orderId);
        holder.orOrderDateTextView.setText(order.getOrderDate().toString());
        holder.orPeopleCountTextView.setText(peopleCount);
        holder.orHotelNameTextView.setText(offer.getHotel().getName());
        holder.orCountryTextView.setText(offer.getHotel().getCity().getCountry().getName()+",");
        holder.orCityTextView.setText(offer.getHotel().getCity().getName());
        holder.orStartDateTextView.setText(offer.getStartDate().toString());
        holder.orDaysTextView.setText("(" + days + " dni)");
        holder.orTotalCostTextView.setText(stringTotalCost);
        holder.orFoodTextView.setText(offer.getFood().getType());
        holder.orHotelImageView.setImageBitmap(bitmap);
        holder.orStarsRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        private final TextView orOrderIdTextView, orHotelNameTextView, orOrderDateTextView, orPeopleCountTextView, orCountryTextView;
        private final TextView orCityTextView, orStartDateTextView, orDaysTextView, orTotalCostTextView, orFoodTextView;
        private final ImageView orHotelImageView;
        private final RatingBar orStarsRatingBar;
        private Context context;
        private Order order;
        private OrdersRecyclerViewAdapter adapter;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orOrderIdTextView = itemView.findViewById(R.id.orOrderIdTextView);
            orOrderDateTextView = itemView.findViewById(R.id.orOrdateDateTextView);
            orPeopleCountTextView = itemView.findViewById(R.id.orPeopleCountTextView);
            orHotelNameTextView = itemView.findViewById(R.id.orHotelNameTextView);
            orCountryTextView = itemView.findViewById(R.id.orCountryTextView);
            orCityTextView = itemView.findViewById(R.id.orCityTextView);
            orStartDateTextView = itemView.findViewById(R.id.orStartDateTextView);
            orDaysTextView = itemView.findViewById(R.id.orDaysTextView);
            orTotalCostTextView = itemView.findViewById(R.id.orTotalCostTextView);
            orFoodTextView = itemView.findViewById(R.id.orFoodTextView);
            orHotelImageView = itemView.findViewById(R.id.orHotelImageView);
            orStarsRatingBar = itemView.findViewById(R.id.orStarsRatingBar);

                itemView.findViewById(R.id.orDeleteButton).setOnClickListener(view -> {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setMessage("Czy na pewno chcesz usunąć wybrany element z bazy wraz z jego powiązaniami?");
                    alertBuilder.setCancelable(true);
                    alertBuilder.setPositiveButton("Ok",
                            (dialog, id) -> {
                                order = adapter.data.get(getAdapterPosition());

                                try {
                                    Database.cancelOrder(order.getId(), order.getOfferId(), order.getPeopleCount());
                                    Toast.makeText(context, "Pomyślnie anulowano rezerwację", Toast.LENGTH_SHORT).show();
                                } catch (SQLException exception) {
                                    Toast.makeText(context, "Napotkano błąd przy anulowaniu rezerwacji.", Toast.LENGTH_SHORT).show();
                                }

                                adapter.data.remove(getAdapterPosition());
                                adapter.notifyItemRemoved(getAdapterPosition());
                            });
                    alertBuilder.setNegativeButton("Anuluj",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog orderAlert = alertBuilder.create();
                    orderAlert.show();
                });
        }

        public OrderViewHolder linkAdapter(OrdersRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }

    }
}
