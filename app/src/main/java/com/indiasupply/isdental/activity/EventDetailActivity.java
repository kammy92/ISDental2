package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.fragment.EventDetailFragment;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.EventDetailsPref;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventDetailActivity extends AppCompatActivity {
    ImageView ivBack;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvTitle;
    RelativeLayout rlBack;
    ImageView ivEventLogo;
    TextView tvEventName;
    TextView tvEventWebsite;
    TextView tvEventOrganiser;
    TextView tvEventRegistration;
    ProgressDialog progressDialog;
    ImageView ivFacebook;
    ImageView ivLinkedin;
    ImageView ivTwitter;
    ImageView ivPinterest;
    ImageView ivInstagram;
    ImageView ivYoutube;
    ImageView ivGooglePlus;
    LinearLayout llSocialButtons;
    LinearLayout llEventLinks;
    CoordinatorLayout clMain;
    EventDetailsPref eventDetailsPref;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    int event_id;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_event_detail);
        initView ();
        getExtras ();
        initData ();
        getEventDetailFromServer ();
    }
    
    private void initData () {
        eventDetailsPref = EventDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        tabLayout.setupWithViewPager (viewPager);
        collapsingToolbarLayout.setTitleEnabled (false);
        Utils.setTypefaceToAllViews (this, tvEventName);
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        event_id = intent.getIntExtra (AppConfigTags.EVENT_ID, 0);
    }
    
    private void getEventDetailFromServer () {
        if (NetworkConnection.isNetworkAvailable (EventDetailActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_EVENT_DETAILS + "/" + event_id, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_EVENT_DETAILS + "/" + event_id,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
    
                                        tvEventName.setText (eventDetailsPref.getStringPref (EventDetailActivity.this, AppConfigTags.EVENT_NAME));
                                        tvEventWebsite.setText ("Website");
                                        tvEventOrganiser.setText ("Organiser");
                                        tvEventRegistration.setText ("Registration");
    
                                        eventDetailsPref.putIntPref (EventDetailActivity.this, EventDetailsPref.EVENT_ID, jsonObj.getInt (AppConfigTags.EVENT_ID));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_NAME, jsonObj.getString (AppConfigTags.EVENT_NAME));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_START_DATE, jsonObj.getString (AppConfigTags.EVENT_START_DATE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_END_DATE, jsonObj.getString (AppConfigTags.EVENT_END_DATE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_FAQ, jsonObj.getString (AppConfigTags.EVENT_FAQ));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_FEES, jsonObj.getString (AppConfigTags.EVENT_FEES));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_SCHEDULE, jsonObj.getString (AppConfigTags.EVENT_SCHEDULE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_VENUE, jsonObj.getString (AppConfigTags.EVENT_VENUE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_CITY, jsonObj.getString (AppConfigTags.EVENT_CITY));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_PINCODE, jsonObj.getString (AppConfigTags.EVENT_PINCODE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_LATITUDE, jsonObj.getString (AppConfigTags.EVENT_LATITUDE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_LONGITUDE, jsonObj.getString (AppConfigTags.EVENT_LONGITUDE));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_INCLUSIONS, jsonObj.getString (AppConfigTags.EVENT_INCLUSIONS));
                                        eventDetailsPref.putStringPref (EventDetailActivity.this, EventDetailsPref.EVENT_CONTACT_DETAILS, jsonObj.getString (AppConfigTags.EVENT_CONTACT_DETAILS));
    
                                        setupViewPager (viewPager);
                                    }
                                    clMain.setVisibility (View.VISIBLE);
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    clMain.setVisibility (View.VISIBLE);
                                    Utils.showSnackBar (EventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                clMain.setVisibility (View.VISIBLE);
                                Utils.showSnackBar (EventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            clMain.setVisibility (View.VISIBLE);
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            clMain.setVisibility (View.VISIBLE);
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (EventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, "610efa7d4477e9461dc63816bff7ff26");
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            clMain.setVisibility (View.VISIBLE);
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
        
    }
    
    private void initView () {
        ivBack = (ImageView) findViewById (R.id.ivBack);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById (R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById (R.id.appbar);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvEventName = (TextView) findViewById (R.id.tvEventName);
        llEventLinks = (LinearLayout) findViewById (R.id.llEventLinks);
        tvEventWebsite = (TextView) findViewById (R.id.tvEventWebsite);
        tvEventOrganiser = (TextView) findViewById (R.id.tvEventOrganisers);
        tvEventRegistration = (TextView) findViewById (R.id.tvEventRegistration);
        ivEventLogo = (ImageView) findViewById (R.id.ivEventLogo);
        
        tabLayout = (TabLayout) findViewById (R.id.tabs);
        viewPager = (ViewPager) findViewById (R.id.viewpager);
        
        llSocialButtons = (LinearLayout) findViewById (R.id.llSocialButtons);
        ivFacebook = (ImageView) findViewById (R.id.ivFacebook);
        ivTwitter = (ImageView) findViewById (R.id.ivTwitter);
        ivLinkedin = (ImageView) findViewById (R.id.ivLinkedIn);
        ivInstagram = (ImageView) findViewById (R.id.ivInstagram);
        ivGooglePlus = (ImageView) findViewById (R.id.ivGooglePlus);
        ivYoutube = (ImageView) findViewById (R.id.ivYouTube);
        ivPinterest = (ImageView) findViewById (R.id.ivPinterest);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    }
    
    private void setupViewPager (ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager ());
        if (eventDetailsPref.getStringPref (EventDetailActivity.this, eventDetailsPref.EVENT_FAQ).length () > 0) {
            adapter.addFragment (new EventDetailFragment (), "FAQ");
        }
        if (eventDetailsPref.getStringPref (EventDetailActivity.this, eventDetailsPref.EVENT_FEES).length () > 0) {
            adapter.addFragment (new EventDetailFragment (), "FEES");
        }
        if (eventDetailsPref.getStringPref (EventDetailActivity.this, eventDetailsPref.EVENT_SCHEDULE).length () > 0) {
            adapter.addFragment (new EventDetailFragment (), "SCHEDULE");
        }
        if (eventDetailsPref.getStringPref (EventDetailActivity.this, eventDetailsPref.EVENT_VENUE).length () > 0) {
            adapter.addFragment (new EventDetailFragment (), "VENUE");
        }
        if (eventDetailsPref.getStringPref (EventDetailActivity.this, eventDetailsPref.EVENT_INCLUSIONS).length () > 0) {
            adapter.addFragment (new EventDetailFragment (), "INCLUSIONS");
        }
        if (eventDetailsPref.getStringPref (EventDetailActivity.this, eventDetailsPref.EVENT_CONTACT_DETAILS).length () > 0) {
            adapter.addFragment (new EventDetailFragment (), "CONTACT");
        }
        viewPager.setAdapter (adapter);
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();
        
        public ViewPagerAdapter (FragmentManager manager) {
            super (manager);
        }
        
        @Override
        public Fragment getItem (int position) {
            return EventDetailFragment.newInstance (mFragmentTitleList.get (position));
        }
        
        @Override
        public int getCount () {
            return mFragmentList.size ();
        }
        
        public void addFragment (Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }
        
        @Override
        public CharSequence getPageTitle (int position) {
            
            return mFragmentTitleList.get (position);
        }
    }

    /*class ViewPagerAdapter extends FragmentPagerAdapter {
        public final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return EventDetailFragment.newInstance(String.valueOf(position));
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }*/
}
