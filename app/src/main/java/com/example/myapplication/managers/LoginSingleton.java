package com.example.myapplication.managers;

import com.example.myapplication.models.User;

public class LoginSingleton {
    private static LoginSingleton instance;
    private User currentUser;
    private User normalUser;
    private boolean isRegistered = false;

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

    public void setNormalUser(User user) {
        this.normalUser = user;
        this.isRegistered = true;
    }

    public User getNormalUser() {
        return normalUser;
    }

    // Método que faltaba: setRegistered (lo usan RegisterPresenter y otros)
    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    // Método que faltaba: getNormalUserEmail (lo usa PasswordListPresenter)
    public String getNormalUserEmail() {
        if (normalUser != null) {
            return normalUser.getEmail();
        }
        return null;
    }

    public void logout() {
        currentUser = null;
    }

    public void logoutAdmin() {
        currentUser = normalUser;
    }
}