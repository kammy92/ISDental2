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
import android.widget.ProgressBar;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class OfferCheckoutActivity extends AppCompatActivity implements PaymentResultListener {
    RecyclerView rvDeliveryAddress;
    MyAddressAdapter addressAdapter;
    ArrayList<MyAddress> addressList = new ArrayList<> ();
    
    int offer_id;
    int address_id = 0;
    
    TextView tvOfferName;
    TextView tvOfferDescription;
    ImageView ivMinus;
    ImageView ivPlus;
    TextView tvQty;
    TextView tvPrice;
    
    TextView tvItemTotal;
    TextView tvOfferDiscount;
    TextView tvToPay;
    TextView tvSavings;
    
    EditText etGSTNumber;
    
    TextView tvCheckout;
    TextView tvNoAddressFound;
    
    RelativeLayout rlAddNewAddress;
    
    RelativeLayout rlBack;
    RelativeLayout rlMain;
    RelativeLayout rlSuccess;
    TextView tvBack;
    
    
    ProgressBar progressBar;
    
    int price;
    int mrp;
    int discount;
    int qty = 1;
    
    ProgressDialog progressDialog;
    
    UserDetailsPref userDetailsPref;
    
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
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    
        tvBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        ivPlus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                qty++;
                tvQty.setText ("" + qty);
                tvPrice.setText ("Rs. " + (price * qty));
                tvCheckout.setText ("Confirm and Pay Rs. " + (price * qty));
    
                tvItemTotal.setText ("Rs. " + (mrp * qty));
                tvOfferDiscount.setText ("Rs. " + (discount * qty));
                tvToPay.setText ("Rs. " + (price * qty));
                tvSavings.setText ("You have saved Rs. " + (discount * qty) + " on the bill");
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
    
                    tvItemTotal.setText ("Rs. " + (mrp * qty));
                    tvOfferDiscount.setText ("Rs. " + (discount * qty));
                    tvToPay.setText ("Rs. " + (price * qty));
                    tvSavings.setText ("You have saved Rs. " + (discount * qty) + " on the bill");
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
        tvCheckout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (address_id != 0) {
                    if (etGSTNumber.getText ().toString ().length () > 0) {
                        userDetailsPref.putStringPref (OfferCheckoutActivity.this, UserDetailsPref.USER_GST_NUMBER, etGSTNumber.getText ().toString ());
                    }
                    startPayment ("IndiaSupply", tvOfferName.getText ().toString (), String.valueOf (price * qty * 100));
                } else {
                    Utils.showToast (OfferCheckoutActivity.this, "Please select a shipping address", false);
                }
                
            }
        });
    
        addressAdapter.SetOnItemClickListener (new MyAddressAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                MyAddress address = addressList.get (position);
                address_id = address.getId ();
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
        userDetailsPref = UserDetailsPref.getInstance ();
    
        etGSTNumber.setText (userDetailsPref.getStringPref (this, UserDetailsPref.USER_GST_NUMBER));
    }
    
    public void initView () {
        rvDeliveryAddress = (RecyclerView) findViewById (R.id.rvDeliveryAddress);
    
        tvOfferName = (TextView) findViewById (R.id.tvOfferName);
        tvOfferDescription = (TextView) findViewById (R.id.tvOfferDescription);
        tvPrice = (TextView) findViewById (R.id.tvPrice);
        tvQty = (TextView) findViewById (R.id.tvQty);
        ivMinus = (ImageView) findViewById (R.id.ivMinus);
        ivPlus = (ImageView) findViewById (R.id.ivPlus);
        tvCheckout = (TextView) findViewById (R.id.tvCheckout);
        tvNoAddressFound = (TextView) findViewById (R.id.tvNoAddressFound);
        rlAddNewAddress = (RelativeLayout) findViewById (R.id.rlAddNewAddress);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
    
        rlMain = (RelativeLayout) findViewById (R.id.rlMain);
    
        tvItemTotal = (TextView) findViewById (R.id.tvItemTotal);
        tvOfferDiscount = (TextView) findViewById (R.id.tvOfferDiscount);
        tvToPay = (TextView) findViewById (R.id.tvToPay);
        tvSavings = (TextView) findViewById (R.id.tvSavings);
        etGSTNumber = (EditText) findViewById (R.id.etGSTNumber);
    
        progressBar = (ProgressBar) findViewById (R.id.progressBar);
    
        rlSuccess = (RelativeLayout) findViewById (R.id.rlSuccess);
        tvBack = (TextView) findViewById (R.id.tvBack);
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
//        offer_id = 1;
        offer_id = intent.getIntExtra (AppConfigTags.OFFER_ID, 0);
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
                                        tvOfferDescription.setText (jsonObj.getString (AppConfigTags.SWIGGY_OFFER_PACKAGING));
                                        price = jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_QTY) * jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_PRICE);
                                        mrp = jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_QTY) * jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_MRP);
                                        discount = ((jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_QTY) * jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_MRP)) - jsonObj.getInt (AppConfigTags.SWIGGY_OFFER_PRICE));
                                        
                                        tvPrice.setText ("Rs. " + price);
                                        tvCheckout.setText ("Confirm and Pay Rs. " + (price * qty));
                                        tvItemTotal.setText ("Rs. " + mrp);
                                        tvOfferDiscount.setText ("Rs. " + discount);
                                        tvToPay.setText ("Rs. " + price);
                                        tvSavings.setText ("You have saved Rs. " + discount + " on the bill");
                                        
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
    
                                        progressBar.setVisibility (View.GONE);
                                        rlMain.setVisibility (View.VISIBLE);
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
    
    @Override
    public void onPaymentSuccess (String razorpayPaymentID) {
        generateOrder (offer_id, address_id, qty, (price * qty), razorpayPaymentID, etGSTNumber.getText ().toString ());
    }
    
    @Override
    public void onPaymentError (int code, String response) {
        Utils.showToast (OfferCheckoutActivity.this, "Payment Failed", false);
    }
    
    public void startPayment (String merchant_name, String description, String amount) {
        UserDetailsPref userDetailsPref = new UserDetailsPref ().getInstance ();
        Checkout checkout = new Checkout ();
        checkout.setImage (R.drawable.ic_logo);
        try {
            JSONObject options = new JSONObject ();
            options.put ("name", merchant_name);
            options.put ("description", description);
            options.put ("currency", "INR");
            
            options.put ("prefill.name", userDetailsPref.getStringPref (OfferCheckoutActivity.this, UserDetailsPref.USER_NAME));
            options.put ("prefill.email", userDetailsPref.getStringPref (OfferCheckoutActivity.this, UserDetailsPref.USER_EMAIL));
            options.put ("prefill.contact", userDetailsPref.getStringPref (OfferCheckoutActivity.this, UserDetailsPref.USER_MOBILE));
            
            /**
             * Amount is always passed in PAISE
             * Eg: "100" = Rs 1.00
             */
            options.put ("amount", amount);
            
            checkout.open (this, options);
        } catch (Exception e) {
            Log.e ("karman", "Error in starting Razorpay Checkout", e);
        }
    }
    
    public void generateOrder (final int offer_id, final int address_id, final int qty, final int total_amount, final String transaction_id, final String gst_number) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, null, false);
            rlMain.setVisibility (View.GONE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_INSERT_ORDER, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_INSERT_ORDER,
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
                                        rlSuccess.setVisibility (View.VISIBLE);
                                        rlMain.setVisibility (View.GONE);
                                        progressBar.setVisibility (View.GONE);
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
                    params.put (AppConfigTags.OFFER_ID, String.valueOf (offer_id));
                    params.put (AppConfigTags.ADDRESS_ID, String.valueOf (address_id));
                    params.put (AppConfigTags.STATUS, String.valueOf (1));
                    params.put (AppConfigTags.QTY, String.valueOf (qty));
                    params.put (AppConfigTags.TOTAL_AMOUNT, String.valueOf (total_amount));
                    params.put (AppConfigTags.GST_NUMBER, gst_number);
                    params.put (AppConfigTags.TRANSACTION_ID, transaction_id);
                    
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