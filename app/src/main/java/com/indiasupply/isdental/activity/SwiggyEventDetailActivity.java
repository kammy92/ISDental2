package com.indiasupply.isdental.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyEventItemAdapter;
import com.indiasupply.isdental.dialog.SwiggyEventExhibitorDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyEventFloorPlanDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyEventInformationDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyEventRegistrationsDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyEventScheduleDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyEventSpeakerDialogFragment;
import com.indiasupply.isdental.helper.DatabaseHandler;
import com.indiasupply.isdental.model.SwiggyEventItem;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.ShimmerFrameLayout;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SwiggyEventDetailActivity extends AppCompatActivity {
    RelativeLayout rlBack;
    RecyclerView rvEventItems;
    CoordinatorLayout clMain;
    
    List<SwiggyEventItem> eventItemList = new ArrayList<> ();
    SwiggyEventItemAdapter eventItemAdapter;
    
    TextView tvTitleEventName;
    TextView tvTitleEventDetail;
    
    int event_id;
    
    String eventExhibitors = "";
    String eventSpeakers = "";
    String eventSchedule = "";
    String eventFloorPlan = "";
    String eventInformation = "";
    String eventRegistration = "";
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    RelativeLayout rlNoResult;
    ImageView ivNoResult;
    TextView tvNoResultTitle;
    TextView tvNoResultDescription;
    TextView tvNoResultButton;
    
    DatabaseHandler db;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_event_detail);
        getExtras ();
        initView ();
        initData ();
        initListener ();
        setData ();
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                
            }
        });
        eventItemAdapter.SetOnItemClickListener (new SwiggyEventItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyEventItem eventItem = eventItemList.get (position);
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                switch (eventItem.getId ()) {
                    case 1:
                        if (eventItem.isEnabled ()) {
                            SwiggyEventScheduleDialogFragment frag1 = SwiggyEventScheduleDialogFragment.newInstance (eventSchedule);
                            frag1.show (ft, "2");
                        } else {
//                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, "No schedule available yet", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                        break;
                    case 2:
                        if (eventItem.isEnabled ()) {
                            SwiggyEventSpeakerDialogFragment frag2 = SwiggyEventSpeakerDialogFragment.newInstance (eventSpeakers);
                            frag2.show (ft, "2");
                        } else {
//                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, "No Speakers available yet", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                        break;
                    case 3:
                        if (eventItem.isEnabled ()) {
                            SwiggyEventExhibitorDialogFragment frag3 = SwiggyEventExhibitorDialogFragment.newInstance (eventExhibitors);
                            frag3.show (ft, "3");
                        } else {
//                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, "No Exhibitors available yet", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                        break;
                    case 4:
                        if (eventItem.isEnabled ()) {
                            SwiggyEventFloorPlanDialogFragment frag4 = SwiggyEventFloorPlanDialogFragment.newInstance (event_id, eventFloorPlan);
                            frag4.show (ft, "4");
                        } else {
//                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, "No Floor Plan available yet", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                        break;
                    case 5:
                        if (eventItem.isEnabled ()) {
                            SwiggyEventInformationDialogFragment frag5 = SwiggyEventInformationDialogFragment.newInstance (eventInformation);
                            frag5.show (ft, "5");
                        } else {
//                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, "No Information available yet", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                        break;
                    case 6:
                        if (eventItem.isEnabled ()) {
                            SwiggyEventRegistrationsDialogFragment frag6 = SwiggyEventRegistrationsDialogFragment.newInstance (eventRegistration);
                            frag6.show (ft, "6");
                        } else {
//                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, "No Registration Details available yet", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                        break;
                }
            }
        });
    }
    
    private void initData () {
        db = new DatabaseHandler (getApplicationContext ());
        eventClicked (event_id);
        Utils.setTypefaceToAllViews (this, tvTitleEventDetail);
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
    
        eventItemAdapter = new SwiggyEventItemAdapter (SwiggyEventDetailActivity.this, eventItemList);
        rvEventItems.setAdapter (eventItemAdapter);
        rvEventItems.setHasFixedSize (true);
        rvEventItems.setLayoutManager (new GridLayoutManager (SwiggyEventDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvEventItems.setItemAnimator (new DefaultItemAnimator ());
        rvEventItems.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rvEventItems = (RecyclerView) findViewById (R.id.rvEventItems);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvTitleEventName = (TextView) findViewById (R.id.tvTitleEventName);
        tvTitleEventDetail = (TextView) findViewById (R.id.tvTitleEventDetail);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) findViewById (R.id.rlMain);
        rlNoResult = (RelativeLayout) findViewById (R.id.rlNoResult);
        ivNoResult = (ImageView) findViewById (R.id.ivNoResult);
        tvNoResultTitle = (TextView) findViewById (R.id.tvNoResultTitle);
        tvNoResultDescription = (TextView) findViewById (R.id.tvNoResultDescription);
        tvNoResultButton = (TextView) findViewById (R.id.tvNoResultButton);
    }
    
    private void getExtras () {
        Intent intent = getIntent ();
        event_id = intent.getIntExtra (AppConfigTags.EVENT_ID, 0);
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private void setData () {
        shimmerFrameLayout.setVisibility (View.VISIBLE);
        rlMain.setVisibility (View.GONE);
        rlNoResult.setVisibility (View.GONE);
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_EVENT_DETAILS + "/" + event_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_EVENT_DETAILS + "/" + event_id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        if (db.isEventExist (event_id)) {
                                            db.updateEventDetails (event_id, response);
                                        } else {
                                            db.insertEvent (event_id, response);
                                        }
                                        if (jsonObj.getJSONObject (AppConfigTags.SWIGGY_EVENT_SCHEDULE).getJSONArray ("schedules").length () > 0) {
                                            eventItemList.add (new SwiggyEventItem (true, 1, R.drawable.ic_event_schedule, "EVENT SCHEDULE", ""));
                                            eventSchedule = jsonObj.getJSONObject (AppConfigTags.SWIGGY_EVENT_SCHEDULE).toString ();
                                        } else {
                                            eventItemList.add (new SwiggyEventItem (false, 1, R.drawable.ic_event_schedule, "EVENT SCHEDULE", ""));
                                        }
                                        if (jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_SPEAKERS).length () > 0) {
                                            eventItemList.add (new SwiggyEventItem (true, 2, R.drawable.ic_event_speaker, "SPEAKERS", ""));
                                            eventSpeakers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_SPEAKERS).toString ();
                                        } else {
                                            eventItemList.add (new SwiggyEventItem (false, 2, R.drawable.ic_event_speaker, "SPEAKERS", ""));
                                        }
                                        if (jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_EXHIBITORS).length () > 0) {
                                            eventItemList.add (new SwiggyEventItem (true, 3, R.drawable.ic_event_exhibitor, "EXHIBITORS", ""));
                                            eventExhibitors = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_EXHIBITORS).toString ();
                                        } else {
                                            eventItemList.add (new SwiggyEventItem (false, 3, R.drawable.ic_event_exhibitor, "EXHIBITORS", ""));
                                        }
                                        boolean flag = false;
                                        for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                                            if (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_FLOOR_PLAN).endsWith (ext)) {
                                                new getBitmapFromURL ().execute (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_FLOOR_PLAN));
                                                flag = true;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                            eventFloorPlan = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_FLOOR_PLAN);
                                            eventItemList.add (new SwiggyEventItem (true, 4, R.drawable.ic_event_floor_plan, "FLOOR PLAN", ""));
                                        } else {
                                            eventItemList.add (new SwiggyEventItem (false, 4, R.drawable.ic_event_floor_plan, "FLOOR PLAN", ""));
                                        }
                                        if (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_INFORMATION).length () > 0) {
                                            eventItemList.add (new SwiggyEventItem (true, 5, R.drawable.ic_event_general_info, "GENERAL INFORMATION", ""));
                                            eventInformation = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_INFORMATION);
                                        } else {
                                            eventItemList.add (new SwiggyEventItem (false, 5, R.drawable.ic_event_general_info, "GENERAL INFORMATION", ""));
                                        }
                                        if (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_REGISTRATION).length () > 0) {
                                            eventItemList.add (new SwiggyEventItem (true, 6, R.drawable.ic_event_registration, "REGISTRATIONS", ""));
                                            eventRegistration = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_REGISTRATION);
                                        } else {
                                            eventItemList.add (new SwiggyEventItem (false, 6, R.drawable.ic_event_registration, "REGISTRATIONS", ""));
                                        }
                                        
                                        tvTitleEventName.setText (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_NAME));
                                        if (jsonObj.getString (AppConfigTags.EVENT_VENUE).length () > 0) {
                                            tvTitleEventDetail.setText (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_VENUE) + ", " + jsonObj.getString (AppConfigTags.SWIGGY_EVENT_CITY));
                                        } else {
                                            if (jsonObj.getString (AppConfigTags.EVENT_START_DATE).equalsIgnoreCase (jsonObj.getString (AppConfigTags.EVENT_END_DATE))) {
                                                tvTitleEventDetail.setText (Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_END_DATE), "yyyy-MM-dd", "dd MMM") + ", " + jsonObj.getString (AppConfigTags.SWIGGY_EVENT_CITY));
                                            } else {
                                                tvTitleEventDetail.setText (Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_START_DATE), "yyyy-MM-dd", "dd") + " - " + Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_END_DATE), "yyyy-MM-dd", "dd MMM") + ", " + jsonObj.getString (AppConfigTags.SWIGGY_EVENT_CITY));
                                            }
                                        }
                                        
                                        eventItemAdapter.notifyDataSetChanged ();
    
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                        rlMain.setVisibility (View.VISIBLE);
//                                       updateLayoutOnResponse (0);
                                    } else {
                                        if (! showOfflineData (event_id)) {
                                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, message, Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                        }
//                                        updateLayoutOnResponse (1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showLog (Log.WARN, AppConfigTags.EXCEPTION, e.getMessage (), true);
                                    if (! showOfflineData (event_id)) {
                                        Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
//                                    updateLayoutOnResponse (2);
                                }
                            } else {
                                if (! showOfflineData (event_id)) {
                                    Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
//                                updateLayoutOnResponse (3);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
//                            updateLayoutOnResponse (4);
                            if (! showOfflineData (event_id)) {
                                Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            }
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SwiggyEventDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
//            updateLayoutOnResponse (5);
            if (! showOfflineData (event_id)) {
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
    }
    
    /*
    private void updateLayoutOnResponse (int type) {
// type => 0(No error data fetched successfully)
// type => 1(error true in server response)
// type => 2(Exception Occurred)
// type => 3(Blank response from server)
// type => 4(In onError of volley)
// type => 5(No Internet connection)
        String title = "";
        String description = "";
        String button = "";
        View.OnClickListener onClickListener = new View.OnClickListener () {
            @Override
            public void onClick (View v) {
            }
        };
        shimmerFrameLayout.setVisibility (View.GONE);
        switch (type) {
            case 0:
                rlMain.setVisibility (View.VISIBLE);
                break;
            case 1:
                title = "Oops.. Error Occurred!";
                description = "There seems to be an error while processing your request";
                button = "RETRY";
                onClickListener = new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        setData ();
                    }
                };
                rlNoResult.setVisibility (View.VISIBLE);
                break;
            case 2:
                title = "Oops.. Exception Occurred!";
                description = "An exception occurred while processing the request";
                button = "RETRY";
                onClickListener = new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        setData ();
                    }
                };
                rlNoResult.setVisibility (View.VISIBLE);
                break;
            case 3:
                title = "Oops.. Error Occurred!";
                description = "A blank response was received from the server";
                button = "RETRY";
                onClickListener = new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        setData ();
                    }
                };
                rlNoResult.setVisibility (View.VISIBLE);
                break;
            case 4:
                title = "Oops.. Error Occurred!";
                description = "An error occurred at the server end.\nPlease try again later";
                button = "TRY AGAIN";
                onClickListener = new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        setData ();
                    }
                };
                rlNoResult.setVisibility (View.VISIBLE);
                break;
            case 5:
                title = "Oops.. No Internet!";
                description = "Seems to be no internet connection.\nPlease check your internet connection and try again";
                button = "RETRY";
                onClickListener = new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        setData ();
                    }
                };
                rlNoResult.setVisibility (View.VISIBLE);
                break;
        }
        tvNoResultTitle.setText (title);
        tvNoResultDescription.setText (description);
        tvNoResultButton.setText (button);
        tvNoResultButton.setOnClickListener (onClickListener);
    }
    */
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
    
    private boolean showOfflineData (int event_id) {
        if (db.isEventExist (event_id)) {
            String response = db.getEventDetails (event_id);
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    if (jsonObj.getJSONObject (AppConfigTags.SWIGGY_EVENT_SCHEDULE).getJSONArray ("schedules").length () > 0) {
                        eventItemList.add (new SwiggyEventItem (true, 1, R.drawable.ic_event_schedule, "EVENT SCHEDULE", ""));
                        eventSchedule = jsonObj.getJSONObject (AppConfigTags.SWIGGY_EVENT_SCHEDULE).toString ();
                    } else {
                        eventItemList.add (new SwiggyEventItem (false, 1, R.drawable.ic_event_schedule, "EVENT SCHEDULE", ""));
                    }
                    if (jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_SPEAKERS).length () > 0) {
                        eventItemList.add (new SwiggyEventItem (true, 2, R.drawable.ic_event_speaker, "SPEAKERS", ""));
                        eventSpeakers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_SPEAKERS).toString ();
                    } else {
                        eventItemList.add (new SwiggyEventItem (false, 2, R.drawable.ic_event_speaker, "SPEAKERS", ""));
                    }
                    if (jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_EXHIBITORS).length () > 0) {
                        eventItemList.add (new SwiggyEventItem (true, 3, R.drawable.ic_event_exhibitor, "EXHIBITORS", ""));
                        eventExhibitors = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_EXHIBITORS).toString ();
                    } else {
                        eventItemList.add (new SwiggyEventItem (false, 3, R.drawable.ic_event_exhibitor, "EXHIBITORS", ""));
                    }
                    boolean flag = false;
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_FLOOR_PLAN).endsWith (ext)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        eventFloorPlan = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_FLOOR_PLAN);
                        eventItemList.add (new SwiggyEventItem (true, 4, R.drawable.ic_event_floor_plan, "FLOOR PLAN", ""));
                    } else {
                        eventItemList.add (new SwiggyEventItem (false, 4, R.drawable.ic_event_floor_plan, "FLOOR PLAN", ""));
                    }
                    if (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_INFORMATION).length () > 0) {
                        eventItemList.add (new SwiggyEventItem (true, 5, R.drawable.ic_event_general_info, "GENERAL INFORMATION", ""));
                        eventInformation = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_INFORMATION);
                    } else {
                        eventItemList.add (new SwiggyEventItem (false, 5, R.drawable.ic_event_general_info, "GENERAL INFORMATION", ""));
                    }
                    if (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_REGISTRATION).length () > 0) {
                        eventItemList.add (new SwiggyEventItem (true, 6, R.drawable.ic_event_registration, "REGISTRATIONS", ""));
                        eventRegistration = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_REGISTRATION);
                    } else {
                        eventItemList.add (new SwiggyEventItem (false, 6, R.drawable.ic_event_registration, "REGISTRATIONS", ""));
                    }
                    
                    tvTitleEventName.setText (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_NAME));
                    tvTitleEventDetail.setText (Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_START_DATE), "yyyy-MM-dd", "dd") + " - " + Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_END_DATE), "yyyy-MM-dd", "dd MMM") + ", " + jsonObj.getString (AppConfigTags.SWIGGY_EVENT_CITY));
                    
                    eventItemAdapter.notifyDataSetChanged ();
                    
                    shimmerFrameLayout.setVisibility (View.GONE);
                    rlMain.setVisibility (View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            return true;
        } else {
            return false;
        }
    }
    
    private void eventClicked (int event_id) {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_EVENT_CLICKED + "/" + event_id, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_EVENT_CLICKED + "/" + event_id,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                    } else {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (SwiggyEventDetailActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
        }
    }
    
    private class getBitmapFromURL extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground (String... params) {
            try {
                URL url = new URL (params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection ();
                connection.setDoInput (true);
                connection.connect ();
                InputStream input = connection.getInputStream ();
                db.updateEventFloorPlan (event_id, Utils.bitmapToBase64 (BitmapFactory.decodeStream (input)));
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }
        
        @Override
        protected void onPostExecute (String result) {
//            ivFloorPlan.setImage (ImageSource.bitmap (bitmap));
//            ivFloorPlan.setVisibility (View.VISIBLE);
//            progressBar.setVisibility (View.GONE);
        }
        
        @Override
        protected void onPreExecute () {
//            progressBar.setVisibility (View.VISIBLE);
        }
        
        @Override
        protected void onProgressUpdate (Void... values) {
        }
    }
}