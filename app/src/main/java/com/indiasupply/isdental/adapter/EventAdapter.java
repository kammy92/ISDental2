package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.EventDetailActivity;
import com.indiasupply.isdental.model.Event;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    EventAdapter.OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Event> eventList = new ArrayList<> ();
    
    public EventAdapter (Activity activity, List<Event> eventList) {
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
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Event event = eventList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvEventName);
        
        holder.tvEventName.setText (event.getName ());
        if (event.getStart_date ().equalsIgnoreCase (event.getEnd_date ())) {
            holder.tvEventDetails.setText (Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd MMM") + ", " + event.getLocation ());
        } else {
            holder.tvEventDetails.setText (Utils.convertTimeFormat (event.getStart_date (), "yyyy-MM-dd", "dd") + " - " + Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd MMM") + ", " + event.getLocation ());
        }
    
        if (event.getImage ().length () == 0) {
            holder.ivEventImage.setImageResource (event.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            holder.progressBar.setVisibility (View.VISIBLE);
            Glide.with (activity)
                    .load (event.getImage ())
                    .listener (new RequestListener<String, GlideDrawable> () {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    
                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .error (event.getIcon ())
                    .into (holder.ivEventImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return eventList.size ();
    }
    
    public void SetOnItemClickListener (final EventAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvEventName;
        TextView tvEventDetails;
        ImageView ivEventImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvEventDetails = (TextView) view.findViewById (R.id.tvEventDetails);
            tvEventName = (TextView) view.findViewById (R.id.tvEventName);
            ivEventImage = (ImageView) view.findViewById (R.id.ivEventImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
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
