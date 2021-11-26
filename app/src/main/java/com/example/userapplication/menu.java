package com.example.userapplication;

public class menu {
    String id, nama_menu, harga_menu, deskripsi_menu, jenis_menu, status_menu;

    public menu(String id, String nama_menu, String harga_menu, String deskripsi_menu, String jenis_menu, String status_menu) {
        this.id = id;
        this.nama_menu = nama_menu;
        this.harga_menu = harga_menu;
        this.deskripsi_menu = deskripsi_menu;
        this.jenis_menu = jenis_menu;
        this.status_menu = status_menu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getHarga_menu() {
        return harga_menu;
    }

    public void setHarga_menu(String harga_menu) {
        this.harga_menu = harga_menu;
    }

    public String getDeskripsi_menu() {
        return deskripsi_menu;
    }

    public void setDeskripsi_menu(String deskripsi_menu) {
        this.deskripsi_menu = deskripsi_menu;
    }

    public String getJenis_menu() {
        return jenis_menu;
    }

    public void setJenis_menu(String jenis_menu) {
        this.jenis_menu = jenis_menu;
    }

    public String getStatus_menu() {
        return status_menu;
    }

    public void setStatus_menu(String status_menu) {
        this.status_menu = status_menu;
    }
}
