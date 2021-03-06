package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.MyAccountSaving;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyAccountSavingAdapter extends RecyclerView.Adapter<MyAccountSavingAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    //    OnBottomReachedListener onBottomReachedListener;
    private Activity activity;
    private List<MyAccountSaving> orderList = new ArrayList<> ();
    
    public MyAccountSavingAdapter (Activity activity, List<MyAccountSaving> orderList) {
        this.activity = activity;
        this.orderList = orderList;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_account_saving, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyAccountSaving saving = orderList.get (position);
        holder.tvOfferName.setText (saving.getOffer_name ());
        holder.tvClaimedDate.setText ("Claimed On :" + Utils.convertTimeFormat (saving.getClaimed_date (), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy"));
        holder.tvSavings.setText (activity.getResources ().getString (R.string.Rs) + saving.getSavings ());
    

        Utils.setTypefaceToAllViews (activity, holder.tvOfferName);
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
        TextView tvSavings;
        
        public ViewHolder (View view) {
            super (view);
            tvOfferName = (TextView) view.findViewById (R.id.tvOfferName);
            tvClaimedDate = (TextView) view.findViewById (R.id.tvClaimedDate);
            tvSavings = (TextView) view.findViewById (R.id.tvSavings);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
//            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}