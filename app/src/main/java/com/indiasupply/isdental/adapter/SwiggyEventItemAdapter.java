package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private List<SwiggyEventItem> swiggyEventItemList = new ArrayList<> ();
    
    public SwiggyEventItemAdapter (Activity activity, List<SwiggyEventItem> swiggyEventItemList) {
        this.activity = activity;
        this.swiggyEventItemList = swiggyEventItemList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_event_item, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyEventItem eventItem = swiggyEventItemList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvItemName);
        holder.tvItemName.setText (eventItem.getName ());
        Glide.with (activity)
                .load (eventItem.getIcon ())
                .into (holder.ivItemIcon);
    }
    
    @Override
    public int getItemCount () {
        return swiggyEventItemList.size ();
    }
    
    public void SetOnItemClickListener (final SwiggyEventItemAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemName;
        ImageView ivItemIcon;
        
        public ViewHolder (View view) {
            super (view);
            tvItemName = (TextView) view.findViewById (R.id.tvItemName);
            ivItemIcon = (ImageView) view.findViewById (R.id.ivItemIcon);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}
