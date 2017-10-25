package com.indiasupply.isdental.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
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
    
    String eventExhibitors;
    String eventSpeakers;
    String eventSchedule;
    String eventFloorPlan;
    String eventInformation;
    String evevntRegistration;
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    
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
                        if (eventSchedule.length () > 0) {
                            SwiggyEventScheduleDialogFragment frag1 = SwiggyEventScheduleDialogFragment.newInstance (eventSchedule);
                            frag1.show (ft, "2");
                        } else {
                        }
                        break;
                    case 2:
                        if (eventSpeakers.length () > 0) {
                            SwiggyEventSpeakerDialogFragment frag2 = SwiggyEventSpeakerDialogFragment.newInstance (eventSpeakers);
                            frag2.show (ft, "2");
                        } else {
                        }
                        break;
                    case 3:
                        if (eventExhibitors.length () > 0) {
                            SwiggyEventExhibitorDialogFragment frag3 = SwiggyEventExhibitorDialogFragment.newInstance (eventExhibitors);
                            frag3.show (ft, "3");
                        } else {
                        }
                        break;
                    case 4:
                        if (eventFloorPlan.length () > 0) {
                            SwiggyEventFloorPlanDialogFragment frag4 = SwiggyEventFloorPlanDialogFragment.newInstance (eventFloorPlan);
                            frag4.show (ft, "4");
                        } else {
                        }
                        break;
                    case 5:
                        if (eventInformation.length () > 0) {
                            SwiggyEventInformationDialogFragment frag5 = SwiggyEventInformationDialogFragment.newInstance (eventInformation);
                            frag5.show (ft, "5");
                        } else {
                        }
                        break;
                    case 6:
                        if (evevntRegistration.length () > 0) {
                            SwiggyEventRegistrationsDialogFragment frag6 = SwiggyEventRegistrationsDialogFragment.newInstance (evevntRegistration);
                            frag6.show (ft, "6");
                        } else {
                        }
                        break;
                }
            }
        });
    }
    
    private void initData () {
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
                                        eventExhibitors = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_EXHIBITORS).toString ();
                                        eventSpeakers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_EVENT_SPEAKERS).toString ();
                                        eventFloorPlan = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_FLOOR_PLAN);
                                        eventInformation = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_INFORMATION);
                                        evevntRegistration = jsonObj.getString (AppConfigTags.SWIGGY_EVENT_REGISTRATION);
                                        tvTitleEventName.setText (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_NAME));
                                        tvTitleEventDetail.setText (Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_START_DATE), "yyyy-MM-dd", "dd") + " - " + Utils.convertTimeFormat (jsonObj.getString (AppConfigTags.SWIGGY_EVENT_END_DATE), "yyyy-MM-dd", "dd MMM") + ", " + jsonObj.getString (AppConfigTags.SWIGGY_EVENT_CITY));
                                        eventSchedule = jsonObj.getJSONObject (AppConfigTags.SWIGGY_EVENT_SCHEDULE).toString ();
    
                                        eventItemList.add (new SwiggyEventItem (1, R.drawable.ic_event_schedule, "EVENT SCHEDULE", ""));
                                        eventItemList.add (new SwiggyEventItem (2, R.drawable.ic_event_speaker, "SPEAKERS", ""));
                                        eventItemList.add (new SwiggyEventItem (3, R.drawable.ic_event_exhibitor, "EXHIBITORS", ""));
                                        eventItemList.add (new SwiggyEventItem (4, R.drawable.ic_event_floor_plan, "FLOOR PLAN", ""));
                                        eventItemList.add (new SwiggyEventItem (5, R.drawable.ic_event_general_info, "GENERAL INFORMATION", ""));
                                        eventItemList.add (new SwiggyEventItem (6, R.drawable.ic_event_registration, "REGISTRATIONS", ""));
    
    
    
    
                                        eventItemAdapter.notifyDataSetChanged ();
    
                                        rlMain.setVisibility (View.VISIBLE);
                                        shimmerFrameLayout.setVisibility (View.GONE);
                                    } else {
                                        Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred) + " : " + message, Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
                            } else {
                                Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (SwiggyEventDetailActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
            Utils.sendRequest (strRequest, 5);
        } else {
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
    
    private void startShimmer () {
        shimmerFrameLayout.useDefaults ();
        shimmerFrameLayout.setDuration (1500);
        shimmerFrameLayout.setBaseAlpha (0.3f);
        shimmerFrameLayout.setRepeatDelay (500);
        if (shimmerFrameLayout.isAnimationStarted ()) {
            shimmerFrameLayout.startShimmerAnimation ();
        }
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        startShimmer ();
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
}