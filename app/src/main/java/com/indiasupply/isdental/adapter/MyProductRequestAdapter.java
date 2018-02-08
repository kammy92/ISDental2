package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.MyProductRequest;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class MyProductRequestAdapter extends RecyclerView.Adapter<MyProductRequestAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<MyProductRequest> swiggyRequestList = new ArrayList<> ();
    
    public MyProductRequestAdapter (Activity activity, List<MyProductRequest> swiggyRequestList) {
        this.activity = activity;
        this.swiggyRequestList = swiggyRequestList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_service_product_request, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyProductRequest request = swiggyRequestList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvProductDescription);
        //  holder.tvProductName.setText (product.getName ());
        holder.tvProductDescription.setText (request.getDescription ());
        holder.tvProductStatus.setText (request.getStatus ());
        holder.tvProductDate.setText (Utils.convertTimeFormat (request.getCreated_at (), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm"));
    
        if (request.getStatus ().equalsIgnoreCase ("OPEN")) {
            holder.tvProductStatus.setTextColor (activity.getResources ().getColor (R.color.mb_green_dark));
//            holder.tvProductStatus.setBackgroundColor (activity.getResources ().getColor (R.color.mb_green_dark));
        } else {
            holder.tvProductStatus.setTextColor (activity.getResources ().getColor (R.color.secondary_text2));
//            holder.tvProductStatus.setBackgroundColor (activity.getResources ().getColor (R.color.text_color_grey_dark2));
        }
        
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
        RelativeLayout rlStatus;
        
        
        public ViewHolder (View view) {
            super (view);
            //   tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductDate = (TextView) view.findViewById (R.id.tvProductDate);
            tvProductStatus = (TextView) view.findViewById (R.id.tvProductStatus);
            rlStatus = (RelativeLayout) view.findViewById (R.id.rlStatus);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
           /* SwiggyServiceRequest serviceRequest=swiggyRequestList.get(getLayoutPosition());
            android.app.FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
            ServiceRequestDetailDialogFragment dialog = new ServiceRequestDetailDialogFragment ()
                    .newInstance (serviceRequest.getBrands(),serviceRequest.getDescription(),serviceRequest.getSerial_number(),
                            serviceRequest.getModel_number(),serviceRequest.getImage1(),serviceRequest.getImage2(),
                            serviceRequest.getImage3(),serviceRequest.getRequest_comments());

            dialog.show (ft, "Contacts");*/
        }
    }
}
