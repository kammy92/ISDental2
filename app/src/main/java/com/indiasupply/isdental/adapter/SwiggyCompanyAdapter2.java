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
import com.indiasupply.isdental.model.SwiggyCompany2;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyCompanyAdapter2 extends RecyclerView.Adapter<SwiggyCompanyAdapter2.ViewHolder> {
    OnItemClickListener mItemClickListener;
    //    OnBottomReachedListener onBottomReachedListener;
    private Activity activity;
    private List<SwiggyCompany2> companyList = new ArrayList<> ();
    
    public SwiggyCompanyAdapter2 (Activity activity, List<SwiggyCompany2> companyList) {
        this.activity = activity;
        this.companyList = companyList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_company2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyCompany2 company = companyList.get (position);
//        if (position == companyList.size () - 1) {
//            onBottomReachedListener.onBottomReached (position);
//        }
    
    
        Utils.setTypefaceToAllViews (activity, holder.tvCompanyName);
    
        holder.tvCompanyName.setText (company.getName ());
        holder.tvCompanyCategory.setText (company.getCategory ());
        holder.tvCompanyContacts.setText (company.getNo_of_contacts () + " CONTACTS");
        
        holder.ivCompanyEmail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (company.getEmail ().length () > 0) {
                    Intent email = new Intent (Intent.ACTION_SEND);
                    email.putExtra (Intent.EXTRA_EMAIL, new String[] {company.getEmail ()});
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
                if (company.getWebsite ().length () > 0) {
                    Uri uri;
                    if (company.getWebsite ().contains ("http://") || company.getWebsite ().contains ("https://")) {
                        uri = Uri.parse (company.getWebsite ());
                    } else {
                        uri = Uri.parse ("http://" + company.getWebsite ());
                    }
                    Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                    activity.startActivity (intent);
                } else {
                    Utils.showToast (activity, "No website specified", false);
                }
            }
        });
        
        if (company.getImage ().length () == 0) {
            holder.ivCompanyImage.setImageResource (company.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (company.getImage ())
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
                    .error (company.getIcon ())
                    .into (holder.ivCompanyImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return companyList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

//    public void setOnBottomReachedListener (OnBottomReachedListener onBottomReachedListener) {
//        this.onBottomReachedListener = onBottomReachedListener;
//    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }

//    public interface OnBottomReachedListener {
//        public void onBottomReached (int position);
//    }
    
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