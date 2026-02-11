package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;

import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements UserAdapter.UserActionListener {

    private RecyclerView recyclerUsers;
    private TextView tvEmpty;
    private UserAdapter adapter;

    private static final int EDIT_USER_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerUsers = findViewById(R.id.recyclerUsers);
        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(List.of(), this);
        recyclerUsers.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        List<User> users = UserManager.getInstance().getAllUsers();

        if (users.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerUsers.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerUsers.setVisibility(View.VISIBLE);
            adapter.updateList(users);
        }
    }

    @Override
    public void onDelete(User user, int position) {
        if (user.isAdmin()) {
            Toast.makeText(this, "No puedes eliminar al administrador", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Seguro que quieres eliminar a " + user.getName() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    UserManager.getInstance().getAllUsers().remove(user);
                    loadUsers();
                    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers(); // Refrescar después de editar usuario
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_USER_REQUEST_CODE && resultCode == RESULT_OK) {
            loadUsers();
        }
    }
}