package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.EventDetailActivity;
import com.indiasupply.isdental.model.Event;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Event> eventList = new ArrayList<> ();

    public EventListAdapter(Activity activity, List<Event> eventList) {
        this.activity = activity;
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_event, parent, false);
        return new ViewHolder (sView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Event event = eventList.get (position);
    
        holder.tvName.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvStartDate.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvEndDate.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvVenue.setTypeface (SetTypeFace.getTypeface (activity));

        holder.tvName.setText (event.getName ());
        holder.tvStartDate.setText (Utils.convertTimeFormat (event.getStart_date (), "yyyy-MM-dd", "dd/MM/yyyy"));
        holder.tvEndDate.setText (Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd/MM/yyyy"));
        holder.tvVenue.setText (event.getCity ());
    }

    @Override
    public int getItemCount () {
        return eventList.size ();
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvStartDate;
        TextView tvEndDate;
        TextView tvVenue;
        RelativeLayout rlMain;

        public ViewHolder (View view) {
            super (view);
            tvName = (TextView) view.findViewById (R.id.tvName);
            tvStartDate = (TextView) view.findViewById (R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById (R.id.tvEndDate);
            tvVenue = (TextView) view.findViewById (R.id.tvVenue);
            view.setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {
            Event event = eventList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, EventDetailActivity.class);
            intent.putExtra (AppConfigTags.EVENT_ID, event.getId ());
            activity.startActivity (intent);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
