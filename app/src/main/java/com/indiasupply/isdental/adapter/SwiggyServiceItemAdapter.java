package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.dialog.SwiggyServiceAddProductDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyServiceAddRequestDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyServiceMyRequestDialogFragment;
import com.indiasupply.isdental.model.SwiggyServiceItem;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyServiceItemAdapter extends RecyclerView.Adapter<SwiggyServiceItemAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyServiceItem> serviceItemList = new ArrayList<> ();
    
    public SwiggyServiceItemAdapter (Activity activity, List<SwiggyServiceItem> serviceItemList) {
        this.activity = activity;
        this.serviceItemList = serviceItemList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_service_item, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyServiceItem serviceItem = serviceItemList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvItemName);
        holder.tvItemName.setText (serviceItem.getName ());
        Glide.with (activity)
                .load (serviceItem.getIcon ())
                .into (holder.ivItemIcon);
    }
    
    @Override
    public int getItemCount () {
        return serviceItemList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemName;
        ImageView ivItemIcon;
        List<String> categoryList = new ArrayList<> ();
        
        public ViewHolder (View view) {
            super (view);
            tvItemName = (TextView) view.findViewById (R.id.tvItemName);
            ivItemIcon = (ImageView) view.findViewById (R.id.ivItemImage);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            final SwiggyServiceItem service = serviceItemList.get (getLayoutPosition ());
            switch (service.getId ()) {
                case 1:
                    android.app.FragmentTransaction ft = activity.getFragmentManager ().beginTransaction ();
                    SwiggyServiceMyProductDialogFragment frag = new SwiggyServiceMyProductDialogFragment ().newInstance (service.getId ());
                    frag.show (ft, "MyProductDialog");
                    break;
                
                case 2:
                    android.app.FragmentTransaction ft2 = activity.getFragmentManager ().beginTransaction ();
                    SwiggyServiceAddProductDialogFragment frag2 = new SwiggyServiceAddProductDialogFragment ().newInstance (service.getId ());
                    frag2.show (ft2, "Add Product Dialog");
                    break;
                
                case 3:
                    android.app.FragmentTransaction ft3 = activity.getFragmentManager ().beginTransaction ();
                    SwiggyServiceMyRequestDialogFragment frag3 = new SwiggyServiceMyRequestDialogFragment ().newInstance (service.getId ());
                    frag3.show (ft3, "MyProductDialog");
                    break;
                case 4:
                    categoryList.clear ();
                    categoryList.add ("Request 1");
                    categoryList.add ("Request 2");
                    categoryList.add ("Request 3");
                    categoryList.add ("Request 4");
                    categoryList.add ("Request 5");
                    categoryList.add ("Request 6");
                    categoryList.add ("Request 7");
                    
                    new MaterialDialog.Builder (activity)
                            .title ("Request List")
                            .items (categoryList)
                            .itemsCallback (new MaterialDialog.ListCallback () {
                                @Override
                                public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                    android.app.FragmentTransaction ft4 = activity.getFragmentManager ().beginTransaction ();
                                    SwiggyServiceAddRequestDialogFragment frag4 = new SwiggyServiceAddRequestDialogFragment ().newInstance (service.getId ());
                                    frag4.show (ft4, "MyProductDialog");
                                    
                                }
                            })
                            .show ();
                    
                    break;
            }
        }
    }
}
