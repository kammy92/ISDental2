package com.indiasupply.isdental.adapter;

import android.app.Activity;
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
import com.indiasupply.isdental.model.SwiggyContactDetail;
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

public class SwiggyContactDetailAdapter extends RecyclerView.Adapter<SwiggyContactDetailAdapter.ViewHolder> {
    private Activity activity;
    private List<SwiggyContactDetail> contactDetailList = new ArrayList<> ();
    
    public SwiggyContactDetailAdapter (Activity activity, List<SwiggyContactDetail> contactDetailList) {
        this.activity = activity;
        this.contactDetailList = contactDetailList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_contacts_detail, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyContactDetail contactsDetail = contactDetailList.get (position);
        
        Utils.setTypefaceToAllViews (activity, holder.tvContactName);
        
        holder.tvContactName.setText (contactsDetail.getName ());
        holder.tvContactLocation.setText (contactsDetail.getLocation ());
        
        switch (contactsDetail.getType ()) {
            case 1:
                holder.tvContactType.setText ("Company Office");
                break;
            case 2:
                holder.tvContactType.setText ("Sales Office");
                break;
            case 3:
                holder.tvContactType.setText ("Service Office");
                break;
            case 4:
                holder.tvContactType.setText ("Dealer / Distributor");
                break;
        }
    
        if (contactsDetail.is_favourite ()) {
            holder.ivContactFavourite.setImageResource (R.drawable.ic_favourite_filled);
        } else {
            holder.ivContactFavourite.setImageResource (R.drawable.ic_favourite);
        }
    
    
        holder.ivContactFavourite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                updateFavourite (contactsDetail.getId (), holder.ivContactFavourite);
                if (contactsDetail.is_favourite ()) {
                    contactsDetail.setIs_favourite (false);
                    holder.ivContactFavourite.setImageResource (R.drawable.ic_favourite);
                } else {
                    contactsDetail.setIs_favourite (true);
                    holder.ivContactFavourite.setImageResource (R.drawable.ic_favourite_filled);
                }
            }
        });
    
        holder.rlCall.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.getContact_number ().length () > 0) {
                    Utils.callPhone (activity, contactsDetail.getContact_number ());
                } else {
                    Utils.showToast (activity, "No Phone specified", false);
                }
            }
        });
    
    
        holder.rlMail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (contactsDetail.getEmail ().length () > 0) {
                    Utils.shareToGmail (activity, new String[] {contactsDetail.getEmail ()}, "Enquiry", "");
                } else {
                    Utils.showToast (activity, "No email specified", false);
                }
            }
        });
    
    
        if (contactsDetail.getImage ().length () == 0) {
            holder.ivContactImage.setImageResource (contactsDetail.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (activity)
                    .load (contactsDetail.getImage ())
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
                    .error (contactsDetail.getIcon ())
                    .into (holder.ivContactImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return contactDetailList.size ();
    }
    
    private void updateFavourite (final int id, final ImageView ivFavourite) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_FAVOURITE, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_FAVOURITE,
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
                                        switch (jsonObj.getInt (AppConfigTags.STATUS)) {
                                            case 1:
                                                ivFavourite.setImageResource (R.drawable.ic_favourite_filled);
                                                break;
                                            case 2:
                                                ivFavourite.setImageResource (R.drawable.ic_favourite);
                                                break;
                                        }
                                    } else {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    // Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                // Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            //    Utils.showSnackBar(getActivity(), clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_TYPE, String.valueOf (3));
                    params.put (AppConfigTags.SWIGGY_TYPE_ID, String.valueOf (id));
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
//            ivFavourite.setImageResource (R.drawable.ic_favourite_filled);
        }
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvContactName;
        TextView tvContactLocation;
        TextView tvContactType;
        ImageView ivContactFavourite;
        RelativeLayout rlCall;
        RelativeLayout rlMail;
        ImageView ivContactImage;
        ProgressBar progressBar;
        
        
        public ViewHolder (View view) {
            super (view);
            tvContactName = (TextView) view.findViewById (R.id.tvContactName);
            tvContactLocation = (TextView) view.findViewById (R.id.tvContactLocation);
            tvContactType = (TextView) view.findViewById (R.id.tvContactType);
            ivContactFavourite = (ImageView) view.findViewById (R.id.ivContactFavourite);
            rlCall = (RelativeLayout) view.findViewById (R.id.rlCall);
            rlMail = (RelativeLayout) view.findViewById (R.id.rlMail);
            ivContactImage = (ImageView) view.findViewById (R.id.ivContactImage);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}