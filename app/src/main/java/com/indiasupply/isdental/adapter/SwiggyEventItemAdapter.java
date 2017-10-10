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
import com.indiasupply.isdental.model.SwiggyEventItem;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyEventItemAdapter extends RecyclerView.Adapter<SwiggyEventItemAdapter.ViewHolder> {
    SwiggyEventItemAdapter.OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyEventItem> eventItemList = new ArrayList<> ();
    
    public SwiggyEventItemAdapter (Activity activity, List<SwiggyEventItem> eventItemList) {
        this.activity = activity;
        this.eventItemList = eventItemList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_event_item, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyEventItem eventItem = eventItemList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvItemName);
        holder.tvItemName.setText (eventItem.getName ());
    
        if (eventItem.getImage ().length () == 0) {
            holder.ivItemImage.setImageResource (eventItem.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (eventItem.getImage ())
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
                    .error (eventItem.getIcon ())
                    .into (holder.ivItemImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return eventItemList.size ();
    }
    
    public void SetOnItemClickListener (final SwiggyEventItemAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemName;
        ImageView ivItemImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvItemName = (TextView) view.findViewById (R.id.tvItemName);
            ivItemImage = (ImageView) view.findViewById (R.id.ivItemImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}
