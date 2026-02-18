package com.example.myapplication.presenters;

import android.content.Context;

import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;

import java.util.List;

public class ManageUsersPresenter {

    private Context context;
    private View view;

    public ManageUsersPresenter(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    // Cargar todos los usuarios
    public void loadUsers() {
        List<User> users = UserManager.getInstance().getAllUsers();
        view.showUsers(users);
    }

    // Eliminar usuario (con validación)
    public void deleteUser(User user) {
        if (user.isAdmin()) {
            view.showToast("No puedes eliminar al administrador");
            return;
        }

        // Confirmación ya está en la View, aquí solo ejecutamos
        UserManager.getInstance().getAllUsers().remove(user);
        view.showToast("Usuario eliminado");
        loadUsers(); // Refrescar lista
    }

    // Interfaz para que la Activity escuche al Presenter
    public interface View {
        void showUsers(List<User> users);
        void showToast(String message);
    }
}