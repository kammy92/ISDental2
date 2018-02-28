package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.MyAccountItemAdapter;
import com.indiasupply.isdental.dialog.EditProfileDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountAboutUsDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountEnquiriesDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountFavouritesDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountHelpSupportDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountOrderDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountPrivacyPolicyDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountSavingDialogFragment;
import com.indiasupply.isdental.dialog.MyAccountTermsofUseDialogFragment;
import com.indiasupply.isdental.model.MyAccountItem;
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

/**
 * Created by l on 23/02/2018.
 */

public class MyAccountActivity extends AppCompatActivity {
    RecyclerView rvMyAccount;
    RecyclerView rvHelp;
    TextView tvEdit;
    TextView tvAppVersion;
    CoordinatorLayout clMain;
    
    TextView tvUserName;
    TextView tvUserMobile;
    TextView tvUserEmail;
    TextView tvUserName2;
    TextView tvUserMobile2;
    TextView tvUserEmail2;
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    
    List<MyAccountItem> myAccountItemList = new ArrayList<> ();
    List<MyAccountItem> myHelpItemList = new ArrayList<> ();
    MyAccountItemAdapter myAccountItemAdapter;
    MyAccountItemAdapter myAccountItemAdapter2;
    
    String myFavourites = "";
    String myOrder = "";
    String myOffers = "";
    String myEnquiries = "";
    String htmlHelpSupport = "";
    String htmlAboutUs = "";
    String htmlTermsOfUse = "";
    String htmlPrivacyPolicy = "";
    
    AppDataPref appDataPref;
    
