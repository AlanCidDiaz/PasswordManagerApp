package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class PasswordEntry implements Parcelable {
    private String appName;
    private String username;
    private String password;
    private Category category;
    private Date createdAt;   // Fecha de creación
    private Date updatedAt;   // Fecha de última actualización (null si no se editó)

    public enum Category {
        RED_SOCIAL, BANCO, ENTRETENIMIENTO, CORREO, EDUCATIVO, OTRO
    }

    public PasswordEntry(String appName, String username, String password, Category category) {
        this.appName = appName;
        this.username = username;
        this.password = password;
        this.category = category;
        this.createdAt = new Date();  // Fecha actual al crear
        this.updatedAt = null;        // No editada aún
    }

    // Getters y setters
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    // Parcelable implementation (necesario para pasar por Intent)
    protected PasswordEntry(Parcel in) {
        appName = in.readString();
        username = in.readString();
        password = in.readString();
        category = Category.valueOf(in.readString());
        createdAt = new Date(in.readLong());
        updatedAt = in.readByte() != 0 ? new Date(in.readLong()) : null;
    }

    public static final Creator<PasswordEntry> CREATOR = new Creator<PasswordEntry>() {
        @Override
        public PasswordEntry createFromParcel(Parcel in) {
            return new PasswordEntry(in);
        }

        @Override
        public PasswordEntry[] newArray(int size) {
            return new PasswordEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(category.name());
        dest.writeLong(createdAt.getTime());
        dest.writeByte((byte) (updatedAt != null ? 1 : 0));
        if (updatedAt != null) {
            dest.writeLong(updatedAt.getTime());
        }
    }
}