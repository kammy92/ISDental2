package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.graphics.Paint;
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
import com.indiasupply.isdental.model.SwiggyOffers;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyOfferAdapter extends RecyclerView.Adapter<SwiggyOfferAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    //    OnBottomReachedListener onBottomReachedListener;
    private Activity activity;
    private List<SwiggyOffers> offerList = new ArrayList<> ();
    
    public SwiggyOfferAdapter (Activity activity, List<SwiggyOffers> offerList) {
        this.activity = activity;
        this.offerList = offerList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_offers, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyOffers offer = offerList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvOfferName);
        
        holder.tvOfferName.setText (offer.getName ());
        holder.tvOfferPackaging.setText (offer.getPackaging ());
        holder.tvOfferDescription.setText (offer.getDescription ());
        holder.tvOfferPrice.setText ("Rs. " + offer.getPrice ());
        holder.tvOfferRegularPrice.setText ("Rs. " + offer.getRegular_price ());
        holder.tvOfferMRP.setText ("Rs. " + offer.getMrp ());
//        holder.tvOfferSavings.setText ("Total Saving\nRs. " + offer.getSaving ());
        holder.tvOfferSavings.setText ("Rs. " + offer.getSaving ());
        
        holder.tvOfferMRP.setPaintFlags (holder.tvOfferMRP.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvOfferRegularPrice.setPaintFlags (holder.tvOfferRegularPrice.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        
        if (offer.getImage ().length () == 0) {
            holder.ivOfferImage.setImageResource (offer.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (offer.getImage ())
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
                    .error (offer.getIcon ())
                    .into (holder.ivOfferImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return offerList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOfferName;
        TextView tvOfferDescription;
        TextView tvOfferPackaging;
        TextView tvOfferPrice;
        TextView tvOfferRegularPrice;
        TextView tvOfferMRP;
        TextView tvOfferSavings;
        TextView tvSendEnquiry;
        ImageView ivOfferImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvOfferName = (TextView) view.findViewById (R.id.tvOfferName);
            tvOfferDescription = (TextView) view.findViewById (R.id.tvOfferDescription);
            ivOfferImage = (ImageView) view.findViewById (R.id.ivOfferImage);
            tvOfferPackaging = (TextView) view.findViewById (R.id.tvOfferPackaging);
            tvOfferPrice = (TextView) view.findViewById (R.id.tvOfferPrice);
            tvOfferRegularPrice = (TextView) view.findViewById (R.id.tvOfferRegularPrice);
            tvOfferMRP = (TextView) view.findViewById (R.id.tvOfferMRP);
            tvOfferSavings = (TextView) view.findViewById (R.id.tvOfferSaving);
            tvSendEnquiry = (TextView) view.findViewById (R.id.tvSendEnquiry);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}