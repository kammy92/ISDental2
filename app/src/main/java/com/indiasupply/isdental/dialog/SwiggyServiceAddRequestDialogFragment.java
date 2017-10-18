package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.TypefaceSpan;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class SwiggyServiceAddRequestDialogFragment extends DialogFragment {
    ImageView ivCancel;
    TextView tvModelNumber;
    TextView tvSerialNumber;
    TextView tvSelectProduct;
    TextView tvCounter;
    CardView cv1;
    EditText etRequestDescription;
    CoordinatorLayout clMain;
    TextView tvSubmit;
    ProgressDialog progressDialog;
    
    String myProducts;
    int[] product_id_list;
    ArrayList<String> productNameList = new ArrayList<> ();
    
    int product_id;
    
    public static SwiggyServiceAddRequestDialogFragment newInstance (String myProducts) {
        SwiggyServiceAddRequestDialogFragment f = new SwiggyServiceAddRequestDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_PRODUCTS, myProducts);
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_add_request, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvModelNumber = (TextView) root.findViewById (R.id.tvModelNumber);
        tvSerialNumber = (TextView) root.findViewById (R.id.tvSerialNumber);
        tvSelectProduct = (TextView) root.findViewById (R.id.tvSelectProduct);
        tvCounter = (TextView) root.findViewById (R.id.tvCounter);
        cv1 = (CardView) root.findViewById (R.id.cv1);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
        tvSubmit = (TextView) root.findViewById (R.id.tvSubmit);
        etRequestDescription = (EditText) root.findViewById (R.id.etRequestDescription);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myProducts = bundle.getString (AppConfigTags.SWIGGY_PRODUCTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvSerialNumber);
        progressDialog = new ProgressDialog (getActivity ());
        try {
            JSONArray jsonArrayProducts = new JSONArray (myProducts);
            product_id_list = new int[jsonArrayProducts.length ()];
            for (int i = 0; i < jsonArrayProducts.length (); i++) {
                JSONObject jsonObjectProduct = jsonArrayProducts.getJSONObject (i);
                product_id_list[i] = jsonObjectProduct.getInt (AppConfigTags.SWIGGY_PRODUCT_ID);
                productNameList.add (jsonObjectProduct.getString (AppConfigTags.SWIGGY_PRODUCT_NAME) + "\nSR : " + jsonObjectProduct.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER));
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
    
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SpannableString s = new SpannableString (getResources ().getString (R.string.dialog_enter_request_description));
                s.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (etRequestDescription.getText ().toString ().trim ().length () == 0) {
                    etRequestDescription.setError (s);
                } else {
                    addRequestToServer (etRequestDescription.getText ().toString ().trim ());
                }
            }
        });
    
        tvSelectProduct.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (getActivity ())
                        .title ("Select a Product")
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .items (productNameList)
                        .itemsIds (product_id_list)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                product_id = view.getId ();
                                try {
                                    JSONArray jsonArrayProducts = new JSONArray (myProducts);
                                    for (int i = 0; i < jsonArrayProducts.length (); i++) {
                                        JSONObject jsonObjectProduct = jsonArrayProducts.getJSONObject (i);
                                        if (jsonObjectProduct.getInt (AppConfigTags.SWIGGY_PRODUCT_ID) == view.getId ()) {
                                            tvModelNumber.setText (jsonObjectProduct.getString (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER));
                                            tvSerialNumber.setText (jsonObjectProduct.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER));
                                            cv1.setVisibility (View.VISIBLE);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            }
                        })
                        .show ();
            }
        });
    
        etRequestDescription.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () < 256) {
                    tvCounter.setTextColor (getResources ().getColor (R.color.secondary_text2));
                } else {
                    tvCounter.setTextColor (getResources ().getColor (R.color.md_edittext_error));
                }
                tvCounter.setText (s.length () + "/255");
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    }
    
    private void addRequestToServer (final String request) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_ADD_REQUEST, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_ADD_REQUEST,
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
                    params.put (AppConfigTags.SWIGGY_PRODUCT_ID, String.valueOf (product_id));
                    params.put (AppConfigTags.SWIGGY_DESCRIPTION, request);
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