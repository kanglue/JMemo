package com.jmemo.db;

import java.util.ArrayList;
import java.util.LinkedList;

import android.R.integer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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
	
	public static boolean isWordExist(String word)
	{
		String selectQuery = "SELECT " + COLUMN_TRANSLATION + " FROM " + TABLE_NAME;
        selectQuery =  selectQuery + " where " + COLUMN_ENGLISH + "='" + word + "'";
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
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
	
	public static int getWordCount()
	{
		int count = 0;
		String selectQuery = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) 
        {
        	do{
        	count = cursor.getInt(0);
        	} while (cursor.moveToNext());
    
		    Log.i(TAG, "The count of words: " + count);		    
		    cursor.close();
        }
        
        return count;
	}
	
	public static ArrayList<Word> getWordListMore(int count, int offset)
	{
		ArrayList<Word> list = new ArrayList<Word>();

		String selectQuery = "SELECT * FROM " + TABLE_NAME + " limit " + count + " offset " + offset;
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
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
        
        Log.i(TAG, "The size of list: " + list.size());
        
        cursor.close();        
		
		return list;
	}
	
	public static ArrayList<Word> getWordListFirst()
	{
		ArrayList<Word> list = new ArrayList<Word>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " limit 100 offset 0";
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
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
        
        Log.i(TAG, "The size of list: " + list.size());
        
        cursor.close();        
		
		return list;
	}
	
	public static ArrayList<Phrase> getPhraseList()
	{
		ArrayList<Phrase> list = new ArrayList<Phrase>();
		
		String selectQuery = "SELECT * FROM " + PHRASE_NAME;
        Log.i(TAG, selectQuery);
        SQLiteDatabase db = DatabaseHelper.getInstance().getReadableDatabase();
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
		
	
	public static boolean insertWordList(ArrayList<Word> wordList)
	{
		if(DatabaseHelper.getInstance() == null || wordList == null || wordList.size() == 0)
		{
			return false;
		}
		
		SQLiteDatabase db = null;  
        try {  
            db = DatabaseHelper.getInstance().getWritableDatabase();
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
	
	public static boolean insertPhrase(Phrase phrase)
	{
		SQLiteDatabase db = null;  
        try {  
            db = DatabaseHelper.getInstance().getWritableDatabase();
            String sql = "insert into " + PHRASE_NAME + " values('" + phrase.getPhrase()
            		+ "'," + phrase.getCategory() + ",'" + phrase.getTranslation() + "')";
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
}
