package com.example.myapplication.presenters;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.User;
import com.example.myapplication.views.AdminAccountsActivity;  // O la actividad que quieras abrir después
import com.example.myapplication.managers.UserManager;
import com.example.myapplication.managers.LoginSingleton;
import com.example.myapplication.views.HomeActivity;

public class LoginPresenter {
    private Context context;
    private ActivityResultLauncher<Intent> pinLauncher;

    public LoginPresenter(AppCompatActivity activity) {
        this.context = activity;
        // Launcher para confirmar PIN del teléfono
        pinLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        // PIN confirmado → abrir la vista de admin (elige una)
                        Intent intent = new Intent(context, HomeActivity.class); // ← Cambia por la que quieras
                        context.startActivity(intent);
                        ((AppCompatActivity) context).finish();
                    } else {
                        Toast.makeText(context, "PIN incorrecto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void login(String email, String password) {
        UserManager userManager = UserManager.getInstance();
        if (userManager.validatePassword(email, password)) {
            User user = userManager.findUserByEmail(email);
            if (user != null && user.isAdmin()) {
                userManager.setCurrentUser(user);
                LoginSingleton.getInstance().setCurrentUser(user);
                // Pedir PIN del teléfono
                KeyguardManager keyguard = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                Intent intent = keyguard.createConfirmDeviceCredentialIntent("Confirma tu PIN", "Para ingresar como admin");
                if (intent != null) {
                    pinLauncher.launch(intent);
                } else {
                    // Si no hay PIN, ir directo (para pruebas)
                    Intent adminIntent = new Intent(context, AdminAccountsActivity.class); // ← Cambia por la que quieras
                    context.startActivity(adminIntent);
                    ((AppCompatActivity) context).finish();
                }
            } else {
                Toast.makeText(context, "Este login es exclusivo para administradores", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
        }
    }
}