package com.example.p3l_apk_rastar.ui.user.model;

import java.io.Serializable;

public class Transaksi implements Serializable {
    public int id;
    public String customer;
    public String no_transaksi;
    public String tgl;
    public int meja;
    public double total;

    public Transaksi(int id, String customer, String no_transaksi, String tgl, int meja, double total) {
        this.id = id;
        this.customer = customer;
        this.no_transaksi = no_transaksi;
        this.tgl = tgl;
        this.meja = meja;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNo_transaksi() {
        return no_transaksi;
    }

    public void setNo_transaksi(String no_transaksi) {
        this.no_transaksi = no_transaksi;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public int getMeja() {
        return meja;
    }

    public void setMeja(int meja) {
        this.meja = meja;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
