package com.example.myapplication.managers;

import com.example.myapplication.models.User;

public class LoginSingleton {
    private static LoginSingleton instance;
    private User currentUser;
    private boolean isRegistered = false;
    private String normalUserEmail = null;  // ← email del usuario normal registrado

    private LoginSingleton() {
    }

    public static synchronized LoginSingleton getInstance() {
        if (instance == null) {
            instance = new LoginSingleton();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setNormalUserEmail(String email) {
        this.normalUserEmail = email;
    }

    public String getNormalUserEmail() {
        return normalUserEmail;
    }
}