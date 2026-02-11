package com.example.myapplication.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.models.PasswordEntry;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private List<PasswordEntry> passwords;
    private boolean isAdminView;
    private PasswordActionListener listener;

    // Array para guardar el estado de visibilidad de cada contraseña
    private boolean[] passwordVisible;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public PasswordAdapter(List<PasswordEntry> passwords, boolean isAdminView, PasswordActionListener listener) {
        this.passwords = passwords;
        this.isAdminView = isAdminView;
        this.listener = listener;
        this.passwordVisible = new boolean[passwords.size()];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordEntry entry = passwords.get(position);

        holder.tvAppName.setText(entry.getAppName());
        holder.tvUsername.setText(entry.getUsername());
        holder.tvCategory.setText("Categoría: " + entry.getCategory().name().replace("_", " "));

        // Fechas (lo nuevo)
        if (entry.getCreatedAt() != null) {
            holder.tvCreatedAt.setText("Creada: " + dateFormat.format(entry.getCreatedAt()));
        } else {
            holder.tvCreatedAt.setText("Creada: —");
        }

        if (isAdminView) {
            holder.tvUpdatedAt.setVisibility(View.VISIBLE);
            if (entry.getUpdatedAt() != null) {
                holder.tvUpdatedAt.setText("Actualizada: " + dateFormat.format(entry.getUpdatedAt()));
            } else {
                holder.tvUpdatedAt.setText("Actualizada: —");
            }
        } else {
            holder.tvUpdatedAt.setVisibility(View.GONE);
        }

        if (isAdminView) {
            // Modo admin: mostrar contraseña + toggle + botones
            holder.tvPassword.setVisibility(View.VISIBLE);
            holder.ivTogglePassword.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            if (passwordVisible[position]) {
                holder.tvPassword.setText(entry.getPassword());
                holder.ivTogglePassword.setImageResource(R.drawable.ic_eye);
            } else {
                holder.tvPassword.setText("••••••••••");
                holder.ivTogglePassword.setImageResource(R.drawable.ic_eye_off);
            }

            holder.ivTogglePassword.setOnClickListener(v -> {
                passwordVisible[position] = !passwordVisible[position];
                notifyItemChanged(position);
            });

            holder.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(entry, position);
            });

            holder.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(entry, position);
            });
        } else {
            // Modo usuario normal
            holder.tvPassword.setVisibility(View.GONE);
            holder.ivTogglePassword.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public void updateList(List<PasswordEntry> newList) {
        this.passwords = newList;
        this.passwordVisible = new boolean[newList.size()];
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppName, tvUsername, tvPassword, tvCategory;
        TextView tvCreatedAt, tvUpdatedAt;           // ← nuevos
        ImageView ivTogglePassword;
        com.google.android.material.button.MaterialButton btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvAppName     = itemView.findViewById(R.id.tvAppName);
            tvUsername    = itemView.findViewById(R.id.tvUsername);
            tvPassword    = itemView.findViewById(R.id.tvPassword);
            tvCategory    = itemView.findViewById(R.id.tvCategory);
            tvCreatedAt   = itemView.findViewById(R.id.tvCreatedAt);     // ← nuevo
            tvUpdatedAt   = itemView.findViewById(R.id.tvUpdatedAt);     // ← nuevo
            ivTogglePassword = itemView.findViewById(R.id.ivTogglePassword);
            btnEdit       = itemView.findViewById(R.id.btnEdit);
            btnDelete     = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface PasswordActionListener {
        void onEdit(PasswordEntry entry, int position);
        void onDelete(PasswordEntry entry, int position);
    }
}