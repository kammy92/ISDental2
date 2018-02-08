package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.MyAccountEnquiry;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MyAccountEnquiryAdapter extends RecyclerView.Adapter<MyAccountEnquiryAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<MyAccountEnquiry> myAccountEnquiryList = new ArrayList<> ();
    
    public MyAccountEnquiryAdapter (Activity activity, List<MyAccountEnquiry> myAccountEnquiryList) {
        this.activity = activity;
        this.myAccountEnquiryList = myAccountEnquiryList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_account_enquiries, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final MyAccountEnquiry myAccountEnquiry = myAccountEnquiryList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvEnquiryTicketNumber);
    
        holder.tvEnquiryTicketNumber.setText (myAccountEnquiry.getEnquiry_ticket_number ());
        holder.tvCompanyName.setText (myAccountEnquiry.getCompany_name ());
        holder.tvProductName.setText (myAccountEnquiry.getProduct_name ());
        holder.tvProductDescription.setText (myAccountEnquiry.getProduct_description ());
        holder.tvProductPrice.setText (myAccountEnquiry.getProduct_price ());
    
        if (myAccountEnquiry.getEnquiry_comment ().length () > 0) {
            holder.tvEnquiryComment.setText ("Comment : " + myAccountEnquiry.getEnquiry_comment ());
            holder.tvEnquiryComment.setVisibility (View.VISIBLE);
        } else {
            holder.tvEnquiryComment.setVisibility (View.GONE);
        }
    
    
        if (myAccountEnquiry.getEnquiry_remark ().length () > 0) {
            holder.tvEnquiryRemark.setVisibility (View.VISIBLE);
        } else {
            holder.tvEnquiryRemark.setVisibility (View.GONE);
        }

      /*  switch (myAccountEnquiry.getEnquiry_status()){
            case 0:
                holder.tvEnquiryStatus.setVisibility(View.VISIBLE);
                 holder.tvEnquiryStatus.setText("Expired");
                break;

            case 1:
                holder.tvEnquiryStatus.setVisibility(View.GONE);
                break;

            case 2:
                holder.tvEnquiryStatus.setVisibility(View.VISIBLE);
                holder.tvEnquiryStatus.setText("Upcoming");
                break;
        }
        */

    }
    
    @Override
    public int getItemCount () {
        return myAccountEnquiryList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
        TextView tvProductName;
        TextView tvEnquiryTicketNumber;
        TextView tvCompanyName;
        TextView tvProductDescription;
        TextView tvProductPrice;
        TextView tvEnquiryStatus;
        TextView tvEnquiryRemark;
        TextView tvEnquiryComment;

        
        public ViewHolder (View view) {
            super (view);
    
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvEnquiryTicketNumber = (TextView) view.findViewById (R.id.tvEnquiryTicketNumber);
            tvCompanyName = (TextView) view.findViewById (R.id.tvCompanyName);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            tvEnquiryStatus = (TextView) view.findViewById (R.id.tvEnquiryStatus);
            tvEnquiryRemark = (TextView) view.findViewById (R.id.tvEnquiryRemark);
            tvEnquiryComment = (TextView) view.findViewById (R.id.tvEnquiryComment);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
