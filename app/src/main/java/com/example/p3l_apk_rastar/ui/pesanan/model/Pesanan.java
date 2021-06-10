package com.example.p3l_apk_rastar.ui.pesanan.model;

import java.io.Serializable;

public class Pesanan implements Serializable {
    public int id;
//    public int id_menu;
    public String nama_menu;
    public int id_transaksi;
    public int jml_pesanan;
    public double total_pesanan;

    public Pesanan(int id, String nama_menu, int id_transaksi, int jml_pesanan, double total_pesanan) {
        this.id = id;
//        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.id_transaksi = id_transaksi;
        this.jml_pesanan = jml_pesanan;
        this.total_pesanan = total_pesanan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getId_menu() {
//        return id_menu;
//    }
//
//    public void setId_menu(int id_menu) {
//        this.id_menu = id_menu;
//    }

    public int getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getJml_pesanan() {
        return jml_pesanan;
    }

    public void setJml_pesanan(int jml_pesanan) {
        this.jml_pesanan = jml_pesanan;
    }

    public double getTotal_pesanan() {
        return total_pesanan;
    }

    public void setTotal_pesanan(double total_pesanan) {
        this.total_pesanan = total_pesanan;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }
}
