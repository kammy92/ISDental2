package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyMyProductRequest;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyMyProductRequestAdapter extends RecyclerView.Adapter<SwiggyMyProductRequestAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyMyProductRequest> swiggyRequestList = new ArrayList<> ();
    
    public SwiggyMyProductRequestAdapter (Activity activity, List<SwiggyMyProductRequest> swiggyRequestList) {
        this.activity = activity;
        this.swiggyRequestList = swiggyRequestList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_service_product_request, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyMyProductRequest request = swiggyRequestList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvProductDescription);
        //  holder.tvProductName.setText (product.getName ());
        holder.tvProductDescription.setText (request.getDescription ());
        holder.tvProductStatus.setText (request.getStatus ());
        holder.tvProductDate.setText (request.getCreated_at ());
        
    }
    
    @Override
    public int getItemCount () {
        return swiggyRequestList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //    TextView tvProductName;
        TextView tvProductDescription;
        TextView tvProductDate;
        TextView tvProductStatus;
        
        
        public ViewHolder (View view) {
            super (view);
            //   tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductDate = (TextView) view.findViewById (R.id.tvProductDate);
            tvProductStatus = (TextView) view.findViewById (R.id.tvProductStatus);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
           /* SwiggyServiceRequest serviceRequest=swiggyRequestList.get(getLayoutPosition());
            android.app.FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
            SwiggyServiceRequestDetailDialogFragment dialog = new SwiggyServiceRequestDetailDialogFragment ()
                    .newInstance (serviceRequest.getBrands(),serviceRequest.getDescription(),serviceRequest.getSerial_number(),
                            serviceRequest.getModel_number(),serviceRequest.getImage1(),serviceRequest.getImage2(),
                            serviceRequest.getImage3(),serviceRequest.getRequest_comments());

            dialog.show (ft, "Contacts");*/
        }
    }
}
