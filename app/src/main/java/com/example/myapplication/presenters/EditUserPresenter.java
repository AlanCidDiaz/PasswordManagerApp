package com.example.myapplication.presenters;

import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;

public class EditUserPresenter {

    private Context context;
    private View view;
    private User currentUser;

    public EditUserPresenter(Context context, View view, User currentUser) {
        this.context = context;
        this.view = view;
        this.currentUser = currentUser;
    }

    // Validar y guardar cambios
    public void saveChanges(String name, String email, String altEmail, String phone) {
        if (name.isEmpty() || email.isEmpty()) {
            view.showToast("Nombre y email son requeridos");
            return;
        }

        // Actualizar usuario
        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setAltEmail(altEmail);
        currentUser.setPhone(phone);

        // Guardar en UserManager
        UserManager.getInstance().updateUser(currentUser);

        view.showToast("Perfil actualizado");
        view.finishActivity();
    }

    // Interfaz para que la Activity escuche al Presenter
    public interface View {
        void showToast(String message);
        void finishActivity();
    }
}