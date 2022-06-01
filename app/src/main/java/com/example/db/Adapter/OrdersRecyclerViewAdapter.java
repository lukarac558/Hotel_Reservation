package com.example.db.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.db.R;
import java.util.ArrayList;
import com.example.db.Class.Hotel;
import com.example.db.Class.Order;
import com.example.db.Database.Database;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.OrderViewHolder> {

    private ArrayList<Order> data;
    private Context context;

    public OrdersRecyclerViewAdapter(Context context, ArrayList<Order> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_item,parent,false);
        if(Database.permission.equalsIgnoreCase("user"))
            view.findViewById(R.id.bDeleteButton).setVisibility(View.INVISIBLE);
        return new OrderViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = data.get(position);
        holder.order = data.get(position);
        holder.context = context;
        int id = order.getId();
        int hotelId = order.getHotelId();
        Hotel hotel = Database.getHotelById(hotelId);

        holder.bOfferIdTextView.setText("Identyfikator oferty: " + id);
        holder.bCountryNameTextView.setText(hotel.getCountry().getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView bOfferIdTextView;
        private TextView bCountryNameTextView;
        private Context context;
        private Order order;
        private OrdersRecyclerViewAdapter adapter;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            bOfferIdTextView = itemView.findViewById(R.id.bOfferIdTextView);
            bCountryNameTextView = itemView.findViewById(R.id.bCountryNameTextView);

                itemView.findViewById(R.id.bDeleteButton).setOnClickListener(view -> {

                    order = adapter.data.get(getAdapterPosition());

                    Database.cancelOrder(order.getId());

                    adapter.data.remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                });
        }

        public OrderViewHolder linkAdapter(OrdersRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }

    }
}
