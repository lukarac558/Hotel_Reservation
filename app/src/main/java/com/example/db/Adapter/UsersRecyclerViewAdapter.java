package com.example.db.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.Activity.LoginActivity;
import com.example.db.Class.User;
import com.example.db.Database.Database;
import com.example.db.R;

import java.sql.SQLException;
import java.util.ArrayList;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.UsersViewHolder> {

    private final ArrayList<User> data;
    private final Context context;

    public UsersRecyclerViewAdapter(Context context, ArrayList<User> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersRecyclerViewAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);
        return new UsersViewHolder(view).linkAdapter(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UsersRecyclerViewAdapter.UsersViewHolder holder, int position) {
        User user = data.get(position);
        holder.user = user;
        holder.context = context;

        holder.uLoginTextView.setText(user.getLogin());
        holder.uEmailTextView.setText(user.getEmail());
        holder.uPermissionTextView.setText(getStringPermission(user.getIsAdmin()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        private final TextView uLoginTextView, uEmailTextView, uPermissionTextView;
        private User user;
        private Context context;
        private UsersRecyclerViewAdapter adapter;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            uLoginTextView = itemView.findViewById(R.id.uLoginTextView);
            uEmailTextView = itemView.findViewById(R.id.uEmailTextView);
            uPermissionTextView = itemView.findViewById(R.id.uPermissionTextView);

            itemView.findViewById(R.id.userDeleteButton).setOnClickListener(view -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setMessage("Czy na pewno chcesz zmienić uprawnienia użytkownika?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok",
                        (dialog, id) -> {
                            user = adapter.data.get(getAdapterPosition());

                            try {
                                if (Database.possibleToChangePermission(user.getId())) {
                                    Database.changeUserPermission(user.getId(), user.getIsAdmin());
                                    Toast.makeText(context, "Pomyślnie zmieniono uprawnienia", Toast.LENGTH_SHORT).show();

                                    user.setPermission(!user.getIsAdmin());
                                    adapter.notifyItemChanged(getAdapterPosition());

                                    if (Database.userId == user.getId()) {
                                        Database.logOut();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(context, "Nie można zmienić uprawnień. Użytkownik złożył już zamówienie.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (SQLException exception) {
                                Toast.makeText(context, "Błąd: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(context, "Napotkano błąd przy próbie zmiany uprawnień.", Toast.LENGTH_SHORT).show();
                            }

                        });
                alertBuilder.setNegativeButton("Anuluj",
                        (dialog, id) -> dialog.cancel());

                AlertDialog orderAlert = alertBuilder.create();
                orderAlert.show();
            });
        }

        public UsersRecyclerViewAdapter.UsersViewHolder linkAdapter(UsersRecyclerViewAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

    private String getStringPermission(boolean isAdmin) {
        if (isAdmin)
            return "Administrator";
        else
            return "Użytkownik";
    }

}
