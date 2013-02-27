	package com.lunatech.joyofcoding;
	
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
            "com.lunatech.joyofcoding.ProximityIntentReceiver";

    private LocationManager locationManager;

    //private static final float LATITUDE = 51.934238f;
    //private static final float LONGITUDE = 4.471843f;

//    private static final float LATITUDE = 51.897648f;
//    private static final float LONGITUDE = 4.494342f;
    
    private static final float LATITUDE = 51.919606f;
    private static final float LONGITUDE = 4.456255f;

    
    private ArrayList<Event> events;

	private PendingIntent proximityIntent;
    
	private BroadcastReceiver receiver;
	
	private boolean monitoringEnabled = false;
		
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	Log.d(getClass().getSimpleName(), "onCreate");
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_program);
        parser = new ProgramParser("program.json", this);
		events = parser.getEvents();	
		
		ListView listView = (ListView) findViewById(R.id.programlist);
		String[] values = new String[events.size()];
		for(int i = 0; i < events.size(); i++) {
			values[i] = events.get(i).getTitle();
		} 

		// get data from the table by the ListAdapter
		EventListAdapter eventAdapter = new EventListAdapter(this, R.layout.eventlistrow, events);

		View headerView = View.inflate(this, R.layout.activity_program_header, null);
		View footerView = View.inflate(this, R.layout.activity_program_footer, null);
		
		listView.addHeaderView(headerView);
		listView.addFooterView(footerView);
		
		addListenerToListView(listView);
		addListenerToLogo();
		addInfoListener();

        // Get our twitterhandle input field and attach a listener
        EditText twitter = (EditText)findViewById(R.id.username);
        twitter.setText(retrieveTwitterFromPreferences());

        addTextChangedListener(twitter);
		 
		// Assign adapter to ListView
		listView.setAdapter(eventAdapter);

		receiver = new ProximityIntentReceiver(this);
    }

	private void addTextChangedListener(EditText twitter) {
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
	
	public void enableMonitoring(View view) {
		if(!monitoringEnabled){
			Log.i(this.getClass().getSimpleName(), "enable monitoring");
			// start monitoring, toggle button text.
			((Button) view).setText("Disable");
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			addProximityAlert(LATITUDE, LONGITUDE);
			//receiver
			monitoringEnabled = true;
		} else {
			Log.i(this.getClass().getSimpleName(), "stop monitoring");
			// stop monitoring, toggle button text, check out user.
			((Button) view).setText("Enable");
	    	PendingIntent pi = getProximityIntent();
	    	
	    	
	    	// WIP: Get ProximityIntentReceiver instance, call force checkout!
			Log.wtf("WIP", "Get ProximityIntentReceiver instance, call force checkout!");
//	    	((ProximityIntentReceiver) pi.).forceCheckout(this, retrieveTwitterFromPreferences());
			
	    	locationManager.removeProximityAlert(pi);
	    	pi.cancel();
			monitoringEnabled = false;
		}
	}

    private void addProximityAlert(double latitude, double longitude) {

        PendingIntent proximityIntent = getProximityIntent();

        locationManager.addProximityAlert(
                LATITUDE, // the latitude of the central point of the alert region
                LONGITUDE, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(receiver, filter);
    }

	private PendingIntent getProximityIntent() {
		if(proximityIntent == null) {
			Intent intent = new Intent(PROX_ALERT_INTENT);
        	proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		return proximityIntent;
	}

    @Override
    protected void onStop(){
    	Log.d(getClass().getSimpleName(), "onStop");
    	unregisterReceiver(receiver);

    	super.onStop();
    }
    
    @Override
    protected void onDestroy(){
    	Log.d(getClass().getSimpleName(), "onDestroy");
    	super.onDestroy();
    }
    
    @Override
    public void finish() {
    	Log.d(getClass().getSimpleName(), "finish");
    	PendingIntent pi = getProximityIntent();
    	locationManager.removeProximityAlert(pi);
    	pi.cancel();
        super.finish();
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