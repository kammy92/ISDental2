package com.indiasupply.isdental.adapter;

import android.app.Activity;
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
import com.indiasupply.isdental.model.SwiggyEventSpeaker;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyEventSpeakerAdapter extends RecyclerView.Adapter<SwiggyEventSpeakerAdapter.ViewHolder> {
    final String dialogTag = "dialog";
    OnItemClickListener mItemClickListener;
    ProgressBar progressBar;
    private TextView fullName;
    private Activity activity;
    // dialogFragment.setOnDiscardFromExtraActionListener(this);
    private List<SwiggyEventSpeaker> swiggyEventSpeakerList = new ArrayList<> ();
    
    public SwiggyEventSpeakerAdapter (Activity activity, List<SwiggyEventSpeaker> swiggyEventSpeakerList) {
        this.activity = activity;
        this.swiggyEventSpeakerList = swiggyEventSpeakerList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        progressBar = new ProgressBar (activity);
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_event_speaker, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyEventSpeaker swiggyEventSpeaker = swiggyEventSpeakerList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvSpeakerName);
        
        Glide.with (activity)
                .load (swiggyEventSpeaker.getImage ())
                .listener (new RequestListener<String, GlideDrawable> () {
                    @Override
                    public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility (View.VISIBLE);
                        return false;
                    }
                    
                    @Override
                    public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility (View.GONE);
                        return false;
                    }
                })
                .into (holder.ivSpeaker);
        holder.tvSpeakerName.setText (swiggyEventSpeaker.getName ());
        holder.tvSpeakerQualification.setText (swiggyEventSpeaker.getQualification ());
    }
    
    @Override
    public int getItemCount () {
        return swiggyEventSpeakerList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivSpeaker;
        TextView tvSpeakerName;
        TextView tvSpeakerQualification;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            ivSpeaker = (ImageView) view.findViewById (R.id.ivSpeaker);
            tvSpeakerName = (TextView) view.findViewById (R.id.tvSpeakerName);
            tvSpeakerQualification = (TextView) view.findViewById (R.id.tvSpeakerQualification);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}