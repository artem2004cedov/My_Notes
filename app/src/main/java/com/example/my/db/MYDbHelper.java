package com.example.my.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MYDbHelper extends SQLiteOpenHelper {

    // передаем названия базы бд, и весрсию
    public MYDbHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.DB_VERSION);
    }

    // передаем таблицу
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstants.TABLE_STRUCTURE);
    }

    // удаляем таблицу, и перезаписываем занова
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // сначало таблица удалятся
        db.execSQL(MyConstants.DROP_TABLE);
        // создаем новую
        onCreate(db);
    }
}