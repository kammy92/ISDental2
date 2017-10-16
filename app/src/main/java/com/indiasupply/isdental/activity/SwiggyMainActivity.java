package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.fragment.SwiggyContactsFragment;
import com.indiasupply.isdental.fragment.SwiggyEventFragment;
import com.indiasupply.isdental.fragment.SwiggyExhibitorsFragment;
import com.indiasupply.isdental.fragment.SwiggyMyAccountFragment;
import com.indiasupply.isdental.fragment.SwiggyServiceFragment;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.ApplicationDetailsPref;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class SwiggyMainActivity extends AppCompatActivity {
    ApplicationDetailsPref applicationDetailsPref;
    CoordinatorLayout clMain;
    
    public static void disableShiftMode (BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt (0);
        try {
            Field shiftingMode = menuView.getClass ().getDeclaredField ("mShiftingMode");
            shiftingMode.setAccessible (true);
            shiftingMode.setBoolean (menuView, false);
            shiftingMode.setAccessible (false);
            for (int i = 0; i < menuView.getChildCount (); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt (i);
                //noinspection RestrictedApi
                item.setShiftingMode (false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked (item.getItemData ().isChecked ());
            }
        } catch (NoSuchFieldException e) {
            Log.e ("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e ("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activtiy_swiggy_main);
        initView ();
        initData ();
        initListener ();
        initApplication ();
        Window window = getWindow ();
//        window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (clMain != null) {
//                clMain.setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//        }
        
        
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById (R.id.navigation);
        
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener () {
                    @Override
                    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId ()) {
                            case R.id.action_item1:
                                selectedFragment = SwiggyExhibitorsFragment.newInstance ();
                                break;
                            case R.id.action_item2:
                                selectedFragment = SwiggyEventFragment.newInstance ();
                                break;
                            case R.id.action_item3:
                                selectedFragment = SwiggyContactsFragment.newInstance ();
                                break;
                            case R.id.action_item4:
                                selectedFragment = SwiggyServiceFragment.newInstance ();
                                break;
                            case R.id.action_item5:
                                selectedFragment = SwiggyMyAccountFragment.newInstance ();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
                        transaction.replace (R.id.frame_layout, selectedFragment);
                        transaction.commit ();
                        return true;
                    }
                });
        disableShiftMode (bottomNavigationView);
        
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
        transaction.replace (R.id.frame_layout, SwiggyExhibitorsFragment.newInstance ());
        transaction.commit ();

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams ();
//        layoutParams.setBehavior (new BottomNavigationViewBehavior ());
        
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    }
    
    private void initData () {
        applicationDetailsPref = ApplicationDetailsPref.getInstance ();
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void initListener () {
    }
    
    @Override
    public void onBackPressed () {
/*
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .content (R.string.dialog_text_quit_application)
                .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                .neutralColor (getResources ().getColor (R.color.app_text_color_dark))
                .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                .negativeColor (getResources ().getColor (R.color.app_text_color_dark))
                .typeface (SetTypeFace.getTypeface (this), SetTypeFace.getTypeface (this))
                .canceledOnTouchOutside (false)
                .cancelable (false)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .neutralText (R.string.dialog_action_logout)
                .onNeutral (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        applicationDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_ID, "");
                        applicationDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                        applicationDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                        applicationDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        applicationDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.HEADER_USER_LOGIN_KEY, "");

                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        applicationDetailsPref.putBooleanPref (MainActivity.this, UserDetailsPref.LOGGED_IN_SESSION, false);


                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
*/
        super.onBackPressed ();
        finish ();
    }
    
    private void initApplication () {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_INIT, true);
            final PackageInfo finalPInfo = pInfo;
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_INIT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        JSONArray jsonArrayCategories = jsonObj.getJSONArray (AppConfigTags.CATEGORIES);
                                        applicationDetailsPref.putStringPref (SwiggyMainActivity.this, ApplicationDetailsPref.SWIGGY_CATEGORIES, jsonArrayCategories.toString ());
                                        
                                        JSONArray jsonArrayBrands = jsonObj.getJSONArray (AppConfigTags.BRANDS);
                                        applicationDetailsPref.putStringPref (SwiggyMainActivity.this, ApplicationDetailsPref.SWIGGY_BRANDS, jsonArrayBrands.toString ());
                                        
                                        if (jsonObj.getBoolean (AppConfigTags.VERSION_UPDATE)) {
                                            new MaterialDialog.Builder (SwiggyMainActivity.this)
                                                    .content (R.string.dialog_text_new_version_available)
                                                    .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                                                    .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                                                    .negativeColor (getResources ().getColor (R.color.app_text_color_dark))
                                                    .typeface (SetTypeFace.getTypeface (SwiggyMainActivity.this), SetTypeFace.getTypeface (SwiggyMainActivity.this))
                                                    .canceledOnTouchOutside (false)
                                                    .cancelable (false)
                                                    .positiveText (R.string.dialog_action_update)
                                                    .negativeText (R.string.dialog_action_ignore)
                                                    .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                        @Override
                                                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            final String appPackageName = getPackageName ();
                                                            try {
                                                                startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("market://details?id=" + appPackageName)));
                                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                                startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                            }
                                                        }
                                                    })
                                                    .onNegative (new MaterialDialog.SingleButtonCallback () {
                                                        @Override
                                                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss ();
                                                        }
                                                    }).show ();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                //   initDefaultBanner ();
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
                    params.put ("app_version", String.valueOf (finalPInfo.versionCode));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SwiggyMainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
        }
    }
}