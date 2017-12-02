package com.ianglei.jmemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.ianglei.jmemo.bean.Phrase;
import com.ianglei.jmemo.bean.Sample;
import com.ianglei.jmemo.bean.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class VocabularyHelper
{
	private static String TAG = "VocabularyHelper";
	
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

	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public VocabularyHelper(Context context)
	{
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	
	public boolean isWordExist(String word)
	{
		String selectQuery = "SELECT " + COLUMN_TRANSLATION + " FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where " + COLUMN_ENGLISH + "='" + word + "'";
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
        //db.close();
	}

	public ArrayList<Word> getWordList(int offset)
	{
		ArrayList<Word> list = new ArrayList<Word>();

		String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY learned limit 80 offset " + offset;
		Log.i(TAG, selectQuery);
		Cursor cursor = database.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Word word = new Word();
				word.setWord(cursor.getString(0));
				word.setPronounce(cursor.getString(1));
				word.setVoicepath(cursor.getString(2));
				word.setTranslation(cursor.getString(3));
				word.setTense(cursor.getString(4));

				Sample sample1 = new Sample();
				sample1.setSentence(cursor.getString(5));
				sample1.setMp3Path(cursor.getString(6));
				word.addSample(sample1);
				Sample sample2 = new Sample();
				sample2.setSentence(cursor.getString(7));
				sample2.setMp3Path(cursor.getString(8));
				word.addSample(sample2);
				Sample sample3 = new Sample();
				sample3.setSentence(cursor.getString(9));
				sample3.setMp3Path(cursor.getString(10));
				word.addSample(sample3);
				Sample sample4 = new Sample();
				sample4.setSentence(cursor.getString(11));
				sample4.setMp3Path(cursor.getString(12));
				word.addSample(sample4);
				Sample sample5 = new Sample();
				sample5.setSentence(cursor.getString(13));
				sample5.setMp3Path(cursor.getString(14));
				word.addSample(sample5);

				word.setLearned(cursor.getInt(15));
				word.setMasted(cursor.getInt(16));
				word.setInitial(cursor.getString(17));

				list.add(word);

			} while (cursor.moveToNext());
		}

		cursor.close();

		return list;
	}
	
	public ArrayList<Word> getWordListFirst()
	{
		ArrayList<Word> list = new ArrayList<Word>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY learned limit 80 offset 0";
        Log.i(TAG, selectQuery);
		Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	Word word = new Word();
            	word.setWord(cursor.getString(0));
            	word.setPronounce(cursor.getString(1));
				word.setVoicepath(cursor.getString(2));
				word.setTranslation(cursor.getString(3));
				word.setTense(cursor.getString(4));

            	Sample sample1 = new Sample();
            	sample1.setSentence(cursor.getString(5));
            	sample1.setMp3Path(cursor.getString(6));
            	word.addSample(sample1);
            	Sample sample2 = new Sample();
            	sample2.setSentence(cursor.getString(7));
            	sample2.setMp3Path(cursor.getString(8));
            	word.addSample(sample2);
            	Sample sample3 = new Sample();
            	sample3.setSentence(cursor.getString(9));
            	sample3.setMp3Path(cursor.getString(10));
            	word.addSample(sample3);
            	Sample sample4 = new Sample();
            	sample4.setSentence(cursor.getString(11));
            	sample4.setMp3Path(cursor.getString(12));
            	word.addSample(sample4);
            	Sample sample5 = new Sample();
            	sample5.setSentence(cursor.getString(13));
            	sample5.setMp3Path(cursor.getString(14));
            	word.addSample(sample5);
            	
            	word.setLearned(cursor.getInt(15));
            	word.setMasted(cursor.getInt(16));
            	word.setInitial(cursor.getString(17));
            	
            	list.add(word);
            	
            } while (cursor.moveToNext());
        }
        
        cursor.close();        
		
		return list;
	}
	

	
	public boolean insertWordList(ArrayList<Word> wordList)
	{
		if(DBHelper.getInstance() == null || wordList == null || wordList.size() == 0)
		{
			return false;
		}
		
		SQLiteDatabase db = null;  
        try {  
            db = DBHelper.getInstance().getWritableDatabase();
            String sql = "insert into " + TABLE_NAME + "(";

            
            SQLiteStatement stat = db.compileStatement(sql);  
            db.beginTransaction();  

            db.setTransactionSuccessful();  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        } finally {  
            try {  
                if (null != db) {  
                    db.endTransaction();  
                    db.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return true;  
	}
	


	public void batchInsert(HashMap<String, Word> map)
	{
		System.out.println("Begin to batch insert " + map.size());

		Word word = null;
		String sql = null;

		SQLiteDatabase db = null;
		try {
			db = DBHelper.getInstance().getWritableDatabase();

			sql = "insert into vocabulary values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			SQLiteStatement stat = db.compileStatement(sql);
			db.beginTransaction();
			Iterator<Map.Entry<String, Word>> it = map.entrySet().iterator();

			while(it.hasNext())
			{
				Map.Entry entry = (Map.Entry) it.next();
				System.out.println(entry.getKey());
				word = (Word)entry.getValue();

				stat.clearBindings();

				stat.bindString(1, word.getWord());
				stat.bindString(2, word.getPronounce());
				stat.bindString(3, word.getVoicepath());
				stat.bindString(4, word.getTranslation());
				stat.bindString(5, word.getTense());
				LinkedList<Sample> linkedList = word.getSamples();
				if(linkedList.size() > 0)
				{
					Sample sample = linkedList.removeFirst();
					stat.bindString(6, sample.getSentence());
					stat.bindString(7, sample.getMp3Path());
					int list_size = linkedList.size();
					String sentence;
					String mp3path;
					if(list_size > 0)
					{
						sample = linkedList.removeFirst();
						sentence = list_size == 0 ? "" : sample.getSentence();
						mp3path = list_size == 0 ? "" : sample.getMp3Path();
						stat.bindString(8, sentence);
						stat.bindString(9, mp3path);
					}

					list_size = linkedList.size();
					if(list_size > 0)
					{
						sample = linkedList.removeFirst();
						sentence = list_size == 0 ? "" : sample.getSentence();
						mp3path = list_size == 0 ? "" : sample.getMp3Path();
						stat.bindString(10, sentence);
						stat.bindString(11, mp3path);
					}

					list_size = linkedList.size();
					if(list_size > 0)
					{
						sample = linkedList.removeFirst();
						sentence = list_size == 0 ? "" : sample.getSentence();
						mp3path = list_size == 0 ? "" : sample.getMp3Path();
						stat.bindString(12, sentence);
						stat.bindString(13, mp3path);
					}

					list_size = linkedList.size();
					if(list_size > 0)
					{
						sample = linkedList.removeFirst();
						sentence = list_size == 0 ? "" : sample.getSentence();
						mp3path = list_size == 0 ? "" : sample.getMp3Path();
						stat.bindString(14, sentence);
						stat.bindString(15, mp3path);
					}
				}

				stat.bindLong(16, word.getLearned());
				stat.bindLong(17, word.getMasted());
				stat.bindString(18, word.getInitial());

				stat.executeInsert();
			}
			db.setTransactionSuccessful();


		} catch (Exception e) {
			Log.w("Exception:", e);
		} finally {
			db.endTransaction();
			db.close();
		}
	}
}
