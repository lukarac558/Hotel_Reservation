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
import com.example.db.model.Offer;
import com.example.db.R;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import com.example.db.model.Order;
import com.example.db.database.Database;

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

        holder.orderIdTextView.setText(orderId);
        holder.orderDateTextView.setText(order.getOrderDate().toString());
        holder.peopleCountTextView.setText(peopleCount);
        holder.hotelNameTextView.setText(offer.getHotel().getName());
        holder.countryTextView.setText(offer.getHotel().getCity().getCountry().getName()+",");
        holder.cityTextView.setText(offer.getHotel().getCity().getName());
        holder.startDateTextView.setText(offer.getStartDate().toString());
        holder.daysTextView.setText("(" + days + " dni)");
        holder.totalCostTextView.setText(stringTotalCost);
        holder.foodTextView.setText(offer.getFood().getType());
        holder.hotelImageView.setImageBitmap(bitmap);
        holder.starsRatingBar.setRating(offer.getHotel().getStarCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        private final TextView orderIdTextView, hotelNameTextView, orderDateTextView, peopleCountTextView, countryTextView;
        private final TextView cityTextView, startDateTextView, daysTextView, totalCostTextView, foodTextView;
        private final ImageView hotelImageView;
        private final RatingBar starsRatingBar;
        private Context context;
        private Order order;
        private OrdersRecyclerViewAdapter adapter;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orOrderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orOrdateDateTextView);
            peopleCountTextView = itemView.findViewById(R.id.orPeopleCountTextView);
            hotelNameTextView = itemView.findViewById(R.id.orHotelNameTextView);
            countryTextView = itemView.findViewById(R.id.orCountryTextView);
            cityTextView = itemView.findViewById(R.id.orCityTextView);
            startDateTextView = itemView.findViewById(R.id.orStartDateTextView);
            daysTextView = itemView.findViewById(R.id.orDaysTextView);
            totalCostTextView = itemView.findViewById(R.id.orTotalCostTextView);
            foodTextView = itemView.findViewById(R.id.orFoodTextView);
            hotelImageView = itemView.findViewById(R.id.orHotelImageView);
            starsRatingBar = itemView.findViewById(R.id.orStarsRatingBar);

                itemView.findViewById(R.id.orDeleteButton).setOnClickListener(view -> {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setMessage("Czy na pewno chcesz usunąć wybraną rezerwację?");
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
