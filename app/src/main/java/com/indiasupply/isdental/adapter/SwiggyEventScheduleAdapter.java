package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyEventSchedule;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyEventScheduleAdapter extends RecyclerView.Adapter<SwiggyEventScheduleAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyEventSchedule> swiggyEventList = new ArrayList<> ();
    
    public SwiggyEventScheduleAdapter (Activity activity, List<SwiggyEventSchedule> swiggyEventList) {
        this.activity = activity;
        this.swiggyEventList = swiggyEventList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_event_schedule, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyEventSchedule swiggyEventSchedule = swiggyEventList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvDateTime);
        
        holder.tvDateTime.setText (swiggyEventSchedule.getStart_time () + "-" + swiggyEventSchedule.getEnd_time ());
        holder.tvEventName.setText (swiggyEventSchedule.getDescription ());
        holder.tvEventLocation.setText (swiggyEventSchedule.getLocation ());
    }
    
    @Override
    public int getItemCount () {
        return swiggyEventList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDateTime;
        TextView tvEventName;
        TextView tvEventLocation;
        
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvDateTime = (TextView) view.findViewById (R.id.tvDateTime);
            tvEventName = (TextView) view.findViewById (R.id.tvEventName);
            tvEventLocation = (TextView) view.findViewById (R.id.tvEventLocation);
            
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}