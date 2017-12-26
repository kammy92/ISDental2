package com.indiasupply.isdental.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.SwiggyServiceAddProductActivity;
import com.indiasupply.isdental.adapter.SwiggyServiceMyProductAdapter2;
import com.indiasupply.isdental.model.SwiggyMyProduct2;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.AppDataPref;
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

import static com.indiasupply.isdental.activity.SwiggyLoginActivity.PERMISSION_REQUEST_CODE;

public class SwiggyServiceFragment2 extends Fragment {
    ImageView ivCancel;
    CoordinatorLayout clMain;
    RecyclerView rvServiceList;
    TextView tvAddServiceRequest;
    
    // SwiggyServiceItemAdapter swiggyServiceAdapter;
    // List<SwiggyServiceItem> swiggyServiceItemList = new ArrayList<> ();
    SwiggyServiceMyProductAdapter2 swiggyServiceMyProductAdapter2;
    List<SwiggyMyProduct2> swiggyServiceItemList = new ArrayList<> ();
    
    String myRequests = "";
    String myProducts = "";
    String categories = "";
    String brands = "";
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    RelativeLayout rlNoServiceAvailable;
    
    AppDataPref appDataPref;
    
    boolean refresh;
    
    public static SwiggyServiceFragment2 newInstance (boolean refresh) {
        SwiggyServiceFragment2 fragment = new SwiggyServiceFragment2 ();
        Bundle args = new Bundle ();
        args.putBoolean (AppConfigTags.REFRESH_FLAG, refresh);
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_swiggy_service2, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        checkPermissions ();
//        setData ();
        return root;
    }
    
    private void initView (View rootView) {
        ivCancel = (ImageView) rootView.findViewById (R.id.ivCancel);
        rvServiceList = (RecyclerView) rootView.findViewById (R.id.rvServiceList);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
        rlNoServiceAvailable = (RelativeLayout) rootView.findViewById (R.id.rlNoServiceAvailable);
        tvAddServiceRequest = (TextView) rootView.findViewById (R.id.tvAddServiceRequest);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        refresh = bundle.getBoolean (AppConfigTags.REFRESH_FLAG);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvServiceList);
        appDataPref = AppDataPref.getInstance ();
        
        swiggyServiceMyProductAdapter2 = new SwiggyServiceMyProductAdapter2 (getActivity (), swiggyServiceItemList);
        rvServiceList.setNestedScrollingEnabled (false);
        rvServiceList.setFocusable (false);
        rvServiceList.setAdapter (swiggyServiceMyProductAdapter2);
        rvServiceList.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
        rvServiceList.setLayoutManager (linearLayoutManager);
        rvServiceList.setItemAnimator (new DefaultItemAnimator ());
        rvServiceList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
        
        if (! refresh) {
            showOfflineData ();
        }
    }
    
    private void initListener () {
        tvAddServiceRequest.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (getActivity (), SwiggyServiceAddProductActivity.class);
                intent.putExtra (AppConfigTags.SWIGGY_BRANDS, brands);
                getActivity ().startActivity (intent);
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
                                        swiggyServiceItemList.clear ();
                                        JSONObject jsonObj = new JSONObject (response);
                                        boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                        String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                        if (! is_error) {
                                            appDataPref.putStringPref (getActivity (), AppDataPref.HOME_SERVICE, response);
                                            categories = jsonObj.getJSONArray (AppConfigTags.SWIGGY_CATEGORIES).toString ();
                                            brands = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BRANDS).toString ();
                                            JSONArray jsonArrayProducts = jsonObj.getJSONArray (AppConfigTags.SWIGGY_PRODUCTS);
                                            
                                            for (int i = 0; i < jsonArrayProducts.length (); i++) {
                                                JSONObject jsonObjectBrand = jsonArrayProducts.getJSONObject (i);
                                                swiggyServiceItemList.add (i, new SwiggyMyProduct2 (
                                                        jsonObjectBrand.getInt (AppConfigTags.SWIGGY_PRODUCT_ID),
                                                        R.drawable.default_my_equipment,
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_NAME),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_BRAND),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_CREATED_AT),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_STATUS),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_TICKET_NUMBER),
                                                        jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE)
                                                ));
                                            }
                                            swiggyServiceMyProductAdapter2.notifyDataSetChanged ();
                                            
                                            if (jsonArrayProducts.length () > 0) {
                                                rlMain.setVisibility (View.VISIBLE);
                                                rlNoServiceAvailable.setVisibility (View.GONE);
                                            } else {
                                                rlMain.setVisibility (View.GONE);
                                                rlNoServiceAvailable.setVisibility (View.VISIBLE);
                                            }
                                            
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
                                        Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
            Utils.sendRequest (strRequest, 30);
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
        setData ();
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
            swiggyServiceItemList.clear ();
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    categories = jsonObj.getJSONArray (AppConfigTags.SWIGGY_CATEGORIES).toString ();
                    brands = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BRANDS).toString ();
                    JSONArray jsonArrayProducts = jsonObj.getJSONArray (AppConfigTags.SWIGGY_PRODUCTS);
    
                    for (int i = 0; i < jsonArrayProducts.length (); i++) {
                        JSONObject jsonObjectBrand = jsonArrayProducts.getJSONObject (i);
                        swiggyServiceItemList.add (i, new SwiggyMyProduct2 (
                                jsonObjectBrand.getInt (AppConfigTags.SWIGGY_PRODUCT_ID),
                                R.drawable.ic_service_my_products,
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_NAME),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_BRAND),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_CREATED_AT),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_STATUS),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_TICKET_NUMBER),
                                jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE)
                        ));
                    }
                    swiggyServiceMyProductAdapter2.notifyDataSetChanged ();
    
                    if (jsonArrayProducts.length () > 0) {
                        rlMain.setVisibility (View.VISIBLE);
                        rlNoServiceAvailable.setVisibility (View.GONE);
                    } else {
                        rlMain.setVisibility (View.GONE);
                        rlNoServiceAvailable.setVisibility (View.VISIBLE);
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
    
    public void checkPermissions () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity ().checkSelfPermission (Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    getActivity ().checkSelfPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }
    }
    
    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale (permission);
                    if (! showRationale) {
                        new MaterialDialog.Builder (getActivity ())
                                .content ("Permission are required please enable them on the App Setting page")
                                .positiveText ("OK")
                                .theme (Theme.LIGHT)
                                .contentColorRes (R.color.primary_text2)
                                .positiveColorRes (R.color.primary_text2)
                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                    @Override
                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss ();
                                        Intent intent = new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts ("package", getActivity ().getPackageName (), null));
                                        startActivity (intent);
                                    }
                                }).show ();
                        // user denied flagging NEVER ASK AGAIN
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    } else if (Manifest.permission.RECEIVE_SMS.equals (permission)) {
//                        Utils.showToast (this, "Camera Permission is required");
//                        showRationale (permission, R.string.permission_denied_contacts);
                        // user denied WITHOUT never ask again
                        // this is a good place to explain the user
                        // why you need the permission and ask if he want
                        // to accept it (the rationale)
                    } else if (Manifest.permission.READ_SMS.equals (permission)) {
                    }
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }
    
    public interface MyDialogCloseListener {
        public void handleDialogClose (DialogInterface dialog);
    }
}