package com.example.joyofcoding;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class EventDetailActivity extends Activity {
	private static WebView contentView;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		Event event = bundle.getParcelable("com.example.joyofcoding.Event");

		TextView title = (TextView) findViewById(R.id.title);
		if (title != null)
			title.setText(event.getTitle());
		else
			Log.i("derp", "null");
		
		contentView = (WebView) findViewById(R.id.contentView);
		contentView.getSettings().setJavaScriptEnabled(true);
		contentView.setBackgroundColor(0x00000000);
	    final ProgressDialog pd = ProgressDialog.show(this, null, "Loading..");

		contentView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if(pd.isShowing() && pd != null)
                {
                    pd.dismiss();
                }
            }
		});
		DownloadExternalContentTask task  = new DownloadExternalContentTask(contentView);
		task.execute(event.getContentURL());
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

class DownloadExternalContentTask extends AsyncTask<String, Void, String> {
    private WebView contentView;

    public DownloadExternalContentTask(WebView contentView) {
        this.contentView = contentView;
    }

    protected String doInBackground(String... args) {
    	String html = "<style type=\"text/css\"></style>\n<link href=\'http://fonts.googleapis.com/css?family=Monda|Arvo\' rel=\'stylesheet\' type=\'text/css\'>\n<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\">\n<link href=\"css/bootstrap-responsive.min.css\" rel=\"stylesheet\" type=\"text/css\">\n<link href=\"css/font-awesome.min.css\" rel=\"stylesheet\">\n<link href=\"css/jquery.fancybox.css?v=2.1.3\" rel=\"stylesheet\" type=\"text/css\">\n<link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\">\n\n";
        try {
        	HttpClient client = new DefaultHttpClient();  
		    HttpGet get = new HttpGet(args[0]);
		    HttpResponse responseGet = client.execute(get);  
		    HttpEntity resEntityGet = responseGet.getEntity();  
		    if (resEntityGet != null) {
		    	html += EntityUtils.toString(resEntityGet);
		    }

            return html;
        } catch (Exception e) {
            // Ignore
            return null;
        }
    }

    protected void onPostExecute(String content) {
        if (content != null)
    	    contentView.loadDataWithBaseURL("http://joyofcoding.org", content, "text/html", "UTF-8", "");
    }
 }
