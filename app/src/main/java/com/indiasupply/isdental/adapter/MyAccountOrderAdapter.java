package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.MyAccountOrder;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyAccountOrderAdapter extends RecyclerView.Adapter<MyAccountOrderAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    //    OnBottomReachedListener onBottomReachedListener;
    private Activity activity;
    private List<MyAccountOrder> orderList = new ArrayList<> ();
    
    public MyAccountOrderAdapter (Activity activity, List<MyAccountOrder> orderList) {
        this.activity = activity;
        this.orderList = orderList;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_account_order, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyAccountOrder order = orderList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvOfferName);
    
        holder.tvOfferName.setText (order.getOffer_name ());
        holder.tvClaimedDate.setText ("Claimed On :" + Utils.convertTimeFormat (order.getClaimed_date (), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy"));
    
        switch (order.getStatus ()) {
            case 0:
                holder.tvOrderStatus.setText ("Pending");
                break;
            case 1:
                holder.tvOrderStatus.setText ("Processing");
                break;
            case 2:
                holder.tvOrderStatus.setText ("Dispatched");
                break;
            case 3:
                holder.tvOrderStatus.setText ("Delivered");
                break;
            case 4:
                holder.tvOrderStatus.setText ("Failed");
                break;
            case 5:
                holder.tvOrderStatus.setText ("Cancelled");
                break;
        }
        
    }
    
    @Override
    public int getItemCount () {
        return orderList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOfferName;
        TextView tvClaimedDate;
        TextView tvOrderStatus;
        
        public ViewHolder (View view) {
            super (view);
            tvOfferName = (TextView) view.findViewById (R.id.tvOfferName);
            tvClaimedDate = (TextView) view.findViewById (R.id.tvClaimedDate);
            tvOrderStatus = (TextView) view.findViewById (R.id.tvOrderStatus);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}