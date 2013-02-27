package com.lunatech.joyofcoding;

import android.os.Parcel;
import android.os.Parcelable;
import com.lunatech.joyofcoding.R;

public class Event implements Parcelable {
	
	private String time;
	private String title;
	private String speaker;
	private String location;
	private String contentURL;
	private int color; 

	public Event(String title) {
		super();
		this.title = title;
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", title=" + title + ", speaker="
				+ speaker + "]";
	}

	public String getTime() {
		return time;
	}

	public String getTitle() {
		return title;
	}

	public String getSpeaker() {
		return speaker;
	}

	public String getLocation() {
		return location;
	}

	public String getContentURL() {
		return contentURL;
	}

	public int getColor() {
		return color;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setContentURL(String contentURL) {
		this.contentURL = contentURL;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(time);
		out.writeString(title);
		out.writeString(speaker);
		out.writeString(location);
		out.writeInt(color);
		out.writeString(contentURL);
	}
	
	@SuppressWarnings("unused")
	private Event(Parcel in) {
		this.time =  in.readString();
		this.title =  in.readString();
		this.speaker =  in.readString();
		this.location =  in.readString();
		this.color =  in.readInt();
		this.contentURL =  in.readString();
	}
	 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
         public Event createFromParcel(Parcel in) {
             return new Event(in); 
         }

         public Event[] newArray(int size) {
             return new Event[size];
         }
     };
}
