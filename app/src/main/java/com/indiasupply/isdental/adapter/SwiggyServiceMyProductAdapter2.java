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
import com.indiasupply.isdental.activity.SwiggyMyProductDetailActivity;
import com.indiasupply.isdental.model.SwiggyMyProduct2;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyServiceMyProductAdapter2 extends RecyclerView.Adapter<SwiggyServiceMyProductAdapter2.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyMyProduct2> swiggyMyProductList = new ArrayList<> ();
    
    public SwiggyServiceMyProductAdapter2 (Activity activity, List<SwiggyMyProduct2> swiggyMyProductList) {
        this.activity = activity;
        this.swiggyMyProductList = swiggyMyProductList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_service_my_product2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyMyProduct2 product = swiggyMyProductList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvProductDescription);
        holder.tvProductDescription.setText (product.getBrand () + " " + product.getDescription () + " - " + product.getSerial_number ());
        holder.tvProductModelNumber.setText (product.getModel_number ());
    
    
        holder.ivProductImage.setImageResource (product.getIcon ());
        holder.progressBar1.setVisibility (View.GONE);
    
        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
            if (product.getImage ().endsWith (ext)) {
                Glide.with (activity)
                        .load (product.getImage ())
                        .placeholder (product.getIcon ())
                        .listener (new RequestListener<String, GlideDrawable> () {
                            @Override
                            public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                holder.progressBar1.setVisibility (View.GONE);
                                return false;
                            }
                        
                            @Override
                            public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                holder.progressBar1.setVisibility (View.GONE);
                                return false;
                            }
                        })
                        .error (product.getIcon ())
                        .into (holder.ivProductImage);
                break;
            }
        }
    
/*
        if (product.getImage ().length () == 0) {
            holder.ivProductImage.setImageResource (product.getIcon ());
            holder.progressBar1.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (product.getImage ())
                    .listener (new RequestListener<String, GlideDrawable> () {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar1.setVisibility (View.GONE);
                            return false;
                        }
    
                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar1.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .error (product.getIcon ())
                    .into (holder.ivProductImage);
        }
*/
        
        if (product.getRequest_status ().equalsIgnoreCase ("")) {
            holder.tvProductRequestDate.setVisibility (View.GONE);
            holder.tvProductStatus.setVisibility (View.GONE);
        } else {
            holder.tvProductRequestDate.setText ("REQ. DATE: " + Utils.convertTimeFormat (product.getRequest_created_at (), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy"));
            holder.tvProductStatus.setText (product.getRequest_status ());
            holder.tvProductRequestDate.setVisibility (View.VISIBLE);
            holder.tvProductStatus.setVisibility (View.VISIBLE);
        }
    }
    
    @Override
    public int getItemCount () {
        return swiggyMyProductList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductDescription;
        TextView tvProductModelNumber;
        TextView tvProductRequestDate;
        TextView tvProductStatus;
        ImageView ivProductImage;
        ProgressBar progressBar1;
    
        public ViewHolder (View view) {
            super (view);
            tvProductModelNumber = (TextView) view.findViewById (R.id.tvProductModelNumber);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductRequestDate = (TextView) view.findViewById (R.id.tvProductRequestDate);
            tvProductStatus = (TextView) view.findViewById (R.id.tvProductStatus);
            ivProductImage = (ImageView) view.findViewById (R.id.ivProductImage);
            progressBar1 = (ProgressBar) view.findViewById (R.id.progressBar2);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            SwiggyMyProduct2 swiggyMyProduct2 = swiggyMyProductList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, SwiggyMyProductDetailActivity.class);
            intent.putExtra (AppConfigTags.SWIGGY_PRODUCT_ID, swiggyMyProduct2.getId ());
            intent.putExtra (AppConfigTags.SWIGGY_PRODUCT_BRAND, swiggyMyProduct2.getBrand ());
            intent.putExtra (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER, swiggyMyProduct2.getModel_number ());
            intent.putExtra (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER, swiggyMyProduct2.getSerial_number ());
            intent.putExtra (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION, swiggyMyProduct2.getDescription ());
            intent.putExtra (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE, swiggyMyProduct2.getPurchase_date ());
            activity.startActivity (intent);
        }
    }
}
