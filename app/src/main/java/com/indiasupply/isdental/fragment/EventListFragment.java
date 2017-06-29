package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.EventListAdapter;
import com.indiasupply.isdental.model.Event;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventListFragment extends Fragment {
    RecyclerView rvEventList;
    List<Event> eventList = new ArrayList<> ();
    List<Event> tempEventList = new ArrayList<> ();
    EventListAdapter eventListAdapter;
    //    CoordinatorLayout clMain;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNoResult;
    
    RelativeLayout rlBack;
    
    String event_type;
    ImageView ivFilter;
    ImageView ivSort;
    TextView tvTitle;
    SearchView searchView;
    
    public static EventListFragment newInstance (String event_type) {
        EventListFragment fragment = new EventListFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.EVENT_TYPE, event_type);
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_event_list, container, false);
        initView (rootView);
        initData ();
        initBundle ();
        initListener ();
        getEventList ();
        return rootView;
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        event_type = bundle.getString (AppConfigTags.EVENT_TYPE);
    }
    
    private void initView (View rootView) {
        rvEventList = (RecyclerView) rootView.findViewById (R.id.rv1);
//        clMain = (CoordinatorLayout) rootView.findViewById(R.id.clMain);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById (R.id.swipeRefreshLayout);
        tvNoResult = (TextView) rootView.findViewById (R.id.tvNoResult);
        
        searchView = (SearchView) getActivity ().findViewById (R.id.searchView);
        ivSort = (ImageView) getActivity ().findViewById (R.id.ivSort);
        tvTitle = (TextView) getActivity ().findViewById (R.id.tvType);
        rlBack = (RelativeLayout) getActivity ().findViewById (R.id.rlBack);
    }
    
    private void initData () {
        swipeRefreshLayout.setRefreshing (true);
        eventListAdapter = new EventListAdapter (getActivity (), eventList);
        rvEventList.setAdapter (eventListAdapter);
        rvEventList.setHasFixedSize (true);
        rvEventList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvEventList.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (getActivity (), tvNoResult);
    }
    
    private void initListener () {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                swipeRefreshLayout.setRefreshing (true);
                getEventList ();
            }
        });
        
        
        searchView.setOnSearchClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
//                Toast.makeText (CompanyListActivity.this, "karman open", Toast.LENGTH_SHORT).show ();
//                ivFilter.setVisibility (View.GONE);
                rlBack.setVisibility (View.GONE);
//                ivSort.setVisibility (View.GONE);
                tvTitle.setVisibility (View.GONE);
            }
        });
        
        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit (String query) {
                return true;
            }
            
            @Override
            public boolean onQueryTextChange (String newText) {
                
                tempEventList.clear ();
                for (Event event : eventList) {
                    if (event.getName ().toUpperCase ().contains (newText.toUpperCase ()) ||
                            event.getName ().toLowerCase ().contains (newText.toLowerCase ()) ||
                            event.getCity ().toLowerCase ().contains (newText.toLowerCase ()) ||
                            event.getCity ().toUpperCase ().contains (newText.toUpperCase ())) {
                        tempEventList.add (event);
                        
                        Log.e ("karman", "event add " + event.getCity ());
                    }
                }
                eventListAdapter = new EventListAdapter (getActivity (), tempEventList);
                rvEventList.setAdapter (eventListAdapter);
                rvEventList.setHasFixedSize (true);
                rvEventList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
                rvEventList.setItemAnimator (new DefaultItemAnimator ());
                return true;
            }
        });
        
        searchView.setOnCloseListener (new SearchView.OnCloseListener () {
            @Override
            public boolean onClose () {
//                Toast.makeText (CompanyListActivity.this, "karman close", Toast.LENGTH_SHORT).show ();
//                ivFilter.setVisibility (View.VISIBLE);
                rlBack.setVisibility (View.VISIBLE);
//                ivSort.setVisibility (View.VISIBLE);
                tvTitle.setVisibility (View.VISIBLE);
                return false;
            }
        });
    }
    
    private void getEventList () {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            tvNoResult.setVisibility (View.GONE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_EVENT_LIST, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_EVENT_LIST,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            eventList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayEvents = jsonObj.getJSONArray (AppConfigTags.EVENTS);
                                        for (int i = 0; i < jsonArrayEvents.length (); i++) {
                                            JSONObject jsonObjectEvent = jsonArrayEvents.getJSONObject (i);
                                            eventList.add (i, new Event (
                                                    jsonObjectEvent.getInt (AppConfigTags.EVENT_ID),
                                                    jsonObjectEvent.getString (AppConfigTags.EVENT_NAME),
                                                    jsonObjectEvent.getString (AppConfigTags.EVENT_START_DATE),
                                                    jsonObjectEvent.getString (AppConfigTags.EVENT_END_DATE),
                                                    jsonObjectEvent.getString (AppConfigTags.EVENT_TYPE),
                                                    jsonObjectEvent.getString (AppConfigTags.EVENT_CITY),
                                                    jsonObjectEvent.getString (AppConfigTags.EVENT_ORGANISER_NAME)
                                            ));
                                        }
                                        eventListAdapter.notifyDataSetChanged ();
                                        if (jsonArrayEvents.length () == 0) {
                                            tvNoResult.setVisibility (View.VISIBLE);
                                        }
                                    } else {
                                        tvNoResult.setVisibility (View.VISIBLE);
//                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    swipeRefreshLayout.setRefreshing (false);
                                } catch (Exception e) {
                                    swipeRefreshLayout.setRefreshing (false);
                                    e.printStackTrace ();
//                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    tvNoResult.setVisibility (View.VISIBLE);
                                }
                            } else {
                                tvNoResult.setVisibility (View.VISIBLE);
//                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
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
//                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            swipeRefreshLayout.setRefreshing (false);
                            tvNoResult.setVisibility (View.VISIBLE);
                        }
                    }) {
    
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.EVENT_TYPE, event_type);
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
            tvNoResult.setVisibility (View.VISIBLE);
            swipeRefreshLayout.setRefreshing (false);
//            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
//                @Override
//                public void onClick (View v) {
//                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
//                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity (dialogIntent);
//                }
//            });
        }
    }
}
