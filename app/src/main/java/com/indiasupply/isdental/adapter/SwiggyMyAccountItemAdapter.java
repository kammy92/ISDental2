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
import com.indiasupply.isdental.model.SwiggyMyAccountItem;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyMyAccountItemAdapter extends RecyclerView.Adapter<SwiggyMyAccountItemAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressBar progressBar;
    private Activity activity;
    private List<SwiggyMyAccountItem> serviceItemList = new ArrayList<> ();
    
    public SwiggyMyAccountItemAdapter (Activity activity, List<SwiggyMyAccountItem> serviceItemList) {
        this.activity = activity;
        this.serviceItemList = serviceItemList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        progressBar = new ProgressBar (activity);
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_my_account_item, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyMyAccountItem swiggyMyAccountItem = serviceItemList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvServiceTitle);
    
        if (swiggyMyAccountItem.getDescription ().length () > 0) {
            holder.tvServiceDescription.setVisibility (View.VISIBLE);
        }
    
        if (swiggyMyAccountItem.getImage ().length () == 0) {
            holder.ivServiceIcon.setImageResource (swiggyMyAccountItem.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (swiggyMyAccountItem.getImage ())
                    .listener (new RequestListener<String, GlideDrawable> () {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.ivServiceIcon.setImageResource (swiggyMyAccountItem.getIcon ());
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    
                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .into (holder.ivServiceIcon);
        }
        holder.tvServiceTitle.setText (swiggyMyAccountItem.getTitle ());
        holder.tvServiceDescription.setText (swiggyMyAccountItem.getDescription ());
    }
    
    @Override
    public int getItemCount () {
        return serviceItemList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivServiceIcon;
        TextView tvServiceTitle;
        TextView tvServiceDescription;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            ivServiceIcon = (ImageView) view.findViewById (R.id.ivServiceIcon);
            tvServiceDescription = (TextView) view.findViewById (R.id.tvServiceDescription);
            tvServiceTitle = (TextView) view.findViewById (R.id.tvServiceTitle);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}
