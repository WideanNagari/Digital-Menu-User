package com.example.userapplication;

public class Table {
    private int id;
    private String nomor_meja;
    private String kode;

    public Table(int id, String nomor_meja, String kode) {
        this.id = id;
        this.nomor_meja = nomor_meja;
        this.kode = kode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomor_meja() {
        return nomor_meja;
    }

    public void setNomor_meja(String nomor_meja) {
        this.nomor_meja = nomor_meja;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
}
