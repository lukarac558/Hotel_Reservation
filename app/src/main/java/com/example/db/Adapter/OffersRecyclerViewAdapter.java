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

import com.example.db.Class.Offer;
import com.example.db.Database.Database;

public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<OffersRecyclerViewAdapter.OfferViewHolder> {

    private ArrayList<Offer> data;
    private Context context;

    public OffersRecyclerViewAdapter(Context context, ArrayList<Offer> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public OffersRecyclerViewAdapter.OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row_item,parent,false);
        return new OffersRecyclerViewAdapter.OfferViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersRecyclerViewAdapter.OfferViewHolder holder, int position) {
        Offer offer = data.get(position);
        holder.offer = data.get(position);
        holder.context = context;
        int id = offer.getId();

        holder.oOfferIdTextView.setText("Identyfikator oferty: " + id);
        holder.oCountryNameTextView.setText(offer.getHotel().getCountry().getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private TextView oOfferIdTextView;
        private TextView oCountryNameTextView;
        private Context context;
        private Offer offer;
        private OffersRecyclerViewAdapter adapter;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            oOfferIdTextView = itemView.findViewById(R.id.oOfferIdTextView);
            oCountryNameTextView = itemView.findViewById(R.id.oCountryNameTextView);

            itemView.findViewById(R.id.oDeleteOfferButton).setOnClickListener(view -> {

                offer = adapter.data.get(getAdapterPosition());

                Database.deleteOffer(offer.getId());

                adapter.data.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });
        }

        public OffersRecyclerViewAdapter.OfferViewHolder linkAdapter(OffersRecyclerViewAdapter adapter){
            this.adapter = adapter;
            return this;
        }

    }
}