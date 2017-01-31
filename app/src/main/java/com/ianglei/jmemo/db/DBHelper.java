package com.ianglei.jmemo.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.StorageUtil;

import java.io.File;

public final class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    public static final String LISTEN_TABLE = "Listening";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_UPDATED = "updated";
    public static final String COLUMN_DESCRIBE = "describe";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_COVERPATH = "coverpath";
    public static final String COLUMN_MP3PATH = "mp3path";
    public static final String COLUMN_PDFPATH = "pdfpath";
    public static final String COLUMN_TRANSCRIPT = "transcript";
    public static final String COLUMN_LEARNEDTIMES = "learnedtimes";

    public static final String VOCABULARY_TABLE = "vocabulary";
    public static final String COLUMN_ENGLISH = "word";
    public static final String COLUMN_TRANSLATION = "translation";
    public static final String COLUMN_PRONOUNCE = "pronounce";
    public static final String COLUMN_VOICE_PATH = "voicepath";
    public static final String COLUMN_TENSE = "tense";
    public static final String COLUMN_SENTENCE1 = "sentence1";
    public static final String COLUMN_MP3PATH1 = "mp3path1";
    public static final String COLUMN_SENTENCE2 = "sentence2";
    public static final String COLUMN_MP3PATH2 = "mp3path2";
    public static final String COLUMN_SENTENCE3 = "sentence3";
    public static final String COLUMN_MP3PATH3 = "mp3path3";
    public static final String COLUMN_SENTENCE4 = "sentence4";
    public static final String COLUMN_MP3PATH4 = "mp3path4";
    public static final String COLUMN_SENTENCE5 = "sentence5";
    public static final String COLUMN_MP3PATH5 = "mp3path5";
    public static final String LEARNED = "learned";
    public static final String MASTED = "masted";
    public static final String INDEX = "initial";


    public static final String PHRASE_TABLE = "Phrase";
    public static final String PHRASE = "phrase";
    public static final String CATEGORY = "category";
    public static final String TRANSLATION = "translation";
    public static final String SAMPLE = "sample";

    static int dbVersion=4;
    private static final String DB_PATH = "db";
    private static final String DB_NAME = "jmemo.db";
    private static final String DB_PATH_NAME = StorageUtil.getSDCardPath() + "jmemo" + File.separator + DB_PATH + File.separator + DB_NAME;
    private static DBHelper dbHelper;

    public static final String CREATE_LISTEN = "create table IF NOT EXISTS " + LISTEN_TABLE +"("
            + COLUMN_ID + " TEXT PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_CATEGORY + " INTEGER, "
            + COLUMN_UPDATED + " TEXT, "
            + COLUMN_DESCRIBE + " TEXT, "
            + COLUMN_LINK + " TEXT, "
            + COLUMN_COVERPATH + " TEXT, "
            + COLUMN_MP3PATH + " TEXT, "
            + COLUMN_PDFPATH + " TEXT, "
            + COLUMN_TRANSCRIPT + " TEXT, "
            + COLUMN_LEARNEDTIMES + " INTEGER)";

    public static final String CREATE_TABLE_VOCABULARY = "create table IF NOT EXISTS " + VOCABULARY_TABLE
            +"("
            + COLUMN_ENGLISH + " TEXT PRIMARY KEY, "
            + COLUMN_PRONOUNCE + " TEXT, "
            + COLUMN_VOICE_PATH + " TEXT, "
            + COLUMN_TRANSLATION + " TEXT, "
            + COLUMN_TENSE + " TEXT, "
            + COLUMN_SENTENCE1 + " TEXT, "
            + COLUMN_MP3PATH1 + " TEXT, "
            + COLUMN_SENTENCE2 + " TEXT, "
            + COLUMN_MP3PATH2 + " TEXT, "
            + COLUMN_SENTENCE3 + " TEXT, "
            + COLUMN_MP3PATH3 + " TEXT, "
            + COLUMN_SENTENCE4 + " TEXT, "
            + COLUMN_MP3PATH4 + " TEXT, "
            + COLUMN_SENTENCE5 + " TEXT, "
            + COLUMN_MP3PATH5 + " TEXT, "
            + LEARNED + " INTEGER, "
            + MASTED + " INTEGER, "
            + INDEX + " TEXT)";

    public static final String CREATE_TABLE_PHRASE = "create table IF NOT EXISTS " + PHRASE_TABLE
            + "("
            + PHRASE + " TEXT PRIMARY KEY, "
            + CATEGORY + " INTEGER, "
            + TRANSLATION + " TEXT, "
            + SAMPLE + " TEXT)";


    //CREATE INDEX mycolumn_index ON mytable (myclumn)

    public DBHelper(Context context) {
        super(context, DB_PATH_NAME , null, dbVersion);
        L.i(TAG, DB_PATH_NAME);
    }

    public static synchronized void init(Context context)
    {
        if(null == dbHelper)
        {
            dbHelper = new DBHelper(context);
        }
    }

    public static DBHelper getInstance()
    {
        return dbHelper;
    }

    @Override
    //Called when the database is created for the front time
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LISTEN);
        db.execSQL(CREATE_TABLE_VOCABULARY);
        db.execSQL(CREATE_TABLE_PHRASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + LISTEN_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + VOCABULARY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PHRASE_TABLE);
        try {
            db.execSQL(CREATE_TABLE_PHRASE);
            onCreate(db);
        } catch (SQLException e) {
            L.e("sqlite onUpgrade error");
        }

    }
}
