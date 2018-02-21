package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.indiasupply.isdental.activity.OfferCheckoutActivity;
import com.indiasupply.isdental.model.Offers;
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

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    //    OnBottomReachedListener onBottomReachedListener;
    private Activity activity;
    private List<Offers> offerList = new ArrayList<> ();
    
    public OfferAdapter (Activity activity, List<Offers> offerList) {
        this.activity = activity;
        this.offerList = offerList;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_offers, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Offers offer = offerList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvOfferName);
        
        holder.tvOfferName.setText (offer.getName ());
        if (offer.getDescription ().length () > 0) {
            holder.rlOfferDescription.setVisibility (View.VISIBLE);
            holder.tvOfferDescription.setText (offer.getDescription ());
        } else {
            holder.rlOfferDescription.setVisibility (View.GONE);
        }
    
        if (offer.getPackaging ().length () > 0) {
            holder.tvOfferPackaging.setVisibility (View.VISIBLE);
            holder.tvOfferPackaging.setText (offer.getPackaging ());
        } else {
            holder.tvOfferPackaging.setVisibility (View.GONE);
        }
        
        
        holder.tvOfferPrice.setText ("Rs. " + offer.getPrice ());
        holder.tvOfferRegularPrice.setText ("Rs. " + offer.getRegular_price ());
        holder.tvOfferMRP.setText ("Rs. " + offer.getMrp ());
        holder.tvOfferSavings.setText ("Rs. " + (offer.getMrp () - offer.getPrice ()));
        try {
            double percentage = ((offer.getMrp () - offer.getPrice ()) * 100) / offer.getMrp ();
            if (percentage > 15) {
                holder.tvOfferPercentage.setVisibility (View.VISIBLE);
                holder.tvOfferPercentage.setText ((int) percentage + "% Off");
            } else {
                holder.tvOfferPercentage.setVisibility (View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        
        
        holder.tvOfferMRP.setPaintFlags (holder.tvOfferMRP.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvOfferRegularPrice.setPaintFlags (holder.tvOfferRegularPrice.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        
        holder.tvSeeDetails.setPaintFlags (holder.tvSeeDetails.getPaintFlags () | Paint.UNDERLINE_TEXT_FLAG);
        
        
        if (offer.getImage ().length () == 0) {
            holder.ivOfferImage.setImageResource (offer.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (offer.getImage ())
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
                    .error (offer.getIcon ())
                    .into (holder.ivOfferImage);
        }
        holder.tvSendEnquiry.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (activity, OfferCheckoutActivity.class);
                intent.putExtra (AppConfigTags.OFFER_ID, offer.getId ());
                activity.startActivity (intent);
                activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);

//                sendEnquiry2 (offer.getId ());
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return offerList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void sendEnquiry2 (final int id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (progressDialog, null, true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_ENQUIRY2, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_ENQUIRY2,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (final String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    Utils.showToast (activity, message, true);
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showToast (activity, "Unstable Internet Connection", false);
                                }
                            } else {
                                Utils.showToast (activity, "Unstable Internet Connection", false);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showToast (activity, "Unstable Internet Connection", false);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_OFFER_ID, String.valueOf (id));
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
            Utils.sendRequest (strRequest, 20);
        } else {
            Utils.showToast (activity, "Unstable Internet Connection", false);
        }
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOfferName;
        TextView tvOfferDescription;
        TextView tvOfferPackaging;
        TextView tvOfferPrice;
        TextView tvOfferRegularPrice;
        TextView tvOfferMRP;
        TextView tvOfferSavings;
        TextView tvSendEnquiry;
        TextView tvSeeDetails;
        TextView tvOfferPercentage;
        RelativeLayout rlOfferDescription;
        ImageView ivOfferImage;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvOfferName = (TextView) view.findViewById (R.id.tvOfferName);
            tvOfferDescription = (TextView) view.findViewById (R.id.tvOfferDescription);
            ivOfferImage = (ImageView) view.findViewById (R.id.ivOfferImage);
            tvOfferPackaging = (TextView) view.findViewById (R.id.tvOfferPackaging);
            tvOfferPrice = (TextView) view.findViewById (R.id.tvOfferPrice);
            tvOfferRegularPrice = (TextView) view.findViewById (R.id.tvOfferRegularPrice);
            tvOfferMRP = (TextView) view.findViewById (R.id.tvOfferMRP);
            tvOfferSavings = (TextView) view.findViewById (R.id.tvOfferSaving);
            tvSendEnquiry = (TextView) view.findViewById (R.id.tvSendEnquiry);
            tvSeeDetails = (TextView) view.findViewById (R.id.tvSeeDetails);
            tvOfferPercentage = (TextView) view.findViewById (R.id.tvOfferPercentage);
            rlOfferDescription = (RelativeLayout) view.findViewById (R.id.rlOfferDescription);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}