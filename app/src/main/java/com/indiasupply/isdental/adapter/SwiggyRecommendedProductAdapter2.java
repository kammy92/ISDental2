package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyProduct;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.indiasupply.isdental.utils.Utils.makeTextViewResizable;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyRecommendedProductAdapter2 extends RecyclerView.Adapter<SwiggyRecommendedProductAdapter2.ViewHolder> {
    CompanyListAdapter.OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyProduct> productList = new ArrayList<> ();
    
    public SwiggyRecommendedProductAdapter2 (Activity activity, List<SwiggyProduct> productList) {
        this.activity = activity;
        this.productList = productList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_product_recommended_large, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final SwiggyProduct product = productList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvProductName);
    
        holder.tvProductName.setText (product.getName ());
        holder.tvProductPrice.setText (product.getPrice ());
        holder.tvProductDescription.setText (product.getDescription ());
        if (holder.tvProductDescription.getText ().toString ().trim ().length () > 70) {
            makeTextViewResizable (holder.tvProductDescription, 2, "...more", true);
        }
    
        if (product.getPackaging ().length () > 0) {
            holder.tvProductPackaging.setText (product.getPackaging ());
            holder.tvProductPackaging.setVisibility (View.VISIBLE);
        }
        
        if (product.getImage ().length () == 0) {
            holder.ivProductImage.setImageResource (product.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            holder.progressBar.setVisibility (View.VISIBLE);
            Glide.with (activity)
                    .load (product.getImage ())
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
                    .error (product.getIcon ())
                    .into (holder.ivProductImage);
    
    
        }
    
        holder.tvAdd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                sendEnquiry (holder.tvAdd, product.getId ());
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return productList.size ();
    }
    
    public void SetOnItemClickListener (final CompanyListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void sendEnquiry (final TextView textView, final int id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_ENQUIRY, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_ENQUIRY,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        textView.setVisibility (View.GONE);
                                    } else {
                                        Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, message, true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                        }
                    }) {
                
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_PRODUCT_ID, String.valueOf (id));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (activity, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
        }
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductDescription;
        TextView tvProductPackaging;
        TextView tvAdd;
        ImageView ivProductImage;
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            tvProductPrice = (TextView) view.findViewById (R.id.tvProductPrice);
            tvProductDescription = (TextView) view.findViewById (R.id.tvProductDescription);
            tvProductPackaging = (TextView) view.findViewById (R.id.tvProductPackaging);
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
