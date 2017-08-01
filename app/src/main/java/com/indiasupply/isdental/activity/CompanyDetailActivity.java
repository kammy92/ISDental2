package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.CompanyContactAdapter;
import com.indiasupply.isdental.model.CompanyContact;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 13/06/2017.
 */

public class CompanyDetailActivity extends AppCompatActivity {
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvTitle;
    RelativeLayout rlBack;
    RecyclerView rvCompanyContactList;
    int company_id;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    TextView tvFooter;
    
    TextView tvAboutUs;
    TextView tvCatalogue;
    TextView tvDealer;
    TextView tvDeal;
    TextView tvCategory;
    TextView tvCategory1;
    TextView tvBrands;
    TextView tvBrands1;
    
    CompanyContactAdapter companyContactAdapter;
    
    List<CompanyContact> companyContactList = new ArrayList<> ();
    List<String> companyBrandList = new ArrayList<> ();
    List<String> companyCategoryList = new ArrayList<> ();
    
    String companyName;
    String companyWebsite;
    String companyAboutUs;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_company_detail);
        getExtras ();
        initView ();
        initData ();
        initListener ();
        getCompanyDetails ();
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        company_id = intent.getIntExtra (AppConfigTags.COMPANY_ID, 0);
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        appBar = (AppBarLayout) findViewById (R.id.appbar);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        tvTitle = (TextView) findViewById (R.id.tvType);
        rvCompanyContactList = (RecyclerView) findViewById (R.id.rvBrandsContactList);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        
        tvAboutUs = (TextView) findViewById (R.id.tvAboutUs);
        tvCatalogue = (TextView) findViewById (R.id.tvCatalogue);
        tvDealer = (TextView) findViewById (R.id.tvDealers);
        tvCategory = (TextView) findViewById (R.id.tvCategory);
        tvCategory1 = (TextView) findViewById (R.id.tvCategory1);
        tvBrands = (TextView) findViewById (R.id.tvBrands);
        tvBrands1 = (TextView) findViewById (R.id.tvBrands1);
        
        
        tvFooter = (TextView) findViewById (R.id.tvFooter);
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initData () {
        appBar.setExpanded (false);
        rvCompanyContactList.setNestedScrollingEnabled (false);
        progressDialog = new ProgressDialog (CompanyDetailActivity.this);
        
        companyContactAdapter = new CompanyContactAdapter (CompanyDetailActivity.this, companyContactList);
        rvCompanyContactList.setAdapter (companyContactAdapter);
        rvCompanyContactList.setHasFixedSize (true);
        rvCompanyContactList.setLayoutManager (new LinearLayoutManager (CompanyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCompanyContactList.setItemAnimator (new DefaultItemAnimator ());
    
    
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        tvAboutUs.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (companyWebsite.contains ("http://") || companyWebsite.contains ("https://")) {
                    uri = Uri.parse (companyWebsite);
                } else {
                    uri = Uri.parse ("http://" + companyWebsite);
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        
        tvCatalogue.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Toast.makeText (CompanyDetailActivity.this, "No Catalogue to display", Toast.LENGTH_LONG).show ();
            }
        });
        tvDealer.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Toast.makeText (CompanyDetailActivity.this, "No Dealer to display", Toast.LENGTH_LONG).show ();
            }
        });

