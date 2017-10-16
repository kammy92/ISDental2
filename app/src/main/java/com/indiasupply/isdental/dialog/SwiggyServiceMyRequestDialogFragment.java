package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyServiceMyRequestAdapter;
import com.indiasupply.isdental.model.SwiggyMyRequest;
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

public class SwiggyServiceMyRequestDialogFragment extends DialogFragment {
    TextView tvTitle;
    ImageView ivCancel;
    RecyclerView rvRequest;
    SwiggyServiceMyRequestAdapter myRequestAdapter;
    CoordinatorLayout clMain;
    List<SwiggyMyRequest> myRequestList = new ArrayList<> ();
    
    public static SwiggyServiceMyRequestDialogFragment newInstance (int contact_id) {
        SwiggyServiceMyRequestDialogFragment f = new SwiggyServiceMyRequestDialogFragment ();
        // Supply num input as an argument.
        Bundle args = new Bundle ();
        // args.putInt("contact_id", contact_id);
        //args.putString("contact_name", contact_name);
        f.setArguments (args);
        return f;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    
    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_my_request, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        getMyRequestList ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rvRequest = (RecyclerView) root.findViewById (R.id.rvRequest);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        myRequestAdapter = new SwiggyServiceMyRequestAdapter (getActivity (), myRequestList);
        rvRequest.setAdapter (myRequestAdapter);
        rvRequest.setHasFixedSize (true);
        rvRequest.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvRequest.setItemAnimator (new DefaultItemAnimator ());
        rvRequest.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
    
    private void setData () {
//        myRequestList.add (new SwiggyMyRequest (1, R.drawable.ic_card, "Request 1", "Description 1", ""));
//        myRequestList.add (new SwiggyMyRequest (2, R.drawable.ic_card, "Request 2", "Description 2", ""));
//        myRequestList.add (new SwiggyMyRequest (3, R.drawable.ic_card, "Request 3", "Description 3", ""));
//        myRequestList.add (new SwiggyMyRequest (4, R.drawable.ic_card, "Request 4", "Description 4", ""));
//        myRequestList.add (new SwiggyMyRequest (5, R.drawable.ic_card, "Request 5", "Description 5", ""));
//        myRequestList.add (new SwiggyMyRequest (6, R.drawable.ic_card, "Request 6", "Description 6", ""));
//        myRequestAdapter.notifyDataSetChanged ();
    
    
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            myRequestList.clear ();
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_MY_REQUEST_LIST, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_MY_REQUEST_LIST,
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
                                        JSONArray jsonArrayBrand = jsonObj.getJSONArray (AppConfigTags.SWIGGY_REQUESTS);
                                        for (int i = 0; i < jsonArrayBrand.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject (i);
                                            SwiggyMyRequest swiggyMyRequest = new SwiggyMyRequest (
                                                    jsonObjectBrand.getInt (AppConfigTags.SWIGGY_REQUEST_ID), R.drawable.ic_person,
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_TICKET_NUMBER),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_DESCRIPTION),
                                                    jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE)
                                            );
                                            myRequestList.add (i, swiggyMyRequest);
                                        }
                                        myRequestAdapter.notifyDataSetChanged ();
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    //tvNoResult.setVisibility (View.VISIBLE);
                                }
                            } else {
                                //tvNoResult.setVisibility (View.VISIBLE);
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
    
    private void getMyRequestList () {
    }
    
    
}