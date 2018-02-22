package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.MyAddress;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    ProgressDialog progressDialog;
    private Activity activity;
    private ArrayList<MyAddress> addressList = new ArrayList<> ();
    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    
    public MyAddressAdapter (Activity activity, ArrayList<MyAddress> addressList) {
        this.activity = activity;
        this.addressList = addressList;
        progressDialog = new ProgressDialog (activity);
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_my_address, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final MyAddress address = addressList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvAddress);
        holder.tvAddress.setText (address.getAddress () + ", " + address.getCity () + ", " + address.getState () + " " + address.getPincode ());
    
        if (currentSelectedPosition == position) {
            holder.ll2.setVisibility (View.VISIBLE);
            holder.tvAddress.setMaxLines (Integer.MAX_VALUE);
            holder.tvAddress.setEllipsize (null);
            holder.rbRadio.setChecked (true);
            holder.tvAddress.setTextColor (activity.getResources ().getColor (R.color.primary_text2));
        } else {
            holder.ll2.setVisibility (View.GONE);
            holder.tvAddress.setLines (1);
            holder.tvAddress.setEllipsize (TextUtils.TruncateAt.END);
            holder.tvAddress.setTextColor (activity.getResources ().getColor (R.color.secondary_text2));
            holder.rbRadio.setChecked (false);
        }
    
    
        holder.ivDelete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                deleteAddress (address.getId ());
            }
        });
    
        holder.ivEdit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final MaterialDialog dialog =
                        new MaterialDialog.Builder (activity)
                                .title ("Edit Address")
                                .customView (R.layout.dialog_add_new_address, true)
                                .positiveText ("UPDATE")
                                .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                                .negativeText ("CANCEL")
                                .build ();
            
                final EditText etLine1 = (EditText) dialog.findViewById (R.id.etLine1);
                final EditText etCity = (EditText) dialog.findViewById (R.id.etCity);
                final EditText etState = (EditText) dialog.findViewById (R.id.etState);
                final EditText etPincode = (EditText) dialog.findViewById (R.id.etPincode);
            
                etLine1.setText (address.getAddress ());
                etCity.setText (address.getCity ());
                etState.setText (address.getState ());
                etPincode.setText (address.getPincode ());
            
                Utils.setTypefaceToAllViews (activity, etLine1);
            
                dialog.getActionButton (DialogAction.POSITIVE).setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        editAddress (address.getId (), etLine1.getText ().toString (), etCity.getText ().toString (), etState.getText ().toString (), etPincode.getText ().toString ());
                        dialog.dismiss ();
                    }
                });
            
                dialog.getActionButton (DialogAction.NEGATIVE).setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        dialog.dismiss ();
                    }
                });
            
                dialog.show ();
            
            
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return addressList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void deleteAddress (int address_id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (progressDialog, null, false);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_DELETE_ADDRESS + "/" + address_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.DELETE, AppConfigURL.URL_DELETE_ADDRESS + "/" + address_id,
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
                                        addressList.clear ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.ADDRESSES);
                                        
                                        
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            addressList.add (new MyAddress (
                                                    jsonObject.getInt (AppConfigTags.ADDRESS_ID),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_LINE1),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_CITY),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_STATE),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_PINCODE)));
                                        }
                                        notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showLog (Log.WARN, AppConfigTags.EXCEPTION, e.getMessage (), true);
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
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
            Utils.sendRequest (strRequest, 20);
        } else {
        }
    }
    
    private void editAddress (final int address_id, final String line1, final String city, final String state, final String pincode) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showProgressDialog (progressDialog, null, false);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_EDIT_ADDRESS + "/" + address_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.PUT, AppConfigURL.URL_EDIT_ADDRESS + "/" + address_id,
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
                                        addressList.clear ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.ADDRESSES);
                                        
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            addressList.add (new MyAddress (
                                                    jsonObject.getInt (AppConfigTags.ADDRESS_ID),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_LINE1),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_CITY),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_STATE),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_PINCODE)));
                                        }
                                        notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showLog (Log.WARN, AppConfigTags.EXCEPTION, e.getMessage (), true);
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
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
                            progressDialog.dismiss ();
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.ADDRESS_ID, String.valueOf (address_id));
                    params.put (AppConfigTags.ADDRESS_LINE1, line1);
                    params.put (AppConfigTags.ADDRESS_CITY, city);
                    params.put (AppConfigTags.ADDRESS_STATE, state);
                    params.put (AppConfigTags.ADDRESS_PINCODE, pincode);
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
        }
    }
    
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvAddress;
        RadioButton rbRadio;
        ImageView ivDelete;
        ImageView ivEdit;
        LinearLayout ll2;
        
        
        public ViewHolder (View view) {
            super (view);
            tvAddress = (TextView) view.findViewById (R.id.tvAddress);
            ivDelete = (ImageView) view.findViewById (R.id.ivDelete);
            ivEdit = (ImageView) view.findViewById (R.id.ivEdit);
            ll2 = (LinearLayout) view.findViewById (R.id.ll2);
            rbRadio = (RadioButton) view.findViewById (R.id.rbRadio);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            currentSelectedPosition = getLayoutPosition ();
            notifyDataSetChanged ();
        }
    }
}

