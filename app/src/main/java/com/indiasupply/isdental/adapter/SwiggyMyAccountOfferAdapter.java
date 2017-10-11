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
import com.indiasupply.isdental.model.SwiggyMyAccountOffer;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyMyAccountOfferAdapter extends RecyclerView.Adapter<SwiggyMyAccountOfferAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyMyAccountOffer> myAccountOfferList = new ArrayList<> ();
    
    public SwiggyMyAccountOfferAdapter (Activity activity, List<SwiggyMyAccountOffer> myAccountOfferList) {
        this.activity = activity;
        this.myAccountOfferList = myAccountOfferList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_my_account_offers, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyMyAccountOffer myAccountOffer = myAccountOfferList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvSpeakerName);
        
        if (myAccountOffer.getImage ().length () == 0) {
            holder.ivSpeaker.setImageResource (myAccountOffer.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (myAccountOffer.getImage ())
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
                    .error (myAccountOffer.getIcon ())
                    .into (holder.ivSpeaker);
        }
        
        holder.tvSpeakerName.setText (myAccountOffer.getName ());
        holder.tvSpeakerQualification.setText (myAccountOffer.getQualification ());
    }
    
    @Override
    public int getItemCount () {
        return myAccountOfferList.size ();
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
