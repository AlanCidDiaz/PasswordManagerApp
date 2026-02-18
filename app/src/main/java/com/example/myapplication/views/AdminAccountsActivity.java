package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.presenters.AdminAccountsPresenter;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountsActivity extends AppCompatActivity implements AdminAccountsPresenter.View, PasswordAdapter.PasswordActionListener {

    private RecyclerView recyclerAllAccounts;
    private TextView tvEmpty;
    private PasswordAdapter adapter;
    private AdminAccountsPresenter presenter;

    private final ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    presenter.loadAllAccounts(); // Refrescar después de editar
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accounts);

        recyclerAllAccounts = findViewById(R.id.recyclerAllAccounts);
        tvEmpty = findViewById(R.id.tvEmpty);

        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText("Todas las Cuentas");
        }

        recyclerAllAccounts.setLayoutManager(new LinearLayoutManager(this));

        // Crear Presenter y pasarle this como View
        presenter = new AdminAccountsPresenter(this, this);

        adapter = new PasswordAdapter(new ArrayList<>(), true, this);
        recyclerAllAccounts.setAdapter(adapter);

        presenter.loadAllAccounts();  // Cargar cuentas desde Presenter
    }

    // Implementación de la interfaz View del Presenter
    @Override
    public void showAccounts(List<PasswordEntry> accounts) {
        if (accounts.isEmpty()) {
            tvEmpty.setText("No hay cuentas registradas por usuarios.");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerAllAccounts.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerAllAccounts.setVisibility(View.VISIBLE);
            adapter.updateList(accounts);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openEditPassword(PasswordEntry entry, String ownerEmail, int realIndex) {
        Intent intent = new Intent(this, EditPasswordActivity.class);
        intent.putExtra("password_entry", entry);
        intent.putExtra("owner_email", ownerEmail);
        intent.putExtra("real_index", realIndex);
        editLauncher.launch(intent);
    }

    // Implementación de PasswordAdapter.PasswordActionListener
    @Override
    public void onEdit(PasswordEntry entry, int position) {
        presenter.editPassword(entry);
    }

    @Override
    public void onDelete(PasswordEntry entry, int position) {
        presenter.deletePassword(entry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadAllAccounts();
    }
}