package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.CompanyDetailActivity;
import com.indiasupply.isdental.model.Company;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Company> brandList = new ArrayList<> ();
    
    private FirebaseAnalytics mFirebaseAnalytics;
    
    public CompanyAdapter (Activity activity, List<Company> brandList) {
        this.activity = activity;
        this.brandList = brandList;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_company1, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Company brand = brandList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvBrandName);
        
        holder.tvBrandName.setText (brand.getTitle ());
        holder.tvBrandCategory.setText (brand.getCategory ());
        holder.tvContacts.setText (brand.getTotal_products () + " PRODUCTS");
        if (! brand.getOffers ().equalsIgnoreCase ("0")) {
            holder.tvOffer.setText (brand.getOffers () + " OFFERS");
        } else {
            holder.tvOffer.setVisibility (View.INVISIBLE);
        }
        
        if (brand.is_isassured ()) {
            holder.ivISAssured.setVisibility (View.VISIBLE);
        } else {
            holder.ivISAssured.setVisibility (View.GONE);
        }
        if (! brand.getTotal_ratings ().equalsIgnoreCase ("0")) {
            holder.tvRating.setText (brand.getRating ());
        } else {
            holder.tvRating.setText ("0.0");
        }
    
        if (brand.getImage ().length () == 0) {
            holder.ivBrandImage.setImageResource (brand.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (brand.getImage ())
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
                    .error (brand.getIcon ())
                    .into (holder.ivBrandImage);
        }
     
    }
    
    @Override
    public int getItemCount () {
        return brandList.size ();
    }
    
    public void SetOnItemClickListener (final CompanyAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvBrandName;
        TextView tvBrandCategory;
        TextView tvOffer;
        ImageView ivISAssured;
        TextView tvRating;
        TextView tvContacts;
        ImageView ivBrandImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvBrandName = (TextView) view.findViewById (R.id.tvBrandName);
            tvBrandCategory = (TextView) view.findViewById (R.id.tvBrandCategory);
            tvOffer = (TextView) view.findViewById (R.id.tvOffer);
            tvRating = (TextView) view.findViewById (R.id.tvRating);
            tvContacts = (TextView) view.findViewById (R.id.tvContacts);
            ivISAssured = (ImageView) view.findViewById (R.id.ivISAssured);
            ivBrandImage = (ImageView) view.findViewById (R.id.ivBrand);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            Company brand = brandList.get (getLayoutPosition ());
            Intent intent5 = new Intent (activity, CompanyDetailActivity.class);
            intent5.putExtra (AppConfigTags.COMPANY_ID, brand.getId ());
            activity.startActivity (intent5);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
    
            // [START custom_event]
            Bundle params = new Bundle ();
            params.putBoolean ("clicked", true);
            params.putInt ("company_id", brand.getId ());
            mFirebaseAnalytics.logEvent ("company_detail", params);
            // [END custom_event]
    
        }
    }
}
