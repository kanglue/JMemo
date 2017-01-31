package com.ianglei.jmemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;

public class Sample implements Parcelable
{
	private String sentence;
	private String mp3Path;
	
	public Sample()
	{}

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
	
	private Sample(Parcel source)
	{  
        this.sentence = source.readString();
        this.mp3Path= source.readString();  
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sentence);
		dest.writeString(mp3Path);		
	}  
	
	public static final Creator<Sample> CREATOR  = new Creator<Sample>() {
        //实现从source中创建出类的实例的功能  
        @Override  
        public Sample createFromParcel(Parcel source) {  
        	return new Sample(source);
        }
        //创建一个类型为T，长度为size的数组  
        @Override  
        public Sample[] newArray(int size) {
            return new Sample[size];  
        }
    };

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
