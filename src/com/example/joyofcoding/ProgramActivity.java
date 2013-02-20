package com.example.joyofcoding;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProgramActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_program);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_program, menu);
		
		ProgramParser parser = new ProgramParser("program.json", this);
		parser.parse();
		
		ListView listView = (ListView) findViewById(R.id.mylist);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
		  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		  "Linux", "OS/2" };

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, android.R.id.text1, values);


		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		
		return true;
	}

}
