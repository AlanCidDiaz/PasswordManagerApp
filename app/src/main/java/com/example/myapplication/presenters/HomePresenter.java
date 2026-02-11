package com.example.myapplication.presenters;

import android.widget.TextView;

import com.example.myapplication.managers.UserManager;

public class HomePresenter {
    public void loadHome(TextView tvWelcome, TextView tvContent) {
        if (UserManager.getInstance().getCurrentUser() == null) return;

        String name = UserManager.getInstance().getCurrentUser().getName();
        tvWelcome.setText("Bienvenido, " + name);

        if (UserManager.getInstance().getCurrentUser().isAdmin()) {
            tvContent.setText("Modo Admin: Gestiona usuarios\n- Lista de usuarios: " + UserManager.getInstance().getAllUsers().size());
            // Aquí agrega botones para editar usuarios, etc.
        } else {
            tvContent.setText("Modo Normal: Tus contraseñas\n- Total: " + UserManager.getInstance().getPasswords(UserManager.getInstance().getCurrentUser().getEmail()).size());
            // Aquí agrega UI para agregar/ver contraseñas por categoría
        }
    }
}