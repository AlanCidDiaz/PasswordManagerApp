package com.example.myapplication.views;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.managers.UserManager;
import com.example.myapplication.managers.LoginSingleton;

import java.util.List;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView recyclerPasswords;
    private TextView tvEmpty;
    private PasswordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        recyclerPasswords = findViewById(R.id.recyclerPasswords);
        tvEmpty = findViewById(R.id.tvEmpty);

        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText("Mis Cuentas");
        }

        recyclerPasswords.setLayoutManager(new LinearLayoutManager(this));

        // Pasar null como listener (no se usan editar/eliminar en modo normal)
        adapter = new PasswordAdapter(List.of(), false, null);
        recyclerPasswords.setAdapter(adapter);

        loadPasswords();
    }

    private void loadPasswords() {
        String emailToLoad;

        // Si hay usuario logueado normal, usa su email
        if (UserManager.getInstance().getCurrentUser() != null && !UserManager.getInstance().getCurrentUser().isAdmin()) {
            emailToLoad = UserManager.getInstance().getCurrentUser().getEmail();
        } else {
            // Si no hay sesión normal (ej: después de admin), usa el email guardado del registro
            emailToLoad = LoginSingleton.getInstance().getNormalUserEmail();
            if (emailToLoad == null) {
                tvEmpty.setText("No hay usuario registrado.");
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerPasswords.setVisibility(View.GONE);
                return;
            }
        }

        List<PasswordEntry> passwords = UserManager.getInstance().getPasswords(emailToLoad);

        if (passwords.isEmpty()) {
            tvEmpty.setText("No tienes cuentas registradas aún. Pídele al Administrador que registre algunas.");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerPasswords.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerPasswords.setVisibility(View.VISIBLE);
            adapter.updateList(passwords);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPasswords();  // Refresca al volver
    }
}