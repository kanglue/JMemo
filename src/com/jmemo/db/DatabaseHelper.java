package com.jmemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jmemo.util.CommonUtil;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String TAG = "DatabaseHelper";
	
    static int dbVersion=1;
	private static final String DB_PATH = "db/";
	private static final String DB_NAME = "jmemo.db";
	
	private static DatabaseHelper dbHelper;
    
	public static final String TABLE_NAME = "vocabulary";
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
	
	
	public static final String PHRASE_NAME = "Phrase";
	public static final String PHRASE = "phrase";
	public static final String CATEGORY = "category";
	public static final String TRANSLATION = "translation";
	

    public static final String CREATE_TABLE_VOCABULARY = "create table IF NOT EXISTS " + TABLE_NAME
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
    
    public static final String CREATE_TABLE_NEWWORD = "create table IF NOT EXISTS " + PHRASE_NAME
    		+ "("
    		+ PHRASE + " TEXT PRIMARY KEY, "
    		+ CATEGORY + " INTEGER, "
    		+ TRANSLATION + " TEXT)";
    
    public DatabaseHelper(Context context) 
    {  
        super(context, CommonUtil.getRootFilePath() + DB_PATH + DB_NAME, null, dbVersion);  
        Log.i(TAG, CommonUtil.getRootFilePath() + DB_PATH + DB_NAME);
    }
    
	public static synchronized void init(Context context)
	{
		if(null == dbHelper)
		{
			dbHelper = new DatabaseHelper(context);
		}
	}
	
	public static DatabaseHelper getInstance()
	{
		return dbHelper;
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, CREATE_TABLE_VOCABULARY);

		db.execSQL(CREATE_TABLE_VOCABULARY);

        db.execSQL(CREATE_TABLE_NEWWORD);
	}	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);	
	}
	
	@Override
	public synchronized void close() {
		Log.d(TAG,"Close DataBase");
		super.close();

	}
}
