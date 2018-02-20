package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.MyAddress;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private ArrayList<MyAddress> addressList = new ArrayList<> ();
    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    
    public MyAddressAdapter (Activity activity, ArrayList<MyAddress> addressList) {
        this.activity = activity;
        this.addressList = addressList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_address, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyAddress address = addressList.get (position);
        Log.e ("MyAddress", address.getAddress ());
        Utils.setTypefaceToAllViews (activity, holder.tvAddress);
        holder.tvAddress.setText (address.getAddress ());
        holder.tvName.setText (address.getName ());
        if (currentSelectedPosition == position) {
            holder.ll2.setVisibility (View.VISIBLE);
        } else {
            holder.ll2.setVisibility (View.GONE);
        }
        
    }
    
    @Override
    public int getItemCount () {
        return addressList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvAddress;
        TextView tvName;
        RadioButton rbRadio;
        LinearLayout ll2;
        
        
        public ViewHolder (View view) {
            super (view);
            tvAddress = (TextView) view.findViewById (R.id.tvAddress);
            tvName = (TextView) view.findViewById (R.id.tvName);
            ll2 = (LinearLayout) view.findViewById (R.id.ll2);
            rbRadio = (RadioButton) view.findViewById (R.id.rbRadio);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            currentSelectedPosition = getLayoutPosition ();
            notifyDataSetChanged ();
        }
    }
    
    
}
