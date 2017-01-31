package com.ianglei.jmemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;

public class Phrase implements Parcelable {
	private String phrase;
	private int category;
	private String symbol;
	private String translation;
	private String sample;
	
	public Phrase()
	{}

	public static final Creator<Phrase> CREATOR = new Creator<Phrase>() {
		@Override
		public Phrase createFromParcel(Parcel source) {
			Phrase phrase = new Phrase(source);
			return phrase;
		}

		@Override
		public Phrase[] newArray(int size) {
			return new Phrase[size];
		}
	};
	
	public Phrase(String p, int c, String m, String t, String s)
	{
		this.phrase = p;
		this.category = c;
		this.symbol = m;
		this.translation = t;
		this.sample = s;
	}

	public Phrase(Parcel in)
	{
		this.phrase = in.readString();
		this.category = in.readInt();
		this.symbol = in.readString();
		this.translation = in.readString();
		this.sample = in.readString();
	}
	
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	public String getSample()
	{
		return sample;
	}
	public void setSample(String sample)
	{
		this.sample = sample;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(phrase);
		dest.writeInt(category);
		dest.writeString(symbol);
		dest.writeString(translation);
		dest.writeString(sample);
	}
}
