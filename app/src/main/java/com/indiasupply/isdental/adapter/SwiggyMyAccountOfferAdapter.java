package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        Utils.setTypefaceToAllViews (activity, holder.tvOfferName);
        holder.tvOfferName.setText (myAccountOffer.getName ());
        if (myAccountOffer.getStart ().equalsIgnoreCase (myAccountOffer.getExpire ())) {
            holder.tvOfferStartEndTime.setText (Utils.convertTimeFormat (myAccountOffer.getExpire (), "yyyy-MM-dd", "dd MMM"));
        } else {
            holder.tvOfferStartEndTime.setText (Utils.convertTimeFormat (myAccountOffer.getStart (), "yyyy-MM-dd", "dd") + " - " + Utils.convertTimeFormat (myAccountOffer.getExpire (), "yyyy-MM-dd", "dd MMM"));
        }
        switch (myAccountOffer.getStatus ()) {
            case 0:
                holder.tvOfferStatus.setVisibility (View.VISIBLE);
                holder.tvOfferStatus.setText ("EXPIRE");
                break;
        
            case 1:
                holder.tvOfferStatus.setVisibility (View.GONE);
            
                break;
        
            case 2:
                holder.tvOfferStatus.setVisibility (View.VISIBLE);
                holder.tvOfferStatus.setText ("UPCOMING");
                break;
        }
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
        TextView tvOfferName;
        TextView tvOfferStartEndTime;
        TextView tvOfferStatus;
        
        public ViewHolder (View view) {
            super (view);
            tvOfferName = (TextView) view.findViewById (R.id.tvOfferName);
            tvOfferStartEndTime = (TextView) view.findViewById (R.id.tvOfferStartEndTime);
            tvOfferStatus = (TextView) view.findViewById (R.id.tvOfferStatus);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
