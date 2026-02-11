package com.example.myapplication.managers;

import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private static UserManager instance;
    private List<User> users = new ArrayList<>();  // Lista de todos los usuarios
    private Map<String, List<PasswordEntry>> userPasswords = new HashMap<>();  // Clave: email, Valor: lista de contraseñas
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
        userPasswords.put(user.getEmail(), new ArrayList<>());  // Inicializa lista de contraseñas vacía
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

    // Métodos para contraseñas (para usuarios normales)
    public void addPassword(String email, PasswordEntry entry) {
        if (userPasswords.containsKey(email)) {
            userPasswords.get(email).add(entry);
        }
    }

    public List<PasswordEntry> getPasswords(String email) {
        return userPasswords.getOrDefault(email, new ArrayList<>());
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

        // Si cambió el email, actualizar clave en userPasswords
        if (oldEmail != null && !oldEmail.equals(updatedUser.getEmail())) {
            List<PasswordEntry> passwords = userPasswords.remove(oldEmail);
            if (passwords != null) {
                userPasswords.put(updatedUser.getEmail(), passwords);
            }
        }
    }
}