package com.example.myapplication.managers;

import com.example.myapplication.models.User;
import com.example.myapplication.models.PasswordEntry;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<User> users = new ArrayList<>();  // Lista de todos los usuarios
    private User currentUser;  // Usuario logueado actual

    private UserManager() {
        // Predefinir un admin (root)
        users.add(new User("Admin", "admin@example.com", "", "", "adminpass", true));
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
        // Ya no inicializamos userPasswords aquí → lo hace PasswordManager cuando se necesite
    }

    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public boolean validatePassword(String email, String password) {
        User user = findUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Métodos para contraseñas → ahora delegan a PasswordManager
    public void addPassword(String email, PasswordEntry entry) {
        PasswordManager.getInstance().addPassword(email, entry);
    }

    public List<PasswordEntry> getPasswords(String email) {
        return PasswordManager.getInstance().getPasswords(email);
    }

    // Para admin: gestionar usuarios
    public List<User> getAllUsers() {
        return users;
    }

    public void updateUser(User updatedUser) {
        String oldEmail = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(updatedUser.getEmail())) {
                oldEmail = users.get(i).getEmail();
                users.set(i, updatedUser);
                break;
            }
        }

        // Si cambió el email, mover contraseñas al nuevo email (usando PasswordManager)
        if (oldEmail != null && !oldEmail.equals(updatedUser.getEmail())) {
            PasswordManager.getInstance().movePasswords(oldEmail, updatedUser.getEmail());
        }
    }
}