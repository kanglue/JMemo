package com.jmemo.db;

import java.lang.reflect.Field;

public class Phrase {
	private String phrase;
	private int category;
	private String translation;
	
	public Phrase()
	{}
	
	public Phrase(String p, int c, String t)
	{
		this.phrase = p;
		this.category = c;
		this.translation = t;
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
}
