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
import com.example.myapplication.presenters.ManageUsersPresenter;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements ManageUsersPresenter.View, UserAdapter.UserActionListener {

    private RecyclerView recyclerUsers;
    private TextView tvEmpty;
    private UserAdapter adapter;
    private ManageUsersPresenter presenter;

    private static final int EDIT_USER_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerUsers = findViewById(R.id.recyclerUsers);
        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        // Crear Presenter y pasarle this como View
        presenter = new ManageUsersPresenter(this, this);

        adapter = new UserAdapter(new ArrayList<>(), this);
        recyclerUsers.setAdapter(adapter);

        presenter.loadUsers();  // Cargar usuarios desde Presenter
    }

    // Implementación de la interfaz View del Presenter
    @Override
    public void showUsers(List<User> users) {
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
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Implementación de UserAdapter.UserActionListener
    @Override
    public void onDelete(User user, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Seguro que quieres eliminar a " + user.getName() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    presenter.deleteUser(user);  // Delegar al Presenter
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadUsers();  // Refrescar desde Presenter
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_USER_REQUEST_CODE && resultCode == RESULT_OK) {
            presenter.loadUsers();  // Refrescar después de editar
        }
    }
}