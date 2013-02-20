package com.example.joyofcoding;

public class Event {
	
	private String time;
	private String title;
	private String speaker;
	private String location;
	private String contentURL;
	private int color; 

	public Event(String time, String title, String speaker, String location,
			String contentURL, int color) {
		super();
		this.time = time;
		this.title = title;
		this.speaker = speaker;
		this.location = location;
		this.contentURL = contentURL;
		this.color = color;
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
}
