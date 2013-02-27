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
    import android.view.KeyEvent;
    import android.view.View;
import android.view.View.OnClickListener;
    import android.view.inputmethod.EditorInfo;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.*;
    import android.widget.AdapterView.OnItemClickListener;
    import com.lunatech.joyofcoding.R;

import static com.lunatech.joyofcoding.Utils.*;

public class ProgramActivity extends Activity {

	public ProgramParser parser;

    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;

    private static final String TWITTER_HANDLE_KEY = "TWITTER_HANDLE_KEY";
    private static final String MONITORING_KEY = "MONITORING_KEY";

    private static final String PROX_ALERT_INTENT =
            "com.lunatech.joyofcoding.ProximityIntentReceiver";

    private LocationManager locationManager;

    private static final float LATITUDE = 51.934238f;
    private static final float LONGITUDE = 4.471843f;

//    private static final float LATITUDE = 51.897648f;
//    private static final float LONGITUDE = 4.494342f;
    
    //private static final float LATITUDE = 51.919606f;
    //private static final float LONGITUDE = 4.456255f;

    
    private ArrayList<Event> events;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_program);
        parser = new ProgramParser("program.json", this);
		events = parser.getEvents();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ListView listView = (ListView) findViewById(R.id.programlist);
		String[] values = new String[events.size()];
		for(int i = 0; i < events.size(); i++) {
			values[i] = events.get(i).getTitle();
		} 

		// Get data from the table by the ListAdapter
		EventListAdapter eventAdapter = new EventListAdapter(this, R.layout.eventlistrow, events);

		View headerView = View.inflate(this, R.layout.activity_program_header, null);
		View footerView = View.inflate(this, R.layout.activity_program_footer, null);
		
		listView.addHeaderView(headerView);
		listView.addFooterView(footerView);
		
		addListenerToListView(listView);
		addListenerToLogo();
		addInfoListener();

        // Get our twitter handle input field and attach a listener
        final EditText twitter = (EditText)findViewById(R.id.username);
        twitter.setText(retrieveTwitterFromPreferences());

        if (isNotEmpty(retrieveTwitterFromPreferences())) {
            enableMonitoring();
        }

        twitter.setImeOptions(EditorInfo.IME_ACTION_DONE);
        twitter.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            private String previousString;

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (EditorInfo.IME_ACTION_DONE == actionId) {
                    final String twitterHandle = twitter.getText().toString();
                    savePreferences(twitterHandle);
                    if (isNotEmpty(twitterHandle)) {
                        enableMonitoring();
                    } else {
                        disableMonitoring(previousString);
                    }

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(twitter.getWindowToken(), 0);


                    return true;
                }
                this.previousString = twitter.getText().toString();
                return false;
            }

        });


        registerReceiver(receiver, filter);
		 
		// Assign adapter to ListView
		listView.setAdapter(eventAdapter);
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
          		if(position > events.size()) {
				  return;
                }
				Intent intent = new Intent(view.getContext(), EventDetailActivity.class);
				Event event = parser.getEvent(position - 1);
				intent.putExtra("com.lunatech.joyofcoding.Event", event);
				startActivity(intent);
			  }
		  });
	}

	private void addListenerToLogo() {
        RelativeLayout footer = (RelativeLayout) findViewById(R.id.footer);
        footer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("http://lunatech.com");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

    }
	
	public void enableMonitoring() {
        boolean monitoringEnabled = retrieveMonitoringPreferences();
        if(!monitoringEnabled){
			Log.i(this.getClass().getSimpleName(), "enable monitoring");
		    addProximityAlert(LATITUDE, LONGITUDE);
            saveMonitoringPreferences(true);
		}
	}

    public void disableMonitoring(String previousTwitterHandle) {
        boolean monitoringEnabled = retrieveMonitoringPreferences();
        if (monitoringEnabled) {
            Log.i(this.getClass().getSimpleName(), "stop monitoring");

            // Checkout the user
            if (isNotEmpty(previousTwitterHandle)) {
                ProximityIntentReceiver.DashboardTask task  = new ProximityIntentReceiver.DashboardTask("http://joyofcoding.lunatech.com/checkin/" + previousTwitterHandle);
                task.execute();
            }

            Intent intent = new Intent(PROX_ALERT_INTENT);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            locationManager.removeProximityAlert(proximityIntent);
            proximityIntent.cancel();
            saveMonitoringPreferences(false);
        }
    }

    private void addProximityAlert(double latitude, double longitude) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        locationManager.addProximityAlert(
                LATITUDE, // the latitude of the central point of the alert region
                LONGITUDE, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );


    }

    IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
    BroadcastReceiver receiver = new ProximityIntentReceiver(this);

    @Override
    protected void onDestroy(){
        unregisterReceiver(receiver);
    	super.onDestroy();
    }
    
    @Override
    public void finish() {
        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        locationManager.removeProximityAlert(proximityIntent);
        proximityIntent.cancel();
        super.finish();
    }
    
    protected void savePreferences(String twitter) {
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

    protected void saveMonitoringPreferences(boolean monitoringEnabled) {
        SharedPreferences prefs =
                this.getSharedPreferences(getClass().getSimpleName(),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(MONITORING_KEY, monitoringEnabled);
        prefsEditor.commit();
    }

    protected boolean retrieveMonitoringPreferences() {
        SharedPreferences prefs =
                this.getSharedPreferences(getClass().getSimpleName(),
                        Context.MODE_PRIVATE);
        return prefs.getBoolean(MONITORING_KEY, false);
    }

}