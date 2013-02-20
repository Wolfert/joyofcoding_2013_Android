package com.example.joyofcoding;

import java.util.ArrayList;

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
		ArrayList<Event> events = parser.getEvents();
		
		ListView listView = (ListView) findViewById(R.id.mylist);
		String[] values = new String[events.size()];
		for(int i = 0; i < events.size(); i++) {
			values[i] = events.get(i).getTitle();
		} 
		// get data from the table by the ListAdapter
		EventListAdapter eventAdapter = new EventListAdapter(this, R.layout.eventlistrow, events);

//		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//		  android.R.layout.simple_list_item_1, android.R.id.text1, values);

		
		// Assign adapter to ListView
		listView.setAdapter(eventAdapter); 
		
		return true;
	}

}
