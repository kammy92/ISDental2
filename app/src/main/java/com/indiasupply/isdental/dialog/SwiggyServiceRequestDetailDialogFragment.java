package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyServiceRequestCommentsAdapter;
import com.indiasupply.isdental.model.SwiggyServiceRequestComments;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SwiggyServiceRequestDetailDialogFragment extends DialogFragment {
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
    TextView tvAddNewRequest;
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
        
        iv1 = (ImageView) root.findViewById (R.id.iv1);
        iv2 = (ImageView) root.findViewById (R.id.iv2);
        iv3 = (ImageView) root.findViewById (R.id.iv3);
        
        rvCommentsList = (RecyclerView) root.findViewById (R.id.rvCommentsList);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        Response = bundle.getString ("Response");
        Request_id = bundle.getInt ("Request_id");
        
        
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvComments);
        Utils.setTypefaceToAllViews (getActivity (), tvCommentsReply);
        Utils.setTypefaceToAllViews (getActivity (), tvImages);
        
        
        try {
            JSONObject jsonObject = new JSONObject (Response);
            tvServiceRequestName.setText (jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_BRAND) + " - " + jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION) + " (" + jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER) + ")");
            tvServiceRequestModelNumber.setText (jsonObject.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER));
            JSONArray jsonArrayRequest = jsonObject.getJSONArray (AppConfigTags.SWIGGY_REQUESTS);
            
            for (int i = 0; i < jsonArrayRequest.length (); i++) {
                JSONObject jsonObjectRequest = jsonArrayRequest.getJSONObject (i);
                if (jsonObjectRequest.getInt (AppConfigTags.SWIGGY_REQUEST_ID) == Request_id) {
                    tvRequestDescription.setText ("Request Description- " + jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_DESCRIPTION));
                    
                    boolean flag = false;
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        
                        if (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE1).endsWith (ext)) {
                            new getBitmapFromURL ().execute (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE1));
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
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
                    } else {
                        rl1.setVisibility (View.GONE);
                        tvNoImage.setVisibility (View.VISIBLE);
                    }
                    
                    boolean flag1 = false;
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE2).endsWith (ext)) {
                            new getBitmapFromURL ().execute (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE2));
                            flag1 = true;
                            break;
                        }
                    }
                    if (flag1) {
                        
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
                    } else {
                        rl2.setVisibility (View.GONE);
                        tvNoImage.setVisibility (View.VISIBLE);
                    }
                    
                    
                    boolean flag3 = false;
                    for (String ext : new String[] {".png", ".jpg", ".jpeg"}) {
                        if (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE3).endsWith (ext)) {
                            new getBitmapFromURL ().execute (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE3));
                            flag3 = true;
                            break;
                        }
                    }
                    if (flag3) {
                        
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
                    } else {
                        rl3.setVisibility (View.GONE);
                        tvNoImage.setVisibility (View.VISIBLE);
                    }
                    
                    JSONArray jsonArrayComments = new JSONArray (jsonObjectRequest.getString (AppConfigTags.SWIGGY_REQUEST_COMMENTS));
                    
                    for (int j = 0; j < jsonArrayComments.length (); j++) {
                        JSONObject jsonObjectComments = jsonArrayComments.getJSONObject (j);
                        serviceRequestCommentsList.add (j, new SwiggyServiceRequestComments (
                                jsonObjectComments.getInt (AppConfigTags.COMMENT_ID),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_FROM),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_TEXT),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_TYPE),
                                jsonObjectComments.getString (AppConfigTags.COMMENT_CREATED_AT)
                        
                        ));
                        serviceRequestCommentsAdapter = new SwiggyServiceRequestCommentsAdapter (getActivity (), serviceRequestCommentsList);
                        rvCommentsList.setAdapter (serviceRequestCommentsAdapter);
                        rvCommentsList.setHasFixedSize (true);
                        rvCommentsList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
                        rvCommentsList.setItemAnimator (new DefaultItemAnimator ());
                        rvCommentsList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 5), (int) Utils.pxFromDp (getActivity (), 5), (int) Utils.pxFromDp (getActivity (), 10), (int) Utils.pxFromDp (getActivity (), 5), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
                        
                        
                    }
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