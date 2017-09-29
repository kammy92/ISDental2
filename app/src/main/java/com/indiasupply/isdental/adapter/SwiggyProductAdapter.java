package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.Product;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyProductAdapter extends RecyclerView.Adapter<SwiggyProductAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Product> productList = new ArrayList<> ();
    
    public SwiggyProductAdapter (Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_products, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Product product = productList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);

//        holder.tvProductName.setTypeface (SetTypeFace.getTypeface (activity), Typeface.BOLD);
//        holder.tvProductCategory.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvProductDescription.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvProductPrice.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvAdd.setTypeface (SetTypeFace.getTypeface (activity));
        
        if (product.getName ().length () > 0) {
            holder.tvProductName.setVisibility (View.VISIBLE);
            holder.tvProductName.setText (product.getName ());
        } else {
            holder.tvProductName.setVisibility (View.GONE);
        }
        if (product.getDescription ().length () > 0) {
            holder.tvProductDescription.setVisibility (View.VISIBLE);
            holder.tvProductDescription.setText (product.getDescription ());
            if (product.getDescription ().length () > 70) {
                Utils.makeTextViewResizable (holder.tvProductDescription, 2, "... more", true);
            }
        } else {
            holder.tvProductDescription.setVisibility (View.GONE);
        }
        if (product.getCategory ().length () > 0) {
            holder.tvProductCategory.setVisibility (View.VISIBLE);
            holder.tvProductCategory.setText (product.getCategory ());
        } else {
            holder.tvProductCategory.setVisibility (View.GONE);
        }
        if (product.getPrice ().length () > 0) {
            holder.tvProductPrice.setVisibility (View.VISIBLE);
            holder.tvProductPrice.setText (product.getPrice ());
        } else {
            holder.tvProductPrice.setVisibility (View.GONE);
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
