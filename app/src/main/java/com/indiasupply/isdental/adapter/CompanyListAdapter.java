package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.CompanyDetailActivity;
import com.indiasupply.isdental.model.Company;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;


public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Company> companyList = new ArrayList<> ();
    
    public CompanyListAdapter (Activity activity, List<Company> companyList) {
        this.activity = activity;
        this.companyList = companyList;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_company, parent, false);
        return new ViewHolder (sView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final Company company = companyList.get (position);
    
        holder.tvCompanyName.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvCompanyBrands.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvCompanyName.setText (company.getName ());
    
        if (company.getBrands ().length () == 0 || company.getBrands ().equalsIgnoreCase ("null")) {
            holder.tvCompanyBrands.setVisibility (View.GONE);
        } else {
            holder.tvCompanyBrands.setText ("Deals In: " + company.getBrands ());
            holder.tvCompanyBrands.setVisibility (View.VISIBLE);
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
        ImageView ivCompanyLogo;
        TextView tvCompanyBrands;
        RelativeLayout rlMain;

        public ViewHolder (View view) {
            super (view);
            tvCompanyName = (TextView) view.findViewById (R.id.tvCompanyName);
            ivCompanyLogo = (ImageView) view.findViewById (R.id.ivCompanyLogo);
            tvCompanyBrands = (TextView) view.findViewById (R.id.tvCompanyBrands);
            rlMain = (RelativeLayout) view.findViewById (R.id.rlMain);
            view.setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {
            Company company = companyList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, CompanyDetailActivity.class);
            intent.putExtra (AppConfigTags.COMPANY_ID, company.getId ());
            activity.startActivity (intent);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
