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
import com.indiasupply.isdental.adapter.SwiggyCompanyAdapter2;
import com.indiasupply.isdental.dialog.SwiggyContactDetailDialogFragment;
import com.indiasupply.isdental.model.SwiggyCompany2;
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
import java.util.List;
import java.util.Map;

public class SwiggyContactsFragment extends Fragment {
    RecyclerView rvContacts;
    CoordinatorLayout clMain;
    List<SwiggyCompany2> companyList = new ArrayList<> ();
    SwiggyCompanyAdapter2 companyAdapter;
    Button btFilter;
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    
    public static SwiggyContactsFragment newInstance () {
        return new SwiggyContactsFragment ();
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_contact, container, false);
        initView (rootView);
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvContacts = (RecyclerView) rootView.findViewById (R.id.rvContacts);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvContacts);
    
        companyAdapter = new SwiggyCompanyAdapter2 (getActivity (), companyList);
        rvContacts.setAdapter (companyAdapter);
        rvContacts.setHasFixedSize (true);
        rvContacts.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvContacts.setItemAnimator (new DefaultItemAnimator ());
        rvContacts.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
            }
        });
        companyAdapter.SetOnItemClickListener (new SwiggyCompanyAdapter2.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyCompany2 contact = companyList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                new SwiggyContactDetailDialogFragment ().newInstance (contact.getName (), contact.getContacts ()).show (ft, "Contacts");
            }
        });
    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_HOME_COMPANIES, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_HOME_COMPANIES,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                companyList.clear ();
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayCompany = jsonObj.getJSONArray (AppConfigTags.SWIGGY_COMPANIES);
                                        for (int i = 0; i < jsonArrayCompany.length (); i++) {
                                            JSONObject jsonObjectCompany = jsonArrayCompany.getJSONObject (i);
                                            companyList.add (new SwiggyCompany2 (
                                                    jsonObjectCompany.getInt (AppConfigTags.SWIGGY_COMPANY_ID),
                                                    R.drawable.ic_person,
                                                    jsonObjectCompany.getJSONArray (AppConfigTags.SWIGGY_COMPANY_CONTACTS).length (),
                                                    jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_NAME),
                                                    jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_DESCRIPTION),
                                                    jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_CATEGORIES),
                                                    jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_EMAIL),
                                                    jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_WEBSITE),
                                                    jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_IMAGE),
                                                    jsonObjectCompany.getJSONArray (AppConfigTags.SWIGGY_COMPANY_CONTACTS).toString ()));
                                        }
                                        companyAdapter.notifyDataSetChanged ();
                                        rlMain.setVisibility (View.VISIBLE);
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        
                        }
                    }) {
            
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