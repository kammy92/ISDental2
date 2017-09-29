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
import com.indiasupply.isdental.model.Product;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.indiasupply.isdental.utils.Utils.makeTextViewResizable;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyRecommendedProductAdapter2 extends RecyclerView.Adapter<SwiggyRecommendedProductAdapter2.ViewHolder> {
    CompanyListAdapter.OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Product> productList = new ArrayList<> ();
    
    public SwiggyRecommendedProductAdapter2 (Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_recommended2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Product product = productList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);

//        holder.tvProductName.setTypeface (SetTypeFace.getTypeface (activity), Typeface.BOLD);
//        holder.tvProductPrice.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvProductDescription.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvAdd.setTypeface (SetTypeFace.getTypeface (activity));
        
        holder.tvProductName.setText (product.getName ());
        holder.tvProductPrice.setText (product.getPrice ());
        holder.tvProductDescription.setText (product.getDescription ());
        if (holder.tvProductDescription.getText ().toString ().trim ().length () > 70) {
            makeTextViewResizable (holder.tvProductDescription, 2, "...more", true);
        }
        
        Glide.with (activity)
                .load (product.getImage ())
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
                .into (holder.ivProductImage);
    }
    
    @Override
    public int getItemCount () {
        return productList.size ();
    }
    
    public void SetOnItemClickListener (final CompanyListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductDescription;
        TextView tvAdd;
        ImageView ivProductImage;
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvAdd = (TextView) view.findViewById (R.id.tvAdd);
            ivProductImage = (ImageView) view.findViewById (R.id.ivProductImage);
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
