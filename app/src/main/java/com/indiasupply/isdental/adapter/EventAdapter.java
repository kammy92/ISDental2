package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.EventDetailActivity;
import com.indiasupply.isdental.model.Event;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    EventAdapter.OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Event> eventList = new ArrayList<> ();
    
    public EventAdapter (Activity activity, List<Event> eventList) {
        this.activity = activity;
        this.eventList = eventList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_event, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final Event event = eventList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvEventName);
        
        holder.tvEventName.setText (event.getName ());
        if (event.getStart_date ().equalsIgnoreCase (event.getEnd_date ())) {
            holder.tvEventDates.setText (Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd MMM"));
        } else {
            holder.tvEventDates.setText (Utils.convertTimeFormat (event.getStart_date (), "yyyy-MM-dd", "dd MMM") + " - " + Utils.convertTimeFormat (event.getEnd_date (), "yyyy-MM-dd", "dd MMM"));
        }
    
        holder.tvEventVenue.setText (event.getLocation ());
    
    
        if (event.isInterested ()) {
            holder.ivInterested.setImageResource (R.drawable.ic_like_filled);
            holder.tvInterested.setTextColor (activity.getResources ().getColor (R.color.fb_colour));
        } else {
            holder.ivInterested.setImageResource (R.drawable.ic_like);
            holder.tvInterested.setTextColor (activity.getResources ().getColor (R.color.secondary_text2));
        }
        
        
        holder.rlInterested.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                updateInterested (event.getId ());
                if (event.isInterested ()) {
                    event.setInterested (false);
                    holder.ivInterested.setImageResource (R.drawable.ic_like);
                    holder.tvInterested.setTextColor (activity.getResources ().getColor (R.color.secondary_text2));
                } else {
                    event.setInterested (true);
                    holder.ivInterested.setImageResource (R.drawable.ic_like_filled);
                    holder.tvInterested.setTextColor (activity.getResources ().getColor (R.color.fb_colour));
                }
            }
        });
    
    
        holder.rlShare.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                final ProgressDialog progressDialog = new ProgressDialog (activity);
                Utils.showProgressDialog (progressDialog, null, true);
    /*
                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance ().createDynamicLink ()
                        .setLink (Uri.parse ("https://example.com/"))
                        .setDynamicLinkDomain ("abc123.app.goo.gl")
                        .setAndroidParameters (
                                new DynamicLink.AndroidParameters.Builder ("com.example.android")
                                        .setMinimumVersion (125)
                                        .build ())
                        .setIosParameters (
                                new DynamicLink.IosParameters.Builder ("com.example.ios")
                                        .setAppStoreId ("123456789")
                                        .setMinimumVersion ("1.0.1")
                                        .build ())
                        .setGoogleAnalyticsParameters (
                                new DynamicLink.GoogleAnalyticsParameters.Builder ()
                                        .setSource ("orkut")
                                        .setMedium ("social")
                                        .setCampaign ("example-promo")
                                        .build ())
                        .setItunesConnectAnalyticsParameters (
                                new DynamicLink.ItunesConnectAnalyticsParameters.Builder ()
                                        .setProviderToken ("123456")
                                        .setCampaignToken ("example-promo")
                                        .build ())
                        .setSocialMetaTagParameters (
                                new DynamicLink.SocialMetaTagParameters.Builder ()
                                        .setTitle ("Example of a Dynamic Link")
                                        .setDescription ("This link works whether the app is installed or not!")
                                        .build ())
                .buildShortDynamicLink();
    */
    
                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance ().createDynamicLink ()
                        .setLink (Uri.parse ("https://indiasupply.com/event/" + event.getId ()))
                        .setDynamicLinkDomain ("ha4qf.app.goo.gl")
                        // Open links with this app on Android
                        .setAndroidParameters (
                                new DynamicLink.AndroidParameters.Builder ("com.indiasupply.isdental")
                                        .setFallbackUrl (Uri.parse ("https://indiasupply.com"))
                                        .build ())
                        .setSocialMetaTagParameters (new DynamicLink.SocialMetaTagParameters.Builder ()
                                .setTitle (event.getName ())
                                .setImageUrl (Uri.parse (event.getImage ()))
                                .setDescription (holder.tvEventDates.getText ().toString () + ", " + holder.tvEventVenue.getText ().toString ())
                                .build ())
                        .buildShortDynamicLink ()
                        .addOnCompleteListener (activity, new OnCompleteListener<ShortDynamicLink> () {
                            @Override
                            public void onComplete (@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful ()) {
                                    String shareBody = "Hi, Following event " + event.getName () + ". deep link : " + task.getResult ().getShortLink ().toString ();
                                    Intent sharingIntent = new Intent (android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType ("text/plain");
                                    sharingIntent.putExtra (android.content.Intent.EXTRA_TEXT, shareBody);
                                    activity.startActivity (Intent.createChooser (sharingIntent, "Share Using"));
                                } else {
                                }
                                progressDialog.dismiss ();
                            }
                        });
            }
        });
    
        holder.rlShare.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction ()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.ivShare.setImageResource (R.drawable.ic_share_filled);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        holder.ivShare.setImageResource (R.drawable.ic_share);
                        break;
                }
                return false;
            }
        });
        
        
        if (event.getImage ().length () == 0) {
            holder.ivEventImage.setImageResource (event.getIcon ());
            holder.progressBar.setVisibility (View.GONE);
        } else {
            holder.progressBar.setVisibility (View.VISIBLE);
            Glide.with (activity)
                    .load (event.getImage ())
                    .listener (new RequestListener<String, GlideDrawable> () {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
    
                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .error (event.getIcon ())
                    .into (holder.ivEventImage);
        }
    }
    
    @Override
    public int getItemCount () {
        return eventList.size ();
    }
    
    public void SetOnItemClickListener (final EventAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    private void updateInterested (final int event_id) {
        if (NetworkConnection.isNetworkAvailable (activity)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_EVENT_INTERESTED, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_EVENT_INTERESTED,
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
                                        switch (jsonObj.getInt (AppConfigTags.STATUS)) {
                                            case 1:
//                                                contactDetail.setIs_favourite (true);
                                                break;
                                            case 2:
//                                                contactDetail.setIs_favourite (false);
                                                break;
                                        }
                                    } else {
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showToast (activity, "Unstable Internet Connection", false);
                                }
                            } else {
                                Utils.showToast (activity, "Unstable Internet Connection", false);
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
                            Utils.showToast (activity, "Unstable Internet Connection", false);
                        }
                    }) {
                
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_EVENT_ID, String.valueOf (event_id));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    UserDetailsPref userDetailsPref = UserDetailsPref.getInstance ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (activity, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 20);
        } else {
            Utils.showToast (activity, "Unstable Internet Connection", false);
        }
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvEventName;
        TextView tvEventDates;
        TextView tvEventVenue;
        ImageView ivEventImage;
        ImageView ivInterested;
        TextView tvInterested;
        ImageView ivShare;
        RelativeLayout rlInterested;
        RelativeLayout rlShare;
        ProgressBar progressBar;
        
        public ViewHolder (View view) {
            super (view);
            tvEventDates = (TextView) view.findViewById (R.id.tvEventDates);
            tvEventName = (TextView) view.findViewById (R.id.tvEventName);
            tvEventVenue = (TextView) view.findViewById (R.id.tvEventVenue);
            ivEventImage = (ImageView) view.findViewById (R.id.ivEventImage);
            ivInterested = (ImageView) view.findViewById (R.id.ivInterested);
            tvInterested = (TextView) view.findViewById (R.id.tvInterested);
            ivShare = (ImageView) view.findViewById (R.id.ivShare);
            rlInterested = (RelativeLayout) view.findViewById (R.id.rlInterested);
            rlShare = (RelativeLayout) view.findViewById (R.id.rlShare);
            progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            Event event = eventList.get (getLayoutPosition ());
            Intent intent = new Intent (activity, EventDetailActivity.class);
            intent.putExtra (AppConfigTags.EVENT_ID, event.getId ());
            activity.startActivity (intent);
            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
    
}
