package com.example.db.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.db.R;
import java.util.ArrayList;
import com.example.db.Class.Hotel;
import com.example.db.Database.Database;

public class HotelsRecyclerViewAdapter extends RecyclerView.Adapter<HotelsRecyclerViewAdapter.HotelViewHolder> {

    private ArrayList<Hotel> data;
    private Context context;

    public HotelsRecyclerViewAdapter(Context context, ArrayList<Hotel> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_row_item,parent,false);
        return new HotelViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelsRecyclerViewAdapter.HotelViewHolder holder, int position) {
        Hotel hotel = data.get(position);
        holder.hotel = data.get(position);
        holder.context = context;
        int id = hotel.getId();
        Bitmap bitmap = hotel.getImage().getBitmap();
        holder.hotelImageView.setImageBitmap(bitmap);
        holder.dCountryTextView.setText("Hotel id: " + id);
        holder.dHotelnameTextView.setText(hotel.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder{
        private ImageView hotelImageView;
        private RecyclerView dHotelsRecyclerView;
        private TextView dCountryTextView;
        private TextView dHotelnameTextView;
        private Context context;
        private Hotel hotel;
        private HotelsRecyclerViewAdapter adapter;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            dHotelsRecyclerView = itemView.findViewById(R.id.dHotelsRecyclerView);
            hotelImageView = itemView.findViewById(R.id.dHotelImageView);
            dCountryTextView = itemView.findViewById(R.id.dCountryTextView);
            dHotelnameTextView = itemView.findViewById(R.id.dHotelNameTextView);

            itemView.findViewById(R.id.dHotelDelete).setOnClickListener(view -> {

                hotel = adapter.data.get(getAdapterPosition());

                Database.deleteHotel(hotel.getId());

                adapter.data.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });
        }

        public HotelsRecyclerViewAdapter.HotelViewHolder linkAdapter(HotelsRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }

    }
}
