package com.example.myapplication.managers;

import com.example.myapplication.models.PasswordEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordManager {
    private static PasswordManager instance;
    private Map<String, List<PasswordEntry>> userPasswords = new HashMap<>();

    private PasswordManager() {
    }

    public static synchronized PasswordManager getInstance() {
        if (instance == null) {
            instance = new PasswordManager();
        }
        return instance;
    }

    public void addPassword(String email, PasswordEntry entry) {
        if (!userPasswords.containsKey(email)) {
            userPasswords.put(email, new ArrayList<>());
        }
        userPasswords.get(email).add(entry);
    }

    public List<PasswordEntry> getPasswords(String email) {
        return userPasswords.getOrDefault(email, new ArrayList<>());
    }

    public void updatePassword(String email, PasswordEntry updatedEntry) {
        List<PasswordEntry> list = getPasswords(email);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAppName().equals(updatedEntry.getAppName())) {  // Asumiendo appName único
                list.set(i, updatedEntry);
                break;
            }
        }
    }

    public void movePasswords(String oldEmail, String newEmail) {
        List<PasswordEntry> passwords = userPasswords.remove(oldEmail);
        if (passwords != null) {
            userPasswords.put(newEmail, passwords);
        }
    }

    public void deletePassword(String email, PasswordEntry entry) {
        List<PasswordEntry> list = getPasswords(email);
        list.remove(entry);
    }
}