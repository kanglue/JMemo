package com.ianglei.jmemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jianglei on 10/15/15.
 */
public class ListeningDataSource
{
    private static final String TAG = "ListeningDataSource";

    private static final String TABLE_NAME = "Listening";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_UPDATED = "updated";
    private static final String COLUMN_DESCRIBE = "describe";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_COVERPATH = "coverpath";
    private static final String COLUMN_MP3PATH = "mp3path";
    private static final String COLUMN_PDFPATH = "pdfpath";
    private static final String COLUMN_TRANSCRIPT = "transcript";
    private static final String COLUMN_LEARNEDTIMES = "learnedtimes";

    private static final String STEP = "10";

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public ListeningDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    public void insertListeningInfo(Listening item)
    {
        Log.i(TAG, "Insert Listening item: id=" + item.getId() + ", title=" + item.getTitle());

            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, item.getId());
            values.put(COLUMN_TITLE, item.getTitle());
            values.put(COLUMN_CATEGORY, item.getCategory());
            values.put(COLUMN_UPDATED, item.getUpdated());
            values.put(COLUMN_DESCRIBE, item.getDescribe());
            values.put(COLUMN_LINK, item.getLink());
            values.put(COLUMN_COVERPATH, item.getCoverpath());
            values.put(COLUMN_MP3PATH, item.getMp3path());
            values.put(COLUMN_PDFPATH, item.getPdfpath());
            values.put(COLUMN_TRANSCRIPT, item.getTranscript());
            values.put(COLUMN_LEARNEDTIMES, 0);

