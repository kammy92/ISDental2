package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyMyRequest;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyServiceMyRequestAdapter extends RecyclerView.Adapter<SwiggyServiceMyRequestAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyMyRequest> swiggyMyRequestList = new ArrayList<> ();
    
    public SwiggyServiceMyRequestAdapter (Activity activity, List<SwiggyMyRequest> swiggyMyRequestList) {
        this.activity = activity;
        this.swiggyMyRequestList = swiggyMyRequestList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_service_my_request, parent, false);
        
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyMyRequest product = swiggyMyRequestList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvRequestName);
        holder.tvRequestName.setText (product.getRequest_name ());
        holder.tvRequestDescription.setText (product.getRequest_description ());

     /*   if (product.getImage().length() == 0) {
            holder.ivIcon.setImageResource(product.getIcon());
            holder.progressBar.setVisibility(View.GONE);
        } else {
// holder.progressBar.setVisibility (View.VISIBLE);
            Glide.with(activity)
                    .load(product.getImage())
// .placeholder (eventSchedule.getIcon ())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(product.getIcon())
                    .into(holder.ivIcon);
        }*/
    }
    
    @Override
    public int getItemCount () {
        return swiggyMyRequestList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvRequestName;
        TextView tvRequestDescription;
        ImageView ivIcon;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvRequestName = (TextView) view.findViewById (R.id.tvRequestName);
            tvRequestDescription = (TextView) view.findViewById (R.id.tvRequestDescription);
            ivIcon = (ImageView) view.findViewById (R.id.ivRequestIcon);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}
