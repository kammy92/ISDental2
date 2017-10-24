package com.indiasupply.isdental.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyBannerAdapter;
import com.indiasupply.isdental.adapter.SwiggyCompanyAdapter;
import com.indiasupply.isdental.model.SwiggyBanner;
import com.indiasupply.isdental.model.SwiggyCompany;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.ShimmerFrameLayout;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 27/09/2017.
 */

public class SwiggyExhibitorsFragment extends Fragment {
    RecyclerView rvBanners;
    RecyclerView rvCompany;
    ShimmerFrameLayout shimmerFrameLayout;
    List<SwiggyBanner> bannerList = new ArrayList<> ();
    List<SwiggyCompany> companyList = new ArrayList<> ();
    SwiggyBannerAdapter bannerAdapter;
    SwiggyCompanyAdapter companyAdapter;
    Button btFilter;
    RelativeLayout rlMain;
    
    CoordinatorLayout clMain;
    
    
    public static SwiggyExhibitorsFragment newInstance () {
        return new SwiggyExhibitorsFragment ();
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_home, container, false);
        initView (rootView);
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvBanners = (RecyclerView) rootView.findViewById (R.id.rvBanners);
        rvCompany = (RecyclerView) rootView.findViewById (R.id.rvCompany);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), btFilter);
        rvBanners.setNestedScrollingEnabled (false);
        rvCompany.setNestedScrollingEnabled (false);
        rvCompany.setFocusable (false);
        rvBanners.setFocusable (false);
    
        bannerAdapter = new SwiggyBannerAdapter (getActivity (), bannerList);
        rvBanners.setAdapter (bannerAdapter);
        rvBanners.setHasFixedSize (true);
        rvBanners.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.HORIZONTAL, false));
        rvBanners.setItemAnimator (new DefaultItemAnimator ());
        rvBanners.addItemDecoration (new RecyclerViewMargin (0, 0, (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 0, 1, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_HORIZONTAL));
    
        companyAdapter = new SwiggyCompanyAdapter (getActivity (), companyList);
        rvCompany.setAdapter (companyAdapter);
        rvCompany.setHasFixedSize (true);
        rvCompany.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvCompany.setItemAnimator (new DefaultItemAnimator ());
        rvCompany.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
            }
        });
    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            //tvNoResult.setVisibility(View.GONE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_EXHIBITORS_LIST, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_EXHIBITORS_LIST,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            //categoryList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayBanners = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BANNERS);
                                        for (int i = 0; i < jsonArrayBanners.length (); i++) {
                                            JSONObject jsonObjectBanners = jsonArrayBanners.getJSONObject (i);
                                            bannerList.add (new SwiggyBanner (
                                                    jsonObjectBanners.getInt (AppConfigTags.BANNER_ID),
                                                    R.drawable.default_banner,
                                                    jsonObjectBanners.getString (AppConfigTags.BANNER_IMAGE),
                                                    jsonObjectBanners.getString (AppConfigTags.BANNER_TITLE),
                                                    jsonObjectBanners.getString (AppConfigTags.BANNER_TYPE),
                                                    jsonObjectBanners.getString (AppConfigTags.BANNER_URL)
                                            ));
                                        }
                                        bannerAdapter.notifyDataSetChanged ();
                                        JSONArray jsonArrayCompanies = jsonObj.getJSONArray (AppConfigTags.SWIGGY_COMPANIES);
                                        for (int j = 0; j < jsonArrayCompanies.length (); j++) {
                                            JSONObject jsonObjectCompanies = jsonArrayCompanies.getJSONObject (j);
                                            companyList.add (new SwiggyCompany (false,
                                                    jsonObjectCompanies.getInt (AppConfigTags.SWIGGY_COMPANY_ID),
                                                    R.drawable.ic_person,
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_COMPANY_NAME),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_COMPANY_DESCRIPTION),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_COMPANY_RATING),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_TOTAL_OFFERS),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_COMPANY_CATEGORIES),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_COMPANY_IMAGE),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_TOTAL_RATINGS),
                                                    jsonObjectCompanies.getString (AppConfigTags.SWIGGY_TOTAL_CONTACTS)
                                            ));
                                        }
    
                                        companyAdapter.notifyDataSetChanged ();
                                        rlMain.setVisibility (View.VISIBLE);
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    // swipeRefreshLayout.setRefreshing(false);
                                    e.printStackTrace ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    // tvNoResult.setVisibility(View.VISIBLE);
                                }
                            } else {
                                // tvNoResult.setVisibility(View.VISIBLE);
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            //  swipeRefreshLayout.setRefreshing(false);
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
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            // swipeRefreshLayout.setRefreshing(false);
                            // tvNoResult.setVisibility(View.VISIBLE);
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (getActivity (), UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            //tvNoResult.setVisibility(View.VISIBLE);
            //swipeRefreshLayout.setRefreshing(false);
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    private void startShimmer () {
        shimmerFrameLayout.useDefaults ();
        shimmerFrameLayout.setDuration (1500);
        shimmerFrameLayout.setBaseAlpha (0.3f);
        shimmerFrameLayout.setRepeatDelay (500);
        if (shimmerFrameLayout.isAnimationStarted ()) {
            shimmerFrameLayout.startShimmerAnimation ();
        }
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        startShimmer ();
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
}
