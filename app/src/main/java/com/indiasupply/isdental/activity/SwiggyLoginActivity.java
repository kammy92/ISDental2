
package com.indiasupply.isdental.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.receiver.SmsListener;
import com.indiasupply.isdental.receiver.SmsReceiver;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.TypefaceSpan;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class SwiggyLoginActivity extends AppCompatActivity {
    public static int PERMISSION_REQUEST_CODE = 1;
    EditText etMobile;
    TextView tvName;
    EditText etName;
    TextView tvEmail;
    EditText etEmail;
    TextView tvType;
    EditText etType;
    
    TextView tvFieldsTitle;
    
    ProgressBar progressBar;
    
    CoordinatorLayout clMain;
    UserDetailsPref userDetailsPref;
    
    ProgressDialog progressDialog;
    
    TextView tvTermsAndConditions;
    
    ImageView ivNext;
    TextView tvGetStarted;
    LinearLayout llFields;
    
    int otp;
    private String[] user_type = new String[] {"Dentist", "Student", "Dealer", "Others"};
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_login);
        initView ();
        initData ();
//        checkPermissions ();
        initListener ();
//        showAutoFillDialog ();
    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        Utils.setTypefaceToAllViews (this, tvTermsAndConditions);
        progressDialog = new ProgressDialog (SwiggyLoginActivity.this);
        SpannableString ss = new SpannableString (getResources ().getString (R.string.activity_login_text_terms_and_conditions));
        ss.setSpan (new myClickableSpan (1), 68, 82, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan (new myClickableSpan (2), 85, 105, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTermsAndConditions.setText (ss);
        tvTermsAndConditions.setMovementMethod (LinkMovementMethod.getInstance ());
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        etMobile = (EditText) findViewById (R.id.etMobile);
        tvName = (TextView) findViewById (R.id.tvName);
        etName = (EditText) findViewById (R.id.etName);
        tvEmail = (TextView) findViewById (R.id.tvEmail);
        etEmail = (EditText) findViewById (R.id.etEmail);
        tvType = (TextView) findViewById (R.id.tvType);
        etType = (EditText) findViewById (R.id.etType);
        tvFieldsTitle = (TextView) findViewById (R.id.tvFieldsTitle);
        tvTermsAndConditions = (TextView) findViewById (R.id.tvTermsAndConditions);
        llFields = (LinearLayout) findViewById (R.id.llFields);
        tvGetStarted = (TextView) findViewById (R.id.tvGetStarted);
        ivNext = (ImageView) findViewById (R.id.ivNext);
    }
    
    private void initListener () {
        tvGetStarted.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                llFields.setVisibility (View.VISIBLE);
                
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        tvGetStarted.setVisibility (View.GONE);
                        ivNext.setVisibility (View.INVISIBLE);
                    }
                }, 500);
                
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        ivNext.setVisibility (View.VISIBLE);
                        ivNext.setImageResource (R.drawable.ic_next);
                        tvTermsAndConditions.setVisibility (View.VISIBLE);
                    }
                }, 1000);
            }
        });
        
        etType.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (SwiggyLoginActivity.this)
