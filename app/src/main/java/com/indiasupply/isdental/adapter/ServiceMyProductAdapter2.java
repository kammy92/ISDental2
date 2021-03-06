package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.SwiggyMyProductDetailActivity;
import com.indiasupply.isdental.dialog.ServiceAddNewRequestDialogFragment;
import com.indiasupply.isdental.fragment.ServiceFragment2;
import com.indiasupply.isdental.model.MyProduct2;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class ServiceMyProductAdapter2 extends RecyclerView.Adapter<ServiceMyProductAdapter2.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<MyProduct2> swiggyMyProductList = new ArrayList<> ();
    
    public ServiceMyProductAdapter2 (Activity activity, List<MyProduct2> swiggyMyProductList) {
        this.activity = activity;
        this.swiggyMyProductList = swiggyMyProductList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_service_my_product2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyProduct2 product = swiggyMyProductList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvProductDescription);
        holder.tvProductDescription.setText (product.getBrand () + " " + product.getDescription () + " - " + product.getSerial_number ());
        holder.tvProductModelNumber.setText (product.getModel_number ());
        holder.ivProductImage.setImageResource (product.getIcon ());
        holder.progressBar1.setVisibility (View.GONE);
    
    
        holder.rlRequestService.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
            
                android.app.FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
                ServiceAddNewRequestDialogFragment dialog = new ServiceAddNewRequestDialogFragment ().newInstance (0, product.getBrand (), product.getDescription (), product.getSerial_number (), product.getModel_number (), product.getPurchase_date (), String.valueOf (product.getId ()));
                dialog.setDismissListener (new SwiggyMyProductDetailActivity.MyDialogCloseListener () {
                    @Override
                    public void handleDialogClose (DialogInterface dialog) {
                        Log.e ("Return Page", "Return Page");
                        Fragment selectedFragment = null;
                        selectedFragment = ServiceFragment2.newInstance (true);
                        ((FragmentActivity) activity).getSupportFragmentManager ().beginTransaction ()
                                .replace (R.id.frame_layout, selectedFragment)
                                .commit ();
            
                    }
                });
                dialog.show (ft, "Contacts");
            
            }
        });
        
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
            holder.tvProductRequestDate.setText (Utils.convertTimeFormat (product.getRequest_created_at (), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy"));
            holder.tvProductStatus.setText (product.getRequest_status ());
            holder.tvProductRequestDate.setVisibility (View.VISIBLE);
            holder.tvProductStatus.setVisibility (View.VISIBLE);
            if (product.getRequest_status ().equalsIgnoreCase ("OPEN")) {
                holder.tvProductStatus.setTextColor (activity.getResources ().getColor (R.color.mb_green_dark));
//                holder.tvProductStatus.setBackgroundColor (activity.getResources ().getColor (R.color.mb_green_dark));
            } else {
                holder.tvProductStatus.setTextColor (activity.getResources ().getColor (R.color.secondary_text2));
//                holder.tvProductStatus.setBackgroundColor (activity.getResources ().getColor (R.color.secondary_text2));
            }
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
        RelativeLayout rlStatus;
        RelativeLayout rlRequestService;
        
        public ViewHolder (View view) {
            super (view);
            tvProductModelNumber = (TextView) view.findViewById (R.id.tvProductModelNumber);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductRequestDate = (TextView) view.findViewById (R.id.tvProductRequestDate);
            tvProductStatus = (TextView) view.findViewById (R.id.tvProductStatus);
            rlStatus = (RelativeLayout) view.findViewById (R.id.rlStatus);
            ivProductImage = (ImageView) view.findViewById (R.id.ivProductImage);
            progressBar1 = (ProgressBar) view.findViewById (R.id.progressBar2);
            rlRequestService = (RelativeLayout) view.findViewById (R.id.rl4);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            MyProduct2 myProduct2 = swiggyMyProductList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, SwiggyMyProductDetailActivity.class);
            intent.putExtra (AppConfigTags.PRODUCT_ID, myProduct2.getId ());
            intent.putExtra (AppConfigTags.PRODUCT_BRAND, myProduct2.getBrand ());
            intent.putExtra (AppConfigTags.PRODUCT_MODEL_NUMBER, myProduct2.getModel_number ());
            intent.putExtra (AppConfigTags.PRODUCT_SERIAL_NUMBER, myProduct2.getSerial_number ());
            intent.putExtra (AppConfigTags.PRODUCT_DESCRIPTION, myProduct2.getDescription ());
            intent.putExtra (AppConfigTags.PRODUCT_PURCHASE_DATE, myProduct2.getPurchase_date ());
            activity.startActivity (intent);
        }
    }
}