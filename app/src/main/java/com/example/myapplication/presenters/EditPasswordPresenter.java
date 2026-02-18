package com.example.myapplication.presenters;

import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.managers.UserManager;

import java.util.Date;
import java.util.List;

public class EditPasswordPresenter {

    private Context context;
    private View view;
    private PasswordEntry currentEntry;
    private String ownerEmail;
    private int realIndex;

    public EditPasswordPresenter(Context context, View view, PasswordEntry entry, String ownerEmail, int realIndex) {
        this.context = context;
        this.view = view;
        this.currentEntry = entry;
        this.ownerEmail = ownerEmail;
        this.realIndex = realIndex;
    }

    // Validar y guardar cambios
    public void saveChanges(String appName, String username, String password, PasswordEntry.Category category) {
        if (appName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            view.showToast("Todos los campos son requeridos");
            return;
        }

        PasswordEntry updatedEntry = new PasswordEntry(appName, username, password, category);
        updatedEntry.setCreatedAt(currentEntry.getCreatedAt());
        updatedEntry.setUpdatedAt(new Date());

        // Guardar en la lista del dueño
        List<PasswordEntry> list = UserManager.getInstance().getPasswords(ownerEmail);
        if (realIndex >= 0 && realIndex < list.size()) {
            list.set(realIndex, updatedEntry);
            view.showToast("Cuenta actualizada");
            view.finishActivity();
        } else {
            view.showToast("Error: posición inválida");
        }
    }

    // Interfaz para que la Activity escuche al Presenter
    public interface View {
        void showToast(String message);
        void finishActivity();
    }
}