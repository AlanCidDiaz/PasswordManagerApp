package com.example.myapplication.views;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;
    private UserActionListener listener;

    public UserAdapter(List<User> users, UserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvPhone.setText("Tel: " + user.getPhone());
        holder.tvType.setText("Tipo: " + (user.isAdmin() ? "Administrador" : "Normal"));

        holder.btnEditUser.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), EditUserActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("position", position);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnDeleteUser.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone, tvType;
        com.google.android.material.button.MaterialButton btnEditUser, btnDeleteUser;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvType = itemView.findViewById(R.id.tvType);
            btnEditUser = itemView.findViewById(R.id.btnEditUser);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }

    public void updateList(List<User> newList) {
        this.users = newList;
        notifyDataSetChanged();
    }

    public interface UserActionListener {
        void onDelete(User user, int position);
        // onEdit lo manejamos directamente en el adapter para lanzar actividad
    }
}