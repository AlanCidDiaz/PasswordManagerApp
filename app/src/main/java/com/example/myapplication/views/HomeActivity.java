package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.managers.UserManager;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvRole = findViewById(R.id.tvRole);
        MaterialButton btnAddPassword = findViewById(R.id.btnAddPassword);
        MaterialButton btnViewCuentasNormal = findViewById(R.id.btnViewCuentasNormal);
        MaterialButton btnViewTodasCuentas = findViewById(R.id.btnViewTodasCuentas);
        MaterialButton btnManageUsers = findViewById(R.id.btnManageUsers);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        if (UserManager.getInstance().getCurrentUser() == null) {
            finish();
            return;
        }

        String name = UserManager.getInstance().getCurrentUser().getName();
        tvWelcome.setText("Bienvenido, " + name);

        boolean isAdmin = UserManager.getInstance().getCurrentUser().isAdmin();

        if (isAdmin) {
            tvRole.setText("Administrador");
            btnAddPassword.setVisibility(View.VISIBLE);
            btnViewTodasCuentas.setVisibility(View.VISIBLE);
            btnManageUsers.setVisibility(View.VISIBLE);
            btnViewCuentasNormal.setVisibility(View.GONE);

            btnAddPassword.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, AddPasswordActivity.class);
                startActivity(intent);
            });

            btnViewTodasCuentas.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, AdminAccountsActivity.class);
                startActivity(intent);
            });

            btnManageUsers.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ManageUsersActivity.class);
                startActivity(intent);
            });
        } else {
            tvRole.setText("Usuario");
            btnAddPassword.setVisibility(View.GONE);
            btnViewTodasCuentas.setVisibility(View.GONE);
            btnManageUsers.setVisibility(View.GONE);
            btnViewCuentasNormal.setVisibility(View.VISIBLE);

            btnViewCuentasNormal.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, PasswordListActivity.class);
                startActivity(intent);
            });
        }

        btnVolver.setOnClickListener(v -> {
            finish();  // Vuelve a MainActivity (logueado)
        });
    }
}