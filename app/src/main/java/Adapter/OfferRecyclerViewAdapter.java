package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.OfferDetailsActivity;
import com.example.db.R;

import java.util.ArrayList;

import Classess.Offer;

public class OfferRecyclerViewAdapter extends RecyclerView.Adapter<OfferRecyclerViewAdapter.OfferViewHolder> {

    private final ArrayList<Offer> data;
    private Context context;

    public OfferRecyclerViewAdapter(Context context, ArrayList<Offer> data){
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_row_item,parent,false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = data.get(position);
        holder.context = context;
        int id = offer.getId();
        String countryName = offer.getHotel().getCountry().getName();

        holder.offer = offer;
        holder.offerIdTextView.setText("Identyfikator oferty: " + id);
        holder.countryNameTextView.setText(countryName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        private TextView offerIdTextView;
        private TextView countryNameTextView;
        private Offer offer;
        private Context context;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerIdTextView = itemView.findViewById(R.id.fOfferIdTextView);
            countryNameTextView = itemView.findViewById(R.id.fCountryNameTextView);

            itemView.findViewById(R.id.fBookButton).setOnClickListener(view -> getItem());
        }

        private void getItem(){
            Intent intent = new Intent(context, OfferDetailsActivity.class);
            intent.putExtra("offer", offer);
            context.startActivity(intent);
        }
    }
}