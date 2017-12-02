package com.ianglei.jmemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ianglei.jmemo.bean.Phrase;

import java.util.ArrayList;

/**
 * Created by j00255628 on 2016/11/3.
 */

public class PhraseHelper {
    private static final String TAG = "PhraseHelper";

    public static final String TABLE_NAME = "phrase";
    private static final String TABLE_NAME_Phrase = "Phrase";
    private static final String TABLE_NAME_sentense = "sentense";

    public static final String PHRASE = "phrase";
    public static final String CATEGORY = "category";
    public static final String SYMBOL = "symbol";
    public static final String TRANSLATION = "translation";
    public static final String SAMPLE = "sample";

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public PhraseHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    public boolean isPhraseExist(String phrase)
    {
        String selectQuery = "SELECT " + PHRASE + " FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where " + PHRASE + "='" + phrase + "'";
        Log.i(TAG, selectQuery);
        Cursor cursor = database.rawQuery(selectQuery, null);
        String s = null;

        if (cursor.moveToFirst()) {
            do {
                s = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();

        if(null == s)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertPhrase(Phrase phrase)
    {
        try {
            String sql = "insert into " + TABLE_NAME + " values('" + phrase.getPhrase() + "','" + phrase.getSymbol()
                    + "'," + phrase.getCategory() + ",'" + phrase.getTranslation() + "','" + phrase.getSample() + "')";
            Log.i(TAG, sql);

            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertPhrase(String phrase, int category, String symbol, String translation, String sample)
    {
        try {
            String sql = "insert into " + TABLE_NAME + " values('" + phrase + "'," + category
                    + ",'" + symbol + "','" + translation + "','" + sample + "')";
            Log.i(TAG, sql);

            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updatePhrase(Phrase phrase)
    {
        try {
            String sql = "update " + TABLE_NAME + " set category=" + phrase.getCategory()
                    + ", symbol='" + phrase.getSymbol() + "', translation='" + phrase.getTranslation()
                    + "', sample='" + phrase.getSample() + "' where phrase='" + phrase.getPhrase() + "'";
            Log.i(TAG, sql);

            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Phrase> getPhraseList()
    {
        ArrayList<Phrase> list = new ArrayList<Phrase>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Log.i(TAG, selectQuery);
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Phrase phrase = new Phrase();
                phrase.setPhrase(cursor.getString(0));
                phrase.setCategory(cursor.getInt(1));
                phrase.setSymbol(cursor.getString(2));
                phrase.setTranslation(cursor.getString(3));
                phrase.setSample(cursor.getString(4));

                list.add(phrase);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    public void delPhrase(String phrase)
    {
        String delSQL = "del from " + TABLE_NAME_Phrase + " where phrase ='" + phrase + "'";
        Log.d(TAG, delSQL);
        database.execSQL(delSQL);
    }
}
