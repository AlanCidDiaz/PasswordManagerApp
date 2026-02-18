package com.example.myapplication.presenters;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import com.example.myapplication.managers.LoginSingleton;
import com.example.myapplication.managers.UserManager;
import com.example.myapplication.models.User;

public class RegisterPresenter {
    private Context context;

    public RegisterPresenter(Context context) {
        this.context = context;
    }

    public boolean validateAndRegister(String name, String email, String altEmail, String phone, String password, String confirmPassword) {
        // Validaciones básicas
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Todos los campos requeridos deben estar llenos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (UserManager.getInstance().findUserByEmail(email) != null) {
            Toast.makeText(context, "Email ya registrado", Toast.LENGTH_SHORT).show();
            return false;
        }

        /// Crear y registrar usuario normal (no admin)
        User newUser = new User(name, email, altEmail, phone, password, false);
        UserManager.getInstance().addUser(newUser);

        // Login automático
        UserManager.getInstance().setCurrentUser(newUser);
        LoginSingleton.getInstance().setCurrentUser(newUser);
        LoginSingleton.getInstance().setNormalUser(newUser);
        LoginSingleton.getInstance().setRegistered(true);

        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
        return true;
    }
}