package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bugsnag.android.Bugsnag;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.fragment.SwiggyContactsFragment;
import com.indiasupply.isdental.fragment.SwiggyEventFragment;
import com.indiasupply.isdental.fragment.SwiggyFeaturedFragment;
import com.indiasupply.isdental.fragment.SwiggyMyAccountFragment;
import com.indiasupply.isdental.fragment.SwiggyOffersFragment;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class SwiggyMainActivity extends AppCompatActivity {
    public static final int REQUEST_LOGIN_SCREEN_RESULT = 2;
    CoordinatorLayout clMain;
    BottomNavigationView bottomNavigationView;
    UserDetailsPref userDetailsPref;
    
    ArrayList<Integer> screenList = new ArrayList<> ();
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activtiy_swiggy_main);
        initView ();
        initData ();
        initListener ();
        isLogin ();
    }
    
    private void initFirstFragment () {
        bottomNavigationView.getMenu ().findItem (R.id.action_item_offers).setIcon (R.drawable.ic_home_featured_filled);
        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
        transaction.replace (R.id.frame_layout, SwiggyOffersFragment.newInstance (false));
        transaction.commit ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        bottomNavigationView = (BottomNavigationView) findViewById (R.id.navigation);
    }
    
    private void initData () {
//        Window window = getWindow ();
//        if (Build.VERSION.SDK_INT >= 21) {
//            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
//        }
        Bugsnag.init (this);
        
        Utils.setTypefaceToAllViews (this, clMain);
        Utils.disableShiftMode (bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu ();
        menu.findItem (R.id.action_item_offers).setIcon (R.drawable.ic_home_featured);

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams ();
//        layoutParams.setBehavior (new BottomNavigationViewBehavior ());
    }
    
    private void isLogin () {
        userDetailsPref = UserDetailsPref.getInstance ();
        if (userDetailsPref.getStringPref (SwiggyMainActivity.this, UserDetailsPref.USER_LOGIN_KEY).length () == 0) {
            Intent intent = new Intent (SwiggyMainActivity.this, SwiggyLoginActivity.class);
            startActivityForResult (intent, REQUEST_LOGIN_SCREEN_RESULT);
            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            initFirstFragment ();
            initApplication ();
        }
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_SCREEN_RESULT) {
            if (data.getBooleanExtra ("LOGIN", false)) {
                initFirstFragment ();
                initApplication ();
            } else {
                finish ();
            }
        }
    }
    
    private void initListener () {
        bottomNavigationView.setOnNavigationItemReselectedListener (new BottomNavigationView.OnNavigationItemReselectedListener () {
            @Override
            public void onNavigationItemReselected (@NonNull MenuItem item) {
                switch (item.getItemId ()) {
                    case R.id.action_item_featured:
//                        Utils.showToast (SwiggyMainActivity.this, "In reselected item 1", false);
                        break;
                    case R.id.action_item_events:
//                        Utils.showToast (SwiggyMainActivity.this, "In reselected item 2", false);
                        break;
                    case R.id.action_item_contacts:
//                        Utils.showToast (SwiggyMainActivity.this, "In reselected item 3", false);
                        break;
                    case R.id.action_item_offers:
//                        Utils.showToast (SwiggyMainActivity.this, "In reselected item 4", false);
                        break;
                    case R.id.action_item_account:
//                        Utils.showToast (SwiggyMainActivity.this, "In reselected item 5", false);
                        break;
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener () {
                    @Override
                    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                        Menu menu = bottomNavigationView.getMenu ();
                        menu.findItem (R.id.action_item_offers).setIcon (R.drawable.ic_home_featured);
                        menu.findItem (R.id.action_item_featured).setIcon (R.drawable.ic_home_featured);
                        menu.findItem (R.id.action_item_events).setIcon (R.drawable.ic_home_events);
                        menu.findItem (R.id.action_item_contacts).setIcon (R.drawable.ic_home_contacts);
                        menu.findItem (R.id.action_item_account).setIcon (R.drawable.ic_home_account);
    
                        Fragment selectedFragment = null;
                        switch (item.getItemId ()) {
                            case R.id.action_item_offers:
                                item.setIcon (R.drawable.ic_home_featured_filled);
                                if (screenList.contains (R.id.action_item_offers)) {
                                    selectedFragment = SwiggyOffersFragment.newInstance (false);
                                } else {
                                    selectedFragment = SwiggyOffersFragment.newInstance (true);
                                }
                                screenList.add (R.id.action_item_offers);
                                break;
                            case R.id.action_item_featured:
                                item.setIcon (R.drawable.ic_home_featured_filled);
                                if (screenList.contains (R.id.action_item_featured)) {
                                    selectedFragment = SwiggyFeaturedFragment.newInstance (false);
                                } else {
                                    selectedFragment = SwiggyFeaturedFragment.newInstance (true);
                                }
                                screenList.add (R.id.action_item_featured);
                                break;
                            case R.id.action_item_events:
                                item.setIcon (R.drawable.ic_home_events_filled);
                                if (screenList.contains (R.id.action_item_events)) {
                                    selectedFragment = SwiggyEventFragment.newInstance (false);
                                } else {
                                    selectedFragment = SwiggyEventFragment.newInstance (true);
                                }
                                screenList.add (R.id.action_item_events);
                                break;
                            case R.id.action_item_contacts:
                                item.setIcon (R.drawable.ic_home_contacts_filled);
                                if (screenList.contains (R.id.action_item_contacts)) {
                                    selectedFragment = SwiggyContactsFragment.newInstance (false);
                                } else {
                                    selectedFragment = SwiggyContactsFragment.newInstance (true);
                                }
                                screenList.add (R.id.action_item_contacts);
                                break;
                            case R.id.action_item_account:
                                item.setIcon (R.drawable.ic_home_account_filled);
                                if (screenList.contains (R.id.action_item_account)) {
                                    selectedFragment = SwiggyMyAccountFragment.newInstance (false);
                                } else {
                                    selectedFragment = SwiggyMyAccountFragment.newInstance (true);
                                }
                                screenList.add (R.id.action_item_account);
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
                        transaction.replace (R.id.frame_layout, selectedFragment);
                        transaction.commit ();
                        return true;
                    }
                });
    }
    
    @Override
    public void onBackPressed () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .content (R.string.dialog_text_quit_application)
                .positiveColor (getResources ().getColor (R.color.primary_text2))
                .contentColor (getResources ().getColor (R.color.primary_text2))
                .negativeColor (getResources ().getColor (R.color.primary_text2))
                .typeface (SetTypeFace.getTypeface (this), SetTypeFace.getTypeface (this))
                .canceledOnTouchOutside (true)
                .cancelable (true)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userDetailsPref.putBooleanPref (SwiggyMainActivity.this, UserDetailsPref.LOGGED_IN_SESSION, false);
                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
//        super.onBackPressed ();
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
                                        if (jsonObj.getBoolean (AppConfigTags.VERSION_UPDATE)) {
                                            if (! userDetailsPref.getBooleanPref (SwiggyMainActivity.this, UserDetailsPref.LOGGED_IN_SESSION)) {
                                                new MaterialDialog.Builder (SwiggyMainActivity.this)
                                                        .content (R.string.dialog_text_new_version_available)
                                                        .positiveColor (getResources ().getColor (R.color.primary_text2))
                                                        .contentColor (getResources ().getColor (R.color.primary_text2))
                                                        .negativeColor (getResources ().getColor (R.color.primary_text2))
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