package com.indiasupply.isdental.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyEventAdapter;
import com.indiasupply.isdental.model.SwiggyEvent;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class SwiggyEventFragment extends Fragment {
    ShimmerRecyclerView rvEvents;
    List<SwiggyEvent> eventList = new ArrayList<> ();
    SwiggyEventAdapter eventAdapter;
    
    CoordinatorLayout clMain;
    
    public static SwiggyEventFragment newInstance () {
        return new SwiggyEventFragment ();
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_event, container, false);
        initView (rootView);
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvEvents = (ShimmerRecyclerView) rootView.findViewById (R.id.rvEvents);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvEvents);
        rvEvents.setNestedScrollingEnabled (false);
        rvEvents.setFocusable (false);
    
        rvEvents.showShimmerAdapter ();
        rvEvents.setHasFixedSize (true);
        rvEvents.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvEvents.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
    }
    
    private void setData () {
//        eventList.add (new SwiggyEvent (1, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
//        eventList.add (new SwiggyEvent (2, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Delhi", "http://famdent.indiasupply.com/isdental/api/images/delhi.jpg"));
//        eventList.add (new SwiggyEvent (3, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
//        eventList.add (new SwiggyEvent (4, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
//        eventList.add (new SwiggyEvent (5, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
//        eventAdapter.notifyDataSetChanged ();
    
    
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            eventList.clear ();
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_EVENT, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_EVENT,
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
                                        JSONArray jsonArrayEvents = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENTS);
                                        for (int i = 0; i < jsonArrayEvents.length (); i++) {
                                            JSONObject jsonObjectEvents = jsonArrayEvents.getJSONObject (i);
                                            SwiggyEvent swiggyEvent = new SwiggyEvent (
                                                    jsonObjectEvents.getInt (AppConfigTags.SWIGGY_EVENT_ID),
                                                    R.drawable.expodent_mumbai,
                                                    jsonObjectEvents.getString (AppConfigTags.SWIGGY_EVENT_TYPE),
                                                    jsonObjectEvents.getString (AppConfigTags.SWIGGY_EVENT_NAME),
                                                    jsonObjectEvents.getString (AppConfigTags.SWIGGY_EVENT_START_DATE),
                                                    jsonObjectEvents.getString (AppConfigTags.SWIGGY_EVENT_END_DATE),
                                                    jsonObjectEvents.getString (AppConfigTags.SWIGGY_EVENT_CITY),
                                                    jsonObjectEvents.getString (AppConfigTags.SWIGGY_EVENT_IMAGE)
                                            );
                                            eventList.add (i, swiggyEvent);
                                        }
    
                                        eventAdapter = new SwiggyEventAdapter (getActivity (), eventList);
                                        rvEvents.setAdapter (eventAdapter);
                                        rvEvents.setHasFixedSize (true);
                                        rvEvents.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
    
                                        rvEvents.hideShimmerAdapter ();
                                        eventAdapter.notifyDataSetChanged ();
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
}