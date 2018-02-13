package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
            holder.tvEventDates.setText (Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd MMM"));
        } else {
            holder.tvEventDates.setText (Utils.convertTimeFormat (event.getStart_date (), "yyyy-MM-dd", "dd MMM") + " - " + Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd MMM"));
        }
    
        holder.tvEventVenue.setText (event.getLocation ());
    
        holder.rlInterested.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
            
            }
        });
    
    
        holder.rlInterested.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction ()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.ivInterested.setImageResource (R.drawable.ic_like_filled);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        holder.ivInterested.setImageResource (R.drawable.ic_like);
                        break;
                }
                return false;
            }
        });
    
        holder.rlShare.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                String shareBody = "Hi, Following event ";
                Intent sharingIntent = new Intent (android.content.Intent.ACTION_SEND);
                sharingIntent.setType ("text/plain");
                sharingIntent.putExtra (android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity (Intent.createChooser (sharingIntent, "Share Using"));
            }
        });
    
    
        holder.rlShare.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction ()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.ivShare.setImageResource (R.drawable.ic_share_filled);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        holder.ivShare.setImageResource (R.drawable.ic_share);
                        break;
                }
                return false;
            }
        });
        
        
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
        TextView tvEventDates;
        TextView tvEventVenue;
        ImageView ivEventImage;
        ImageView ivInterested;
        ImageView ivShare;
        RelativeLayout rlInterested;
        RelativeLayout rlShare;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvEventDates = (TextView) view.findViewById (R.id.tvEventDates);
            tvEventName = (TextView) view.findViewById (R.id.tvEventName);
            tvEventVenue = (TextView) view.findViewById (R.id.tvEventVenue);
            ivEventImage = (ImageView) view.findViewById (R.id.ivEventImage);
            ivInterested = (ImageView) view.findViewById (R.id.ivInterested);
            ivShare = (ImageView) view.findViewById (R.id.ivShare);
            rlInterested = (RelativeLayout) view.findViewById (R.id.rlInterested);
            rlShare = (RelativeLayout) view.findViewById (R.id.rlShare);
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
