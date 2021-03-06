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
import com.indiasupply.isdental.model.Company2;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter2 extends RecyclerView.Adapter<CompanyAdapter2.ViewHolder> {
    OnItemClickListener mItemClickListener;
    //    OnBottomReachedListener onBottomReachedListener;
    private Activity activity;
    private List<Company2> companyList = new ArrayList<> ();
    
    public CompanyAdapter2 (Activity activity, List<Company2> companyList) {
        this.activity = activity;
        this.companyList = companyList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_company2, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Company2 company = companyList.get (position);
    
        Utils.setTypefaceToAllViews (activity, holder.tvCompanyName);
    
        holder.tvCompanyName.setText (company.getName ());
        holder.tvCompanyCategory.setText (company.getCategory ());
        holder.tvCompanyContacts.setText (company.getNo_of_contacts () + " CONTACTS");
        
        if (company.getImage ().length () == 0) {
            holder.ivCompanyImage.setImageResource (company.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (company.getImage ())
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
                    .error (company.getIcon ())
                    .into (holder.ivCompanyImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return companyList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCompanyName;
        TextView tvCompanyCategory;
        TextView tvCompanyContacts;
        ImageView ivCompanyImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvCompanyName = (TextView) view.findViewById (R.id.tvCompanyName);
            tvCompanyCategory = (TextView) view.findViewById (R.id.tvCompanyCategory);
            tvCompanyContacts = (TextView) view.findViewById (R.id.tvContactContacts);
            ivCompanyImage = (ImageView) view.findViewById (R.id.ivCompanyImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}