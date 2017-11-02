package com.indiasupply.isdental.adapter;
import android.app.Activity;
import android.content.Intent;
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
import com.indiasupply.isdental.activity.SwiggyCompanyDetailActivity;
import com.indiasupply.isdental.model.SwiggyCompany;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyCompanyAdapter extends RecyclerView.Adapter<SwiggyCompanyAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyCompany> brandList = new ArrayList<> ();
    
    public SwiggyCompanyAdapter (Activity activity, List<SwiggyCompany> brandList) {
        this.activity = activity;
        this.brandList = brandList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_company1, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyCompany brand = brandList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvBrandName);
        
        holder.tvBrandName.setText (brand.getTitle ());
        holder.tvBrandCategory.setText (brand.getCategory ());
        holder.tvContacts.setText (brand.getTotal_contacts () + " CONTACTS");
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
    
    public void SetOnItemClickListener (final SwiggyCompanyAdapter.OnItemClickListener mItemClickListener) {
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
            SwiggyCompany brand = brandList.get (getLayoutPosition ());
            Intent intent5 = new Intent (activity, SwiggyCompanyDetailActivity.class);
            intent5.putExtra (AppConfigTags.COMPANY_ID, brand.getId ());
            activity.startActivity (intent5);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
