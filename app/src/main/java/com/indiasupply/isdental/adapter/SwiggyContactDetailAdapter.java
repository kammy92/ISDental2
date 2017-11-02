package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyContactDetail;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyContactDetailAdapter extends RecyclerView.Adapter<SwiggyContactDetailAdapter.ViewHolder> {
    private Activity activity;
    private List<SwiggyContactDetail> contactDetailList = new ArrayList<> ();
    
    public SwiggyContactDetailAdapter (Activity activity, List<SwiggyContactDetail> contactDetailList) {
        this.activity = activity;
        this.contactDetailList = contactDetailList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_contacts_detail, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyContactDetail contactsDetail = contactDetailList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvContactName);
        
        holder.tvContactName.setText (contactsDetail.getName ());
        holder.tvContactLocation.setText (contactsDetail.getLocation ());
        
        switch (contactsDetail.getType ()) {
            case 1:
                holder.tvContactType.setText ("Company Office");
                break;
            case 2:
                holder.tvContactType.setText ("Sales Office");
                break;
            case 3:
                holder.tvContactType.setText ("Service Office");
                break;
            case 4:
                holder.tvContactType.setText ("Dealer / Distributor");
                break;
        }
    
    
        holder.ivContactFavourite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.is_favourite ()) {
                    contactsDetail.setIs_favourite (false);
                    holder.ivContactFavourite.setImageResource (R.drawable.ic_favourite);
                } else {
                    contactsDetail.setIs_favourite (true);
                    holder.ivContactFavourite.setImageResource (R.drawable.ic_favourite_filled);
                }
            }
        });
    
        holder.rlCall.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.getContact_number ().length () > 0) {
                    Utils.callPhone (activity, contactsDetail.getContact_number ());
                } else {
                    Utils.showToast (activity, "No Phone specified", false);
                }
            }
        });
    
    
        holder.rlMail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.getEmail ().length () > 0) {
                    Utils.shareToGmail (activity, new String[] {contactsDetail.getEmail ()}, "Enquiry", "");
                } else {
                    Utils.showToast (activity, "No email specified", false);
                }
            }
        });
    
    
        if (contactsDetail.getImage ().length () == 0) {
            holder.ivContactImage.setImageResource (contactsDetail.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (contactsDetail.getImage ())
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
                    .error (contactsDetail.getIcon ())
                    .into (holder.ivContactImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return contactDetailList.size ();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvContactName;
        TextView tvContactLocation;
        TextView tvContactType;
        ImageView ivContactFavourite;
        RelativeLayout rlCall;
        RelativeLayout rlMail;
        ImageView ivContactImage;
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvContactName = (TextView) view.findViewById (R.id.tvContactName);
            tvContactLocation = (TextView) view.findViewById (R.id.tvContactLocation);
            tvContactType = (TextView) view.findViewById (R.id.tvContactType);
            ivContactFavourite = (ImageView) view.findViewById (R.id.ivContactFavourite);
            rlCall = (RelativeLayout) view.findViewById (R.id.rlCall);
            rlMail = (RelativeLayout) view.findViewById (R.id.rlMail);
            ivContactImage = (ImageView) view.findViewById (R.id.ivContactImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}