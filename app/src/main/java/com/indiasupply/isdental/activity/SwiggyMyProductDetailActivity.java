package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.indiasupply.isdental.adapter.SwiggyMyProductRequestAdapter;
import com.indiasupply.isdental.dialog.SwiggyServiceRequestDetailDialogFragment;
import com.indiasupply.isdental.model.SwiggyMyProductRequest;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    RecyclerView rvServiceList;
    TextView tvAddNewRequest;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    
    CoordinatorLayout clMain;
    List<SwiggyMyProductRequest> swiggyServiceRequestList = new ArrayList<> ();
    SwiggyMyProductRequestAdapter swiggyServiceRequestAdapter;
    
    String Request;
    String Response;
    
    int id;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_service_product_detail);
        initView ();
        initDate ();
        initListener ();
        getExtra ();
        setData ();
    }
    
    private void getExtra () {
        Intent intent = getIntent ();
        tvServiceRequestName.setText ((intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_NAME)) + " " + (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION) + " " + (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER))));
        tvServiceRequestModelNumber.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER));
        tvServiceRequestDate.setText (intent.getStringExtra (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE));
        id = intent.getIntExtra (AppConfigTags.SWIGGY_PRODUCT_ID, 0);
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
        
        iv1 = (ImageView) findViewById (R.id.iv1);
        iv2 = (ImageView) findViewById (R.id.iv2);
        iv3 = (ImageView) findViewById (R.id.iv3);
        
        rvServiceList = (RecyclerView) findViewById (R.id.rvServiceList);
        tvAddNewRequest = (TextView) findViewById (R.id.tvAddNewRequest);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        
    }
    
    private void initDate () {
        Utils.setTypefaceToAllViews (SwiggyMyProductDetailActivity.this, tvAddNewRequest);
        Utils.setTypefaceToAllViews (SwiggyMyProductDetailActivity.this, tvServiceRequestName);
        Utils.setTypefaceToAllViews (SwiggyMyProductDetailActivity.this, tvServiceRequestModelNumber);
        Utils.setTypefaceToAllViews (SwiggyMyProductDetailActivity.this, tvServiceRequestDate);
        
        swiggyServiceRequestAdapter = new SwiggyMyProductRequestAdapter (SwiggyMyProductDetailActivity.this, swiggyServiceRequestList);
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
        swiggyServiceRequestAdapter.SetOnItemClickListener (new SwiggyMyProductRequestAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyMyProductRequest serviceRequest = swiggyServiceRequestList.get (position);
                android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                SwiggyServiceRequestDetailDialogFragment dialog = new SwiggyServiceRequestDetailDialogFragment ().newInstance (Response, serviceRequest.getRequest_id ());
                dialog.show (ft, "Contacts");
            }
        });
        
    }
    
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (SwiggyMyProductDetailActivity.this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_MY_PRODUCT_DETAIL + id, true);
            StringRequest strRequest = new StringRequest (com.android.volley.Request.Method.GET, AppConfigURL.URL_SWIGGY_MY_PRODUCT_DETAIL + id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            Response = response;
                            if (response != null) {
                                try {
                                    swiggyServiceRequestList.clear ();
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        Request = jsonObj.getJSONArray (AppConfigTags.SWIGGY_REQUESTS).toString ();
                                        JSONArray jsonArrayRequest = jsonObj.getJSONArray (AppConfigTags.SWIGGY_REQUESTS);
                                        
                                        for (int i = 0; i < jsonArrayRequest.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayRequest.getJSONObject (i);
                                            
                                            swiggyServiceRequestList.add (i, new SwiggyMyProductRequest (
                                                    jsonObjectBrand.getInt (AppConfigTags.SWIGGY_REQUEST_ID),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_DESCRIPTION),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_STATUS),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_CREATED_AT),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_TICKET_NUMBER),
                                                    jsonObjectBrand.getJSONArray (AppConfigTags.SWIGGY_REQUEST_COMMENTS).toString (),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE1),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE2),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE3)));
                                            
                                        }
                                        swiggyServiceRequestAdapter.notifyDataSetChanged ();
                                        boolean flag = false;
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE1).endsWith (ext)) {
                                                new getBitmapFromURL ().execute (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE1));
                                                flag = true;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                            rl1.setVisibility (View.VISIBLE);
                                            tvNoImage.setVisibility (View.GONE);
                                            Glide.with (SwiggyMyProductDetailActivity.this)
                                                    .load (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE1))
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
                                        } else {
                                            rl1.setVisibility (View.GONE);
                                            tvNoImage.setVisibility (View.VISIBLE);
                                        }
                                        
                                        boolean flag1 = false;
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE2).endsWith (ext)) {
                                                new getBitmapFromURL ().execute (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE2));
                                                flag1 = true;
                                                break;
                                            }
                                        }
                                        if (flag1) {
                                            
                                            rl2.setVisibility (View.VISIBLE);
                                            tvNoImage.setVisibility (View.GONE);
                                            Glide.with (SwiggyMyProductDetailActivity.this)
                                                    .load (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE2))
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
                                        } else {
                                            rl2.setVisibility (View.GONE);
                                            tvNoImage.setVisibility (View.VISIBLE);
                                        }
                                        
                                        
                                        boolean flag3 = false;
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE3).endsWith (ext)) {
                                                new getBitmapFromURL ().execute (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE3));
                                                flag3 = true;
                                                break;
                                            }
                                        }
                                        if (flag3) {
                                            
                                            rl3.setVisibility (View.VISIBLE);
                                            tvNoImage.setVisibility (View.GONE);
                                            Glide.with (SwiggyMyProductDetailActivity.this)
                                                    .load (jsonObj.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE3))
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
                                        } else {
                                            rl3.setVisibility (View.GONE);
                                            tvNoImage.setVisibility (View.VISIBLE);
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
    
    
    private class getBitmapFromURL extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground (String... params) {
            try {
                URL url = new URL (params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
                connection.setDoInput (true);
                connection.connect ();
                InputStream input = connection.getInputStream ();
                
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }
        
        @Override
        protected void onPostExecute (String result) {
//            ivFloorPlan.setImage (ImageSource.bitmap (bitmap));
//            ivFloorPlan.setVisibility (View.VISIBLE);
//            progressBar.setVisibility (View.GONE);
        }
        
        @Override
        protected void onPreExecute () {
//            progressBar.setVisibility (View.VISIBLE);
        }
        
        @Override
        protected void onProgressUpdate (Void... values) {
        }
    }
    
    
}




