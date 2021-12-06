package com.example.userapplication.Classes;

public class DJual {
    private String nota;
    private String nama_menu;
    private String jenis_menu;
    private double rating;
    private int quantity;
    private int harga;
    private String gambar;

    public DJual(String nota, String nama_menu, String jenis_menu, double rating, int quantity, int harga, String gambar) {
        this.nota = nota;
        this.nama_menu = nama_menu;
        this.jenis_menu = jenis_menu;
        this.rating = rating;
        this.quantity = quantity;
        this.harga = harga;
        this.gambar = "https://drive.google.com/uc?id="+gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getJenis_menu() {
        return jenis_menu;
    }

    public void setJenis_menu(String jenis_menu) {
        this.jenis_menu = jenis_menu;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