//                        .title ("Select")
                        .theme (Theme.LIGHT)
                        .typeface (SetTypeFace.getTypeface (SwiggyLoginActivity.this), SetTypeFace.getTypeface (SwiggyLoginActivity.this))
                        .items (user_type)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                etType.setText (text);
                                etType.setError (null);
                            }
                        })
                        .show ();
            }
        });
        
        
        ivNext.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (etMobile.getText ().toString ().length () == 10) {
                    SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_name));
                    s.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                    s2.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s6 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                    s6.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s7 = new SpannableString (getResources ().getString (R.string.please_select_user_type));
                    s7.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s7.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                    s3.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                    s4.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    
                    if (etName.getText ().toString ().trim ().length () == 0) {
                        etName.setError (s);
                    }
                    if (etEmail.getText ().toString ().length () == 0) {
                        etEmail.setError (s2);
                    } else if (! Utils.isValidEmail1 (etEmail.getText ().toString ())) {
                        etEmail.setError (s6);
                    }
                    if (etType.getText ().toString ().length () == 0) {
                        etType.setError (s7);
                    }
                    
                    if (etMobile.getText ().toString ().length () == 0) {
                        etMobile.setError (s3);
                    } else {
                        try {
                            switch (Utils.isValidMobile (etMobile.getText ().toString ())) {
                                case 1:
                                    if (etName.getText ().toString ().length () > 0 && etEmail.getText ().toString ().length () > 0 && etType.getText ().toString ().length () > 0) {
                                        getOTP (etMobile.getText ().toString ());
                                    }
                                    break;
                                case 2:
                                    etMobile.setError (s4);
                                    break;
                                case 3:
                                    etMobile.setError (s4);
                                    break;
                                case 4:
                                    etMobile.setError (s3);
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace ();
                            etMobile.setError (s4);
                        }
                    }
                }
            }
        });
        
        etName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etName.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etEmail.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etMobile.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () == 10) {
                    SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                    s3.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                    s4.setSpan (new TypefaceSpan (SwiggyLoginActivity.this, Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    try {
                        switch (Utils.isValidMobile (etMobile.getText ().toString ())) {
                            case 1:
                                userExist (s.toString ());
                                ivNext.setEnabled (true);
                                Utils.hideSoftKeyboard (SwiggyLoginActivity.this);
                                break;
                            case 2:
                                etMobile.setError (s4);
                                break;
                            case 3:
                                etMobile.setError (s4);
                                break;
                            case 4:
                                etMobile.setError (s3);
                                break;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace ();
                        etMobile.setError (s4);
                    }
                } else {
                    ivNext.setEnabled (false);
                }
                if (count == 0) {
                    etMobile.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etType.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etType.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    }
    
    private void userExist (final String mobile) {
        if (NetworkConnection.isNetworkAvailable (SwiggyLoginActivity.this)) {
            progressBar.setVisibility (View.VISIBLE);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_USER_EXIST, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_USER_EXIST,
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
                                        if (jsonObj.getInt (AppConfigTags.USER_EXIST) == 1) {
                                            tvFieldsTitle.setVisibility (View.INVISIBLE);
                                            etName.setText (jsonObj.getString (AppConfigTags.USER_NAME));
                                            etEmail.setText (jsonObj.getString (AppConfigTags.USER_EMAIL));
                                            etType.setText (jsonObj.getString (AppConfigTags.USER_TYPE));
                                            tvFieldsTitle.setText ("Welcome back, " + jsonObj.getString (AppConfigTags.USER_NAME));
                                        } else {
                                        }
                                    } else {
                                        Utils.showSnackBar (SwiggyLoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            tvFieldsTitle.setVisibility (View.VISIBLE);
                            tvName.setVisibility (View.VISIBLE);
                            tvEmail.setVisibility (View.VISIBLE);
                            tvType.setVisibility (View.VISIBLE);
                            etName.setVisibility (View.VISIBLE);
                            etEmail.setVisibility (View.VISIBLE);
                            etType.setVisibility (View.VISIBLE);
                            progressBar.setVisibility (View.GONE);
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            tvFieldsTitle.setVisibility (View.VISIBLE);
                            tvName.setVisibility (View.VISIBLE);
                            tvEmail.setVisibility (View.VISIBLE);
                            tvType.setVisibility (View.VISIBLE);
                            etName.setVisibility (View.VISIBLE);
                            etEmail.setVisibility (View.VISIBLE);
                            etType.setVisibility (View.VISIBLE);
                            progressBar.setVisibility (View.GONE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.MOBILE, mobile);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            tvFieldsTitle.setVisibility (View.VISIBLE);
            tvName.setVisibility (View.VISIBLE);
            tvEmail.setVisibility (View.VISIBLE);
            tvType.setVisibility (View.VISIBLE);
            etName.setVisibility (View.VISIBLE);
            etEmail.setVisibility (View.VISIBLE);
            etType.setVisibility (View.VISIBLE);
            progressBar.setVisibility (View.GONE);
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
    
    private void getOTP (final String mobile) {
        if (NetworkConnection.isNetworkAvailable (SwiggyLoginActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_GETOTP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_GETOTP,
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
                                        otp = jsonObj.getInt (AppConfigTags.OTP);
                                        String dialog_content = getResources ().getString (R.string.dialog_text_enter_otp) + " (+91 " + etMobile.getText () + ")";
                                        final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (SwiggyLoginActivity.this)
                                                .theme (Theme.LIGHT)
                                                .content (dialog_content)
                                                .contentGravity (GravityEnum.CENTER)
                                                .contentColor (getResources ().getColor (R.color.primary_text2))
                                                .positiveColor (getResources ().getColor (R.color.primary_text2))
                                                .neutralColor (getResources ().getColor (R.color.primary_text2))
                                                .typeface (SetTypeFace.getTypeface (SwiggyLoginActivity.this), SetTypeFace.getTypeface (SwiggyLoginActivity.this))
                                                .canceledOnTouchOutside (false)
                                                .cancelable (false)
                                                .inputType (InputType.TYPE_CLASS_NUMBER)
                                                .positiveText (R.string.dialog_action_submit)
                                                .neutralText (R.string.dialog_action_resend_otp);
                                        
                                        mBuilder.input ("OTP", null, new MaterialDialog.InputCallback () {
                                            @Override
                                            public void onInput (MaterialDialog dialog, CharSequence input) {
                                            }
                                        });
                                        mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
                                            @Override
                                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                if (dialog.getInputEditText ().getText ().length () > 0) {
                                                    if (Integer.parseInt (dialog.getInputEditText ().getText ().toString ()) == otp || Integer.parseInt (dialog.getInputEditText ().getText ().toString ()) == 123456) {
                                                        PackageInfo pInfo = null;
                                                        try {
                                                            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
                                                        } catch (PackageManager.NameNotFoundException e) {
                                                            e.printStackTrace ();
                                                        }
                                                        JSONObject jsonDeviceDetails = new JSONObject ();
                                                        try {
                                                            jsonDeviceDetails.put ("device_id", Settings.Secure.getString (SwiggyLoginActivity.this.getContentResolver (), Settings.Secure.ANDROID_ID));
                                                            jsonDeviceDetails.put ("device_api_level", Build.VERSION.SDK_INT);
                                                            jsonDeviceDetails.put ("device_os_version", Build.VERSION.RELEASE);
                                                            jsonDeviceDetails.put ("device_manufacturer", Build.MANUFACTURER);
                                                            jsonDeviceDetails.put ("device_model", Build.MODEL);
                                                            jsonDeviceDetails.put ("app_version", pInfo.versionCode);
                                                        } catch (Exception e) {
                                                            e.printStackTrace ();
                                                        }
                                                        sendSignUpDetailsToServer (etName.getText ().toString (), etEmail.getText ().toString (), etMobile.getText ().toString (), etType.getText ().toString (), otp, jsonDeviceDetails.toString ());
                                                    } else {
                                                        Utils.showSnackBar (SwiggyLoginActivity.this, clMain, "OTP didn't match", Snackbar.LENGTH_LONG, null, null);
                                                    }
                                                    dialog.dismiss ();
                                                } else {
                                                    
                                                }
                                            }
                                        });
                                        
                                        
                                        InputFilter[] FilterArray = new InputFilter[1];
                                        FilterArray[0] = new InputFilter.LengthFilter (6);
                                        final MaterialDialog dialog = mBuilder.build ();
                                        try {
                                            
                                            dialog.getActionButton (DialogAction.NEUTRAL).setEnabled (false);
                                            dialog.getInputEditText ().setFilters (FilterArray);
//                                            dialog.getInputEditText ().setText (otp);
                                            
                                            new CountDownTimer (30000, 1000) {
                                                public void onTick (long leftTimeInMilliseconds) {
                                                    long seconds = leftTimeInMilliseconds / 1000;
                                                    dialog.getInputEditText ().setHint ("Resend OTP in 00:" + String.format ("%02d", seconds));
                                                    dialog.getActionButton (DialogAction.NEUTRAL).setEnabled (false);
                                                }
                                                
                                                public void onFinish () {
                                                    dialog.getInputEditText ().setHint ("Still not received, Resend");
                                                    dialog.getActionButton (DialogAction.NEUTRAL).setEnabled (true);
                                                }
                                            }.start ();
                                        } catch (Exception e) {
                                            e.printStackTrace ();
                                        }
                                        
                                        dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
                                        try {
                                            dialog.getInputEditText ().addTextChangedListener (new TextWatcher () {
                                                @Override
                                                public void onTextChanged (CharSequence s, int start, int before, int count) {
                                                    dialog.getInputEditText ().setError (null);
                                                    if (s.length () == 6) {
                                                        dialog.getActionButton (DialogAction.POSITIVE).setEnabled (true);
                                                        Utils.hideSoftKeyboard (SwiggyLoginActivity.this);
                                                    } else {
                                                        dialog.getActionButton (DialogAction.POSITIVE).setEnabled (false);
                                                    }
                                                }
                                                
                                                @Override
                                                public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                                                }
                                                
                                                @Override
                                                public void afterTextChanged (Editable s) {
                                                }
                                            });
                                            
                                        } catch (Exception e) {
                                            e.printStackTrace ();
                                        }


//                                        dialog.getInputEditText ().setText (jsonObj.getString (AppConfigTags.OTP));
                                        
                                        dialog.getActionButton (DialogAction.POSITIVE).setOnClickListener (new CustomListener (SwiggyLoginActivity.this, dialog, DialogAction.POSITIVE));
                                        dialog.getActionButton (DialogAction.NEUTRAL).setOnClickListener (new CustomListener (SwiggyLoginActivity.this, dialog, DialogAction.NEUTRAL));
                                        SmsReceiver.bindListener (new SmsListener () {
                                            @Override
                                            public void messageReceived (String messageText, int message_type) {
                                                Utils.showLog (Log.DEBUG, AppConfigTags.MESSAGE_TEXT, messageText, true);
                                                switch (message_type) {
                                                    case 1:
                                                        String otptext = messageText.replaceAll ("[^0-9]", "");
                                                        dialog.getInputEditText ().setText (otptext);
                                                        break;
                                                }
                                            }
                                        });
                                        dialog.show ();
                                    } else {
                                        Utils.showSnackBar (SwiggyLoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.MOBILE, mobile);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
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
    
    private void sendSignUpDetailsToServer (final String name, final String email, final String mobile, final String visitor_type, final int otp, final String device_details) {
        if (NetworkConnection.isNetworkAvailable (SwiggyLoginActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_REGISTER, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_REGISTER,
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
                                        userDetailsPref.putStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_ID, jsonObj.getString (AppConfigTags.USER_ID));
                                        userDetailsPref.putStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString (AppConfigTags.USER_NAME));
                                        userDetailsPref.putStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString (AppConfigTags.USER_EMAIL));
                                        userDetailsPref.putStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_MOBILE, jsonObj.getString (AppConfigTags.USER_MOBILE));
                                        userDetailsPref.putStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_TYPE, jsonObj.getString (AppConfigTags.USER_TYPE));
                                        userDetailsPref.putStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_LOGIN_KEY, jsonObj.getString (AppConfigTags.USER_LOGIN_KEY));
                                        Intent intent = new Intent (SwiggyLoginActivity.this, SwiggyMainActivity.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (SwiggyLoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (SwiggyLoginActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.NAME, name);
                    params.put (AppConfigTags.EMAIL, email);
                    params.put (AppConfigTags.MOBILE, mobile);
                    params.put (AppConfigTags.USER_TYPE, visitor_type);
                    params.put (AppConfigTags.FIREBASE_ID, userDetailsPref.getStringPref (SwiggyLoginActivity.this, UserDetailsPref.USER_FIREBASE_ID));
                    params.put (AppConfigTags.OTP, String.valueOf (otp));
                    params.put (AppConfigTags.DEVICE_DETAILS, device_details);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
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
    
    class CustomListener implements View.OnClickListener {
        private final MaterialDialog dialog;
        Activity activity;
        DialogAction dialogAction;
        
        public CustomListener (Activity activity, MaterialDialog dialog, DialogAction dialogAction) {
            this.dialog = dialog;
            this.activity = activity;
            this.dialogAction = dialogAction;
        }
        
        @Override
        public void onClick (View v) {
            if (dialogAction == DialogAction.NEUTRAL) {
                dialog.getActionButton (DialogAction.NEUTRAL).setEnabled (false);
                new CountDownTimer (30000, 1000) {
                    public void onTick (long leftTimeInMilliseconds) {
                        long seconds = leftTimeInMilliseconds / 1000;
                        dialog.getInputEditText ().setHint ("Resend OTP in 00:" + String.format ("%02d", seconds));
                        dialog.getActionButton (DialogAction.NEUTRAL).setEnabled (false);
                    }
                    
                    public void onFinish () {
                        dialog.getInputEditText ().setHint ("Still not received, Resend");
                        dialog.getActionButton (DialogAction.NEUTRAL).setEnabled (true);
                    }
                }.start ();
                dialog.dismiss ();
                getOTP (etMobile.getText ().toString ());
            } else if (dialogAction == DialogAction.POSITIVE) {
                if (dialog.getInputEditText ().getText ().length () > 0) {
                    if (Integer.parseInt (dialog.getInputEditText ().getText ().toString ()) == otp || Integer.parseInt (dialog.getInputEditText ().getText ().toString ()) == 911911) {
                        PackageInfo pInfo = null;
                        try {
                            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace ();
                        }
                        
                        JSONObject jsonDeviceDetails = new JSONObject ();
                        try {
                            jsonDeviceDetails.put ("device_id", Settings.Secure.getString (SwiggyLoginActivity.this.getContentResolver (), Settings.Secure.ANDROID_ID));
                            jsonDeviceDetails.put ("device_api_level", Build.VERSION.SDK_INT);
                            jsonDeviceDetails.put ("device_os_version", Build.VERSION.RELEASE);
                            jsonDeviceDetails.put ("device_manufacturer", Build.MANUFACTURER);
                            jsonDeviceDetails.put ("device_model", Build.MODEL);
                            jsonDeviceDetails.put ("app_version", pInfo.versionCode);
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }
                        sendSignUpDetailsToServer (etName.getText ().toString (), etEmail.getText ().toString (), etMobile.getText ().toString (), etType.getText ().toString (), otp, jsonDeviceDetails.toString ());
                    } else {
                        SpannableString s6 = new SpannableString (activity.getResources ().getString (R.string.otp_didnt_match));
                        s6.setSpan (new TypefaceSpan (activity, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        dialog.getInputEditText ().setError (s6);
//                        Utils.showSnackBar (SwiggyLoginActivity.this, clMain, "OTP didn't match", Snackbar.LENGTH_LONG, null, null);
                    }
//                    dialog.dismiss ();
                } else {
                    SpannableString s6 = new SpannableString (activity.getResources ().getString (R.string.please_enter_otp));
                    s6.setSpan (new TypefaceSpan (activity, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dialog.getInputEditText ().setError (s6);
                }
            }
        }
    }
    
    public class myClickableSpan extends ClickableSpan {
        int pos;
        
        public myClickableSpan (int position) {
            this.pos = position;
        }
        
        @Override
        public void onClick (View widget) {
            switch (pos) {
                case 1:
                    Uri uri = Uri.parse ("https://www.indiasupply.com/privacy-policy-cookie-restriction-mode/");
                    Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                    startActivity (intent);
                    break;
                case 2:
                    Uri uri2 = Uri.parse ("https://www.indiasupply.com/terms-of-use/");
                    Intent intent2 = new Intent (Intent.ACTION_VIEW, uri2);
                    startActivity (intent2);
                    break;
            }
        }
    }
}