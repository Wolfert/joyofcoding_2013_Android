package com.lunatech.joyofcoding;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.lunatech.joyofcoding.R;

public class EventListAdapter extends ArrayAdapter<Event> {
	private List<Event> events;
	private Context context;
	public EventListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public EventListAdapter(Context context, int resource, List<Event> events) {
	    super(context, resource, events);
	    this.events = events;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = LayoutInflater.from(getContext());
        View view = inflator.inflate(R.layout.eventlistrow, null);

	    Event event = events.get(position);
	    
	    if (event != null) {
	        TextView tvTime = (TextView) view.findViewById(R.id.time);
	        TextView tvTitle = (TextView) view.findViewById(R.id.title);
	        TextView tvSpeaker = (TextView) view.findViewById(R.id.speaker);
	        tvTime.setText(event.getTime());
	        tvTitle.setText(event.getTitle());
	        tvSpeaker.setText(event.getSpeaker());

			Typeface font = Typeface.createFromAsset(context.getAssets(), "Arvo-Regular.ttf");
			tvSpeaker.setTypeface(font);

	        int color = event.getColor();
	        switch (color) {
	        	case 0: //Green
	        		color = 0x329CA958;
	        		view.setEnabled(false);
	        		break;
	        	case 1: // Red
	        		color = 0x32AE5E72;
	        		break;
	        	case 2: // Blue
	        		color = 0x3262879F;
	        		break;
	        	default:
	        		color = 0x329CA958;
	        		break;
	        }
	        view.setBackgroundColor(color);
	    }
	    return view;
	}
	public boolean isEnabled(int position) {
		return events.get(position).getColor() == 0 ? false : true;
	}
	
	public boolean areAllItemsEnabled() {
		return false;
	}
}