            long result =database.insert(DBHelper.LISTEN_TABLE, null, values);
            Log.i(TAG, "Insert Listening item result "+ result);

    }

    public void insertOrUpdateNewsList(Listening item) {
        if (isListeningExist(item.getId())) {
            //updateNewsList(date, content);
        } else {
            insertListeningInfo(item);
        }
    }

    public boolean isListeningExist(String id)
    {
        boolean result;
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where id='" + id + "'";
        Log.i(TAG, selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToNext())
        {
            result = true;
        }
        else
        {
            result = false;
        }

        cursor.close();
        return result;
    }

    public List<Listening> getItemDetail(String id) {
        List<Listening> itemList = new ArrayList<Listening>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where id='" + id + "'";
        Log.i(TAG, selectQuery);
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Listening item = new Listening(cursor.getString(0)
                        ,cursor.getString(1)
                        , Integer.parseInt(cursor.getString(2))
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getString(5)
                        ,cursor.getString(6)
                        ,cursor.getString(7)
                        ,cursor.getString(8)
                        ,cursor.getString(9)
                        , Integer.parseInt(cursor.getString(10)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //db.close();

        return itemList;
    }

    public int getCountByCategory(int category)
    {
        int count = 0;
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_NAME + " where "
                + COLUMN_CATEGORY + "=" + category;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.i(TAG, "The count of category " + category + " is " + count);

        return count;
    }

    public ArrayList<Listening> getItemListByCategory(int category, int startPos) {
        ArrayList<Listening> itemList = new ArrayList<Listening>();

        String selectQuery = "SELECT " + COLUMN_ID + "," + COLUMN_TITLE + ","
                + COLUMN_UPDATED + "," + COLUMN_DESCRIBE + "," + COLUMN_LINK + ","
                + COLUMN_COVERPATH + "," + COLUMN_MP3PATH + "," + COLUMN_TRANSCRIPT
                + "," + COLUMN_LEARNEDTIMES
                + " FROM " + TABLE_NAME + " where " + COLUMN_CATEGORY + "=" + category
                + " order by " + COLUMN_UPDATED + " DESC limit " + STEP + " offset " + startPos;

        L.i(TAG, selectQuery);
        if(null == database)
            L.i(TAG, "Error database is null");

        Cursor cursor = database.rawQuery(selectQuery, null);

        Log.i(TAG, cursor.getCount() + " records");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Listening item = new Listening(cursor.getString(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getString(5)
                        ,cursor.getString(6)
                        ,cursor.getString(7)
                        , Integer.parseInt(cursor.getString(8)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //Collections.reverse(itemList);
        return itemList;
    }

    public synchronized void updateCoverImgPath(String urlPath, String localPath)
    {
        Log.i(TAG, "Update url path:"+urlPath + ", local path:" + localPath);

        ContentValues values = new ContentValues();
        values.put(COLUMN_COVERPATH, localPath);
        int resultrow = database.update(TABLE_NAME, values, "coverpath=?", new String[]{urlPath});
        Log.i(TAG, "Update row " + resultrow);
        //db.close();
    }

    public synchronized void updateCoverImgPath(Listening item)
    {
        String localPath = item.getCoverpath();
        Log.i(TAG, "Update url path:"+localPath);

        ContentValues values = new ContentValues();
        values.put(COLUMN_COVERPATH, localPath);
        int resultrow = database.update(TABLE_NAME, values, "id=?", new String[]{item.getId()});
        Log.i(TAG, "Update row " + resultrow);
        //db.close();
    }

//	public static void updateTranscript(Listening item)
//	{
//		Log.i(TAG, "Update Listening item: id="+item.getId() +"'s transcript");
//
//		SQLiteDatabase db = DBAgent.getInstance().getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(COLUMN_TRANSCRIPT, item.getTranscript());
//		int resultrow = db.update(TABLE_NAME, values, "id=?", new String[]{item.getId()});
//		Log.i(TAG, "Update row " + resultrow);
//		//db.close();
//	}

    public void updateInfoPath(Listening item)
    {
        Log.i(TAG, "Update Listening item: id="+item.getId() +"'s mp3 path");

        ContentValues values = new ContentValues();
        values.put(COLUMN_MP3PATH, item.getMp3path());
        values.put(COLUMN_TRANSCRIPT, item.getTranscript());
        values.put(COLUMN_PDFPATH, item.getPdfpath());
        int resultrow = database.update(TABLE_NAME, values, "id=?", new String[]{item.getId()});
        Log.i(TAG, "Update row " + resultrow);
    }

    public Listening queryDetailById(Listening item)
    {
        Log.i(TAG, "Update Listening item: id="+item.getId() +"'s transcript");

        String selectQuery = "SELECT " + COLUMN_TRANSCRIPT + ","+ COLUMN_MP3PATH
                + " FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where id='" + item.getId() + "'";
        Log.i(TAG, selectQuery);
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                item.setTranscript(cursor.getString(0));
                item.setMp3path(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //db.close();

        return item;
    }

    public ArrayList<Listening> getItemListByCategory(int category) {
        ArrayList<Listening> itemList = new ArrayList<Listening>();

        String selectQuery = "SELECT " + COLUMN_ID + "," + COLUMN_TITLE + ","
                + COLUMN_UPDATED + "," + COLUMN_DESCRIBE + "," + COLUMN_LINK + ","
                + COLUMN_COVERPATH + "," + COLUMN_MP3PATH + "," + COLUMN_TRANSCRIPT
                + "," + COLUMN_LEARNEDTIMES
                + " FROM " + TABLE_NAME + " where " + COLUMN_CATEGORY + "=" + category;

        Log.i(TAG, selectQuery);
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Listening item = new Listening(cursor.getString(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getString(5)
                        ,cursor.getString(6)
                        ,cursor.getString(7)
                        , Integer.parseInt(cursor.getString(8)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //        Collections.sort(itemList,new Comparator<Listening>(){
        //            @Override
        //            public int compare(Listening b1, Listening b2) {
        //
        //            	SimpleDateFormat fmt =new SimpleDateFormat("dd mm yyyy");
        //            	try {
        //					Date date1 = fmt.parse(b1.getUpdated().split("/")[1]);
        //					Date date2 = fmt.parse(b2.getUpdated().split("/")[1]);
        //					return date1.compareTo(date2);
        //
        //				} catch (ParseException e) {
        //
        //					e.printStackTrace();
        //				}
        //            	return b1.getUpdated().compareTo(b2.getUpdated());
        //            }
        //
        //        });

        //db.close();

        Collections.reverse(itemList);
        return itemList;
    }

    public void updateTranscript(Listening item)
    {
        Log.i(TAG, "Update Listening item: id="+item.getId() +"'s transcript");

        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSCRIPT, item.getTranscript());
        int resultrow = database.update(TABLE_NAME, values, "id=?", new String[]{item.getId()});
        Log.i(TAG, "Update row " + resultrow);
        //db.close();
    }

    public void updateMp3Path(Listening item)
    {
        Log.i(TAG, "Update Listening item: id="+item.getId() +"'s mp3 path");

        ContentValues values = new ContentValues();
        values.put(COLUMN_MP3PATH, item.getMp3path());
        int resultrow = database.update(TABLE_NAME, values, "id=?", new String[]{item.getId()});
        Log.i(TAG, "Update row " + resultrow);
    }

}
