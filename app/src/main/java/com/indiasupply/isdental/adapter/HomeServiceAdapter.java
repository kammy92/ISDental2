package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.CompanyListActivity;
import com.indiasupply.isdental.activity.EventListActivity;
import com.indiasupply.isdental.activity.InformationActivity;
import com.indiasupply.isdental.activity.ShopOnlineActivity;
import com.indiasupply.isdental.activity.SpecialEventDetailActivity;
import com.indiasupply.isdental.activity.SwiggyMainActivity;
import com.indiasupply.isdental.model.HomeService;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    private Activity activity;
    private List<HomeService> homeServiceList = new ArrayList<HomeService> ();
    
    public HomeServiceAdapter (Activity activity, List<HomeService> homeServiceList) {
        this.activity = activity;
        this.homeServiceList = homeServiceList;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_home, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {// runEnterAnimation (holder.itemView);
        final HomeService homeService = homeServiceList.get (position);
        Glide.with (activity).load ("").placeholder (homeService.getIcon ()).into (holder.ivIcon);
    }
    
    @Override
    public int getItemCount () {
        return homeServiceList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void isUserRegisterToEvent (final int event_id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (progressDialog, "Please wait ...", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_REGISTER_EVENT + "/" + event_id, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_REGISTER_EVENT + "/" + event_id,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                progressDialog.dismiss ();
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        if (jsonObj.getBoolean ("registered")) {
                                            Intent intent = new Intent (activity, SpecialEventDetailActivity.class);
                                            intent.putExtra (AppConfigTags.EVENT_ID, event_id);
                                            activity.startActivity (intent);
                                            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                        } else {
                                            MaterialDialog dialog = new MaterialDialog.Builder (activity)
                                                    .content ("Do you wish to register for the event")
                                                    .positiveColor (activity.getResources ().getColor (R.color.app_text_color_dark))
                                                    .contentColor (activity.getResources ().getColor (R.color.app_text_color_dark))
                                                    .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                                                    .canceledOnTouchOutside (true)
                                                    .cancelable (true)
                                                    .positiveText ("Yes, I'm in")
                                                    .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                        @Override
                                                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            registerUserToEvent (event_id);
                                                        }
                                                    }).build ();
                                            dialog.show ();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    progressDialog.dismiss ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                                progressDialog.dismiss ();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            progressDialog.dismiss ();
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
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
            Utils.sendRequest (strRequest1, 60);
        } else {
        }
    }
    
    private void registerUserToEvent (final int event_id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (progressDialog, "Please wait ...", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_REGISTER_EVENT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_REGISTER_EVENT,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        Intent intent = new Intent (activity, SpecialEventDetailActivity.class);
                                        intent.putExtra (AppConfigTags.EVENT_ID, event_id);
                                        activity.startActivity (intent);
                                        activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                } catch (Exception e) {
                                    
                                    e.printStackTrace ();
                                }
                            } else {
                                progressDialog.dismiss ();
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            progressDialog.dismiss ();
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.EVENT_ID, String.valueOf (event_id));
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
            Utils.sendRequest (strRequest1, 60);
        } else {
            progressDialog.dismiss ();
        }
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivIcon;
        
        public ViewHolder (View view) {
            super (view);
            ivIcon = (ImageView) view.findViewById (R.id.ivServiceIcon);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            HomeService homeService = homeServiceList.get (getLayoutPosition ());
            switch (homeService.getId ()) {
                case 1:
                    Intent intent1 = new Intent (activity, CompanyListActivity.class);
                    activity.startActivity (intent1);
                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
    
                    break;
                
                case 2:
                    Intent intent3 = new Intent (activity, EventListActivity.class);
                    activity.startActivity (intent3);
                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case 3:
                    Intent intent2 = new Intent (activity, ShopOnlineActivity.class);
                    activity.startActivity (intent2);
                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case 4:
                    isUserRegisterToEvent (2);
                    break;
                case 6:
                    Intent intent6 = new Intent (activity, InformationActivity.class);
                    activity.startActivity (intent6);
                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case 5:
                    Intent intent5 = new Intent (activity, SwiggyMainActivity.class);
                    activity.startActivity (intent5);
                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
//                    Intent intent5 = new Intent (activity, SwiggyBrandDetailActivity.class);
//                    activity.startActivity (intent5);
//                    activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
    
                    break;
                default:
                    Utils.showSnackBar (activity, (CoordinatorLayout) activity.findViewById (R.id.clMain), "Coming Soon", Snackbar.LENGTH_LONG, null, null);
                    break;
            }
        }
    }
}