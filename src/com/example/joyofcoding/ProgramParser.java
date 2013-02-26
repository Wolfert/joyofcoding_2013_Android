package com.example.joyofcoding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ProgramParser {

	private String fileName;
	private Context context;
	private JSONObject program;
	private String prevTime;
	private ArrayList<Event> list;

	public ProgramParser(String fileName, Context context) {
		super();
		this.fileName = fileName;
		this.context = context;
	}

	public JSONObject getProgram() {
		if (program == null)
			parse();
		return program;
	}
	
	public ArrayList<Event> getEvents() {
		if(list != null)
			return list;
		
		JSONArray events;		
		list = new ArrayList<Event>();
		prevTime = "";
		
		try {
			events = (JSONArray) getProgram().get("events");
			for (int i = 0; i < events.length(); i++) {
				JSONObject JSONEvent = events.getJSONObject(i);
				Event event = new Event(JSONEvent.getString("title"));
				
				if(prevTime.equals(JSONEvent.getString("time"))) {
					event.setTime("");
				} else {
					event.setTime(JSONEvent.getString("time"));
					prevTime = event.getTime();
				}
				
				event.setSpeaker(JSONEvent.getString("speaker"));
				event.setColor(JSONEvent.getInt("color"));
				event.setContentURL(JSONEvent.getString("contentURL"));
				list.add(event);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Boolean parse(){
		Log.i(this.getClass().getSimpleName(), "Parsing program file");
		try{
			InputStream program = context.getResources().getAssets().open(fileName);
			InputStreamReader streamReader = new InputStreamReader(program);
			BufferedReader reader = new BufferedReader(streamReader);
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			this.program = new JSONObject(builder.toString());
		    reader.close();
		} catch(Exception e){
		    e.printStackTrace();
		    return false;
		}
		return true;
	}
	
	public Event getEvent(int index){
		return getEvents().get(index);
	}

}
