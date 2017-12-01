package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.activity.SwiggyMyProductDetailActivity;
import com.indiasupply.isdental.adapter.SwiggyServiceRequestCommentsAdapter;
import com.indiasupply.isdental.model.SwiggyServiceRequestComments;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class SwiggyServiceRequestDetailDialogFragment extends DialogFragment {
    SwiggyMyProductDetailActivity.MyDialogCloseListener closeListener;
    TextView tvServiceRequestName;
    TextView tvServiceRequestModelNumber;
    TextView tvServiceRequestDate;
    TextView tvRequestDescription;
    TextView tvNoImage;
    TextView tvImages;
    TextView tvComments;
    TextView tvCommentsReply;
    ;
    RelativeLayout rl1;
    RelativeLayout rl2;
    RelativeLayout rl3;
    RecyclerView rvCommentsList;
    TextView tvCloseRequest;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView ivCancel;
    String comments;
    String Response;
    int Request_id;
    ProgressDialog progressDialog;
    
    CoordinatorLayout clMain;
    RelativeLayout rl5;
    
    
    String brand_name;
    String serial_number;
    String model_number;
    String description;
    String request_description;
    String image1;
    String image2;
    String image3;
    int bread_id;
    
    List<SwiggyServiceRequestComments> serviceRequestCommentsList = new ArrayList<> ();
    SwiggyServiceRequestCommentsAdapter serviceRequestCommentsAdapter;
    
    public SwiggyServiceRequestDetailDialogFragment newInstance (String Response, int Request_id) {
        SwiggyServiceRequestDetailDialogFragment fragment = new SwiggyServiceRequestDetailDialogFragment ();
        Bundle args = new Bundle ();
        args.putString ("Response", Response);
        args.putInt ("Request_id", Request_id);
        
        Log.e ("Response1", Response + Request_id);
        fragment.setArguments (args);
        return fragment;
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
    public void onResume () {
        super.onResume ();
        getDialog ().setOnKeyListener (new DialogInterface.OnKeyListener () {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction () != KeyEvent.ACTION_UP)
                        return true;
                    else {
                        getDialog ().dismiss ();
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_request_detail, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvServiceRequestName = (TextView) root.findViewById (R.id.tvName);
        tvServiceRequestModelNumber = (TextView) root.findViewById (R.id.tvModelName);
        tvServiceRequestDate = (TextView) root.findViewById (R.id.tvPurchaseDate);
        tvRequestDescription = (TextView) root.findViewById (R.id.tvRequestDescription);
        tvNoImage = (TextView) root.findViewById (R.id.tvNoImage);
        tvImages = (TextView) root.findViewById (R.id.tvImages);
        tvComments = (TextView) root.findViewById (R.id.tvComments);
        tvCommentsReply = (TextView) root.findViewById (R.id.tvCommentsReply);
        progressBar1 = (ProgressBar) root.findViewById (R.id.progressBar1);
        progressBar2 = (ProgressBar) root.findViewById (R.id.progressBar2);
        progressBar3 = (ProgressBar) root.findViewById (R.id.progressBar3);
        
        rl1 = (RelativeLayout) root.findViewById (R.id.rl1);
        rl2 = (RelativeLayout) root.findViewById (R.id.rl2);
        rl3 = (RelativeLayout) root.findViewById (R.id.rl3);
        rl5 = (RelativeLayout) root.findViewById (R.id.rl5);
        
        iv1 = (ImageView) root.findViewById (R.id.iv1);
        iv2 = (ImageView) root.findViewById (R.id.iv2);
        iv3 = (ImageView) root.findViewById (R.id.iv3);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
        rvCommentsList = (RecyclerView) root.findViewById (R.id.rvCommentsList);
        tvCloseRequest = (TextView) root.findViewById (R.id.tvCloseRequest);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        Response = bundle.getString ("Response");
        Request_id = bundle.getInt ("Request_id");
        
        
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (getActivity ());
        Utils.setTypefaceToAllViews (getActivity (), tvComments);
    
    
        serviceRequestCommentsAdapter = new SwiggyServiceRequestCommentsAdapter (getActivity (), serviceRequestCommentsList);
        rvCommentsList.setAdapter (serviceRequestCommentsAdapter);
        rvCommentsList.setHasFixedSize (true);
        rvCommentsList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvCommentsList.setItemAnimator (new DefaultItemAnimator ());
        rvCommentsList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 4), (int) Utils.pxFromDp (getActivity (), 4), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
        
        try {
            JSONObject jsonObject = new JSONObject (Response);
            tvServiceRequestName.setText (jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_BRAND) + " " + jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION) + " - " + jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER));
            tvServiceRequestModelNumber.setText (jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER));
            JSONArray jsonArrayRequest = jsonObject.getJSONArray (AppConfigTags.SWIGGY_REQUESTS);
    
            brand_name = jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_BRAND);
            description = jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION);
            serial_number = jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER);
            model_number = jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER);
            
            
            for (int i = 0; i < jsonArrayRequest.length (); i++) {
                JSONObject jsonObjectRequest = jsonArrayRequest.getJSONObject (i);
                if (jsonObjectRequest.getInt (AppConfigTags.SWIGGY_REQUEST_ID) == Request_id) {
                    tvRequestDescription.setText ("Request Description : " + jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_DESCRIPTION));
    
                    if (! jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_STATUS).equalsIgnoreCase ("OPEN")) {
                        tvCloseRequest.setVisibility (View.GONE);
                    } else {
                        tvCloseRequest.setVisibility (View.VISIBLE);
                    }
                    request_description = jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_DESCRIPTION);
                    image1 = jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE1);
                    image2 = jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE2);
                    image3 = jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE3);
                    
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE1).endsWith (ext)) {
                            rl1.setVisibility (View.VISIBLE);
                            tvNoImage.setVisibility (View.GONE);
                            Glide.with (getActivity ())
                                    .load (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE1))
                                    .listener (new RequestListener<String, GlideDrawable> () {
                                        @Override
                                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            progressBar1.setVisibility (View.GONE);
                                            return false;
                                        }
    
                                        @Override
                                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            progressBar1.setVisibility (View.GONE);
                                            return false;
                                        }
                                    })
                                    .into (iv1);
                            break;
                        }
                    }
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE2).endsWith (ext)) {
                            rl2.setVisibility (View.VISIBLE);
                            tvNoImage.setVisibility (View.GONE);
                            Glide.with (getActivity ())
                                    .load (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE2))
                                    .listener (new RequestListener<String, GlideDrawable> () {
                                        @Override
                                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            progressBar2.setVisibility (View.GONE);
                                            return false;
                                        }
    
                                        @Override
                                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            progressBar2.setVisibility (View.GONE);
                                            return false;
                                        }
                                    })
                                    .into (iv2);
                            break;
                        }
                    }
                    
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE3).endsWith (ext)) {
                            rl3.setVisibility (View.VISIBLE);
                            tvNoImage.setVisibility (View.GONE);
                            Glide.with (getActivity ())
                                    .load (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE3))
                                    .listener (new RequestListener<String, GlideDrawable> () {
                                        @Override
                                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            progressBar3.setVisibility (View.GONE);
                                            return false;
                                        }
    
                                        @Override
                                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            progressBar3.setVisibility (View.GONE);
                                            return false;
                                        }
                                    })
                                    .into (iv3);
                            break;
                        }
                    }
    
                    JSONArray jsonArrayComments = new JSONArray (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_COMMENTS));
                    
                    for (int j = 0; j < jsonArrayComments.length (); j++) {
                        JSONObject jsonObjectComments = jsonArrayComments.getJSONObject (j);
                        serviceRequestCommentsList.add (j, new SwiggyServiceRequestComments (
                                jsonObjectComments.getInt (AppConfigTags.COMMENT_ID),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_FROM),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_TEXT),
                                jsonObjectComments.getInt (AppConfigTags.COMMENT_TYPE),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_CREATED_AT)
                        ));
                    }
                    serviceRequestCommentsAdapter.notifyDataSetChanged ();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    
        tvCloseRequest.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                ratingDialog ();
            }
        });
    
        tvCommentsReply.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                CommentReplyDialog ();
            }
        });
        rl5.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                SwiggyServiceAddNewRequestDialogFragment dialog = new SwiggyServiceAddNewRequestDialogFragment ()
                        .newInstance2 (1, Request_id, brand_name, description, serial_number, model_number, request_description, image1, image2, image3);
                dialog.show (ft, "Contacts");
                
            }
        });
    
    }
    
    public void CommentReplyDialog () {
        final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (getActivity ())
                .content ("Reply Comment")
                .contentColor (getResources ().getColor (R.color.primary_text2))
                .positiveColor (getResources ().getColor (R.color.primary_text2))
                .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                .inputRangeRes (1, 256, R.color.primary_text2)
                .alwaysCallInputCallback ()
                .canceledOnTouchOutside (true)
                .cancelable (true)
                .positiveText (R.string.dialog_action_proceed);
        
        
        mBuilder.input (null, null, new MaterialDialog.InputCallback () {
            @Override
            public void onInput (MaterialDialog dialog, CharSequence input) {
            }
        });
        
        mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
            @Override
            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ReplyCommentSentToServer (dialog.getInputEditText ().getText ().toString ());
            }
        });
        
        
        MaterialDialog dialog = mBuilder.build ();

