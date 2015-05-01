package com.jmemo.db;

import java.lang.reflect.Field;

public class Sample {
	private String sentence;
	private String mp3Path;

	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	
	public String getMp3Path()
	{
		return mp3Path;
	}
	public void setMp3Path(String mp3Path)
	{
		this.mp3Path = mp3Path;
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
