package com.example.joyofcoding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ProgramParser {

	private String fileName;
	private Context context;
	private JSONObject program;

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

}
