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
import com.indiasupply.isdental.dialog.SwiggyRecommendedProductDialogFragment;
import com.indiasupply.isdental.model.SwiggyProduct;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;


public class SwiggyRecommendedProductAdapter extends RecyclerView.Adapter<SwiggyRecommendedProductAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private ArrayList<SwiggyProduct> productList = new ArrayList<> ();
    
    public SwiggyRecommendedProductAdapter (Activity activity, ArrayList<SwiggyProduct> productList) {
        this.activity = activity;
        this.productList = productList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_product_recommended_small, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyProduct product = productList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);
    
        holder.tvProductName.setText (product.getName ());
        holder.tvProductDescription.setText (product.getDescription ());
        holder.tvProductPrice.setText (product.getPrice ());
    
        if (product.getPackaging ().length () > 0) {
            holder.tvProductPackaging.setText (product.getPackaging ());
            holder.tvProductPackaging.setVisibility (View.VISIBLE);
        }
        
        if (product.getImage ().length () == 0) {
            holder.ivProductImage.setImageResource (product.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            holder.progressBar.setVisibility (View.VISIBLE);
            Glide.with (activity)
                    .load (product.getImage ())
//                    .placeholder (eventSchedule.getIcon ())
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
                    .error (product.getIcon ())
                    .into (holder.ivProductImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return productList.size ();
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
        TextView tvProductDescription;
        TextView tvProductPackaging;
        TextView tvProductPrice;
        TextView tvAdd;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            ivProductImage = (ImageView) view.findViewById (R.id.ivProductImage);
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductPackaging = (TextView) view.findViewById (R.id.tvProductPackaging);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            tvAdd = (TextView) view.findViewById (R.id.tvAdd);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
         /*   FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
            SwiggyRecommendedProductDialogFragment frag = new SwiggyRecommendedProductDialogFragment ();
            frag.show (ft, "rahul");*/
            android.app.FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
            new SwiggyRecommendedProductDialogFragment ().newInstance (productList, getLayoutPosition ()).show (ft, "Products");
        }
    }
}