//        if (config.smallestScreenWidthDp >= 600 && config.smallestScreenWidthDp <= 720) {
//            dialog.getActionButton (DialogAction.POSITIVE).setTextSize (TypedValue.COMPLEX_UNIT_SP, getResources ().getDimension (R.dimen.text_size_medium));
//            dialog.getContentView ().setTextSize (TypedValue.COMPLEX_UNIT_SP, getResources ().getDimension (R.dimen.text_size_medium));
//            dialog.getInputEditText ().setTextSize (TypedValue.COMPLEX_UNIT_SP, getResources ().getDimension (R.dimen.text_size_medium));
//        } else {
        // fall-back code goes here
//        }
        
        dialog.show ();
    }
    
    private void ratingDialog () {
        boolean wrapInScrollView = true;
        final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (getActivity ())
                .customView (R.layout.dialog_rating, wrapInScrollView)
                .positiveText ("Submit");
        MaterialDialog dialog = mBuilder.build ();
        final RatingBar ratingBar = (RatingBar) dialog.getCustomView ().findViewById (R.id.ratingBar);
        final EditText etComment = (EditText) dialog.getCustomView ().findViewById (R.id.etComment);
        mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
            @Override
            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                CloseRequestSentToServer (Request_id, String.valueOf (ratingBar.getRating ()), etComment.getText ().toString ());
            }
        });
        
        
        dialog.show ();
        
    }
    
    
    private void ReplyCommentSentToServer (final String replyComment) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_SERVICE_REQUEST_COMMENTS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_SERVICE_REQUEST_COMMENTS,
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
                                        Calendar c = Calendar.getInstance ();
                                        System.out.println ("Current time => " + c.getTime ());
                                        
                                        SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                                        
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        serviceRequestCommentsList.add (new SwiggyServiceRequestComments (
                                                0,
                                                "YOU",
                                                replyComment,
                                                0,
                                                df.format (c.getTime ())
                                        ));
                                        serviceRequestCommentsAdapter.notifyDataSetChanged ();
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                                
                            }
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_REQUEST_ID, String.valueOf (Request_id));
                    params.put (AppConfigTags.SWIGGY_COMMENT, replyComment);
                    
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
            Utils.sendRequest (strRequest1, 60);
        } else {
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
    
    public void setDismissListener (SwiggyMyProductDetailActivity.MyDialogCloseListener closeListener) {
        this.closeListener = closeListener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        if (closeListener != null) {
            closeListener.handleDialogClose (null);
        }
    }
    
    private void CloseRequestSentToServer (final int request_id, final String rating, final String comment) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_SERVICE_REQUEST_CLOSE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_SERVICE_REQUEST_CLOSE,
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
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        getDialog ().dismiss ();
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                                
                            }
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SWIGGY_REQUEST_ID, String.valueOf (request_id));
                    params.put (AppConfigTags.SWIGGY_COMMENT, comment);
                    params.put (AppConfigTags.SWIGGY_RATING, rating);
                    
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
            Utils.sendRequest (strRequest1, 60);
        } else {
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