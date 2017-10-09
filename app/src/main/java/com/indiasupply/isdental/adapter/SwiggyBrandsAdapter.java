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
import com.indiasupply.isdental.activity.SwiggyBrandDetailActivity;
import com.indiasupply.isdental.model.SwiggyBrand;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyBrandsAdapter extends RecyclerView.Adapter<SwiggyBrandsAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyBrand> brandList = new ArrayList<> ();
    
    public SwiggyBrandsAdapter (Activity activity, List<SwiggyBrand> brandList) {
        this.activity = activity;
        this.brandList = brandList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_brand, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyBrand brand = brandList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvBrandName);
        
        holder.tvBrandName.setText (brand.getTitle ());
        holder.tvBrandCategory.setText (brand.getCategory ());
        holder.tvContacts.setText (brand.getDescription ());
        holder.tvOffer.setText (brand.getOffers ());
        
        if (brand.is_isassured ()) {
            holder.ivISAssured.setVisibility (View.VISIBLE);
        } else {
            holder.ivISAssured.setVisibility (View.GONE);
        }
        holder.tvRating.setText (brand.getRating ());
    
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
    
    public void SetOnItemClickListener (final SwiggyBrandsAdapter.OnItemClickListener mItemClickListener) {
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
            SwiggyBrand brand = brandList.get (getLayoutPosition ());
            Intent intent5 = new Intent (activity, SwiggyBrandDetailActivity.class);
            activity.startActivity (intent5);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
