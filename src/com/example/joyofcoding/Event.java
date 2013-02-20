package com.example.joyofcoding;

public class Event {
	
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
	
}
