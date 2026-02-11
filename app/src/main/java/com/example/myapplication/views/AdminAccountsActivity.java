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
import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountsActivity extends AppCompatActivity implements PasswordAdapter.PasswordActionListener {

    private RecyclerView recyclerAllAccounts;
    private TextView tvEmpty;
    private PasswordAdapter adapter;
    private List<PasswordEntry> allAccounts = new ArrayList<>();

    private final ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadAllAccounts(); // Refrescar después de editar
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
        adapter = new PasswordAdapter(allAccounts, true, this);
        recyclerAllAccounts.setAdapter(adapter);

        loadAllAccounts();
    }

    private void loadAllAccounts() {
        allAccounts.clear();

        for (User user : UserManager.getInstance().getAllUsers()) {
            if (!user.isAdmin()) {
                List<PasswordEntry> userPasswords = UserManager.getInstance().getPasswords(user.getEmail());
                allAccounts.addAll(userPasswords);
            }
        }

        if (allAccounts.isEmpty()) {
            tvEmpty.setText("No hay cuentas registradas aún.");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerAllAccounts.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerAllAccounts.setVisibility(View.VISIBLE);
            adapter.updateList(allAccounts);
        }
    }

    @Override
    public void onEdit(PasswordEntry entry, int position) {
        // Encontrar el email del dueño y el índice real en su lista
        String ownerEmail = null;
        int realIndex = -1;

        outerLoop:
        for (User user : UserManager.getInstance().getAllUsers()) {
            if (!user.isAdmin()) {
                List<PasswordEntry> userPasswords = UserManager.getInstance().getPasswords(user.getEmail());
                for (int i = 0; i < userPasswords.size(); i++) {
                    if (userPasswords.get(i) == entry) {  // Comparar por referencia (misma instancia)
                        ownerEmail = user.getEmail();
                        realIndex = i;
                        break outerLoop;
                    }
                }
            }
        }

        if (ownerEmail == null || realIndex == -1) {
            Toast.makeText(this, "Error: Cuenta no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, EditPasswordActivity.class);
        intent.putExtra("password_entry", entry);
        intent.putExtra("owner_email", ownerEmail);
        intent.putExtra("real_index", realIndex);  // ← pasamos el índice real
        editLauncher.launch(intent);
    }

    @Override
    public void onDelete(PasswordEntry entry, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar cuenta")
                .setMessage("¿Seguro que quieres eliminar " + entry.getAppName() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // Buscar y eliminar del mapa de UserManager
                    for (User user : UserManager.getInstance().getAllUsers()) {
                        if (!user.isAdmin()) {
                            List<PasswordEntry> list = UserManager.getInstance().getPasswords(user.getEmail());
                            if (list.remove(entry)) {
                                loadAllAccounts(); // Refrescar
                                Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllAccounts();
    }
}