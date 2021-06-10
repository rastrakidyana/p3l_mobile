package com.example.p3l_apk_rastar.ui.menu.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class PesananAwal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "menu")
    public String menuP;

    @ColumnInfo(name = "id_menu")
    public int id_menuP;

    @ColumnInfo(name = "jml")
    public int jmlP;

    @ColumnInfo(name = "harga")
    public double hargaP;

    @ColumnInfo(name = "total")
    public double totalP;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuP() {
        return menuP;
    }

    public void setMenuP(String menuP) {
        this.menuP = menuP;
    }

    public int getJmlP() {
        return jmlP;
    }

    public void setJmlP(int jmlP) {
        this.jmlP = jmlP;
    }

    public double getTotalP() {
        return totalP;
    }

    public void setTotalP(double totalP) {
        this.totalP = totalP;
    }

    public double getHargaP() {
        return hargaP;
    }

    public void setHargaP(double hargaP) {
        this.hargaP = hargaP;
    }

    public int getId_menuP() {
        return id_menuP;
    }

    public void setId_menuP(int id_menuP) {
        this.id_menuP = id_menuP;
    }
}
