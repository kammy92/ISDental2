package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventDetailActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvTitle;
    RelativeLayout rlBack;
    TextView tvEventName;
    TextView tvEventWebsite;
    TextView tvEventDetail;
    TextView tvEventOrganiser;
    ProgressDialog progressDialog;
    ImageView ivWebsite;
    ImageView ivFacebook;
    ImageView ivLinkedin;
    ImageView ivTwitter;
    ImageView ivYoutube;
    LinearLayout llSocialButtons;
    LinearLayout llEventLinks;
    CoordinatorLayout clMain;
    int event_id;
    String eventWebsite;
    int organiser_id;
    List<String> event = new ArrayList<> ();
    Map<String, String> htmlMap = new HashMap<> ();
    ArrayList<String> tabNames = new ArrayList<> ();
    String TAB_DESCRIPTION = "DESCRIPTION";
    String TAB_SCHEDULE = "SCHEDULE";
    String TAB_INCLUSION = "INCLUSION";
    String TAB_FEES = "FEES";
    String TAB_CONTACT = "CONTACT";
    String TAB_VENUE = "VENUE";
    String TAB_FAQ = "FAQ";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_event_detail);
        initView ();
        getExtras ();
        initData ();
        initListener ();
        getEventDetailFromServer ();
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (this);
        tabLayout.setupWithViewPager (viewPager);
        collapsingToolbarLayout.setTitleEnabled (false);
        Utils.setTypefaceToAllViews (this, tvEventName);
    
        tabNames.add (0, TAB_SCHEDULE);
        tabNames.add (1, TAB_INCLUSION);
        tabNames.add (2, TAB_FEES);
        tabNames.add (3, TAB_CONTACT);
        tabNames.add (4, TAB_VENUE);
        tabNames.add (5, TAB_FAQ);
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        event_id = intent.getIntExtra (AppConfigTags.EVENT_ID, 0);
    }
    
    private void getEventDetailFromServer () {
        final ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager ());
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
                                        tvEventName.setText (jsonObj.getString (AppConfigTags.EVENT_NAME));
                                        tvTitle.setText (jsonObj.getString (AppConfigTags.EVENT_NAME));
    
                                        if (jsonObj.getString (AppConfigTags.EVENT_DESCRIPTION).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_DESCRIPTION);
                                        }
                                        if (jsonObj.getString (AppConfigTags.EVENT_FEES).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_FEES);
                                        }
                                        if (jsonObj.getString (AppConfigTags.EVENT_INCLUSIONS).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_INCLUSION);
                                        }
                                        if (jsonObj.getString (AppConfigTags.EVENT_SCHEDULE).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_SCHEDULE);
                                        }
                                        if (jsonObj.getString (AppConfigTags.EVENT_CONTACT_DETAILS).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_CONTACT);
                                        }
                                        if (jsonObj.getString (AppConfigTags.EVENT_VENUE).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_VENUE);
                                        }
                                        if (jsonObj.getString (AppConfigTags.EVENT_FAQ).length () > 0) {
                                            adapter.addFragment (new EventDetailFragment (), TAB_FAQ);
                                        }
    
    
                                        if (jsonObj.getString (AppConfigTags.EVENT_ORGANISER_NAME).length () > 0 && ! (jsonObj.getString (AppConfigTags.EVENT_ORGANISER_NAME).equalsIgnoreCase ("null"))) {
                                            tvEventOrganiser.setTextColor (getResources ().getColor (R.color.colorPrimaryDark));
                                            tvEventOrganiser.setEnabled (true);
                                            organiser_id = jsonObj.getInt (AppConfigTags.EVENT_ORGANISER_ID);
                                        } else {
                                            tvEventOrganiser.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.EVENT_WEBSITE).length () > 0) && ! (jsonObj.getString (AppConfigTags.EVENT_WEBSITE).equalsIgnoreCase ("null"))) {
                                            ivWebsite.setImageResource (R.drawable.ic_web);
                                            ivWebsite.setContentDescription (jsonObj.getString (AppConfigTags.EVENT_WEBSITE));
                                            ivWebsite.setEnabled (true);
                                            eventWebsite = jsonObj.getString (AppConfigTags.EVENT_WEBSITE);
                                            tvEventWebsite.setEnabled (true);
                                            tvEventWebsite.setTextColor (getResources ().getColor (R.color.colorPrimaryDark));
                                        } else {
                                            tvEventWebsite.setEnabled (false);
                                            ivWebsite.setImageResource (R.drawable.ic_website_disabled);
                                            ivWebsite.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.EVENT_FACEBOOK).length () > 0) && ! (jsonObj.getString (AppConfigTags.EVENT_FACEBOOK).equalsIgnoreCase ("null"))) {
                                            ivFacebook.setImageResource (R.drawable.ic_facebook);
                                            ivFacebook.setContentDescription (jsonObj.getString (AppConfigTags.EVENT_FACEBOOK));
                                            ivFacebook.setEnabled (true);
                                        } else {
                                            ivFacebook.setImageResource (R.drawable.ic_fb_disabled);
                                            ivFacebook.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.EVENT_TWITTER).length () > 0) && ! (jsonObj.getString (AppConfigTags.EVENT_TWITTER).equalsIgnoreCase ("null"))) {
                                            ivTwitter.setContentDescription (jsonObj.getString (AppConfigTags.EVENT_TWITTER));
                                            ivTwitter.setImageResource (R.drawable.ic_twitter);
                                            ivTwitter.setEnabled (true);
                                        } else {
                                            ivTwitter.setImageResource (R.drawable.ic_twitter_disabled);
                                            ivTwitter.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.EVENT_LINKEDIN).length () > 0) && ! (jsonObj.getString (AppConfigTags.EVENT_LINKEDIN).equalsIgnoreCase ("null"))) {
                                            ivLinkedin.setContentDescription (jsonObj.getString (AppConfigTags.EVENT_LINKEDIN));
                                            ivLinkedin.setImageResource (R.drawable.ic_linkedin);
                                            ivLinkedin.setEnabled (true);
                                        } else {
                                            ivLinkedin.setImageResource (R.drawable.ic_linkedin_disabled);
                                            ivLinkedin.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.EVENT_YOUTUBE).length () > 0) && ! (jsonObj.getString (AppConfigTags.EVENT_YOUTUBE).equalsIgnoreCase ("null"))) {
                                            ivYoutube.setImageResource (R.drawable.ic_youtube);
                                            ivYoutube.setEnabled (true);
                                            ivYoutube.setContentDescription (jsonObj.getString (AppConfigTags.EVENT_YOUTUBE));
                                        } else {
                                            ivYoutube.setImageResource (R.drawable.ic_youtube_disabled);
                                            ivYoutube.setEnabled (false);
                                        }
    
                                        tvEventDetail.setText (Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.EVENT_START_DATE), "yyyy-MM-dd", "dd/MM/yyyy") + " - " + Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.EVENT_END_DATE), "yyyy-MM-dd", "dd/MM/yyyy") + ", " + jsonObj.getString (AppConfigTags.EVENT_CITY));
    
                                        htmlMap.put (TAB_DESCRIPTION, jsonObj.getString (AppConfigTags.EVENT_DESCRIPTION));
                                        htmlMap.put (TAB_INCLUSION, jsonObj.getString (AppConfigTags.EVENT_INCLUSIONS));
                                        htmlMap.put (TAB_CONTACT, jsonObj.getString (AppConfigTags.EVENT_CONTACT_DETAILS));
                                        htmlMap.put (TAB_FEES, jsonObj.getString (AppConfigTags.EVENT_FEES));
                                        htmlMap.put (TAB_SCHEDULE, jsonObj.getString (AppConfigTags.EVENT_SCHEDULE));
                                        htmlMap.put (TAB_VENUE, jsonObj.getString (AppConfigTags.EVENT_VENUE));
                                        htmlMap.put (TAB_FAQ, jsonObj.getString (AppConfigTags.EVENT_FAQ));
    
                                        viewPager.setAdapter (adapter);
                                    }
                                    clMain.setVisibility (View.VISIBLE);
                                    llEventLinks.setVisibility (View.VISIBLE);
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
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (EventDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
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
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById (R.id.collapsing_toolbar);
        appBar = (AppBarLayout) findViewById (R.id.appbar);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvEventName = (TextView) findViewById (R.id.tvEventName);
        llEventLinks = (LinearLayout) findViewById (R.id.llEventLinks);
        tvEventOrganiser = (TextView) findViewById (R.id.tvEventOrganisers);
        tvEventWebsite = (TextView) findViewById (R.id.tvEventWebsite);
        
        tabLayout = (TabLayout) findViewById (R.id.tabs);
        viewPager = (ViewPager) findViewById (R.id.viewpager);
        
        llSocialButtons = (LinearLayout) findViewById (R.id.llSocialButtons);
        ivWebsite = (ImageView) findViewById (R.id.ivWebsite);
        ivFacebook = (ImageView) findViewById (R.id.ivFacebook);
        ivTwitter = (ImageView) findViewById (R.id.ivTwitter);
        ivLinkedin = (ImageView) findViewById (R.id.ivLinkedIn);
        ivYoutube = (ImageView) findViewById (R.id.ivYouTube);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    
        tvEventDetail = (TextView) findViewById (R.id.tvEventDetail);
    }
    
    private void initListener () {
        tvEventOrganiser.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
// Utils.showSnackBar (EventDetailActivity.this, clMain, "Coming Soon", Snackbar.LENGTH_SHORT, null, null);
                Intent intent = new Intent (EventDetailActivity.this, OrganiserDetailActivity.class);
                intent.putExtra (AppConfigTags.ORGANISER_ID, organiser_id);
                startActivity (intent);
            }
        });
        appBar.addOnOffsetChangedListener (new AppBarLayout.OnOffsetChangedListener () {
            @Override
            public void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //expanded
                    tvTitle.setVisibility (View.GONE);
                } else if (Math.abs (verticalOffset) >= appBarLayout.getTotalScrollRange ()) {
                    //collapsed
                    tvTitle.setVisibility (View.VISIBLE);
                } else {
                    //idle
                    tvTitle.setVisibility (View.GONE);
                }
            }
        });
    
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        tvEventWebsite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (eventWebsite.contains ("http://") || eventWebsite.contains ("https://")) {
                    uri = Uri.parse (eventWebsite);
                } else {
                    uri = Uri.parse ("http://" + eventWebsite);
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();
        
        public ViewPagerAdapter (FragmentManager manager) {
            super (manager);
        }
        
        @Override
        public Fragment getItem (int position) {
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_DESCRIPTION)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_DESCRIPTION));
            }
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_FEES)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_FEES));
            }
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_INCLUSION)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_INCLUSION));
            }
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_SCHEDULE)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_SCHEDULE));
            }
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_FAQ)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_FAQ));
            }
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_CONTACT)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_CONTACT));
            }
            if (mFragmentTitleList.get (position).equalsIgnoreCase (TAB_VENUE)) {
                return EventDetailFragment.newInstance (mFragmentTitleList.get (position), htmlMap.get (TAB_VENUE));
            }
    
            return EventDetailFragment.newInstance (mFragmentTitleList.get (position), TAB_FEES);
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
}
