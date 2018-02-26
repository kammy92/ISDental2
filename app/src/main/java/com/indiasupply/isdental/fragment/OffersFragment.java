package com.indiasupply.isdental.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.MyAccountActivity;
import com.indiasupply.isdental.adapter.OfferAdapter;
import com.indiasupply.isdental.dialog.OfferDetailDialogFragment;
import com.indiasupply.isdental.model.Banner;
import com.indiasupply.isdental.model.Offers;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.AppDataPref;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.ShimmerFrameLayout;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by l on 27/09/2017.
 */

public class OffersFragment extends Fragment {
    RecyclerView rvOffers;
    ImageView ivBanner;
    ShimmerFrameLayout shimmerFrameLayout;
    List<Banner> bannerList = new ArrayList<> ();
    List<Offers> offerList = new ArrayList<> ();
    OfferAdapter offerAdapter;
    RelativeLayout rlMain;
    
    ImageView ivMyAccount;
    
    CoordinatorLayout clMain;
    
    AppDataPref appDataPref;
    
    boolean refresh;
    
    public static OffersFragment newInstance (boolean refresh) {
        OffersFragment fragment = new OffersFragment ();
        Bundle args = new Bundle ();
        args.putBoolean (AppConfigTags.REFRESH_FLAG, refresh);
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_offers, container, false);
        initView (rootView);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        ivBanner = (ImageView) rootView.findViewById (R.id.ivBanner);
        rvOffers = (RecyclerView) rootView.findViewById (R.id.rvOffers);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
        ivMyAccount = (ImageView) rootView.findViewById (R.id.ivMyAccount);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        refresh = bundle.getBoolean (AppConfigTags.REFRESH_FLAG);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), clMain);
        appDataPref = AppDataPref.getInstance ();
    
        offerAdapter = new OfferAdapter (getActivity (), offerList);
        rvOffers.setAdapter (offerAdapter);
        rvOffers.setNestedScrollingEnabled (false);
        rvOffers.setFocusable (false);
        rvOffers.setHasFixedSize (true);
        rvOffers.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvOffers.setItemAnimator (new DefaultItemAnimator ());
        rvOffers.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
        if (! refresh) {
            showOfflineData ();
        }
    }
    
    private void initListener () {
        offerAdapter.SetOnItemClickListener (new OfferAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Offers offers = offerList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                OfferDetailDialogFragment dialog = new OfferDetailDialogFragment ().newInstance (offers.getId (), offers.getName (), offers.getPackaging (),
                        offers.getDescription (), offers.getImage (), offers.getPrice (), offers.getRegular_price (),
                        offers.getMrp (), offers.getQty (), offers.getHtml_dates (), offers.getHtml_details (),
                        offers.getHtml_tandc (), offers.getIcon ());
                dialog.show (ft, "Contacts");
            }
        });
        ivMyAccount.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (getActivity (), MyAccountActivity.class);
                getActivity ().startActivity (intent);
                getActivity ().overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_HOME_OFFERS, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_HOME_OFFERS,
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
                                            appDataPref.putStringPref (getActivity (), AppDataPref.HOME_OFFERS, response);
                                            JSONArray jsonArrayBanners = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BANNERS);
                                            for (int i = 0; i < jsonArrayBanners.length (); i++) {
                                                JSONObject jsonObjectBanners = jsonArrayBanners.getJSONObject (i);
                                                if (jsonObjectBanners.getString (AppConfigTags.BANNER_IMAGE).length () == 0) {
                                                    ivBanner.setImageResource (R.drawable.default_event);
                                                } else {
                                                    Glide.with (getActivity ())
                                                            .load (jsonObjectBanners.getString (AppConfigTags.BANNER_IMAGE))
                                                            .listener (new RequestListener<String, GlideDrawable> () {
                                                                @Override
                                                                public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                    return false;
                                                                }
                                                                
                                                                @Override
                                                                public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                    return false;
                                                                }
                                                            })
                                                            .error (R.drawable.default_event)
                                                            .into (ivBanner);
                                                }
                                            }
                                            
                                            JSONArray jsonArrayOffers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_OFFERS);
                                            offerList.clear ();
                                            for (int j = 0; j < jsonArrayOffers.length (); j++) {
                                                JSONObject jsonObjectOffer = jsonArrayOffers.getJSONObject (j);
                                                offerList.add (new Offers (
                                                        jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_ID),
                                                        R.drawable.default_company,
                                                        jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_PRICE),
                                                        jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_MRP),
                                                        jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_REGULAR_PRICE),
                                                        jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_QTY),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_NAME),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_IMAGE),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_DESCRIPTION),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_PACKAGING),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_START_DATE),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_END_DATE),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_HTML_DATES),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_HTML_DETAILS),
                                                        jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_HTML_TANDC)
                                                ));
                                            }
                                            offerAdapter.notifyDataSetChanged ();
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
                                        Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_unstable_internet), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                }
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (getActivity (), UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 30);
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
        String response = appDataPref.getStringPref (getActivity (), AppDataPref.HOME_OFFERS);
        if (response.length () > 0) {
            try {
                JSONObject jsonObj = new JSONObject (response);
                boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                String message = jsonObj.getString (AppConfigTags.MESSAGE);
                if (! is_error) {
                    appDataPref.putStringPref (getActivity (), AppDataPref.HOME_OFFERS, response);
                    JSONArray jsonArrayBanners = jsonObj.getJSONArray (AppConfigTags.SWIGGY_BANNERS);
                    for (int i = 0; i < jsonArrayBanners.length (); i++) {
                        JSONObject jsonObjectBanners = jsonArrayBanners.getJSONObject (i);
                        if (jsonObjectBanners.getString (AppConfigTags.BANNER_IMAGE).length () == 0) {
                            ivBanner.setImageResource (R.drawable.default_event);
                        } else {
                            Glide.with (getActivity ())
                                    .load (jsonObjectBanners.getString (AppConfigTags.BANNER_IMAGE))
                                    .listener (new RequestListener<String, GlideDrawable> () {
                                        @Override
                                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            return false;
                                        }
                                        
                                        @Override
                                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .error (R.drawable.default_event)
                                    .into (ivBanner);
                        }
                    }
                    
                    JSONArray jsonArrayOffers = jsonObj.getJSONArray (AppConfigTags.SWIGGY_OFFERS);
                    offerList.clear ();
                    for (int j = 0; j < jsonArrayOffers.length (); j++) {
                        JSONObject jsonObjectOffer = jsonArrayOffers.getJSONObject (j);
                        offerList.add (new Offers (
                                jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_ID),
                                R.drawable.default_company,
                                jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_PRICE),
                                jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_MRP),
                                jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_REGULAR_PRICE),
                                jsonObjectOffer.getInt (AppConfigTags.SWIGGY_OFFER_QTY),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_NAME),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_IMAGE),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_DESCRIPTION),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_PACKAGING),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_START_DATE),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_END_DATE),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_HTML_DATES),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_HTML_DETAILS),
                                jsonObjectOffer.getString (AppConfigTags.SWIGGY_OFFER_HTML_TANDC)
                        ));
                    }
                    offerAdapter.notifyDataSetChanged ();
                    rlMain.setVisibility (View.VISIBLE);
                    shimmerFrameLayout.setVisibility (View.GONE);
                } else {
                    Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                }
            } catch (Exception e) {
                e.printStackTrace ();
                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
            }
            return true;
        } else {
            return false;
        }
    }
}
