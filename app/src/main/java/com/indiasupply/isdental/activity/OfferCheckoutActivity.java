package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.indiasupply.isdental.adapter.MyAddressAdapter;
import com.indiasupply.isdental.model.MyAddress;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class OfferCheckoutActivity extends AppCompatActivity {
    RecyclerView rvDeliveryAddress;
    MyAddressAdapter addressAdapter;
    ArrayList<MyAddress> addressList = new ArrayList<> ();
    
    int offer_id;
    
    TextView tvOfferName;
    ImageView ivMinus;
    ImageView ivPlus;
    TextView tvQty;
    TextView tvPrice;
    
    TextView tvCheckout;
    TextView tvNoAddressFound;
    
    RelativeLayout rlAddNewAddress;
    
    int price;
    int qty = 1;
    
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_offer_checkout);
        getExtras ();
        initView ();
        initData ();
        initListener ();
        setData ();
    }
    
    private void initListener () {
//        tvAddNewAddress.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick (View v) {
//                android.app.FragmentManager fm = OfferCheckoutActivity.this.getFragmentManager ();
//                android.app.FragmentTransaction ft = fm.beginTransaction ();
//                AddNewAddressDialogFragment dialog = new AddNewAddressDialogFragment ().newInstance ("","");
//                dialog.show (ft, "MyDialog");
//            }
//        });
    
        ivPlus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                qty++;
                tvQty.setText ("" + qty);
                tvPrice.setText ("Rs. " + (price * qty));
                tvCheckout.setText ("Confirm and Pay Rs. " + (price * qty));
            }
        });
        ivMinus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (qty > 1) {
                    qty--;
                    tvQty.setText ("" + qty);
                    tvPrice.setText ("Rs. " + price * qty);
                    tvCheckout.setText ("Confirm and Pay Rs. " + (price * qty));
                }
            }
        });
    
        rlAddNewAddress.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final MaterialDialog dialog =
                        new MaterialDialog.Builder (OfferCheckoutActivity.this)
                                .title ("Add New Address")
                                .customView (R.layout.dialog_add_new_address, true)
                                .positiveText ("SAVE")
                                .typeface (SetTypeFace.getTypeface (OfferCheckoutActivity.this), SetTypeFace.getTypeface (OfferCheckoutActivity.this))
                                .negativeText ("CANCEL")
                                .build ();
            
                final EditText etLine1 = (EditText) dialog.findViewById (R.id.etLine1);
                final EditText etCity = (EditText) dialog.findViewById (R.id.etCity);
                final EditText etState = (EditText) dialog.findViewById (R.id.etState);
                final EditText etPincode = (EditText) dialog.findViewById (R.id.etPincode);
            
                Utils.setTypefaceToAllViews (OfferCheckoutActivity.this, etLine1);
            
                dialog.getActionButton (DialogAction.POSITIVE).setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        addNewAddress (etLine1.getText ().toString (), etCity.getText ().toString (), etState.getText ().toString (), etPincode.getText ().toString ());
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
    
    private void initData () {
        addressAdapter = new MyAddressAdapter (OfferCheckoutActivity.this, addressList);
        rvDeliveryAddress.setAdapter (addressAdapter);
        rvDeliveryAddress.setLayoutManager (new LinearLayoutManager (OfferCheckoutActivity.this, LinearLayoutManager.VERTICAL, false));
        rvDeliveryAddress.setHasFixedSize (true);
        rvDeliveryAddress.setItemAnimator (new DefaultItemAnimator ());
        rvDeliveryAddress.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (OfferCheckoutActivity.this, 8), (int) Utils.pxFromDp (OfferCheckoutActivity.this, 8), (int) Utils.pxFromDp (OfferCheckoutActivity.this, 4), (int) Utils.pxFromDp (OfferCheckoutActivity.this, 4), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    
        Utils.setTypefaceToAllViews (this, tvOfferName);
        progressDialog = new ProgressDialog (this);
    }
    
    public void initView () {
        rvDeliveryAddress = (RecyclerView) findViewById (R.id.rvDeliveryAddress);
    
        tvOfferName = (TextView) findViewById (R.id.tvOfferName);
        tvPrice = (TextView) findViewById (R.id.tvPrice);
        tvQty = (TextView) findViewById (R.id.tvQty);
        ivMinus = (ImageView) findViewById (R.id.ivMinus);
        ivPlus = (ImageView) findViewById (R.id.ivPlus);
        tvCheckout = (TextView) findViewById (R.id.tvCheckout);
        tvNoAddressFound = (TextView) findViewById (R.id.tvNoAddressFound);
        rlAddNewAddress = (RelativeLayout) findViewById (R.id.rlAddNewAddress);
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        offer_id = 1;
//        offer_id = intent.getIntExtra (AppConfigTags.OFFER_ID, 0);
    }
    
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_CHECKOUT, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_CHECKOUT,
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
                                        tvOfferName.setText (jsonObj.getString (AppConfigTags.SWIGGY_OFFER_NAME));
                                        price = jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_QTY) * jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_PRICE);
                                        tvPrice.setText ("Rs. " + price);
                                        
                                        tvCheckout.setText ("Confirm and Pay Rs. " + (price * qty));
                                        
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.ADDRESSES);
                                        
                                        if (jsonArray.length () == 0) {
                                            tvNoAddressFound.setVisibility (View.VISIBLE);
                                            rvDeliveryAddress.setVisibility (View.GONE);
                                        } else {
                                            tvNoAddressFound.setVisibility (View.GONE);
                                            rvDeliveryAddress.setVisibility (View.VISIBLE);
                                        }
                                        
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            addressList.add (new MyAddress (
                                                    jsonObject.getInt (AppConfigTags.ADDRESS_ID),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_LINE1),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_CITY),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_STATE),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_PINCODE)));
                                        }
                                        addressAdapter.notifyDataSetChanged ();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showLog (Log.WARN, AppConfigTags.EXCEPTION, e.getMessage (), true);
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
                    params.put (AppConfigTags.OFFER_ID, String.valueOf (offer_id));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (OfferCheckoutActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
        }
    }
    
    
    private void addNewAddress (final String line1, final String city, final String state, final String pincode) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, null, false);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_ADD_NEW_ADDRESS, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_ADD_NEW_ADDRESS,
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
                                        
                                        if (jsonArray.length () == 0) {
                                            tvNoAddressFound.setVisibility (View.VISIBLE);
                                            rvDeliveryAddress.setVisibility (View.GONE);
                                        } else {
                                            tvNoAddressFound.setVisibility (View.GONE);
                                            rvDeliveryAddress.setVisibility (View.VISIBLE);
                                        }
                                        
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            addressList.add (new MyAddress (
                                                    jsonObject.getInt (AppConfigTags.ADDRESS_ID),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_LINE1),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_CITY),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_STATE),
                                                    jsonObject.getString (AppConfigTags.ADDRESS_PINCODE)));
                                        }
                                        addressAdapter.notifyDataSetChanged ();
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (OfferCheckoutActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
        }
    }
}