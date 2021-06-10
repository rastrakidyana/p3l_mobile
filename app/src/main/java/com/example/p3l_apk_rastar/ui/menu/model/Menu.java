package com.example.p3l_apk_rastar.ui.menu.model;


import java.io.Serializable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu implements Serializable {
//    public int id;
//    public int id_menu;
//    public int id_transaksi;
//    public int jml_pesanan;
//    public double total_pesanan;
//    public String status_pesanan;

    public int id;
    public int id_bahan;
    public String nama_menu;
    public String deskripsi_menu;
    public String unit_menu;
    public double harga_menu;
    public String tipe_menu;
    public int stok_menu;
    public String gambar_menu;
    public int serving_size;
    public int status_hapus;

    public Menu(int id, int id_bahan, String nama_menu, String deskripsi_menu, String unit_menu,
                double harga_menu, String tipe_menu, int stok_menu, String gambar_menu, int serving_size, int status_hapus) {
        this.id = id;
        this.id_bahan = id_bahan;
        this.nama_menu = nama_menu;
        this.deskripsi_menu = deskripsi_menu;
        this.unit_menu = unit_menu;
        this.harga_menu = harga_menu;
        this.tipe_menu = tipe_menu;
        this.stok_menu = stok_menu;
        this.gambar_menu = gambar_menu;
        this.serving_size = serving_size;
        this.status_hapus = status_hapus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_bahan() {
        return id_bahan;
    }

    public void setId_bahan(int id_bahan) {
        this.id_bahan = id_bahan;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getDeskripsi_menu() {
        return deskripsi_menu;
    }

    public void setDeskripsi_menu(String deskripsi_menu) {
        this.deskripsi_menu = deskripsi_menu;
    }

    public String getUnit_menu() {
        return unit_menu;
    }

    public void setUnit_menu(String unit_menu) {
        this.unit_menu = unit_menu;
    }

    public double getHarga_menu() {
        return harga_menu;
    }

    public void setHarga_menu(double harga_menu) {
        this.harga_menu = harga_menu;
    }

    public String getTipe_menu() {
        return tipe_menu;
    }

    public void setTipe_menu(String tipe_menu) {
        this.tipe_menu = tipe_menu;
    }

    public int getStok_menu() {
        return stok_menu;
    }

    public void setStok_menu(int stok_menu) {
        this.stok_menu = stok_menu;
    }

    public String getGambar_menu() {
        return gambar_menu;
    }

    public void setGambar_menu(String gambar_menu) {
        this.gambar_menu = gambar_menu;
    }

    public int getServing_size() {
        return serving_size;
    }

    @BindingAdapter("cImage")
    public static void loadImage(CircleImageView circleImageView, String imgUrlC) {
        Glide.with(circleImageView.getContext())
                .load(imgUrlC)
                .into(circleImageView);
    }

    public void setServing_size(int serving_size) {
        this.serving_size = serving_size;
    }

    public int getStatus_hapus() {
        return status_hapus;
    }

    public void setStatus_hapus(int status_hapus) {
        this.status_hapus = status_hapus;
    }
}
