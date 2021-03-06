package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.ProductAdapter;
import com.indiasupply.isdental.adapter.RecommendedProductAdapter;
import com.indiasupply.isdental.dialog.ContactDetailDialogFragment;
import com.indiasupply.isdental.helper.DatabaseHandler;
import com.indiasupply.isdental.model.Product;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.ShimmerFrameLayout;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyDetailActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    CoordinatorLayout clMain;
    TextView tvTitleBrandCategory;
    TextView tvTitleBrandName;
    View v1;
    RecommendedProductAdapter recommendedProductAdapter;
    ProductAdapter productAdapter;
    RelativeLayout rlBack;
    LinearLayout llDynamic;
    
    TextView tvBrandName;
    TextView tvBrandCategory;
    TextView tvRating;
    TextView tvTotalRating;
    TextView tvEnquiry;
    TextView tvContacts;
    TextView tvOffer;
    RelativeLayout rlOffer;
    LinearLayout llContacts;
    
    String company_name;
    String company_contacts;
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    int company_id;
    
    DatabaseHandler db;
    
    FirebaseAnalytics mFirebaseAnalytics;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_company_detail);
        getExtras ();
        initView ();
        initData ();
        initListener ();
        setData ();
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        company_id = intent.getIntExtra (AppConfigTags.COMPANY_ID, 0);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                
            }
        });
        
        nestedScrollView.setOnScrollChangeListener (new NestedScrollView.OnScrollChangeListener () {
            @Override
            public void onScrollChange (NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
//                    Log.i (TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
//                    Log.i (TAG, "Scroll UP");
                }
                
                if (scrollY == 0) {
//                    Log.i (TAG, "TOP SCROLL");
                    v1.setVisibility (View.GONE);
                }
                
                if (scrollY > 0) {
                    v1.setVisibility (View.VISIBLE);
                    tvTitleBrandCategory.setVisibility (View.VISIBLE);
                    tvTitleBrandName.setVisibility (View.VISIBLE);
                }
                
                tvTitleBrandName.setAlpha ((float) (scrollY * 0.003));
                tvTitleBrandCategory.setAlpha ((float) (scrollY * 0.003));
//                v1.setAlpha ((float) (scrollY * 0.003));
    
                if (scrollY == (v.getChildAt (0).getMeasuredHeight () - v.getMeasuredHeight ())) {
//                    Log.i (TAG, "BOTTOM SCROLL");
                }
            }
        });
    
        llContacts.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                // [START custom_event]
                Bundle params = new Bundle ();
                params.putBoolean ("clicked", true);
                params.putInt ("company_id", company_id);
                mFirebaseAnalytics.logEvent ("company_details_contacts", params);
                // [END custom_event]
    
                
                android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                new ContactDetailDialogFragment ().newInstance (company_name, company_contacts).show (ft, "Contacts");
            }
        });
        
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        v1 = findViewById (R.id.v1);
        nestedScrollView = (NestedScrollView) findViewById (R.id.nestedScrollView);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvTitleBrandCategory = (TextView) findViewById (R.id.tvTitleBrandCategory);
        tvTitleBrandName = (TextView) findViewById (R.id.tvTitleBrandName);
        llDynamic = (LinearLayout) findViewById (R.id.llDynamic);
    
        tvBrandName = (TextView) findViewById (R.id.tvBrandName);
        tvBrandCategory = (TextView) findViewById (R.id.tvBrandCategory);
        tvRating = (TextView) findViewById (R.id.tvRating);
        tvTotalRating = (TextView) findViewById (R.id.tvTotalRating);
        tvEnquiry = (TextView) findViewById (R.id.tvEnquiry);
        tvContacts = (TextView) findViewById (R.id.tvContacts);
        tvOffer = (TextView) findViewById (R.id.tvOffer);
        rlOffer = (RelativeLayout) findViewById (R.id.rlOffer);
    
        llContacts = (LinearLayout) findViewById (R.id.llContacts);
    
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) findViewById (R.id.rlMain);
    }
    
    private void initData () {
        db = new DatabaseHandler (getApplicationContext ());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance (this);
    
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
        Utils.setTypefaceToAllViews (CompanyDetailActivity.this, tvTitleBrandCategory);
    
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_CATEGORY_DETAILS + "/" + company_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_CATEGORY_DETAILS + "/" + company_id,
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
                                        if (db.isCompanyExist (company_id)) {
                                            db.updateCompanyDetails (company_id, response);
                                        } else {
                                            db.insertCompany (company_id, response);
                                        }
                                        tvBrandName.setText (jsonObj.getString (AppConfigTags.CATEGORY_NAME));
                                        company_name = jsonObj.getString (AppConfigTags.CATEGORY_NAME);
                                        tvBrandCategory.setText (jsonObj.getString (AppConfigTags.CATEGORY_COMPANIES));
                                        tvTitleBrandName.setText (jsonObj.getString (AppConfigTags.CATEGORY_NAME));
                                        tvTitleBrandCategory.setText (jsonObj.getString (AppConfigTags.CATEGORY_COMPANIES));
    
                                        tvContacts.setText ("" + jsonObj.getJSONArray (AppConfigTags.COMPANY_CONTACTS).length ());
                                        company_contacts = jsonObj.getJSONArray (AppConfigTags.COMPANY_CONTACTS).toString ();
                                        
                                        JSONArray jsonArrayProductGroup = jsonObj.getJSONArray (AppConfigTags.COMPANY_PRODUCT_GROUPS);
                                        for (int i = 0; i < jsonArrayProductGroup.length (); i++) {
                                            ArrayList<Product> productList = new ArrayList<> ();
                                            productList.clear ();
                                            JSONObject jsonObjectProductGroup = jsonArrayProductGroup.getJSONObject (i);
                                            View view = new View (CompanyDetailActivity.this);
                                            view.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.pxFromDp (CompanyDetailActivity.this, 16)));
                                            view.setBackgroundColor (getResources ().getColor (R.color.view_color));
                                            llDynamic.addView (view);
                                            TextView tv = new TextView (CompanyDetailActivity.this);
                                            tv.setText (jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE));
                                            tv.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            tv.setTextSize (TypedValue.COMPLEX_UNIT_SP, 18);
                                            tv.setTypeface (SetTypeFace.getTypeface (CompanyDetailActivity.this, "AvenirNextLTPro-Demi.otf"), Typeface.BOLD);
                                            tv.setTextColor (getResources ().getColor (R.color.primary_text2));
                                            tv.setPadding ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 0);
                                            llDynamic.addView (tv);
                                            JSONArray jsonArrayProduct = jsonObjectProductGroup.getJSONArray (AppConfigTags.PRODUCTS);
                                            for (int j = 0; j < jsonArrayProduct.length (); j++) {
                                                JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject (j);
                                                productList.add (new Product (
                                                        jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ID),
                                                        R.drawable.default_event,
                                                        jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ENQUIRY),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_NAME),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_DESCRIPTION),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_PACKAGING),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_CATEGORY),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_PRICE),
                                                        jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE),
                                                        jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_IMAGE)
                                                ));
                                            }
    
                                            if (jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE) == 1) {
                                                RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                                                recommendedProductAdapter = new RecommendedProductAdapter (CompanyDetailActivity.this, productList);
                                                rv.setAdapter (recommendedProductAdapter);
                                                rv.setHasFixedSize (true);
                                                rv.setNestedScrollingEnabled (false);
                                                rv.setFocusable (false);
                                                rv.setLayoutManager (new GridLayoutManager (CompanyDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
                                                rv.setItemAnimator (new DefaultItemAnimator ());
                                                rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
                                                llDynamic.addView (rv);
                                            } else {
                                                RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                                                productAdapter = new ProductAdapter (CompanyDetailActivity.this, productList);
                                                rv.setAdapter (productAdapter);
                                                rv.setNestedScrollingEnabled (false);
                                                rv.setFocusable (false);
                                                rv.setHasFixedSize (true);
                                                rv.setLayoutManager (new LinearLayoutManager (CompanyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                                                rv.setItemAnimator (new DefaultItemAnimator ());
                                                rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
                                                llDynamic.addView (rv);
                                            }
    
                                        }
                                        rlMain.setVisibility (View.VISIBLE);
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                    } else {
                                        if (! showOfflineData (company_id)) {
                                            Utils.showSnackBar (CompanyDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    if (! showOfflineData (company_id)) {
                                        Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
                                }
                            } else {
                                if (! showOfflineData (company_id)) {
                                    Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
//                            int totalHeight = nestedScrollView.getChildAt (0).getHeight ();
//                            Log.e ("karman", "total height : " + totalHeight);
//                            for (int k = 0; k< totalHeight; k++) {
//                                final int l = k;
//                                new Handler ().postDelayed (new Runnable () {
//                                    @Override
//                                    public void run () {
//                                        nestedScrollView.smoothScrollTo (0, l);
//                                        Log.e ("karman", "value of l : " + l);
//                                    }
//                                }, 500);
//                            }
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
                            if (! showOfflineData (company_id)) {
                                Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            }
                        }
                    }) {
        
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (CompanyDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
            if (! showOfflineData (company_id)) {
                Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity (dialogIntent);
                    }
                });
            }
        }
    }
    
    private void setDataBKP () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_COMPANY_DETAILS + "/" + company_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_COMPANY_DETAILS + "/" + company_id,
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
                                        if (db.isCompanyExist (company_id)) {
                                            db.updateCompanyDetails (company_id, response);
                                        } else {
                                            db.insertCompany (company_id, response);
                                        }
                                        tvBrandName.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME));
                                        company_name = jsonObj.getString (AppConfigTags.COMPANY_NAME);
                                        tvBrandCategory.setText (jsonObj.getString (AppConfigTags.COMPANY_CATEGORIES));
                                        tvTitleBrandName.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME));
                                        tvTitleBrandCategory.setText (jsonObj.getString (AppConfigTags.COMPANY_CATEGORIES));
                                        tvRating.setText (jsonObj.getString (AppConfigTags.COMPANY_RATING));
                                        if (jsonObj.getInt (AppConfigTags.COMPANY_TOTAL_RATINGS) > 0) {
                                            tvTotalRating.setText (jsonObj.getInt (AppConfigTags.COMPANY_TOTAL_RATINGS) + " Ratings");
                                        } else {
                                            tvTotalRating.setText ("0 Ratings");
                                        }
                                        tvContacts.setText (jsonObj.getString (AppConfigTags.COMPANY_TOTAL_CONTACTS));
                                        if (jsonObj.getString (AppConfigTags.COMPANY_OFFERS).length () > 0) {
                                            tvOffer.setText (jsonObj.getString (AppConfigTags.COMPANY_OFFERS));
                                            rlOffer.setVisibility (View.VISIBLE);
                                        } else {
                                            rlOffer.setVisibility (View.GONE);
                                        }
                                        company_contacts = jsonObj.getJSONArray (AppConfigTags.COMPANY_CONTACTS).toString ();
                                        
                                        JSONArray jsonArrayProductGroup = jsonObj.getJSONArray (AppConfigTags.COMPANY_PRODUCT_GROUPS);
                                        for (int i = 0; i < jsonArrayProductGroup.length (); i++) {
                                            ArrayList<Product> productList = new ArrayList<> ();
                                            productList.clear ();
                                            JSONObject jsonObjectProductGroup = jsonArrayProductGroup.getJSONObject (i);
                                            View view = new View (CompanyDetailActivity.this);
                                            view.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.pxFromDp (CompanyDetailActivity.this, 16)));
                                            view.setBackgroundColor (getResources ().getColor (R.color.view_color));
                                            llDynamic.addView (view);
                                            TextView tv = new TextView (CompanyDetailActivity.this);
                                            tv.setText (jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE));
                                            tv.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            tv.setTextSize (TypedValue.COMPLEX_UNIT_SP, 18);
                                            tv.setTypeface (SetTypeFace.getTypeface (CompanyDetailActivity.this, "AvenirNextLTPro-Demi.otf"), Typeface.BOLD);
                                            tv.setTextColor (getResources ().getColor (R.color.primary_text2));
                                            tv.setPadding ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 0);
                                            llDynamic.addView (tv);
                                            JSONArray jsonArrayProduct = jsonObjectProductGroup.getJSONArray (AppConfigTags.PRODUCTS);
                                            for (int j = 0; j < jsonArrayProduct.length (); j++) {
                                                JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject (j);
                                                productList.add (new Product (
                                                        jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ID),
                                                        R.drawable.default_event,
                                                        jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ENQUIRY),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_NAME),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_DESCRIPTION),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_PACKAGING),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_CATEGORY),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_PRICE),
                                                        jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE),
                                                        jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE),
                                                        jsonObjectProduct.getString (AppConfigTags.PRODUCT_IMAGE)
                                                ));
                                            }
                                            
                                            if (jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE) == 1) {
                                                RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                                                recommendedProductAdapter = new RecommendedProductAdapter (CompanyDetailActivity.this, productList);
                                                rv.setAdapter (recommendedProductAdapter);
                                                rv.setHasFixedSize (true);
                                                rv.setNestedScrollingEnabled (false);
                                                rv.setFocusable (false);
                                                rv.setLayoutManager (new GridLayoutManager (CompanyDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
                                                rv.setItemAnimator (new DefaultItemAnimator ());
                                                rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
                                                llDynamic.addView (rv);
                                            } else {
                                                RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                                                productAdapter = new ProductAdapter (CompanyDetailActivity.this, productList);
                                                rv.setAdapter (productAdapter);
                                                rv.setNestedScrollingEnabled (false);
                                                rv.setFocusable (false);
                                                rv.setHasFixedSize (true);
                                                rv.setLayoutManager (new LinearLayoutManager (CompanyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                                                rv.setItemAnimator (new DefaultItemAnimator ());
                                                rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
                                                llDynamic.addView (rv);
                                            }
                                            
                                        }
                                        rlMain.setVisibility (View.VISIBLE);
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                    } else {
                                        if (! showOfflineData (company_id)) {
                                            Utils.showSnackBar (CompanyDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    if (! showOfflineData (company_id)) {
                                        Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
                                }
                            } else {
                                if (! showOfflineData (company_id)) {
                                    Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
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
                            if (! showOfflineData (company_id)) {
                                Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            }
                        }
                    }) {
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (CompanyDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
            if (! showOfflineData (company_id)) {
                Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity (dialogIntent);
                    }
                });
            }
        }
    }
    
    
    @Override
    public void onStart () {
        super.onStart ();
        Utils.startShimmer (shimmerFrameLayout);
    }
    
    @Override
    public void onResume () {
        super.onResume ();
        shimmerFrameLayout.startShimmerAnimation ();
    }
    
    @Override
    public void onPause () {
        shimmerFrameLayout.stopShimmerAnimation ();
        super.onPause ();
    }
    
    private boolean showOfflineData (int company_id) {
        if (db.isCompanyExist (company_id)) {
            String response = db.getCompanyDetails (company_id);
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    if (db.isCompanyExist (company_id)) {
                        db.updateCompanyDetails (company_id, response);
                    } else {
                        db.insertCompany (company_id, response);
                    }
                    tvBrandName.setText (jsonObj.getString (AppConfigTags.CATEGORY_NAME));
                    company_name = jsonObj.getString (AppConfigTags.CATEGORY_NAME);
                    tvBrandCategory.setText (jsonObj.getString (AppConfigTags.CATEGORY_COMPANIES));
                    tvTitleBrandName.setText (jsonObj.getString (AppConfigTags.CATEGORY_NAME));
                    tvTitleBrandCategory.setText (jsonObj.getString (AppConfigTags.CATEGORY_COMPANIES));
                    
                    tvContacts.setText ("" + jsonObj.getJSONArray (AppConfigTags.COMPANY_CONTACTS).length ());
                    company_contacts = jsonObj.getJSONArray (AppConfigTags.COMPANY_CONTACTS).toString ();
                    
                    JSONArray jsonArrayProductGroup = jsonObj.getJSONArray (AppConfigTags.COMPANY_PRODUCT_GROUPS);
                    for (int i = 0; i < jsonArrayProductGroup.length (); i++) {
                        ArrayList<Product> productList = new ArrayList<> ();
                        productList.clear ();
                        JSONObject jsonObjectProductGroup = jsonArrayProductGroup.getJSONObject (i);
                        View view = new View (CompanyDetailActivity.this);
                        view.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.pxFromDp (CompanyDetailActivity.this, 16)));
                        view.setBackgroundColor (getResources ().getColor (R.color.view_color));
                        llDynamic.addView (view);
                        TextView tv = new TextView (CompanyDetailActivity.this);
                        tv.setText (jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE));
                        tv.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv.setTextSize (TypedValue.COMPLEX_UNIT_SP, 18);
                        tv.setTypeface (SetTypeFace.getTypeface (CompanyDetailActivity.this, "AvenirNextLTPro-Demi.otf"), Typeface.BOLD);
                        tv.setTextColor (getResources ().getColor (R.color.primary_text2));
                        tv.setPadding ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 0);
                        llDynamic.addView (tv);
                        JSONArray jsonArrayProduct = jsonObjectProductGroup.getJSONArray (AppConfigTags.PRODUCTS);
                        for (int j = 0; j < jsonArrayProduct.length (); j++) {
                            JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject (j);
                            productList.add (new Product (
                                    jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ID),
                                    R.drawable.default_event,
                                    jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ENQUIRY),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_NAME),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_DESCRIPTION),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_PACKAGING),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_CATEGORY),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_PRICE),
                                    jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE),
                                    jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_IMAGE)
                            ));
                        }
                        
                        if (jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE) == 1) {
                            RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                            recommendedProductAdapter = new RecommendedProductAdapter (CompanyDetailActivity.this, productList);
                            rv.setAdapter (recommendedProductAdapter);
                            rv.setHasFixedSize (true);
                            rv.setNestedScrollingEnabled (false);
                            rv.setFocusable (false);
                            rv.setLayoutManager (new GridLayoutManager (CompanyDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
                            rv.setItemAnimator (new DefaultItemAnimator ());
                            rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
                            llDynamic.addView (rv);
                        } else {
                            RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                            productAdapter = new ProductAdapter (CompanyDetailActivity.this, productList);
                            rv.setAdapter (productAdapter);
                            rv.setNestedScrollingEnabled (false);
                            rv.setFocusable (false);
                            rv.setHasFixedSize (true);
                            rv.setLayoutManager (new LinearLayoutManager (CompanyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                            rv.setItemAnimator (new DefaultItemAnimator ());
                            rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
                            llDynamic.addView (rv);
                        }
                        
                    }
                    rlMain.setVisibility (View.VISIBLE);
                    shimmerFrameLayout.setVisibility (View.GONE);
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            return true;
        } else {
            return false;
        }
    }
    
    private boolean showOfflineDataBKP (int company_id) {
        if (db.isCompanyExist (company_id)) {
            String response = db.getCompanyDetails (company_id);
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    tvBrandName.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME));
                    company_name = jsonObj.getString (AppConfigTags.COMPANY_NAME);
                    tvBrandCategory.setText (jsonObj.getString (AppConfigTags.COMPANY_CATEGORIES));
                    tvTitleBrandName.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME));
                    tvTitleBrandCategory.setText (jsonObj.getString (AppConfigTags.COMPANY_CATEGORIES));
                    tvRating.setText (jsonObj.getString (AppConfigTags.COMPANY_RATING));
                    if (jsonObj.getInt (AppConfigTags.COMPANY_TOTAL_RATINGS) > 0) {
                        tvTotalRating.setText (jsonObj.getInt (AppConfigTags.COMPANY_TOTAL_RATINGS) + " Ratings");
                    } else {
                        tvTotalRating.setText ("0 Ratings");
                    }
                    tvContacts.setText (jsonObj.getString (AppConfigTags.COMPANY_TOTAL_CONTACTS));
                    if (jsonObj.getString (AppConfigTags.COMPANY_OFFERS).length () > 0) {
                        tvOffer.setText (jsonObj.getString (AppConfigTags.COMPANY_OFFERS));
                        rlOffer.setVisibility (View.VISIBLE);
                    } else {
                        rlOffer.setVisibility (View.GONE);
                    }
                    company_contacts = jsonObj.getJSONArray (AppConfigTags.COMPANY_CONTACTS).toString ();
                    
                    JSONArray jsonArrayProductGroup = jsonObj.getJSONArray (AppConfigTags.COMPANY_PRODUCT_GROUPS);
                    for (int i = 0; i < jsonArrayProductGroup.length (); i++) {
                        ArrayList<Product> productList = new ArrayList<> ();
                        productList.clear ();
                        JSONObject jsonObjectProductGroup = jsonArrayProductGroup.getJSONObject (i);
                        View view = new View (CompanyDetailActivity.this);
                        view.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.pxFromDp (CompanyDetailActivity.this, 16)));
                        view.setBackgroundColor (getResources ().getColor (R.color.view_color));
                        llDynamic.addView (view);
                        TextView tv = new TextView (CompanyDetailActivity.this);
                        tv.setText (jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE));
                        tv.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv.setTextSize (TypedValue.COMPLEX_UNIT_SP, 18);
                        tv.setTypeface (SetTypeFace.getTypeface (CompanyDetailActivity.this, "AvenirNextLTPro-Demi.otf"), Typeface.BOLD);
                        tv.setTextColor (getResources ().getColor (R.color.primary_text2));
                        tv.setPadding ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 0);
                        llDynamic.addView (tv);
                        JSONArray jsonArrayProduct = jsonObjectProductGroup.getJSONArray (AppConfigTags.PRODUCTS);
                        for (int j = 0; j < jsonArrayProduct.length (); j++) {
                            JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject (j);
                            productList.add (new Product (
                                    jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ID),
                                    R.drawable.default_event,
                                    jsonObjectProduct.getInt (AppConfigTags.PRODUCT_ENQUIRY),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_NAME),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_DESCRIPTION),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_PACKAGING),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_CATEGORY),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_PRICE),
                                    jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE),
                                    jsonObjectProductGroup.getString (AppConfigTags.GROUP_TITLE),
                                    jsonObjectProduct.getString (AppConfigTags.PRODUCT_IMAGE)
                            ));
                        }
                        
                        if (jsonObjectProductGroup.getInt (AppConfigTags.GROUP_TYPE) == 1) {
                            RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                            recommendedProductAdapter = new RecommendedProductAdapter (CompanyDetailActivity.this, productList);
                            rv.setAdapter (recommendedProductAdapter);
                            rv.setHasFixedSize (true);
                            rv.setNestedScrollingEnabled (false);
                            rv.setFocusable (false);
                            rv.setLayoutManager (new GridLayoutManager (CompanyDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
                            rv.setItemAnimator (new DefaultItemAnimator ());
                            rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
                            llDynamic.addView (rv);
                        } else {
                            RecyclerView rv = new RecyclerView (CompanyDetailActivity.this);
                            productAdapter = new ProductAdapter (CompanyDetailActivity.this, productList);
                            rv.setAdapter (productAdapter);
                            rv.setNestedScrollingEnabled (false);
                            rv.setFocusable (false);
                            rv.setHasFixedSize (true);
                            rv.setLayoutManager (new LinearLayoutManager (CompanyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                            rv.setItemAnimator (new DefaultItemAnimator ());
                            rv.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), (int) Utils.pxFromDp (CompanyDetailActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
                            llDynamic.addView (rv);
                        }
                        
                    }
                    rlMain.setVisibility (View.VISIBLE);
                    shimmerFrameLayout.setVisibility (View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            return true;
        } else {
            return false;
        }
    }
}