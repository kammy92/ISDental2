package com.indiasupply.isdental.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.MyProductRequestAdapter;
import com.indiasupply.isdental.dialog.ServiceAddNewRequestDialogFragment;
import com.indiasupply.isdental.dialog.ServiceRequestDetailDialogFragment;
import com.indiasupply.isdental.model.MyProductRequest;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 21/11/2017.
 */

public class SwiggyMyProductDetailActivity extends AppCompatActivity {
    TextView tvServiceRequestName;
    TextView tvServiceRequestModelNumber;
    TextView tvServiceRequestDate;
    TextView tvNoImage;
    RelativeLayout rl1;
    RelativeLayout rl2;
    RelativeLayout rl3;
    RelativeLayout rl5;
    RecyclerView rvServiceList;
    TextView tvAddNewRequest;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    
    String image1;
    String image2;
    String image3;

    ImageView ivCancel;
    
    
    CoordinatorLayout clMain;
    List<MyProductRequest> swiggyServiceRequestList = new ArrayList<> ();
    MyProductRequestAdapter swiggyServiceRequestAdapter;
    
    String server_response;
    
    int product_id;
    String product_name, product_description, product_serial_number, product_model_number, product_purchase_date;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_service_product_detail);
        getExtra ();
        initView ();
        initData ();
        initListener ();
    
    }
    
    @Override
    public void onResume () {
        super.onResume ();
        setData ();
        // put your code here...

    }
    
    
    private void getExtra () {
        Intent intent = getIntent ();
        product_id = intent.getIntExtra (AppConfigTags.PRODUCT_ID, 0);
        product_name = intent.getStringExtra (AppConfigTags.PRODUCT_BRAND);
        product_description = intent.getStringExtra (AppConfigTags.PRODUCT_DESCRIPTION);
        product_serial_number = intent.getStringExtra (AppConfigTags.PRODUCT_SERIAL_NUMBER);
        product_model_number = intent.getStringExtra (AppConfigTags.PRODUCT_MODEL_NUMBER);
        product_purchase_date = intent.getStringExtra (AppConfigTags.PRODUCT_PURCHASE_DATE);
    }
    
    private void initView () {
    
        tvServiceRequestName = (TextView) findViewById (R.id.tvName);
        tvServiceRequestModelNumber = (TextView) findViewById (R.id.tvModelName);
        tvServiceRequestDate = (TextView) findViewById (R.id.tvPurchaseDate);
        tvNoImage = (TextView) findViewById (R.id.tvNoImage);
        progressBar1 = (ProgressBar) findViewById (R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById (R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById (R.id.progressBar3);
        
        rl1 = (RelativeLayout) findViewById (R.id.rl1);
        rl2 = (RelativeLayout) findViewById (R.id.rl2);
        rl3 = (RelativeLayout) findViewById (R.id.rl3);
        rl5 = (RelativeLayout) findViewById (R.id.rl5);

        iv1 = (ImageView) findViewById (R.id.iv1);
        iv2 = (ImageView) findViewById (R.id.iv2);
        iv3 = (ImageView) findViewById (R.id.iv3);
        
        rvServiceList = (RecyclerView) findViewById (R.id.rvServiceList);
        tvAddNewRequest = (TextView) findViewById (R.id.tvAddNewRequest);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        ivCancel = (ImageView) findViewById (R.id.ivCancel);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (SwiggyMyProductDetailActivity.this, tvAddNewRequest);
        
        
        tvServiceRequestName.setText (product_name + " " + product_description + " - " + product_serial_number);
        tvServiceRequestModelNumber.setText (product_model_number);
        tvServiceRequestDate.setText (product_purchase_date);
    
    
        swiggyServiceRequestAdapter = new MyProductRequestAdapter (SwiggyMyProductDetailActivity.this, swiggyServiceRequestList);
        rvServiceList.setNestedScrollingEnabled (false);
        rvServiceList.setFocusable (false);
        rvServiceList.setAdapter (swiggyServiceRequestAdapter);
        rvServiceList.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (SwiggyMyProductDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rvServiceList.setLayoutManager (linearLayoutManager);
        rvServiceList.setItemAnimator (new DefaultItemAnimator ());
        rvServiceList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (SwiggyMyProductDetailActivity.this, 16), (int) Utils.pxFromDp (SwiggyMyProductDetailActivity.this, 16), (int) Utils.pxFromDp (SwiggyMyProductDetailActivity.this, 16), (int) Utils.pxFromDp (SwiggyMyProductDetailActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
    
    
        swiggyServiceRequestAdapter.SetOnItemClickListener (new MyProductRequestAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                MyProductRequest serviceRequest = swiggyServiceRequestList.get (position);
                android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                ServiceRequestDetailDialogFragment dialog = new ServiceRequestDetailDialogFragment ().newInstance (server_response, serviceRequest.getRequest_id ());
                dialog.setDismissListener (new MyDialogCloseListener () {
                    @Override
                    public void handleDialogClose (DialogInterface dialog) {
                        Log.e ("Return Page", "Return Page");
                        setData ();
                    }
                });
                dialog.show (ft, "Contacts");
            }
        });
    
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                finish ();
            }
        });
    
        tvAddNewRequest.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                ServiceAddNewRequestDialogFragment dialog = new ServiceAddNewRequestDialogFragment ().newInstance (0, product_name, product_description, product_serial_number, product_model_number, product_purchase_date, String.valueOf (product_id));
                dialog.setDismissListener (new MyDialogCloseListener () {
                    @Override
                    public void handleDialogClose (DialogInterface dialog) {
                        Log.e ("Return Page", "Return Page");
                        setData ();
                    }
                });
                dialog.show (ft, "Contacts");
            }
        });
    
        rl5.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent2 = new Intent (SwiggyMyProductDetailActivity.this, SwiggyServiceAddProductActivity.class);
                intent2.putExtra (AppConfigTags.PRODUCT_ID, product_id);
                intent2.putExtra (AppConfigTags.PRODUCT_BRAND, product_name);
                intent2.putExtra (AppConfigTags.PRODUCT_MODEL_NUMBER, product_model_number);
                intent2.putExtra (AppConfigTags.PRODUCT_SERIAL_NUMBER, product_serial_number);
                intent2.putExtra (AppConfigTags.PRODUCT_DESCRIPTION, product_description);
                intent2.putExtra (AppConfigTags.PRODUCT_PURCHASE_DATE, product_purchase_date);
                intent2.putExtra (AppConfigTags.IMAGE1, image1);
                intent2.putExtra (AppConfigTags.IMAGE2, image2);
                intent2.putExtra (AppConfigTags.IMAGE3, image3);
                intent2.putExtra ("flag", 1);
                startActivity (intent2);
            }
        });


    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (SwiggyMyProductDetailActivity.this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_MY_PRODUCT_DETAIL + "/" + product_id, true);
            StringRequest strRequest = new StringRequest (com.android.volley.Request.Method.GET, AppConfigURL.URL_MY_PRODUCT_DETAIL + "/" + product_id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            server_response = response;
                            if (response != null) {
                                try {
                                    swiggyServiceRequestList.clear ();
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayRequest = jsonObj.getJSONArray (AppConfigTags.REQUESTS);
    
                                        tvServiceRequestName.setText (jsonObj.getString (AppConfigTags.PRODUCT_BRAND) + " " + jsonObj.getString (AppConfigTags.PRODUCT_DESCRIPTION) + " - " + jsonObj.getString (AppConfigTags.PRODUCT_SERIAL_NUMBER));
                                        tvServiceRequestModelNumber.setText (jsonObj.getString (AppConfigTags.PRODUCT_MODEL_NUMBER));
                                        tvServiceRequestDate.setText (jsonObj.getString (AppConfigTags.PRODUCT_PURCHASE_DATE));



                                        for (int i = 0; i < jsonArrayRequest.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayRequest.getJSONObject (i);
    
    
                                            swiggyServiceRequestList.add (i, new MyProductRequest (
                                                    jsonObjectBrand.getInt (AppConfigTags.REQUEST_ID),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_DESCRIPTION),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_STATUS),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_CREATED_AT),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_TICKET_NUMBER),
                                                    jsonObjectBrand.getJSONArray (AppConfigTags.REQUEST_COMMENTS).toString (),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_IMAGE1),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_IMAGE2),
                                                    jsonObjectBrand.getString (AppConfigTags.REQUEST_IMAGE3)));
                                        }
                                        swiggyServiceRequestAdapter.notifyDataSetChanged ();
                                        image1 = jsonObj.getString (AppConfigTags.PRODUCT_IMAGE1);
                                        image2 = jsonObj.getString (AppConfigTags.PRODUCT_IMAGE2);
                                        image3 = jsonObj.getString (AppConfigTags.PRODUCT_IMAGE3);

                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.PRODUCT_IMAGE1).endsWith (ext)) {
                                                rl1.setVisibility (View.VISIBLE);
                                                tvNoImage.setVisibility (View.GONE);
                                                Glide.with (SwiggyMyProductDetailActivity.this)
                                                        .load (jsonObj.getString (AppConfigTags.PRODUCT_IMAGE1))
                                                        .listener (new RequestListener<String, GlideDrawable> () {
                                                            @Override
                                                            public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                progressBar1.setVisibility (View.GONE);
                                                                return false;
                                                            }
                
                                                            @Override
                                                            public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                progressBar1.setVisibility (View.GONE);
                                                                return false;
                                                            }
                                                        })
                                                        .into (iv1);
                                                break;
                                            }
                                        }
                                        
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.PRODUCT_IMAGE2).endsWith (ext)) {
                                                rl2.setVisibility (View.VISIBLE);
                                                tvNoImage.setVisibility (View.GONE);
                                                Glide.with (SwiggyMyProductDetailActivity.this)
                                                        .load (jsonObj.getString (AppConfigTags.PRODUCT_IMAGE2))
                                                        .listener (new RequestListener<String, GlideDrawable> () {
                                                            @Override
                                                            public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                progressBar2.setVisibility (View.GONE);
                                                                return false;
                                                            }
                
                                                            @Override
                                                            public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                progressBar2.setVisibility (View.GONE);
                                                                return false;
                                                            }
                                                        })
                                                        .into (iv2);
                                                break;
                                            }
                                        }
                                        
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.PRODUCT_IMAGE3).endsWith (ext)) {
                                                rl3.setVisibility (View.VISIBLE);
                                                tvNoImage.setVisibility (View.GONE);
                                                Glide.with (SwiggyMyProductDetailActivity.this)
                                                        .load (jsonObj.getString (AppConfigTags.PRODUCT_IMAGE3))
                                                        .listener (new RequestListener<String, GlideDrawable> () {
                                                            @Override
                                                            public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                progressBar3.setVisibility (View.GONE);
                                                                return false;
                                                            }
                
                                                            @Override
                                                            public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                progressBar3.setVisibility (View.GONE);
                                                                return false;
                                                            }
                                                        })
                                                        .into (iv3);
                                                break;
                                            }
                                        }
                                    } else {
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
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SwiggyMyProductDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 30);
        } else {
            Utils.showSnackBar (SwiggyMyProductDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    public interface MyDialogCloseListener {
        public void handleDialogClose (DialogInterface dialog);
    }
}




