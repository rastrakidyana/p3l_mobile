package com.example.p3l_apk_rastar.ui.menu.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.p3l_apk_rastar.ui.menu.model.PesananAwal;

import java.util.List;

@Dao
public interface PesananDAO {

    @Query("SELECT * FROM pesananawal")
    List<PesananAwal> getAll();

    @Query("DELETE FROM pesananawal")
    void delete();

    @Insert
    void insert(PesananAwal pesanan);

    @Update
    void update(PesananAwal pesanan);

    @Delete
    void delete(PesananAwal pesanan);
}
