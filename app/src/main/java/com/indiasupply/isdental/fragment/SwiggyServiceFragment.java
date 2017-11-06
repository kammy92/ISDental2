package com.indiasupply.isdental.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyServiceItemAdapter;
import com.indiasupply.isdental.dialog.SwiggyServiceAddProductDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyServiceAddRequestDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyServiceMyProductDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyServiceMyRequestDialogFragment;
import com.indiasupply.isdental.model.SwiggyServiceItem;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.AppDataPref;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.ShimmerFrameLayout;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwiggyServiceFragment extends Fragment {
    ImageView ivCancel;
    CoordinatorLayout clMain;
    RecyclerView rvServiceList;
    SwiggyServiceItemAdapter swiggyServiceAdapter;
    List<SwiggyServiceItem> swiggyServiceItemList = new ArrayList<> ();
    
    String myRequests = "";
    String myProducts = "";
    String categories = "";
    String brands = "";
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    AppDataPref appDataPref;
    
    public static SwiggyServiceFragment newInstance () {
        SwiggyServiceFragment fragment = new SwiggyServiceFragment ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_swiggy_service, container, false);
        initView (root);
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View rootView) {
        ivCancel = (ImageView) rootView.findViewById (R.id.ivCancel);
        rvServiceList = (RecyclerView) rootView.findViewById (R.id.rvServiceList);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvServiceList);
        appDataPref = AppDataPref.getInstance ();
        
        swiggyServiceItemList.add (new SwiggyServiceItem (1, R.drawable.ic_service_my_products, "MY PRODUCTS", ""));
        swiggyServiceItemList.add (new SwiggyServiceItem (2, R.drawable.ic_service_add_product, "ADD PRODUCT", ""));
        swiggyServiceItemList.add (new SwiggyServiceItem (3, R.drawable.ic_service_my_requests, "MY REQUESTS", ""));
        swiggyServiceItemList.add (new SwiggyServiceItem (4, R.drawable.ic_service_add_request, "ADD REQUEST", ""));
    
    
        swiggyServiceAdapter = new SwiggyServiceItemAdapter (getActivity (), swiggyServiceItemList);
        rvServiceList.setNestedScrollingEnabled (false);
        rvServiceList.setFocusable (false);
        rvServiceList.setAdapter (swiggyServiceAdapter);
        rvServiceList.setHasFixedSize (true);
        rvServiceList.setLayoutManager (new GridLayoutManager (getActivity (), 2, GridLayoutManager.VERTICAL, false));
        rvServiceList.setItemAnimator (new DefaultItemAnimator ());
        rvServiceList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        swiggyServiceAdapter.SetOnItemClickListener (new SwiggyServiceItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                final SwiggyServiceItem service = swiggyServiceItemList.get (position);
                final android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                switch (service.getId ()) {
                    case 1:
                        SwiggyServiceMyProductDialogFragment frag = new SwiggyServiceMyProductDialogFragment ().newInstance (myProducts);
                        frag.show (ft, "");
                        break;
                    case 2:
                        SwiggyServiceAddProductDialogFragment frag2 = new SwiggyServiceAddProductDialogFragment ().newInstance (categories, brands);
                        frag2.show (ft, "");
                        break;
                    case 3:
                        SwiggyServiceMyRequestDialogFragment frag3 = new SwiggyServiceMyRequestDialogFragment ().newInstance (myRequests);
                        frag3.show (ft, "");
                        break;
                    case 4:
                        SwiggyServiceAddRequestDialogFragment frag4 = new SwiggyServiceAddRequestDialogFragment ().newInstance (myProducts);
                        frag4.show (ft, "");
                        break;
                }
            }
        });
    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_HOME_SERVICE, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_HOME_SERVICE,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (getActivity () != null && isAdded ()) {
                                if (response != null) {
                                    try {
                                        JSONObject jsonObj = new JSONObject (response);
                                        boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                        String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                        if (! is_error) {
                                            appDataPref.putStringPref (getActivity (), AppDataPref.HOME_SERVICE, response);
                                            myRequests = jsonObj.getJSONArray (AppConfigTags.SWIGGY_REQUESTS).toString ();
                                            myProducts = jsonObj.getJSONArray (AppConfigTags.SWIGGY_PRODUCTS).toString ();
                                            categories = jsonObj.getJSONArray (AppConfigTags.SWIGGY_CATEGORIES).toString ();
                                            brands = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BRANDS).toString ();
    
                                            rlMain.setVisibility (View.VISIBLE);
                                            shimmerFrameLayout.setVisibility (View.GONE);
                                        } else {
                                            if (! showOfflineData ()) {
                                                Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace ();
                                        if (! showOfflineData ()) {
                                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                        }
                                    }
                                } else {
                                    if (! showOfflineData ()) {
                                        Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
                                    Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            if (getActivity () != null && isAdded ()) {
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                                }
                                if (! showOfflineData ()) {
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            }
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
            if (getActivity () != null && isAdded ()) {
                if (! showOfflineData ()) {
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
    
    private boolean showOfflineData () {
        String response = appDataPref.getStringPref (getActivity (), AppDataPref.HOME_SERVICE);
        if (response.length () > 0) {
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    appDataPref.putStringPref (getActivity (), AppDataPref.HOME_SERVICE, response);
                    myRequests = jsonObj.getJSONArray (AppConfigTags.SWIGGY_REQUESTS).toString ();
                    myProducts = jsonObj.getJSONArray (AppConfigTags.SWIGGY_PRODUCTS).toString ();
                    categories = jsonObj.getJSONArray (AppConfigTags.SWIGGY_CATEGORIES).toString ();
                    brands = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BRANDS).toString ();
                    
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