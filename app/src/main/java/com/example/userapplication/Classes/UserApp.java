package com.example.userapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class UserApp implements Parcelable {
    private int id;
    private String name;
    private String email;
    private String telp;
    private String password;
    private int saldo;
    private String role;
    private String status;

    public UserApp(int id, String name, String email, String telp, String password, int saldo, String role, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telp = telp;
        this.password = password;
        this.saldo = saldo;
        this.role = role;
        this.status = status;
    }

    protected UserApp(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        telp = in.readString();
        password = in.readString();
        saldo = in.readInt();
        role = in.readString();
        status = in.readString();
    }

    public static final Creator<UserApp> CREATOR = new Creator<UserApp>() {
        @Override
        public UserApp createFromParcel(Parcel in) {
            return new UserApp(in);
        }

        @Override
        public UserApp[] newArray(int size) {
            return new UserApp[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(telp);
        parcel.writeString(password);
        parcel.writeInt(saldo);
        parcel.writeString(role);
        parcel.writeString(status);
    }
}