//        tvAboutUs.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick (View v) {
//                MaterialDialog dialog = new MaterialDialog.Builder (CompanyDetailActivity.this)
//                        .title (companyName)
////                        .items (items)
//                        .content (companyAboutUs)
//                        .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
//                        .contentColor (getResources ().getColor (R.color.app_text_color_dark))
//                        .typeface (SetTypeFace.getTypeface (CompanyDetailActivity.this), SetTypeFace.getTypeface (CompanyDetailActivity.this))
//                        .canceledOnTouchOutside (false)
//                        .cancelable (false)
//                        .positiveText (R.string.dialog_action_ok)
//                        .build ();
//                dialog.show ();
//            }
//        });
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private void getCompanyDetails () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_COMPANY_DETAILS + "/" + company_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_COMPANY_DETAILS + "/" + company_id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            companyContactList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
    
                                    if (! is_error) {
                                        tvTitle.setText (jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ());
                                        companyName = jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ();
        
                                        if (jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ().charAt (0) == 'A' ||
                                                jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ().charAt (0) == 'E' ||
                                                jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ().charAt (0) == 'I' ||
                                                jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ().charAt (0) == 'O' ||
                                                jsonObj.getString (AppConfigTags.COMPANY_NAME).toUpperCase ().charAt (0) == 'U') {
            
            
                                            SpannableString ss = new SpannableString ("If you are an " + jsonObj.getString (AppConfigTags.COMPANY_NAME) + " authorised dealer and your company doesn't appear on this listing, please contact us here or reach us at contact@indiasupply.com");
                                            ss.setSpan (new StyleSpan (android.graphics.Typeface.BOLD), 14, jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                            ss.setSpan (new myClickableSpan (1), jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 100, jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 104, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            ss.setSpan (new myClickableSpan (2), jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 120, jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 143, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            tvFooter.setText (ss);
                                            tvFooter.setMovementMethod (LinkMovementMethod.getInstance ());
                                        } else {
                                            SpannableString ss = new SpannableString ("If you are a " + jsonObj.getString (AppConfigTags.COMPANY_NAME) + " authorised dealer and your company doesn't appear on this listing, please contact us here or reach us at contact@indiasupply.com");
                                            ss.setSpan (new StyleSpan (android.graphics.Typeface.BOLD), 13, jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                            ss.setSpan (new myClickableSpan (1), jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 99, jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 103, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            ss.setSpan (new myClickableSpan (2), jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 119, jsonObj.getString (AppConfigTags.COMPANY_NAME).length () + 142, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            tvFooter.setText (ss);
                                            tvFooter.setMovementMethod (LinkMovementMethod.getInstance ());
                                        }
        
                                        if ((jsonObj.getString (AppConfigTags.COMPANY_WEBSITE).length () > 0) && ! (jsonObj.getString (AppConfigTags.COMPANY_WEBSITE).equalsIgnoreCase ("null"))) {
                                            companyWebsite = jsonObj.getString (AppConfigTags.COMPANY_WEBSITE);
                                        } else {
                                            companyWebsite = "www.indiasupply.com";
                                        }
        
                                        JSONArray jsonArrayBrands = jsonObj.getJSONArray (AppConfigTags.BRANDS);
                                        companyBrandList.clear ();
                                        for (int i = 0; i < jsonArrayBrands.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayBrands.getJSONObject (i);
                                            companyBrandList.add (i, jsonObjectBrand.getString (AppConfigTags.BRAND_NAME));
                                        }
        
        
                                        JSONArray jsonArrayCategories = jsonObj.getJSONArray (AppConfigTags.CATEGORIES);
                                        companyCategoryList.clear ();
                                        for (int i = 0; i < jsonArrayCategories.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayCategories.getJSONObject (i);
                                            companyCategoryList.add (i, jsonObjectBrand.getString (AppConfigTags.CATEGORY_NAME));
                                        }
        
        
                                        companyAboutUs = jsonObj.getString (AppConfigTags.COMPANY_DESCRIPTION);
                                        
                                        
                                        JSONArray jsonArrayContacts = jsonObj.getJSONArray (AppConfigTags.CONTACTS);
                                        companyContactList.clear ();
                                        for (int i = 0; i < jsonArrayContacts.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayContacts.getJSONObject (i);
                                            companyContactList.add (i, new CompanyContact (
                                                    jsonObjectBrand.getInt (AppConfigTags.CONTACT_ID),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_ATTENDANT),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_DESIGNATION),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_TYPE),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_ADDRESS),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_EMAIL),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_WEBSITE),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_PHONE1),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_PHONE2),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_TITLE),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_OPEN_TIME),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_CLOSE_TIME),
                                                    jsonObjectBrand.getString (AppConfigTags.CONTACT_HOLIDAY)
                                            ));
                                        }
                                        companyContactAdapter.notifyDataSetChanged ();
        
        
                                        if (companyBrandList.size () == 0) {
                                            tvBrands.setVisibility (View.GONE);
                                            tvBrands1.setVisibility (View.GONE);
                                        } else {
                                            tvBrands.setVisibility (View.VISIBLE);
                                            tvBrands1.setVisibility (View.VISIBLE);
                                        }
        
        
                                        if (companyCategoryList.size () == 0) {
                                            tvCategory.setVisibility (View.GONE);
                                            tvCategory1.setVisibility (View.GONE);
            
                                        } else {
                                            tvCategory.setVisibility (View.VISIBLE);
                                            tvCategory1.setVisibility (View.VISIBLE);
                                        }
        
                                        String categories = "";
                                        for (int i = 0; i < companyCategoryList.size (); i++) {
                                            if (i < companyCategoryList.size () - 1) {
                                                categories = categories + companyCategoryList.get (i) + ", ";
                                            } else {
                                                categories = categories + companyCategoryList.get (i);
                                            }
                                        }
                                        Log.e ("karman", "ffd " + categories);
                                        tvCategory.setText (categories);
        
                                        String brands = "";
                                        for (int i = 0; i < companyBrandList.size (); i++) {
                                            if (i < companyBrandList.size () - 1) {
                                                brands = brands + companyBrandList.get (i) + ", ";
                                            } else {
                                                brands = brands + companyBrandList.get (i);
                                            }
                                        }
                                        tvBrands.setText (brands);
                                        progressDialog.dismiss ();
                                    } else {
                                        Utils.showSnackBar (CompanyDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    progressDialog.dismiss ();
                                }
                            } else {
                                Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (CompanyDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
    
                            }
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
                    params.put (AppConfigTags.USER_LOGIN_KEY, userDetailsPref.getStringPref (CompanyDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
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
    
    public class myClickableSpan extends ClickableSpan {
        
        int pos;
        
        public myClickableSpan (int position) {
            this.pos = position;
        }
        
        @Override
        public void onClick (View widget) {
            switch (pos) {
                case 1:
                    Uri uri = Uri.parse ("https://www.indiasupply.com/contact.html");
                    Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                    startActivity (intent);
                    break;
                case 2:
                    Intent email = new Intent (Intent.ACTION_SEND);
                    email.putExtra (Intent.EXTRA_EMAIL, new String[] {"contact@indiasupply.com"});
                    email.putExtra (Intent.EXTRA_SUBJECT, "Contact Us");
                    email.putExtra (Intent.EXTRA_TEXT, "");
                    email.setType ("message/rfc822");
                    startActivity (Intent.createChooser (email, "Choose an Email client :"));
                    break;
            }
        }
        
    }
}