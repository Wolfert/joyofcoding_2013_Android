package com.lunatech.joyofcoding;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import com.lunatech.joyofcoding.R;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;
    private ProgramActivity activity;
    private Context context;

    public ProximityIntentReceiver(ProgramActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	this.context = context;
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);
        String twitterHandle = activity.retrieveTwitterFromPreferences();
        if (twitterHandle != null && twitterHandle.length() > 1) {
        	
            if (twitterHandle.length() > 1) {
                if (twitterHandle.toString().startsWith("@"))
                    twitterHandle = twitterHandle.substring(1);
            }

            if (entering) {
                Log.d(getClass().getSimpleName(), "entering");
                // Get our twitter handle
                checkin(twitterHandle, context);
            }
            else {
                Log.d(getClass().getSimpleName(), "exiting");
                checkout(twitterHandle, context);
            }


        }
    }

    private void checkin(String twitterHandle, Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(context, ProgramActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i , 0);

        Notification notification = createNotification("You are near Joy of Coding.");
        notification.setLatestEventInfo(context,
                "JoyOfCoding!", "You are near Joy of Coding.", pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, notification);
        DashboardTask task  = new DashboardTask("http://joyofcoding.lunatech.com/checkin/" + twitterHandle);
        task.execute();
    }

    private void checkout(String twitterHandle, Context context) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent i = new Intent(context, ProgramActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i , 0);

            Notification notification = createNotification("You are left Joy of Coding.");
            notification.setLatestEventInfo(context,
                    "JoyOfCoding!", "You left Joy of Coding.", pendingIntent);

            notificationManager.notify(NOTIFICATION_ID, notification);

            DashboardTask task  = new DashboardTask("http://joyofcoding.lunatech.com/checkout/" + twitterHandle);
            task.execute();
     }

    private Notification createNotification(String text) {
        Notification notification = new Notification();

        notification.icon = R.drawable.logo;
        notification.when = System.currentTimeMillis();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;

//        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;
        notification.tickerText = text;
        
        return notification;
    }


    class DashboardTask extends AsyncTask<Void, Void, Void> {

        private String url;

        public DashboardTask(String url) {
            this.url = url;
        }

        protected Void doInBackground(Void... args) {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            try {
                client.execute(get);
            } catch(Throwable e) {
                // Ignore
            }
            return null;
        }

        protected void onPostExecute(String content) {
        }
    }


	public void forceCheckout(Context context, String twitterhandle) {
		checkout(twitterhandle, context);
	}
}
