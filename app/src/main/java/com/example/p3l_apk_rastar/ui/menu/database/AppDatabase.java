package com.example.p3l_apk_rastar.ui.menu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.p3l_apk_rastar.ui.menu.model.PesananAwal;

@Database(entities = {PesananAwal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PesananDAO pesananDAO();
}
