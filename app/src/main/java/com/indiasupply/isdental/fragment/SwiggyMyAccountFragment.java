package com.indiasupply.isdental.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyMyAccountItemAdapter;
import com.indiasupply.isdental.dialog.SwiggyEditProfileDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountAboutUsDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountEnquiriesDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountFavouritesDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountHelpSupportDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountOffersDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountPrivacyPolicyDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyMyAccountTermsofUseDialogFragment;
import com.indiasupply.isdental.model.SwiggyMyAccountItem;
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

public class SwiggyMyAccountFragment extends Fragment {
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
    
    
    List<SwiggyMyAccountItem> myAccountItemList = new ArrayList<> ();
    List<SwiggyMyAccountItem> myHelpItemList = new ArrayList<> ();
    SwiggyMyAccountItemAdapter myAccountItemAdapter;
    SwiggyMyAccountItemAdapter myAccountItemAdapter2;
    
    String myFavourites = "";
    String myOffers = "";
    String myEnquiries = "";
    String htmlHelpSupport = "";
    String htmlAboutUs = "";
    String htmlTermsOfUse = "";
    String htmlPrivacyPolicy = "";
    
    AppDataPref appDataPref;
    
    public static SwiggyMyAccountFragment newInstance () {
        SwiggyMyAccountFragment fragment = new SwiggyMyAccountFragment ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_my_account, container, false);
        initView (rootView);
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvMyAccount = (RecyclerView) rootView.findViewById (R.id.rvMyAccount);
        rvHelp = (RecyclerView) rootView.findViewById (R.id.rvHelp);
        tvEdit = (TextView) rootView.findViewById (R.id.tvEdit);
        tvAppVersion = (TextView) rootView.findViewById (R.id.tvAppVersion);
    
        tvUserName = (TextView) rootView.findViewById (R.id.tvUserName);
        tvUserName2 = (TextView) rootView.findViewById (R.id.tvUserName2);
        tvUserMobile = (TextView) rootView.findViewById (R.id.tvUserMobile);
        tvUserMobile2 = (TextView) rootView.findViewById (R.id.tvUserMobile2);
        tvUserEmail = (TextView) rootView.findViewById (R.id.tvUserEmail);
        tvUserEmail2 = (TextView) rootView.findViewById (R.id.tvUserEmail2);
    
