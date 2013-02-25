package com.example.joyofcoding;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ProgramActivity extends Activity {
	public ProgramParser parser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_program);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_program, menu);
		
		parser = new ProgramParser("program.json", this);
		ArrayList<Event> events = parser.getEvents();
		
		ListView listView = (ListView) findViewById(R.id.mylist);
		String[] values = new String[events.size()];
		for(int i = 0; i < events.size(); i++) {
			values[i] = events.get(i).getTitle();
		} 
		// get data from the table by the ListAdapter
		EventListAdapter eventAdapter = new EventListAdapter(this, R.layout.eventlistrow, events);
		

		listView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), EventDetailActivity.class);
				Event event = parser.getEvent(position);
				intent.putExtra("com.example.joyofcoding.Event", event);
				startActivity(intent);
			  }
		  });


	 
		View headerView = View.inflate(this, R.layout.activity_program_header, null);
		View footerView = View.inflate(this, R.layout.activity_program_footer, null);
		listView.addHeaderView(headerView);
		listView.addFooterView(footerView);

        EditText twitter = (EditText)findViewById(R.id.username);
        // This is where we start our
        twitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterTextChanged(Editable s) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

		
		// Assign adapter to ListView
		listView.setAdapter(eventAdapter); 
		
		return true;
	}
}