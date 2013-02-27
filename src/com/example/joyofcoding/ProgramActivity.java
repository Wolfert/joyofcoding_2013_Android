package com.example.joyofcoding;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.lunatech.joyofcoding.R;

public class ProgramActivity extends Activity {

	public ProgramParser parser;

    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;

    private static final String TWITTER_HANDLE_KEY = "TWITTER_HANDLE_KEY";

    private static final String PROX_ALERT_INTENT =
            "com.example.joyofcoding.ProximityIntentReceiver";

    private LocationManager locationManager;

    //private static final float LATITUDE = 51.934238f;
    //private static final float LONGITUDE = 4.471843f;

    private static final float LATITUDE = 51.897648f;
    private static final float LONGITUDE = 4.494342f;
    
    private BroadcastReceiver receiver;
    private ArrayList<Event> events;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_program);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        receiver = new ProximityIntentReceiver(this);
        parser = new ProgramParser("program.json", this);
		events = parser.getEvents();
		
		ListView listView = (ListView) findViewById(R.id.mylist);
		String[] values = new String[events.size()];
		for(int i = 0; i < events.size(); i++) {
			values[i] = events.get(i).getTitle();
		} 
		// get data from the table by the ListAdapter
		EventListAdapter eventAdapter = new EventListAdapter(this, R.layout.eventlistrow, events);

		View headerView = View.inflate(this, R.layout.activity_program_header, null);
		View footerView = View.inflate(this, R.layout.activity_program_footer, null);
		
		// footerView receives ListView's onItemClick event, wtf Android?
		footerView.setEnabled(false);
		listView.addHeaderView(headerView);
		listView.addFooterView(footerView);
		
		addListenerToListView(listView);
		addListenerToLogo();
		addInfoListener();

        EditText twitter = (EditText)findViewById(R.id.username);
        // Get our twitter view
        twitter.setText(retrieveTwitterFromPreferences());

        // This is where we start our
        twitter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               saveTwitterInPreferences(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
		
		// Assign adapter to ListView
		listView.setAdapter(eventAdapter);

        addProximityAlert(LATITUDE, LONGITUDE);
		
    }

	private void addInfoListener() {
		ImageView iv = (ImageView) findViewById(R.id.infoIcon);
		iv.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  Uri uriUrl = Uri.parse("http://joyofcoding.lunatech.com"); 
				  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
				  startActivity(launchBrowser);
			  }
		});
	}

	private void addListenerToListView(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position >= events.size())
				  return;
				Intent intent = new Intent(view.getContext(), EventDetailActivity.class);
				Event event = parser.getEvent(position - 1);
				intent.putExtra("com.lunatech.joyofcoding.Event", event);
				startActivity(intent);
			  }
		  });
	}

	private void addListenerToLogo() {
		ImageView iv = (ImageView) findViewById(R.id.footerImage);
		iv.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View view) {
				  Uri uriUrl = Uri.parse("http://lunatech.com"); 
				  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
				  startActivity(launchBrowser);
			  }
		});
	}

    private void addProximityAlert(double latitude, double longitude) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        locationManager.addProximityAlert(
                LATITUDE, // the latitude of the central point of the alert region
                LONGITUDE, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProximityIntentReceiver(this), filter);
    }


    protected void saveTwitterInPreferences(String twitter) {
        SharedPreferences prefs =
                this.getSharedPreferences(getClass().getSimpleName(),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(TWITTER_HANDLE_KEY, twitter);
        prefsEditor.commit();
    }

    protected String retrieveTwitterFromPreferences() {
        SharedPreferences prefs =
                this.getSharedPreferences(getClass().getSimpleName(),
                        Context.MODE_PRIVATE);
        return prefs.getString(TWITTER_HANDLE_KEY, "");
    }


}