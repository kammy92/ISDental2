package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class OfferDetailDialogFragment extends DialogFragment {
    RelativeLayout rlOfferDescription;
    int offer_id;
    String name, packaging, description, image, dates, details, terms_conditions;
    int price, regular_price, mrp, icon;
    ProgressDialog progressDialog;
    // OffersFragment.MyDialogCloseListener closeListener;
    private ImageView ivCancel;
    private TextView tvTitle;
    private View v1;
    private RelativeLayout rlImage;
    private ProgressBar progressBar;
    private ImageView ivOfferImage;
    private TextView tvOfferPercentage;
    private TextView tvOfferName;
    private TextView tvOfferPackaging;
    private TextView tvOfferDescription;
    private TableLayout tlPrice;
    private TextView tvOfferPrice;
    private TextView tvOfferMRP;
    private TextView tvOfferRegularPrice;
    private TextView tvOfferSaving;
    private TextView tvSendEnquiry;
    private TextView tvOfferDateText;
    private TextView tvOfferDate;
    private TextView tvDetailsText;
    private TextView tvDetails;
    private TextView tvTermsAndConditionsText;
    private TextView tvTermsAndConditions;
    
    public static OfferDetailDialogFragment newInstance (int offer_id, String name, String packaging, String description,
                                                         String image, int price, int regular_price,
                                                         int mrp, String offer_dates, String offer_details,
                                                         String offer_terms_conditions, int icon) {
        OfferDetailDialogFragment f = new OfferDetailDialogFragment ();
        Bundle args = new Bundle ();
        args.putInt (AppConfigTags.SWIGGY_OFFER_ID, offer_id);
        args.putString (AppConfigTags.SWIGGY_OFFER_NAME, name);
        args.putString (AppConfigTags.SWIGGY_OFFER_PACKAGING, packaging);
        args.putString (AppConfigTags.SWIGGY_OFFER_DESCRIPTION, description);
        args.putString (AppConfigTags.SWIGGY_OFFER_IMAGE, image);
        args.putInt (AppConfigTags.SWIGGY_OFFER_PRICE, price);
        args.putInt (AppConfigTags.SWIGGY_OFFER_REGULAR_PRICE, regular_price);
        args.putInt (AppConfigTags.SWIGGY_OFFER_MRP, mrp);
        args.putInt ("icon", icon);
        args.putString (AppConfigTags.SWIGGY_OFFER_HTML_DATES, offer_dates);
        args.putString (AppConfigTags.SWIGGY_OFFER_HTML_DETAILS, offer_details);
        args.putString (AppConfigTags.SWIGGY_OFFER_HTML_TANDC, offer_terms_conditions);
        f.setArguments (args);
        return f;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    
    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_offer_detail, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        v1 = (View) root.findViewById (R.id.v1);
        rlImage = (RelativeLayout) root.findViewById (R.id.rlImage);
        progressBar = (ProgressBar) root.findViewById (R.id.progressBar);
        ivOfferImage = (ImageView) root.findViewById (R.id.ivOfferImage);
        tvOfferPercentage = (TextView) root.findViewById (R.id.tvOfferPercentage);
        tvOfferName = (TextView) root.findViewById (R.id.tvOfferName);
        tvOfferPackaging = (TextView) root.findViewById (R.id.tvOfferPackaging);
        tvOfferDescription = (TextView) root.findViewById (R.id.tvOfferDescription);
        tlPrice = (TableLayout) root.findViewById (R.id.tlPrice);
        tvOfferPrice = (TextView) root.findViewById (R.id.tvOfferPrice);
        tvOfferMRP = (TextView) root.findViewById (R.id.tvOfferMRP);
        tvOfferRegularPrice = (TextView) root.findViewById (R.id.tvOfferRegularPrice);
        tvOfferSaving = (TextView) root.findViewById (R.id.tvOfferSaving);
        rlOfferDescription = (RelativeLayout) root.findViewById (R.id.rlOfferDescription);
        tvSendEnquiry = (TextView) root.findViewById (R.id.tvSendEnquiry);
        tvOfferDateText = (TextView) root.findViewById (R.id.tvOfferDateText);
        tvOfferDate = (TextView) root.findViewById (R.id.tvOfferDate);
        tvDetailsText = (TextView) root.findViewById (R.id.tvDetailsText);
        tvDetails = (TextView) root.findViewById (R.id.tvDetails);
        tvTermsAndConditionsText = (TextView) root.findViewById (R.id.tvTermsAndConditionsText);
        tvTermsAndConditions = (TextView) root.findViewById (R.id.tvTermsAndConditions);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        offer_id = bundle.getInt (AppConfigTags.SWIGGY_OFFER_ID);
        name = bundle.getString (AppConfigTags.SWIGGY_OFFER_NAME);
        packaging = bundle.getString (AppConfigTags.SWIGGY_OFFER_PACKAGING);
        description = bundle.getString (AppConfigTags.SWIGGY_OFFER_DESCRIPTION);
        image = bundle.getString (AppConfigTags.SWIGGY_OFFER_IMAGE);
        price = bundle.getInt (AppConfigTags.SWIGGY_OFFER_PRICE, 0);
        regular_price = bundle.getInt (AppConfigTags.SWIGGY_OFFER_REGULAR_PRICE, 0);
        mrp = bundle.getInt (AppConfigTags.SWIGGY_OFFER_MRP, 0);
        icon = bundle.getInt ("icon", 0);
        dates = bundle.getString (AppConfigTags.SWIGGY_OFFER_HTML_DATES);
        details = bundle.getString (AppConfigTags.SWIGGY_OFFER_HTML_DETAILS);
        terms_conditions = bundle.getString (AppConfigTags.SWIGGY_OFFER_HTML_TANDC);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvOfferName);
        progressDialog = new ProgressDialog (getActivity ());
        tvOfferName.setText (name);
        tvOfferPackaging.setText (packaging);
        
        if (description.length () > 0) {
            rlOfferDescription.setVisibility (View.VISIBLE);
            tvOfferDescription.setText (description);
        } else {
            rlOfferDescription.setVisibility (View.GONE);
        }
        
        tvOfferPrice.setText ("Rs. " + price);
        tvOfferRegularPrice.setText ("Rs. " + regular_price);
        tvOfferMRP.setText ("Rs. " + mrp);
        tvOfferSaving.setText ("Rs. " + (mrp - price));
        
        if (dates.length () > 0) {
            tvOfferDateText.setVisibility (View.VISIBLE);
            tvOfferDate.setVisibility (View.VISIBLE);
            tvOfferDate.setText (Html.fromHtml (dates.trim ()));
        } else {
            tvOfferDateText.setVisibility (View.GONE);
            tvOfferDate.setVisibility (View.GONE);
        }
        
        if (details.length () > 0) {
            tvDetailsText.setVisibility (View.VISIBLE);
            tvDetails.setVisibility (View.VISIBLE);
            tvDetails.setText (Html.fromHtml (details.trim ()));
        } else {
            tvDetailsText.setVisibility (View.GONE);
            tvDetails.setVisibility (View.GONE);
        }
        
        if (terms_conditions.length () > 0) {
            tvTermsAndConditionsText.setVisibility (View.VISIBLE);
            tvTermsAndConditions.setVisibility (View.VISIBLE);
            tvTermsAndConditions.setText (Html.fromHtml (terms_conditions.trim ()));
        } else {
            tvTermsAndConditions.setVisibility (View.GONE);
            tvTermsAndConditionsText.setVisibility (View.GONE);
        }
        
        double percentage = ((mrp - price) * 100) / mrp;
        if (percentage > 15) {
            tvOfferPercentage.setText ((int) percentage + "% Off");
            tvOfferPercentage.setVisibility (View.VISIBLE);
        } else {
            tvOfferPercentage.setVisibility (View.GONE);
        }
        
        tvOfferMRP.setPaintFlags (tvOfferMRP.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        tvOfferRegularPrice.setPaintFlags (tvOfferRegularPrice.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        
        if (image.length () == 0) {
            ivOfferImage.setImageResource (icon);
            progressBar.setVisibility (View.GONE);
        } else {
            Glide.with (getActivity ())
                    .load (image)
                    .listener (new RequestListener<String, GlideDrawable> () {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility (View.GONE);
                            return false;
                        }
                        
                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .error (icon)
                    .into (ivOfferImage);
        }
        
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        
        tvSendEnquiry.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                sendEnquiry2 (offer_id);
            }
        });
    }
    
    private void sendEnquiry2 (final int id) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, null, true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_ENQUIRY2, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_ENQUIRY2,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (final String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    new MaterialDialog.Builder (getActivity ())
                                            .content (message)
                                            .positiveColor (getResources ().getColor (R.color.primary_text2))
                                            .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                                            .canceledOnTouchOutside (true)
                                            .cancelable (true)
                                            .positiveText (R.string.dialog_action_ok)
                                            .show ();
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    Utils.showToast (getActivity (), "Unstable Internet Connection", false);
                                }
                            } else {
                                Utils.showToast (getActivity (), "Unstable Internet Connection", false);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showToast (getActivity (), "Unstable Internet Connection", false);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_OFFER_ID, String.valueOf (id));
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
            Utils.sendRequest (strRequest, 20);
        } else {
            Utils.showToast (getActivity (), "Unstable Internet Connection", false);
        }
    }


    
    /*public void setDismissListener (OffersFragment.MyDialogCloseListener closeListener) {
        this.closeListener = closeListener;
    }*/
    
    /*@Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        if (closeListener != null) {
            closeListener.handleDialogClose (null);
        }
    }*/
}