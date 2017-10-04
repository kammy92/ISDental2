package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.indiasupply.isdental.model.SwiggyContactsDetail;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyContactDetailAdapter extends RecyclerView.Adapter<SwiggyContactDetailAdapter.ViewHolder> {
    private Activity activity;
    private List<SwiggyContactsDetail> swiggyContactList2 = new ArrayList<> ();
    
    public SwiggyContactDetailAdapter (Activity activity, List<SwiggyContactsDetail> swiggyContactList2) {
        this.activity = activity;
        this.swiggyContactList2 = swiggyContactList2;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_contacts_detail, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyContactsDetail contactsDetail = swiggyContactList2.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvContactName);
        
        holder.tvContactName.setText (contactsDetail.getName ());
        holder.tvContactLocation.setText (contactsDetail.getLocation ().toUpperCase ());
        
        switch (contactsDetail.getType ()) {
            case 1:
                holder.tvContactType.setText ("Service Center");
                break;
            case 2:
                holder.tvContactType.setText ("Dealer");
                break;
            case 3:
                holder.tvContactType.setText ("Distributor");
                break;
            case 4:
                holder.tvContactType.setText ("Sales Office");
                break;
        }
        
        holder.tvCall.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.is_favourite ()) {
                    Utils.showToast (activity, "Removed from favourite", false);
                } else {
                    Utils.showToast (activity, "Added to favourite", false);
                }
            }
        });
        holder.tvCall.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.getContact_number ().length () > 0) {
                    Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactsDetail.getContact_number ()));
                    sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity (sIntent);
                } else {
                    Utils.showToast (activity, "No Phone specified", false);
                }
            }
        });
        
        
        Glide.with (activity)
                .load (contactsDetail.getImage_url ())
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
                .into (holder.ivContactImage);
    }
    
    @Override
    public int getItemCount () {
        return swiggyContactList2.size ();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvContactName;
        TextView tvContactLocation;
        TextView tvContactType;
        ImageView ivContactFavourite;
        TextView tvCall;
        ImageView ivContactImage;
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvContactName = (TextView) view.findViewById (R.id.tvContactName);
            tvContactLocation = (TextView) view.findViewById (R.id.tvContactLocation);
            tvContactType = (TextView) view.findViewById (R.id.tvContactType);
            ivContactFavourite = (ImageView) view.findViewById (R.id.ivContactFavourite);
            tvCall = (TextView) view.findViewById (R.id.tvCall);
            ivContactImage = (ImageView) view.findViewById (R.id.ivContactImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}