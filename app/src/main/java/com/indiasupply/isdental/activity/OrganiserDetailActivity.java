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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.EventListAdapter;
import com.indiasupply.isdental.model.Event;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class OrganiserDetailActivity extends AppCompatActivity {
    ImageView ivBack;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    TextView tvTitle;
    RelativeLayout rlBack;
    TextView tvOrganiserName;
    ProgressDialog progressDialog;
    ImageView ivWebsite;
    ImageView ivFacebook;
    ImageView ivLinkedin;
    ImageView ivTwitter;
    ImageView ivYoutube;
    LinearLayout llSocialButtons;
    LinearLayout llOrganiserLinks;
    CoordinatorLayout clMain;
    String organiserWebsite;
    int organiser_id;
    
    Button btShowMore;
    boolean show = true;
    
    
    RecyclerView rvUpcoming;
    RecyclerView rvPast;
    
    List<Event> organiserUpcomingList = new ArrayList<> ();
    List<Event> organiserPastList = new ArrayList<> ();
    EventListAdapter eventListAdapter;
    TextView tvUpcomingNoResultFound;
    TextView tvPastNoResultFound;
    TextView tvDescription;
    CardView cardView1;
    
    WebView webViewDescription;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_organiser_detail);
        initView ();
        getExtras ();
        initData ();
        initListener ();
        getOrganiserDetailFromServer ();
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (this);
        collapsingToolbarLayout.setTitleEnabled (false);
        Utils.setTypefaceToAllViews (this, tvOrganiserName);
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        organiser_id = intent.getIntExtra (AppConfigTags.ORGANISER_ID, 0);
    }
    
    private void getOrganiserDetailFromServer () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_ORGANISER_DETAILS + "/" + organiser_id, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_ORGANISER_DETAILS + "/" + organiser_id,
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
                                        tvOrganiserName.setText (jsonObj.getString (AppConfigTags.ORGANISER_NAME));
                                        tvTitle.setText (jsonObj.getString (AppConfigTags.ORGANISER_NAME));
    
                                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + jsonObj.getString (AppConfigTags.ORGANISER_DESCRIPTION));
                                        webViewDescription.loadDataWithBaseURL ("www.google.com", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
                                        WebSettings webSettings = webViewDescription.getSettings ();
                                        webSettings.setStandardFontFamily (Constants.font_name);
                             
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.PAST_EVENT);
                                        if (jsonArray.length () > 0) {
                                            for (int i = 0; i < jsonArray.length (); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject (i);
                                                organiserPastList.add (i, new Event (
                                                        jsonObject.getInt (AppConfigTags.EVENT_ID),
                                                        jsonObject.getString (AppConfigTags.EVENT_NAME),
                                                        jsonObject.getString (AppConfigTags.EVENT_START_DATE),
                                                        jsonObject.getString (AppConfigTags.EVENT_END_DATE),
                                                        jsonObject.getString (AppConfigTags.EVENT_TYPE),
                                                        jsonObject.getString (AppConfigTags.EVENT_CITY),
                                                        "N/A"
                                                ));
                                            }
                                            eventListAdapter = new EventListAdapter (OrganiserDetailActivity.this, organiserPastList);
                                            rvPast.setAdapter (eventListAdapter);
                                            rvPast.setHasFixedSize (true);
                                            rvPast.setLayoutManager (new LinearLayoutManager (OrganiserDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                                            rvPast.setItemAnimator (new DefaultItemAnimator ());
                                        } else {
                                            tvPastNoResultFound.setVisibility (View.VISIBLE);
                                            rvPast.setVisibility (View.GONE);
                                        }
                
                                        JSONArray jsonArrayUpcoming = jsonObj.getJSONArray (AppConfigTags.UPCOMING_EVENT);
                                        if (jsonArrayUpcoming.length () > 0) {
                                            for (int i = 0; i < jsonArrayUpcoming.length (); i++) {
                                                JSONObject jsonObjectUpcoming = jsonArrayUpcoming.getJSONObject (i);
                                                organiserUpcomingList.add (i, new Event (
                                                        jsonObjectUpcoming.getInt (AppConfigTags.EVENT_ID),
                                                        jsonObjectUpcoming.getString (AppConfigTags.EVENT_NAME),
                                                        jsonObjectUpcoming.getString (AppConfigTags.EVENT_START_DATE),
                                                        jsonObjectUpcoming.getString (AppConfigTags.EVENT_END_DATE),
                                                        jsonObjectUpcoming.getString (AppConfigTags.EVENT_TYPE),
                                                        jsonObjectUpcoming.getString (AppConfigTags.EVENT_CITY),
                                                        "N/A"
                                                ));
                                            }
                                            eventListAdapter = new EventListAdapter (OrganiserDetailActivity.this, organiserUpcomingList);
                                            rvUpcoming.setAdapter (eventListAdapter);
                                            rvUpcoming.setHasFixedSize (true);
                                            rvUpcoming.setLayoutManager (new LinearLayoutManager (OrganiserDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                                            rvUpcoming.setItemAnimator (new DefaultItemAnimator ());
                                        } else {
                                            tvUpcomingNoResultFound.setVisibility (View.VISIBLE);
                                            rvUpcoming.setVisibility (View.GONE);
                                        }
                
                
                                        if ((jsonObj.getString (AppConfigTags.ORGANISER_WEBSITE).length ()) > 0 && ! (jsonObj.getString (AppConfigTags.ORGANISER_WEBSITE).equalsIgnoreCase ("null"))) {
                                            ivWebsite.setImageResource (R.drawable.ic_web);
                                            ivWebsite.setContentDescription (jsonObj.getString (AppConfigTags.ORGANISER_WEBSITE));
                                            ivWebsite.setEnabled (true);
                                        } else {
                                            ivWebsite.setImageResource (R.drawable.ic_website_disabled);
                                            ivWebsite.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.ORGANISER_FACEBOOK).length () > 0) && ! (jsonObj.getString (AppConfigTags.ORGANISER_FACEBOOK).equalsIgnoreCase ("null"))) {
                                            ivFacebook.setImageResource (R.drawable.ic_facebook);
                                            ivFacebook.setContentDescription (jsonObj.getString (AppConfigTags.ORGANISER_FACEBOOK));
                                            ivFacebook.setEnabled (true);
                                        } else {
                                            ivFacebook.setImageResource (R.drawable.ic_fb_disabled);
                                            ivFacebook.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.ORGANISER_TWITTER).length () > 0) && ! (jsonObj.getString (AppConfigTags.ORGANISER_TWITTER).equalsIgnoreCase ("null"))) {
                                            ivTwitter.setContentDescription (jsonObj.getString (AppConfigTags.ORGANISER_TWITTER));
                                            ivTwitter.setImageResource (R.drawable.ic_twitter);
                                            ivTwitter.setEnabled (true);
                                        } else {
                                            ivTwitter.setImageResource (R.drawable.ic_twitter_disabled);
                                            ivTwitter.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.ORGANISER_LINKEDIN).length () > 0) && ! (jsonObj.getString (AppConfigTags.ORGANISER_LINKEDIN).equalsIgnoreCase ("null"))) {
                                            ivLinkedin.setContentDescription (jsonObj.getString (AppConfigTags.ORGANISER_LINKEDIN));
                                            ivLinkedin.setImageResource (R.drawable.ic_linkedin);
                                            ivLinkedin.setEnabled (true);
                                        } else {
                                            ivLinkedin.setImageResource (R.drawable.ic_linkedin_disabled);
                                            ivLinkedin.setEnabled (false);
                                        }
                                        if ((jsonObj.getString (AppConfigTags.ORGANISER_YOUTUBE).length () > 0) && ! (jsonObj.getString (AppConfigTags.ORGANISER_YOUTUBE).equalsIgnoreCase ("null"))) {
                                            ivYoutube.setImageResource (R.drawable.ic_youtube);
                                            ivYoutube.setEnabled (true);
                                            ivYoutube.setContentDescription (jsonObj.getString (AppConfigTags.ORGANISER_YOUTUBE));
                                        } else {
                                            ivYoutube.setImageResource (R.drawable.ic_youtube_disabled);
                                            ivYoutube.setEnabled (false);
                                        }
                                    }
                                    llSocialButtons.setVisibility (View.VISIBLE);
                                    clMain.setVisibility (View.VISIBLE);
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    clMain.setVisibility (View.VISIBLE);
                                    Utils.showSnackBar (OrganiserDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                clMain.setVisibility (View.VISIBLE);
                                Utils.showSnackBar (OrganiserDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (OrganiserDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (OrganiserDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
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
        tvDescription = (TextView) findViewById (R.id.tvDescription);
        tvOrganiserName = (TextView) findViewById (R.id.tvOrganiserName);
        llOrganiserLinks = (LinearLayout) findViewById (R.id.llOrganiserLinks);
        
        llSocialButtons = (LinearLayout) findViewById (R.id.llSocialButtons);
        ivWebsite = (ImageView) findViewById (R.id.ivWebsite);
        ivFacebook = (ImageView) findViewById (R.id.ivFacebook);
        ivTwitter = (ImageView) findViewById (R.id.ivTwitter);
        ivLinkedin = (ImageView) findViewById (R.id.ivLinkedIn);
        ivYoutube = (ImageView) findViewById (R.id.ivYouTube);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        
        rvUpcoming = (RecyclerView) findViewById (R.id.rvUpcoming);
        rvPast = (RecyclerView) findViewById (R.id.rvPast);
        tvUpcomingNoResultFound = (TextView) findViewById (R.id.tvUpcomingNoResultFound);
        tvPastNoResultFound = (TextView) findViewById (R.id.tvPastNoResultFound);
        webViewDescription = (WebView) findViewById (R.id.webViewDescription);
        cardView1 = (CardView) findViewById (R.id.cardView1);
        btShowMore = (Button) findViewById (R.id.btShowMore);
    }
    
    private void initListener () {
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
        
        ivWebsite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivWebsite.getContentDescription ().toString ().contains ("http://") || ivWebsite.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivWebsite.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivWebsite.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivFacebook.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivFacebook.getContentDescription ().toString ().contains ("http://") || ivFacebook.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivFacebook.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivFacebook.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivTwitter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivTwitter.getContentDescription ().toString ().contains ("http://") || ivTwitter.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivTwitter.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivTwitter.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivLinkedin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivLinkedin.getContentDescription ().toString ().contains ("http://") || ivLinkedin.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivLinkedin.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivLinkedin.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        ivYoutube.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Uri uri;
                if (ivYoutube.getContentDescription ().toString ().contains ("http://") || ivYoutube.getContentDescription ().toString ().contains ("https://")) {
                    uri = Uri.parse (ivYoutube.getContentDescription ().toString ());
                } else {
                    uri = Uri.parse ("http://" + ivYoutube.getContentDescription ().toString ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity (intent);
            }
        });
        
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    
        btShowMore.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (show) {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    params.addRule (RelativeLayout.BELOW, R.id.tv5);
//                    params.setMargins ((int) (Utils.pxFromDp (getActivity (), 8.0f)), 0, (int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)));
//                    cardView3.setLayoutParams (params);
                    btShowMore.setText ("SHOW LESS");
                    show = false;
                
                    Animation a = new Animation () {
                        @Override
                        protected void applyTransformation (float interpolatedTime, Transformation t) {
//                            Log.e ("karman", "wrap contant height", + cardView3.get);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule (RelativeLayout.BELOW, R.id.tvDescription);
                            params.setMargins ((int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)), 0, (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)), (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)));
                            cardView1.setLayoutParams (params);
                        }
                    };
                    a.setDuration (2000); // in ms
                    cardView1.startAnimation (a);
                } else {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, (int) (Utils.pxFromDp (getActivity (), 200.0f)));
