package com.example.myapplication.presenters;

import android.content.Context;

import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountsPresenter {

    private Context context;
    private View view;  // Interfaz para comunicarse con la Activity

    public AdminAccountsPresenter(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    // Cargar todas las cuentas de usuarios normales
    public void loadAllAccounts() {
        List<PasswordEntry> allAccounts = new ArrayList<>();

        for (User user : UserManager.getInstance().getAllUsers()) {
            if (!user.isAdmin()) {
                List<PasswordEntry> userPasswords = UserManager.getInstance().getPasswords(user.getEmail());
                allAccounts.addAll(userPasswords);
            }
        }

        view.showAccounts(allAccounts);
    }

    // Eliminar cuenta
    public void deletePassword(PasswordEntry entry) {
        boolean deleted = false;
        for (User user : UserManager.getInstance().getAllUsers()) {
            if (!user.isAdmin()) {
                List<PasswordEntry> list = UserManager.getInstance().getPasswords(user.getEmail());
                if (list.remove(entry)) {
                    deleted = true;
                    break;
                }
            }
        }

        if (deleted) {
            view.showToast("Cuenta eliminada");
            loadAllAccounts();  // Refrescar lista
        } else {
            view.showToast("Error al eliminar cuenta");
        }
    }

    // Editar cuenta (solo notifica a la View para abrir edición)
    public void editPassword(PasswordEntry entry) {
        // Encontrar el email del dueño y el índice real
        String ownerEmail = null;
        int realIndex = -1;

        outerLoop:
        for (User user : UserManager.getInstance().getAllUsers()) {
            if (!user.isAdmin()) {
                List<PasswordEntry> userPasswords = UserManager.getInstance().getPasswords(user.getEmail());
                for (int i = 0; i < userPasswords.size(); i++) {
                    if (userPasswords.get(i) == entry) {
                        ownerEmail = user.getEmail();
                        realIndex = i;
                        break outerLoop;
                    }
                }
            }
        }

        if (ownerEmail == null || realIndex == -1) {
            view.showToast("Error: Cuenta no encontrada");
        } else {
            view.openEditPassword(entry, ownerEmail, realIndex);
        }
    }

    // Interfaz para que la Activity escuche al Presenter
    public interface View {
        void showAccounts(List<PasswordEntry> accounts);
        void showToast(String message);
        void openEditPassword(PasswordEntry entry, String ownerEmail, int realIndex);
    }
}