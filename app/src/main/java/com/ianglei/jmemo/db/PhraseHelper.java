package com.ianglei.jmemo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ianglei.jmemo.bean.Phrase;

import java.util.ArrayList;

/**
 * Created by j00255628 on 2016/11/3.
 */

public class PhraseHelper {
    private static final String TAG = "ReciteHelper";

    public static final String TABLE_NAME = "phrase";
    private static final String TABLE_NAME_Phrase = "Phrase";
    private static final String TABLE_NAME_sentense = "sentense";

    public static final String PHRASE = "phrase";
    public static final String CATEGORY = "category";
    public static final String SYMBOL = "symbol";
    public static final String TRANSLATION = "translation";
    public static final String SAMPLE = "sample";

    public static boolean isPhraseExist(String phrase)
    {
        String selectQuery = "SELECT " + PHRASE + " FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where " + PHRASE + "='" + phrase + "'";
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
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

    public static boolean insertPhrase(Phrase phrase)
    {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getInstance().getWritableDatabase();
            String sql = "insert into " + TABLE_NAME + " values('" + phrase.getPhrase() + "'," + phrase.getSymbol()
                    + "'," + phrase.getCategory() + ",'" + phrase.getTranslation() + ",'" + phrase.getSample() + ")";
            Log.i(TAG, sql);

            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean insertPhrase(String phrase, int category, String symbol, String translation, String sample)
    {
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getInstance().getWritableDatabase();
            String sql = "insert into " + TABLE_NAME + " values('" + phrase + "'," + category
                    + "'," + symbol + ",'" + translation + ",'" + sample + ")";
            Log.i(TAG, sql);

            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static ArrayList<Phrase> getPhraseList()
    {
        ArrayList<Phrase> list = new ArrayList<Phrase>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Phrase phrase = new Phrase();
                phrase.setPhrase(cursor.getString(0));
                phrase.setCategory(cursor.getInt(1));
                phrase.setTranslation(cursor.getString(2));

                list.add(phrase);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }
}
