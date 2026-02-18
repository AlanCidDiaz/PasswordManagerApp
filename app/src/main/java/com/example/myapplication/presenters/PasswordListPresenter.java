package com.example.myapplication.presenters;

import android.content.Context;

import com.example.myapplication.managers.LoginSingleton;
import com.example.myapplication.managers.UserManager;
import com.example.myapplication.models.PasswordEntry;

import java.util.ArrayList;
import java.util.List;

public class PasswordListPresenter {

    private Context context;

    public PasswordListPresenter(Context context) {
        this.context = context;
    }

    // Método principal: devuelve la lista de contraseñas a mostrar
    public List<PasswordEntry> loadPasswords() {
        String emailToLoad;

        // Prioridad 1: si hay usuario logueado y es normal, usa su email
        if (UserManager.getInstance().getCurrentUser() != null &&
                !UserManager.getInstance().getCurrentUser().isAdmin()) {
            emailToLoad = UserManager.getInstance().getCurrentUser().getEmail();
        } else {
            // Prioridad 2: usa el email guardado del usuario normal registrado
            emailToLoad = LoginSingleton.getInstance().getNormalUserEmail();
            if (emailToLoad == null) {
                return new ArrayList<>(); // Lista vacía si no hay usuario registrado
            }
        }

        return UserManager.getInstance().getPasswords(emailToLoad);
    }

    // Método auxiliar para saber si hay usuario registrado
    public boolean isUserRegistered() {
        return LoginSingleton.getInstance().isRegistered();
    }

    // Método auxiliar para mostrar mensaje cuando no hay cuentas
    public String getEmptyMessage() {
        return "No tienes cuentas registradas aún. Pídele al Administrador que registre algunas.";
    }
}