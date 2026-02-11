package com.example.myapplication.models;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String email;
    private String altEmail;
    private String phone;
    private String password;
    private boolean isAdmin;

    public User(String name, String email, String altEmail, String phone, String password, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.altEmail = altEmail;
        this.phone = phone;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getAltEmail() {
        return altEmail;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        altEmail = in.readString();
        phone = in.readString();
        password = in.readString();
        isAdmin = in.readByte() != 0;
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(altEmail);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}