//                    params.addRule (RelativeLayout.BELOW, R.id.tv5);
//                    params.setMargins ((int) (Utils.pxFromDp (getActivity (), 8.0f)), 0, (int) (Utils.pxFromDp (getActivity (), 8.0f)), (int) (Utils.pxFromDp (getActivity (), 8.0f)));
//                    cardView3.setLayoutParams (params);
                    btShowMore.setText ("SHOW MORE");
                    show = true;
                    Animation a = new Animation () {
                        @Override
                        protected void applyTransformation (float interpolatedTime, Transformation t) {
                            if ((1.0f - interpolatedTime) < 1.0f) {
                                if ((cardView1.getHeight () * (1.0f - interpolatedTime)) <= Utils.pxFromDp (OrganiserDetailActivity.this, 200.0f)) {
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 200.0f)));
                                    params.addRule (RelativeLayout.BELOW, R.id.tvDescription);
                                    params.setMargins ((int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)), 0, (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)), (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)));
                                    cardView1.setLayoutParams (params);
                                } else {
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, (int) (cardView1.getHeight () * (1.0f - interpolatedTime)));
                                    params.addRule (RelativeLayout.BELOW, R.id.tvDescription);
                                    params.setMargins ((int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)), 0, (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)), (int) (Utils.pxFromDp (OrganiserDetailActivity.this, 8.0f)));
                                    cardView1.setLayoutParams (params);
                                }
                            }
                        }
                    };
                    a.setDuration (2000); // in ms
                    cardView1.startAnimation (a);
                }
            }
        });
    
    }
    
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
}
