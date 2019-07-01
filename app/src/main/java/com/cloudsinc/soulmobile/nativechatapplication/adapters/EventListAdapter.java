package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.utils.DateClass;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developers on 14/6/19.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    private List<Event> eventList;
    private Context context;
    private Fragment mfragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, event_from,event_to, location;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            event_from = (TextView) view.findViewById(R.id.event_from);
            event_to = (TextView) view.findViewById(R.id.event_to);
            location = (TextView) view.findViewById(R.id.location);
        }
    }


    public EventListAdapter(Context contex, ArrayList<Event> eventList, Fragment mfragment) {
        this.eventList = eventList;
        this.context = contex;
        this.mfragment = mfragment;
    }

    public void reload(ArrayList<Event> list) {
        eventList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eventlist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event eventData=eventList.get(position);
        holder.title.setText(eventData.getCmlTitle());
        holder.location.setText(eventData.getEventLocation());
        Log.d("SSSSSSSSSSSSSSSSSSS",eventData.getEventFromDate());
        String timetoevent=DateClass.changeDateFormatToTimeanddate(eventData.getEventToDate());
        String timefromevent= DateClass.changeDateFormatToTimeanddate(eventData.getEventFromDate());
        Log.d("ffffffff",timefromevent);
        holder.event_from.setText(timefromevent);
        holder.event_to.setText(timetoevent);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