    boolean refresh;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.fragment_my_account);
        
        initView ();
        initData ();
        initListener ();
        
    }
    
    
    private void initView () {
        rvMyAccount = (RecyclerView) findViewById (R.id.rvMyAccount);
        rvHelp = (RecyclerView) findViewById (R.id.rvHelp);
        tvEdit = (TextView) findViewById (R.id.tvEdit);
        tvAppVersion = (TextView) findViewById (R.id.tvAppVersion);
        
        tvUserName = (TextView) findViewById (R.id.tvUserName);
        tvUserName2 = (TextView) findViewById (R.id.tvUserName2);
        tvUserMobile = (TextView) findViewById (R.id.tvUserMobile);
        tvUserMobile2 = (TextView) findViewById (R.id.tvUserMobile2);
        tvUserEmail = (TextView) findViewById (R.id.tvUserEmail);
        tvUserEmail2 = (TextView) findViewById (R.id.tvUserEmail2);
        
        tvEdit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                EditProfileDialogFragment frag1 = new EditProfileDialogFragment ();
                frag1.show (getFragmentManager ().beginTransaction (), "2");
            }
        });
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) findViewById (R.id.rlMain);
    }
    
    
    private void initData () {
        Utils.setTypefaceToAllViews (MyAccountActivity.this, rvMyAccount);
        appDataPref = AppDataPref.getInstance ();
        rvMyAccount.setNestedScrollingEnabled (false);
        PackageInfo pInfo = null;
        try {
            pInfo = MyAccountActivity.this.getPackageManager ().getPackageInfo (MyAccountActivity.this.getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        
        
        tvAppVersion.setText ("App Version " + pInfo.versionName + "(" + pInfo.versionCode + ")");
        
        
        myAccountItemAdapter = new MyAccountItemAdapter (MyAccountActivity.this, myAccountItemList);
        rvMyAccount.setAdapter (myAccountItemAdapter);
        rvMyAccount.setHasFixedSize (true);
        rvMyAccount.setNestedScrollingEnabled (false);
        rvMyAccount.setFocusable (false);
        rvMyAccount.setLayoutManager (new LinearLayoutManager (MyAccountActivity.this, LinearLayoutManager.VERTICAL, false));
        rvMyAccount.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (MyAccountActivity.this, 16), (int) Utils.pxFromDp (MyAccountActivity.this, 16), (int) Utils.pxFromDp (MyAccountActivity.this, 16), (int) Utils.pxFromDp (MyAccountActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
        myAccountItemAdapter2 = new MyAccountItemAdapter (MyAccountActivity.this, myHelpItemList);
        rvHelp.setAdapter (myAccountItemAdapter2);
        rvHelp.setNestedScrollingEnabled (false);
        rvHelp.setFocusable (false);
        rvHelp.setHasFixedSize (true);
        rvHelp.setLayoutManager (new LinearLayoutManager (MyAccountActivity.this, LinearLayoutManager.VERTICAL, false));
        rvHelp.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (MyAccountActivity.this, 16), (int) Utils.pxFromDp (MyAccountActivity.this, 16), (int) Utils.pxFromDp (MyAccountActivity.this, 16), (int) Utils.pxFromDp (MyAccountActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    
    
        myAccountItemList.add (new MyAccountItem (1, R.drawable.ic_my_account_favourites, "My Favourites", "", ""));
// myAccountItemList.add (new MyAccountItem (2, R.drawable.ic_my_account_offers, "My Offers", "", ""));
        myAccountItemList.add (new MyAccountItem (2, R.drawable.ic_my_account_inquiries, "My Enquiries", "", ""));
        myAccountItemList.add (new MyAccountItem (3, R.drawable.ic_my_account_inquiries, "My Orders", "", ""));
        myAccountItemList.add (new MyAccountItem (4, R.drawable.ic_my_account_inquiries, "My Savings", "", ""));
        
        myHelpItemList.add (new MyAccountItem (5, R.drawable.ic_my_account_help_support, "Help & Support", "", ""));
//        myHelpItemList.add (new MyAccountItem (6, R.drawable.ic_my_account_help_support, "FAQs", "", ""));
        myHelpItemList.add (new MyAccountItem (7, R.drawable.ic_my_account_terms_of_use, "Terms of Use", "", ""));
        myHelpItemList.add (new MyAccountItem (8, R.drawable.ic_my_account_privacy_policy, "Privacy Policy", "", ""));
        myAccountItemAdapter.notifyDataSetChanged ();

       /* if (! refresh) {
            showOfflineData ();
        }*/
    }
    
    private void initListener () {
        myAccountItemAdapter.SetOnItemClickListener (new MyAccountItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                MyAccountItem myAccountItem = myAccountItemList.get (position);
                android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                switch (myAccountItem.getId ()) {
                    case 1:
                        MyAccountFavouritesDialogFragment frag1 = MyAccountFavouritesDialogFragment.newInstance (myFavourites);
                        frag1.show (ft, "2");
                        break;
                    case 2:
                        MyAccountEnquiriesDialogFragment frag3 = MyAccountEnquiriesDialogFragment.newInstance (myEnquiries);
                        frag3.show (ft, "2");
                        break;
                    case 3:
                        MyAccountOrderDialogFragment frag4 = MyAccountOrderDialogFragment.newInstance (myOrder);
                        frag4.show (ft, "3");
                        break;
                    case 4:
                        MyAccountSavingDialogFragment frag5 = MyAccountSavingDialogFragment.newInstance (myOrder);
                        frag5.show (ft, "4");
                        break;
                }
            }
        });
        
        myAccountItemAdapter2.SetOnItemClickListener (new MyAccountItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                MyAccountItem myAccountItem = myHelpItemList.get (position);
                android.app.FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                switch (myAccountItem.getId ()) {
                    case 5:
                        MyAccountHelpSupportDialogFragment frag5 = MyAccountHelpSupportDialogFragment.newInstance (htmlHelpSupport);
                        frag5.show (ft, "2");
                        break;
                    case 6:
                        MyAccountAboutUsDialogFragment frag6 = MyAccountAboutUsDialogFragment.newInstance (htmlAboutUs);
                        frag6.show (ft, "2");
                        break;
                    case 7:
                        MyAccountTermsofUseDialogFragment frag7 = MyAccountTermsofUseDialogFragment.newInstance (htmlTermsOfUse);
                        frag7.show (ft, "2");
                        break;
                    case 8:
                        MyAccountPrivacyPolicyDialogFragment frag8 = MyAccountPrivacyPolicyDialogFragment.newInstance (htmlPrivacyPolicy);
                        frag8.show (ft, "2");
                        break;
                }
            }
        });
    }
    
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (MyAccountActivity.this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_HOME_ACCOUNT, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_HOME_ACCOUNT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            //  if (MyAccountActivity.this != null && isAdded ()) {
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        appDataPref.putStringPref (MyAccountActivity.this, AppDataPref.HOME_ACCOUNT, response);
                                        tvUserName.setText (jsonObj.getString (AppConfigTags.USER_NAME).toUpperCase ());
                                        tvUserEmail.setText (jsonObj.getString (AppConfigTags.USER_EMAIL));
                                        tvUserMobile.setText (jsonObj.getString (AppConfigTags.USER_MOBILE));
    
                                        myFavourites = jsonObj.getJSONArray (AppConfigTags.FAVOURITES).toString ();
                                        myOffers = jsonObj.getJSONArray (AppConfigTags.OFFERS).toString ();
                                        myEnquiries = jsonObj.getJSONArray (AppConfigTags.ENQUIRIES).toString ();
                                        myOrder = jsonObj.getJSONArray (AppConfigTags.ORDERS).toString ();
    
                                        htmlPrivacyPolicy = jsonObj.getString (AppConfigTags.HTML_PRIVACY_POLICY);
                                        htmlAboutUs = jsonObj.getString (AppConfigTags.HTML_ABOUT_US);
                                        htmlTermsOfUse = jsonObj.getString (AppConfigTags.HTML_TERMS_OF_USE);
                                        htmlHelpSupport = jsonObj.getString (AppConfigTags.HTML_HELP_AND_SUPPORT);
                                        rlMain.setVisibility (View.VISIBLE);
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                    } else {
                                        if (! showOfflineData ()) {
                                            Utils.showSnackBar (MyAccountActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    if (! showOfflineData ()) {
                                        Utils.showSnackBar (MyAccountActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
                                }
                            } else {
                                if (! showOfflineData ()) {
                                    Utils.showSnackBar (MyAccountActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                        // }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            //   if (MyAccountActivity.this != null && isAdded ()) {
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            if (! showOfflineData ()) {
                                Utils.showSnackBar (MyAccountActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            }
                            // }
                        }
                    }) {
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (MyAccountActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 30);
        } else {
            // if (MyAccountActivity.this != null && isAdded ()) {
            if (! showOfflineData ()) {
                Utils.showSnackBar (MyAccountActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity (dialogIntent);
                    }
                });
            }
        }
        //  }
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
        String response = appDataPref.getStringPref (MyAccountActivity.this, AppDataPref.HOME_ACCOUNT);
        if (response.length () > 0) {
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    tvUserName.setText (jsonObj.getString (AppConfigTags.USER_NAME).toUpperCase ());
                    tvUserEmail.setText (jsonObj.getString (AppConfigTags.USER_EMAIL));
                    tvUserMobile.setText (jsonObj.getString (AppConfigTags.USER_MOBILE));
    
                    myFavourites = jsonObj.getJSONArray (AppConfigTags.FAVOURITES).toString ();
                    myOffers = jsonObj.getJSONArray (AppConfigTags.OFFERS).toString ();
                    myEnquiries = jsonObj.getJSONArray (AppConfigTags.ENQUIRIES).toString ();
                    htmlPrivacyPolicy = jsonObj.getString (AppConfigTags.HTML_PRIVACY_POLICY);
                    htmlAboutUs = jsonObj.getString (AppConfigTags.HTML_ABOUT_US);
                    htmlTermsOfUse = jsonObj.getString (AppConfigTags.HTML_TERMS_OF_USE);
                    htmlHelpSupport = jsonObj.getString (AppConfigTags.HTML_HELP_AND_SUPPORT);
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
