package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
import com.indiasupply.isdental.fragment.SwiggyFeaturedFragment;
import com.indiasupply.isdental.fragment.SwiggyMyAccountFragment;
import com.indiasupply.isdental.fragment.SwiggyServiceFragment;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;
import com.stephentuso.welcome.WelcomeHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class SwiggyMainActivity extends AppCompatActivity {
    private static final int REQUEST_WELCOME_SCREEN_RESULT = 13;
    CoordinatorLayout clMain;
    BottomNavigationView bottomNavigationView;
    UserDetailsPref userDetailsPref;
    private WelcomeHelper sampleWelcomeScreen;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activtiy_swiggy_main);
        initView ();
        initData ();
        initListener ();
        initApplication ();
    
        Intent intent = new Intent (SwiggyMainActivity.this, SwiggyLoginActivity.class);
        startActivity (intent);
    
        // The welcome screen for this app (only one that automatically shows)
//        sampleWelcomeScreen = new WelcomeHelper (this, SwiggyIntroActivity.class);
//        sampleWelcomeScreen.show (savedInstanceState);
//        sampleWelcomeScreen.forceShow (REQUEST_WELCOME_SCREEN_RESULT);
    
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        bottomNavigationView = (BottomNavigationView) findViewById (R.id.navigation);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, clMain);
        Utils.disableShiftMode (bottomNavigationView);
        userDetailsPref = UserDetailsPref.getInstance ();
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
        transaction.replace (R.id.frame_layout, SwiggyFeaturedFragment.newInstance ());
        transaction.commit ();
        
        Menu menu = bottomNavigationView.getMenu ();
        menu.findItem (R.id.action_item1).setIcon (R.drawable.ic_home_featured);
    }
    
    private void initListener () {
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener () {
                    @Override
                    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
    
                        Menu menu = bottomNavigationView.getMenu ();
                        menu.findItem (R.id.action_item1).setIcon (R.drawable.ic_home_featured);
                        menu.findItem (R.id.action_item2).setIcon (R.drawable.ic_home_events);
                        menu.findItem (R.id.action_item3).setIcon (R.drawable.ic_home_contacts);
                        menu.findItem (R.id.action_item4).setIcon (R.drawable.ic_home_services);
                        menu.findItem (R.id.action_item5).setIcon (R.drawable.ic_home_account);
    
                        switch (item.getItemId ()) {
                            case R.id.action_item1:
                                item.setIcon (R.drawable.ic_home_featured_filled);
                                selectedFragment = SwiggyFeaturedFragment.newInstance ();
                                break;
                            case R.id.action_item2:
                                item.setIcon (R.drawable.ic_home_events_filled);
                                selectedFragment = SwiggyEventFragment.newInstance ();
                                break;
                            case R.id.action_item3:
                                item.setIcon (R.drawable.ic_home_contacts_filled);
                                selectedFragment = SwiggyContactsFragment.newInstance ();
                                break;
                            case R.id.action_item4:
                                item.setIcon (R.drawable.ic_home_services_filled);
                                selectedFragment = SwiggyServiceFragment.newInstance ();
                                break;
                            case R.id.action_item5:
                                item.setIcon (R.drawable.ic_home_account_filled);
                                selectedFragment = SwiggyMyAccountFragment.newInstance ();
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
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        
        if (requestCode == REQUEST_WELCOME_SCREEN_RESULT) {
            
            if (resultCode == RESULT_OK) {
                Toast.makeText (getApplicationContext (), "Completed (RESULT_OK)", Toast.LENGTH_SHORT).show ();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText (getApplicationContext (), "Canceled (RESULT_CANCELED)", Toast.LENGTH_SHORT).show ();
            }
            
        }
        
    }
    
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        // This is needed to prevent welcome screens from being
        // automatically shown multiple times
        
        // This is the only one needed because it is the only one that
        // is shown automatically. The others are only force shown.
//        sampleWelcomeScreen.onSaveInstanceState (outState);
    }
}