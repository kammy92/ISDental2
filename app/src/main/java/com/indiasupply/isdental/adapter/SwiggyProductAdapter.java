package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyProduct;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyProductAdapter extends RecyclerView.Adapter<SwiggyProductAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyProduct> swiggyProductList = new ArrayList<> ();
    
    public SwiggyProductAdapter (Activity activity, List<SwiggyProduct> swiggyProductList) {
        this.activity = activity;
        this.swiggyProductList = swiggyProductList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_product_normal, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyProduct swiggyProduct = swiggyProductList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);

//        holder.tvProductName.setTypeface (SetTypeFace.getTypeface (activity), Typeface.BOLD);
//        holder.tvProductCategory.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvProductDescription.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvProductPrice.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvAdd.setTypeface (SetTypeFace.getTypeface (activity));
    
        if (swiggyProduct.getName ().length () > 0) {
            holder.tvProductName.setVisibility (View.VISIBLE);
            holder.tvProductName.setText (swiggyProduct.getName ());
        } else {
            holder.tvProductName.setVisibility (View.GONE);
        }
        if (swiggyProduct.getDescription ().length () > 0) {
            holder.tvProductDescription.setVisibility (View.VISIBLE);
            holder.tvProductDescription.setText (swiggyProduct.getDescription ());
            if (swiggyProduct.getDescription ().length () > 70) {
                Utils.makeTextViewResizable (holder.tvProductDescription, 2, "... more", true);
            }
        } else {
            holder.tvProductDescription.setVisibility (View.GONE);
        }
        if (swiggyProduct.getCategory ().length () > 0) {
            holder.tvProductCategory.setVisibility (View.VISIBLE);
            holder.tvProductCategory.setText (swiggyProduct.getCategory ());
        } else {
            holder.tvProductCategory.setVisibility (View.GONE);
        }
        if (swiggyProduct.getPrice ().length () > 0) {
            holder.tvProductPrice.setVisibility (View.VISIBLE);
            holder.tvProductPrice.setText (swiggyProduct.getPrice ());
        } else {
            holder.tvProductPrice.setVisibility (View.GONE);
        }
    }
    
    @Override
    public int getItemCount () {
        return swiggyProductList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductDescription;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductCategory;
        TextView tvAdd;
        
        public ViewHolder (View view) {
            super (view);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            tvProductCategory = (TextView) view.findViewById (R.id.tvProductCategory);
            tvAdd = (TextView) view.findViewById (R.id.tvAdd);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
