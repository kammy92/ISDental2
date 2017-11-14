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
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyBannerAdapter extends RecyclerView.Adapter<SwiggyBannerAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyBanner> bannerList = new ArrayList<> ();
    
    public SwiggyBannerAdapter (Activity activity, List<SwiggyBanner> bannerList) {
        this.activity = activity;
        this.bannerList = bannerList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_banner, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyBanner banner = bannerList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvBannerTitle);
        holder.tvBannerTitle.setText (banner.getTitle ());
    
        if (banner.getTitle ().length () > 0) {
            holder.tvBannerTitle.setVisibility (View.VISIBLE);
        } else {
            holder.tvBannerTitle.setVisibility (View.GONE);
        }
    
        if (banner.getImage ().length () == 0) {
            holder.ivBanner.setImageResource (banner.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
            holder.tvBannerTitle.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (banner.getImage ())
                    .listener (new RequestListener<String, GlideDrawable> () {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            holder.tvBannerTitle.setVisibility (View.GONE);
                            return false;
                        }
                    
                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .error (banner.getIcon ())
                    .into (holder.ivBanner);
        }
    
    
    }
    
    @Override
    public int getItemCount () {
        return bannerList.size ();
    }
    
    public void SetOnItemClickListener (final SwiggyBannerAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvBannerTitle;
        ImageView ivBanner;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvBannerTitle = (TextView) view.findViewById (R.id.tvBannerTitle);
            ivBanner = (ImageView) view.findViewById (R.id.ivBanner);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
