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
import com.indiasupply.isdental.model.SwiggyContacts;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyContactAdapter extends RecyclerView.Adapter<SwiggyContactAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyContacts> swiggyContactList = new ArrayList<> ();
    
    public SwiggyContactAdapter (Activity activity, List<SwiggyContacts> swiggyContactList) {
        this.activity = activity;
        this.swiggyContactList = swiggyContactList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_contacts, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyContacts contacts = swiggyContactList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvCompanyName);
        
        holder.tvCompanyName.setText (contacts.getTitle ());
        holder.tvCompanyCategory.setText (contacts.getCategory ());
        holder.tvCompanyContacts.setText (contacts.getContacts ());
        
        
        holder.ivCompanyEmail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contacts.getEmail ().length () > 0) {
                    Intent email = new Intent (Intent.ACTION_SEND);
                    email.putExtra (Intent.EXTRA_EMAIL, new String[] {contacts.getEmail ()});
                    email.putExtra (Intent.EXTRA_SUBJECT, "Enquiry");
                    email.putExtra (Intent.EXTRA_TEXT, "");
                    email.setType ("message/rfc822");
                    activity.startActivity (Intent.createChooser (email, "Choose an Email client :"));
                } else {
                    Utils.showToast (activity, "No email specified", false);
                }
                
            }
        });
        
        holder.ivCompanyWebSite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contacts.getWebsite ().length () > 0) {
                    Uri uri;
                    if (contacts.getWebsite ().contains ("http://") || contacts.getWebsite ().contains ("https://")) {
                        uri = Uri.parse (contacts.getWebsite ());
                    } else {
                        uri = Uri.parse ("http://" + contacts.getWebsite ());
                    }
                    Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                    activity.startActivity (intent);
                } else {
                    Utils.showToast (activity, "No website specified", false);
                }
            }
        });
        
        
        Glide.with (activity)
                .load (contacts.getImage ())
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
                .into (holder.ivCompanyImage);
    }
    
    @Override
    public int getItemCount () {
        return swiggyContactList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCompanyName;
        TextView tvCompanyCategory;
        TextView tvCompanyContacts;
        ImageView ivCompanyEmail;
        ImageView ivCompanyWebSite;
        ImageView ivCompanyImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvCompanyName = (TextView) view.findViewById (R.id.tvCompanyName);
            tvCompanyCategory = (TextView) view.findViewById (R.id.tvCompanyCategory);
            tvCompanyContacts = (TextView) view.findViewById (R.id.tvContactContacts);
            ivCompanyEmail = (ImageView) view.findViewById (R.id.ivCompanyEmail);
            ivCompanyImage = (ImageView) view.findViewById (R.id.ivCompanyImage);
            ivCompanyWebSite = (ImageView) view.findViewById (R.id.ivCompanyWebSite);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}