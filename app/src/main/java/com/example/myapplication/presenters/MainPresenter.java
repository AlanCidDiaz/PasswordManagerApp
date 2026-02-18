package com.example.myapplication.presenters;

import android.content.Context;

import com.example.myapplication.managers.LoginSingleton;
import com.example.myapplication.managers.UserManager;
import com.example.myapplication.models.User;

public class MainPresenter {

    private Context context;
    private View view;

    public MainPresenter(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    // Método principal: actualizar la UI según estado
    public void updateUI() {
        boolean isRegistered = LoginSingleton.getInstance().isRegistered();

        if (isRegistered) {
            // Obtener nombre del usuario actual
            String name = "";
            User currentUser = UserManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                name = currentUser.getName();
            }

            view.showRegisteredState("¡Bienvenido de nuevo, " + name + "!");
        } else {
            view.showInitialState("Bienvenido");
        }
    }

    // Interfaz para que la Activity escuche al Presenter
    public interface View {
        void showRegisteredState(String welcomeText);
        void showInitialState(String welcomeText);
    }
}