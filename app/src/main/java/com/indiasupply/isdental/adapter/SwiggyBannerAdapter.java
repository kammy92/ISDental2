package com.indiasupply.isdental.adapter;

import android.app.Activity;
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
import com.indiasupply.isdental.model.SwiggyBanner;
import com.indiasupply.isdental.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyBannerAdapter extends RecyclerView.Adapter<SwiggyBannerAdapter.ViewHolder> {
    SwiggyBannerAdapter.OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyBanner> swiggyBannerList = new ArrayList<> ();
    
    public SwiggyBannerAdapter (Activity activity, List<SwiggyBanner> swiggyBannerList) {
        this.activity = activity;
        this.swiggyBannerList = swiggyBannerList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_banner, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyBanner product = swiggyBannerList.get (position);
        
        holder.tvProductType.setTypeface (SetTypeFace.getTypeface (activity));
        
        holder.tvProductType.setText (product.getBanner_title ());
        
        Glide.with (activity)
                .load (product.getBanner_image ())
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
                .into (holder.ivProductLogo);
        
        
    }
    
    @Override
    public int getItemCount () {
        return swiggyBannerList.size ();
    }
    
    public void SetOnItemClickListener (final SwiggyBannerAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductType;
        ImageView ivProductLogo;
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvProductType = (TextView) view.findViewById (R.id.tvBannerTitle);
            ivProductLogo = (ImageView) view.findViewById (R.id.ivProduct);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
           /* Company company = companyList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, CompanyDetailActivity.class);
            intent.putExtra (AppConfigTags.COMPANY_ID, company.getId ());
            activity.startActivity (intent);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);*/
        }
    }
}
