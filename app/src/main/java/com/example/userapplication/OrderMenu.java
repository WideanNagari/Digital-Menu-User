package com.example.userapplication;

import android.os.Parcel;

public class OrderMenu extends Menu {
    private int jumlah;
    private boolean confirm;
    private String status;

    public OrderMenu(String id, String nama_menu, String harga_menu, String deskripsi_menu, String jenis_menu, String status_menu) {
        super(id, nama_menu, harga_menu, deskripsi_menu, jenis_menu, status_menu);
        this.jumlah = 1;
        this.confirm = false;
        this.status = "Pending";
        this.harga_menu = currency(this.getHarga_menu());
    }

    public OrderMenu(String id, String nama_menu, String harga_menu, String deskripsi_menu, String jenis_menu, String status_menu, int jumlah, String status) {
        super(id, nama_menu, harga_menu, deskripsi_menu, jenis_menu, status_menu);
        this.jumlah = jumlah;
        this.confirm = true;
        this.status = status;
        this.harga_menu = currency(this.getHarga_menu());
    }

    private String currency(String angkaAwal){
        String hasil = "";

        if (angkaAwal.length()>=3){
            int ctr = 1;
            for (int i = angkaAwal.length()-1; i >= 0; i--) {
                hasil = angkaAwal.charAt(i) + hasil;
                if (ctr%3==0 && ctr<angkaAwal.length()) hasil = "."+hasil;
                ctr++;
            }
        }else{
            hasil = angkaAwal;
        }
        return "Rp. "+hasil;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(jumlah);
        parcel.writeByte((byte) (confirm ? 1 : 0));
        parcel.writeString(status);
    }

    protected OrderMenu(Parcel in) {
        super(in);
        jumlah = in.readInt();
        confirm = in.readByte() != 0;
        status = in.readString();
    }
}
