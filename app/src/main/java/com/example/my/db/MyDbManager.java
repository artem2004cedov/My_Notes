package com.example.my.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.my.MyAdapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private Context context;
    private MYDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        // передает контекст
        myDbHelper = new MYDbHelper(context);
    }

    // метод для открытия бд
    public void openDb() {
        // открываем базу данных
        db = myDbHelper.getWritableDatabase();
    }

    // метод для записи
    public void insertToDb(String title, String disc, String uri) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DISC, disc);
        cv.put(MyConstants.URI, uri);
        // записывается в таблицу
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    // метод для считывания
    public List<ListItem> getFromDb(String search_text) {
        List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstants.TITLE + " like ?";
        // поиск
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, selection, new String[]{"%" + search_text + "%"},
                null, null, null);
        // проходит по всем элемнтам
        while (cursor.moveToNext()) {
            ListItem item = new ListItem();
            // считывется заголовок и записыватся в переменую по индексу
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            String disc = cursor.getString(cursor.getColumnIndex(MyConstants.DISC));
            String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            item.setTitle(title);
            item.setDick(disc);
            item.setUri(uri);
            tempList.add(item);
        }
        cursor.close();
        return tempList;
    }

    // закрываем базу даных
    public void closeDb() {
        myDbHelper.close();
    }


}