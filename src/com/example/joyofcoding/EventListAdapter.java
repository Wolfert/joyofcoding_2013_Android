package com.example.joyofcoding;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventListAdapter extends ArrayAdapter<Event> {
	private List<Event> events;

	public EventListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public EventListAdapter(Context context, int resource, List<Event> events) {
	    super(context, resource, events);
	    this.events = events;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;

	    if (view == null) {
	        LayoutInflater inflator;
	        inflator = LayoutInflater.from(getContext());
	        view = inflator.inflate(R.layout.eventlistrow, null);
	    }

	    Event event = events.get(position);

	    if (event != null) {
	        TextView tvTime = (TextView) view.findViewById(R.id.time);
	        TextView tvTitle = (TextView) view.findViewById(R.id.title);
	        TextView tvSpeaker = (TextView) view.findViewById(R.id.speaker);
	        tvTime.setText(event.getTime());
	        tvTitle.setText(event.getTitle());
	        tvSpeaker.setText(event.getSpeaker());
	    }
	    
	    return view;
	}
}
