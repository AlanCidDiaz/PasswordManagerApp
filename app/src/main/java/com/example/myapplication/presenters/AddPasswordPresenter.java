package com.example.myapplication.presenters;

import android.content.Context;
import android.widget.Toast;
import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;

public class AddPasswordPresenter {

    private Context context;

    public AddPasswordPresenter(Context context) {
        this.context = context;
    }

    public boolean savePassword(String appName, String username, String password, PasswordEntry.Category category) {
        if (appName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return false;
        }

        UserManager userManager = UserManager.getInstance();
        if (userManager.getCurrentUser() == null) {
            Toast.makeText(context, "Debes iniciar sesión primero", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Determinar a qué usuario asignar la cuenta
        String targetEmail = userManager.getCurrentUser().getEmail();
        if (userManager.getCurrentUser().isAdmin()) {
            // Admin agrega a un usuario normal (el primero que encuentre)
            for (User u : userManager.getAllUsers()) {
                if (!u.isAdmin()) {
                    targetEmail = u.getEmail();
                    break;
                }
            }
            if (targetEmail.equals(userManager.getCurrentUser().getEmail())) { // No encontró usuario normal
                Toast.makeText(context, "No hay usuarios normales para asignar la cuenta", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Aquí se crea la entrada → createdAt se asigna automáticamente en el constructor
        PasswordEntry entry = new PasswordEntry(appName, username, password, category);

        userManager.addPassword(targetEmail, entry);

        Toast.makeText(context, "¡Contraseña guardada en " + category.name() + "!", Toast.LENGTH_LONG).show();
        return true;
    }
}