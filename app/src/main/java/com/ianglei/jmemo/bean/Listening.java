package com.ianglei.jmemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Listening implements Parcelable
{
	private String id;
	private String title;
	private int category;
	private String updated;
	private String describe;
	private String link;
	private String coverpath;
	private String mp3path;
	private String pdfpath;
	private String transcript;
	private int learnedtimes;

	public Listening(String id, String title, int category, String updated, String describe, String link,
					 String coverpath, String mp3path, String pdfpath, String transcript, int learnedtimes)
	{
		super();
		this.id = id;
		this.title = title;
		this.category = category;
		this.updated = updated;
		this.describe = describe;
		this.link = link;
		this.coverpath = coverpath;
		this.mp3path = mp3path;
		this.pdfpath = pdfpath;
		this.transcript = transcript;
		this.learnedtimes = learnedtimes;
	}

	public Listening(String id, String title, String updated, String describe,
					 String link, String coverpath, String mp3path, String transcript, int learnedtimes)
	{
		super();
		this.id = id;
		this.title = title;
		this.updated = updated;
		this.describe = describe;
		this.link = link;
		this.coverpath = coverpath;
		this.mp3path = mp3path;
		this.transcript = transcript;
		this.learnedtimes = learnedtimes;
	}
	
	public Listening(Listening item)
	{
		super();
		this.id = item.getId();
		this.title = item.getTitle();
		this.updated = item.getUpdated();
		this.describe = item.getDescribe();
		this.link = item.getLink();
		this.coverpath = item.getCoverpath();
		this.mp3path = item.getMp3path();
		this.transcript = item.getTranscript();
		this.learnedtimes = item.getLearnedtimes();
	}

	public Listening()
	{}
	
	private Listening(Parcel in)
	{
		this.id = in.readString();
		this.title = in.readString();
		this.category = in.readInt();
		this.updated = in.readString();
		this.describe = in.readString();
		this.link = in.readString();
		this.coverpath = in.readString();
		this.mp3path = in.readString();
		this.pdfpath = in.readString();
		this.transcript = in.readString();
		this.learnedtimes = in.readInt();
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public int getCategory()
	{
		return category;
	}
	public void setCategory(int category)
	{
		this.category = category;
	}
	public String getUpdated()
	{
		return updated;
	}
	public void setUpdated(String updated)
	{
		this.updated = updated;
	}
	public String getDescribe()
	{
		return describe;
	}
	public void setDescribe(String describe)
	{
		this.describe = describe;
	}
	public String getLink()
	{
		return link;
	}
	public void setLink(String link)
	{
		this.link = link;
	}
	public String getCoverpath()
	{
		return coverpath;
	}
	public void setCoverpath(String coverpath)
	{
		this.coverpath = coverpath;
	}
	public String getMp3path()
	{
		return mp3path;
	}
	public void setMp3path(String mp3path)
	{
		this.mp3path = mp3path;
	}
	public String getPdfpath()
	{
		return pdfpath;
	}
	public void setPdfpath(String pdfpath)
	{
		this.pdfpath = pdfpath;
	}
	public String getTranscript()
	{
		return transcript;
	}
	public void setTranscript(String transcript)
	{
		this.transcript = transcript;
	}
	public int getLearnedtimes()
	{
		return learnedtimes;
	}
	public void setLearnedtimes(int learnedtimes)
	{
		this.learnedtimes = learnedtimes;
	}
	
	public static final Creator<Listening> CREATOR = new Creator<Listening>() {
        public Listening createFromParcel(Parcel source) {
        	Listening item = new Listening(source);
            return item;  
        }  
        public Listening[] newArray(int size) {
            return new Listening[size];
        }  
    };  
      	

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(id);
		dest.writeString(title);
		dest.writeInt(category);
		dest.writeString(updated);
		dest.writeString(describe);
		dest.writeString(link);
		dest.writeString(coverpath);
		dest.writeString(mp3path);
		dest.writeString(pdfpath);
		dest.writeString(transcript);
		dest.writeInt(learnedtimes);
	}
	
	
}
