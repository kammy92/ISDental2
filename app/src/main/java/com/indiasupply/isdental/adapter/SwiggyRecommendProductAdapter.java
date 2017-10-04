package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import com.indiasupply.isdental.fragment.SwiggyProductDetailDialogFragment;
import com.indiasupply.isdental.model.SwiggyProduct;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyRecommendProductAdapter extends RecyclerView.Adapter<SwiggyRecommendProductAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyProduct> recommendList = new ArrayList<> ();
    
    public SwiggyRecommendProductAdapter (Activity activity, List<SwiggyProduct> recommendList) {
        this.activity = activity;
        this.recommendList = recommendList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_product_recommended_small, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyProduct recommended = recommendList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);

//        holder.tvProductName.setTypeface (SetTypeFace.getTypeface (activity), Typeface.BOLD);
//        holder.tvProductCategory.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvProductPrice.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvAdd.setTypeface (SetTypeFace.getTypeface (activity));
        
        holder.tvProductName.setText (recommended.getName ());
        holder.tvProductCategory.setText (recommended.getCategory ());
        holder.tvProductPrice.setText (recommended.getPrice ());
        
        Glide.with (activity)
                .load (recommended.getImage ())
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
                .into (holder.ivProductImage);
    }
    
    @Override
    public int getItemCount () {
        return recommendList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductCategory;
        TextView tvProductPrice;
        TextView tvAdd;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            ivProductImage = (ImageView) view.findViewById (R.id.ivProductImage);
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductCategory = (TextView) view.findViewById (R.id.tvProductCategory);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            tvAdd = (TextView) view.findViewById (R.id.tvAdd);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
            SwiggyProductDetailDialogFragment frag = new SwiggyProductDetailDialogFragment ();
            frag.show (ft, "rahul");
        }
    }
}