        tvEdit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SwiggyEditProfileDialogFragment frag1 = new SwiggyEditProfileDialogFragment ();
                frag1.show (getActivity ().getFragmentManager ().beginTransaction (), "2");
            }
        });
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
    
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvMyAccount);
        appDataPref = AppDataPref.getInstance ();
        rvMyAccount.setNestedScrollingEnabled (false);
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity ().getPackageManager ().getPackageInfo (getActivity ().getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        
        
        tvAppVersion.setText ("App Version " + pInfo.versionName + "(" + pInfo.versionCode + ")");
        
        
        myAccountItemAdapter = new SwiggyMyAccountItemAdapter (getActivity (), myAccountItemList);
        rvMyAccount.setAdapter (myAccountItemAdapter);
        rvMyAccount.setHasFixedSize (true);
        rvMyAccount.setNestedScrollingEnabled (false);
        rvMyAccount.setFocusable (false);
        rvMyAccount.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyAccount.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    
        myAccountItemAdapter2 = new SwiggyMyAccountItemAdapter (getActivity (), myHelpItemList);
        rvHelp.setAdapter (myAccountItemAdapter2);
        rvHelp.setNestedScrollingEnabled (false);
        rvHelp.setFocusable (false);
        rvHelp.setHasFixedSize (true);
        rvHelp.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvHelp.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        myAccountItemAdapter.SetOnItemClickListener (new SwiggyMyAccountItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyMyAccountItem swiggyMyAccountItem = myAccountItemList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                switch (swiggyMyAccountItem.getId ()) {
                    case 1:
                        SwiggyMyAccountFavouritesDialogFragment frag1 = SwiggyMyAccountFavouritesDialogFragment.newInstance (myFavourites);
                        frag1.show (ft, "2");
                        break;
                    case 2:
                        SwiggyMyAccountOffersDialogFragment frag2 = SwiggyMyAccountOffersDialogFragment.newInstance (myOffers);
                        frag2.show (ft, "2");
                        break;
                    case 3:
                        SwiggyMyAccountEnquiriesDialogFragment frag3 = SwiggyMyAccountEnquiriesDialogFragment.newInstance (myEnquiries);
                        frag3.show (ft, "2");
                        break;
                }
            }
        });
    
        myAccountItemAdapter2.SetOnItemClickListener (new SwiggyMyAccountItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyMyAccountItem swiggyMyAccountItem = myHelpItemList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                switch (swiggyMyAccountItem.getId ()) {
                    case 5:
                        SwiggyMyAccountHelpSupportDialogFragment frag5 = SwiggyMyAccountHelpSupportDialogFragment.newInstance (htmlHelpSupport);
                        frag5.show (ft, "2");
                        break;
                    case 6:
                        SwiggyMyAccountAboutUsDialogFragment frag6 = SwiggyMyAccountAboutUsDialogFragment.newInstance (htmlAboutUs);
                        frag6.show (ft, "2");
                        break;
                    case 7:
                        SwiggyMyAccountTermsofUseDialogFragment frag7 = SwiggyMyAccountTermsofUseDialogFragment.newInstance (htmlTermsOfUse);
                        frag7.show (ft, "2");
                        break;
                    case 8:
                        SwiggyMyAccountPrivacyPolicyDialogFragment frag8 = SwiggyMyAccountPrivacyPolicyDialogFragment.newInstance (htmlPrivacyPolicy);
                        frag8.show (ft, "2");
                        break;
                }
            }
        });
    }
    
    private void setData () {
        myAccountItemList.add (new SwiggyMyAccountItem (1, R.drawable.ic_my_account_favourites, "My Favourites", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (2, R.drawable.ic_my_account_offers, "My Offers", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (3, R.drawable.ic_my_account_inquiries, "My Enquiries", "", ""));
    
        myHelpItemList.add (new SwiggyMyAccountItem (5, R.drawable.ic_my_account_help_support, "Help & Support", "", ""));
//        myHelpItemList.add (new SwiggyMyAccountItem (6, R.drawable.ic_my_account_help_support, "About Us", "", ""));
        myHelpItemList.add (new SwiggyMyAccountItem (7, R.drawable.ic_my_account_terms_of_use, "Terms of Use", "", ""));
        myHelpItemList.add (new SwiggyMyAccountItem (8, R.drawable.ic_my_account_privacy_policy, "Privacy Policy", "", ""));
        
        myAccountItemAdapter.notifyDataSetChanged ();
    
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_HOME_ACCOUNT, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_HOME_ACCOUNT,
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
                                            appDataPref.putStringPref (getActivity (), AppDataPref.HOME_ACCOUNT, response);
                                            tvUserName.setText (jsonObj.getString (AppConfigTags.USER_NAME).toUpperCase ());
                                            tvUserEmail.setText (jsonObj.getString (AppConfigTags.USER_EMAIL));
                                            tvUserMobile.setText (jsonObj.getString (AppConfigTags.USER_MOBILE));
    
                                            myFavourites = jsonObj.getJSONArray (AppConfigTags.SWIGGY_FAVOURITES).toString ();
                                            myOffers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_OFFERS).toString ();
                                            myEnquiries = jsonObj.getJSONArray (AppConfigTags.SWIGGY_ENQUIRIES).toString ();
                                            htmlPrivacyPolicy = jsonObj.getString (AppConfigTags.SWIGGY_HTML_PRIVACY_POLICY);
                                            htmlAboutUs = jsonObj.getString (AppConfigTags.SWIGGY_HTML_ABOUT_US);
                                            htmlTermsOfUse = jsonObj.getString (AppConfigTags.SWIGGY_HTML_TERMS_OF_USE);
                                            htmlHelpSupport = jsonObj.getString (AppConfigTags.SWIGGY_HTML_HELP_AND_SUPPORT);
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
        String response = appDataPref.getStringPref (getActivity (), AppDataPref.HOME_ACCOUNT);
        if (response.length () > 0) {
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    tvUserName.setText (jsonObj.getString (AppConfigTags.USER_NAME).toUpperCase ());
                    tvUserEmail.setText (jsonObj.getString (AppConfigTags.USER_EMAIL));
                    tvUserMobile.setText (jsonObj.getString (AppConfigTags.USER_MOBILE));
                    
                    myFavourites = jsonObj.getJSONArray (AppConfigTags.SWIGGY_FAVOURITES).toString ();
                    myOffers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_OFFERS).toString ();
                    myEnquiries = jsonObj.getJSONArray (AppConfigTags.SWIGGY_ENQUIRIES).toString ();
                    htmlPrivacyPolicy = jsonObj.getString (AppConfigTags.SWIGGY_HTML_PRIVACY_POLICY);
                    htmlAboutUs = jsonObj.getString (AppConfigTags.SWIGGY_HTML_ABOUT_US);
                    htmlTermsOfUse = jsonObj.getString (AppConfigTags.SWIGGY_HTML_TERMS_OF_USE);
                    htmlHelpSupport = jsonObj.getString (AppConfigTags.SWIGGY_HTML_HELP_AND_SUPPORT);
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