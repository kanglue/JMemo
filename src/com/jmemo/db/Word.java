package com.jmemo.db;

import java.lang.reflect.Field;
import java.util.LinkedList;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable
{
	private String word;
	private String pronounce;
	private String voicepath;
	private String tense;
	private String translation;
	private LinkedList<Sample> samples = new LinkedList<Sample>();
	private String initial;
	private int learned;
	private int masted;
	
	public Word()
	{
		
	}
	
	
	
	public String getWord()
	{
		return word;
	}



	public void setWord(String word)
	{
		this.word = word;
	}



	public String getPronounce()
	{
		return pronounce;
	}



	public void setPronounce(String pronounce)
	{
		this.pronounce = pronounce;
	}



	public String getVoicepath()
	{
		return voicepath;
	}



	public void setVoicepath(String voicepath)
	{
		this.voicepath = voicepath;
	}



	public String getTense()
	{
		return tense;
	}



	public void setTense(String tense)
	{
		this.tense = tense;
	}



	public String getTranslation()
	{
		return translation;
	}



	public void setTranslation(String translation)
	{
		this.translation = translation;
	}



	public String getInitial()
	{
		return initial;
	}



	public void setInitial(String initial)
	{
		this.initial = initial;
	}



	public int getLearned()
	{
		return learned;
	}



	public void setLearned(int learned)
	{
		this.learned = learned;
	}



	public int getMasted()
	{
		return masted;
	}



	public void setMasted(int masted)
	{
		this.masted = masted;
	}



	public LinkedList<Sample> getSamples() {
		return samples;
	}

	public void setSamples(LinkedList<Sample> samples) {
		this.samples = samples;
	}
	
	public void addSample(Sample sample)
	{
		samples.add(sample);
	}
	
	public Sample getSample()
	{
		if(samples.size() > 0)
		{
			return samples.removeFirst();
		}
		else {
			return null;
		}
	}
	
	public String toString()  
	{  
	    // TODO Auto-generated method stub  
	    Field[] fields=this.getClass().getDeclaredFields();  
	    StringBuffer strBuf=new StringBuffer();  
	    strBuf.append(this.getClass().getName());  
	    strBuf.append("(");  
	    for(int i=0;i<fields.length;i++)  
	    {  
	    Field fd=fields[i];   
	    strBuf.append(fd.getName()+":");  
	    try  
	    {
	        strBuf.append(fd.get(this));  
	    }  
	    catch (Exception e)  
	    {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }      
	    if(i!=fields.length-1)  
	    strBuf.append("|");  
	    }  
	    
	    strBuf.append(")");  
	    return strBuf.toString();  
	}



	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}  
}
