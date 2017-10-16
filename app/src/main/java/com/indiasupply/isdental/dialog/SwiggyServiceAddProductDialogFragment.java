package com.indiasupply.isdental.dialog;

import android.app.DatePickerDialog;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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
import com.indiasupply.isdental.model.SwiggyBrand;
import com.indiasupply.isdental.model.SwiggyCategory;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.ApplicationDetailsPref;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class SwiggyServiceAddProductDialogFragment extends DialogFragment {
    
    ImageView ivCancel;
    EditText etProductCategory;
    EditText etModelNo;
    EditText etSerialNo;
    EditText etPurchaseDate;
    EditText etBrand;
    TextView tvSubmit;
    TextView tvTitle;
    
    int[] category_id;
    int[] brand_id;
    int category_item_id;
    int brand_item_id;
    
    ProgressDialog progressDialog;
    List<SwiggyCategory> categoryList = new ArrayList<> ();
    List<SwiggyBrand> brandList = new ArrayList<> ();
    String date = "";
    ArrayList<String> categorynamelist = new ArrayList<> ();
    ArrayList<String> brandnamelist = new ArrayList<> ();
    ApplicationDetailsPref applicationDetailsPref;
    CoordinatorLayout clMain;
    private int mYear, mMonth, mDay;
    
    public static SwiggyServiceAddProductDialogFragment newInstance (int contact_id) {
        SwiggyServiceAddProductDialogFragment f = new SwiggyServiceAddProductDialogFragment ();
        // Supply num input as an argument.
        Bundle args = new Bundle ();
        // args.putInt("contact_id", contact_id);
        //args.putString("contact_name", contact_name);
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_add_product, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        etProductCategory = (EditText) root.findViewById (R.id.etProductCategory);
        etSerialNo = (EditText) root.findViewById (R.id.etSerialNo);
        etPurchaseDate = (EditText) root.findViewById (R.id.etPurchaseDate);
        etModelNo = (EditText) root.findViewById (R.id.etModelNo);
        etBrand = (EditText) root.findViewById (R.id.etProductBrand);
        tvSubmit = (TextView) root.findViewById (R.id.tvSubmit);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        applicationDetailsPref = ApplicationDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (getActivity ());
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        try {
            JSONArray jsonArrayBrands = new JSONArray (applicationDetailsPref.getStringPref (getActivity (), ApplicationDetailsPref.SWIGGY_BRANDS));
            brand_id = new int[jsonArrayBrands.length ()];
            for (int i = 0; i < jsonArrayBrands.length (); i++) {
                JSONObject jsonObjectBrands = jsonArrayBrands.getJSONObject (i);
                brandList.add (new SwiggyBrand (jsonObjectBrands.getInt (AppConfigTags.SWIGGY_BRAND_ID), jsonObjectBrands.getString (AppConfigTags.SWIGGY_BRAND_NAME)));
                brand_id[i] = jsonObjectBrands.getInt (AppConfigTags.SWIGGY_BRAND_ID);
                brandnamelist.add (jsonObjectBrands.getString (AppConfigTags.SWIGGY_BRAND_NAME));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    
        try {
            JSONArray jsonArrayCategory = new JSONArray (applicationDetailsPref.getStringPref (getActivity (), ApplicationDetailsPref.SWIGGY_CATEGORIES));
            category_id = new int[jsonArrayCategory.length ()];
            for (int j = 0; j < jsonArrayCategory.length (); j++) {
                JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject (j);
                categoryList.add (new SwiggyCategory (
                        jsonObjectCategory.getInt (AppConfigTags.SWIGGY_CATEGORY_ID),
                        jsonObjectCategory.getString (AppConfigTags.SWIGGY_CATEGORY_NAME),
                        jsonObjectCategory.getString (AppConfigTags.SWIGGY_CATEGORY_IMAGE)
                ));
                categorynamelist.add (jsonObjectCategory.getString (AppConfigTags.CATEGORY_NAME));
                category_id[j] = jsonObjectCategory.getInt (AppConfigTags.CATEGORY_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }

      /*  categoryList.add ("Category 1");
        categoryList.add ("Category 2");
        categoryList.add ("Category 3");
        categoryList.add ("Category 4");
        categoryList.add ("Category 5");
        categoryList.add ("Category 6");
    
        brandList.add ("Brand 1");
        brandList.add ("Brand 2");
        brandList.add ("Brand 3");
        brandList.add ("Brand 4");
        brandList.add ("Brand 5");*/
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        etProductCategory.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (getActivity ())
                        .title ("Select a Category")
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .items (categorynamelist)
                        .itemsIds (category_id)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                etProductCategory.setText (text);
                                category_item_id = view.getId ();
                            }
                        })
                        .show ();
            }
        });
    
        etBrand.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (getActivity ())
                        .title ("Select a Brand")
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .items (brandnamelist)
                        .itemsIds (brand_id)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                brand_item_id = view.getId ();
                                etBrand.setText (text);
                            }
                        })
                        .show ();
            }
        });
        
        etPurchaseDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final Calendar c = Calendar.getInstance ();
                mYear = c.get (Calendar.YEAR);
                mMonth = c.get (Calendar.MONTH);
                mDay = c.get (Calendar.DAY_OF_MONTH);
                
                DatePickerDialog datePickerDialog = new DatePickerDialog (getActivity (), new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etPurchaseDate.setText (String.format ("%02d", dayOfMonth) + "/" + String.format ("%02d", monthOfYear + 1) + "/" + year);
                        date = etPurchaseDate.getText ().toString ().trim ();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show ();
            }
            
        });
    
    
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SpannableString s = new SpannableString (getResources ().getString (R.string.dialog_enter_product_brand));
                s.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.dialog_select_product_category));
                s2.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.dialog_enter_model_number));
                s3.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.dialog_enter_serial_number));
                s4.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.dialog_select_purchase_date));
                s5.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
                if (etBrand.getText ().toString ().trim ().length () == 0 && etProductCategory.getText ().toString ().trim ().length () == 0
                        && etModelNo.getText ().toString ().trim ().length () == 0 && etSerialNo.getText ().toString ().trim ().length () == 0
                        && etPurchaseDate.getText ().toString ().trim ().length () == 0) {
                    etBrand.setError (s);
                    etProductCategory.setError (s2);
                    etModelNo.setError (s3);
                    etSerialNo.setError (s4);
                    etPurchaseDate.setError (s5);
                } else if (etBrand.getText ().toString ().trim ().length () == 0) {
                    etBrand.setError (s);
                } else if (etProductCategory.getText ().toString ().trim ().length () == 0) {
                    etProductCategory.setError (s);
                } else if (etModelNo.getText ().toString ().trim ().length () == 0) {
                    etModelNo.setError (s);
                } else if (etSerialNo.getText ().toString ().trim ().length () == 0) {
                    etSerialNo.setError (s);
                } else if (etPurchaseDate.getText ().toString ().trim ().length () == 0) {
                    etPurchaseDate.setError (s);
                } else {
                    addProductToServer (etBrand.getText ().toString ().trim (), etProductCategory.getText ().toString ().trim (), etModelNo.getText ().toString ().trim (), etSerialNo.getText ().toString ().trim (), Utils.convertTimeFormat (date, "dd/MM/yyyy", "yyyy-MM-dd"));
                }
            }
        });
    }
    
    private void addProductToServer (final String brand, final String category, final String model, final String serial_number, final String purchase_date) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SWIGGY_PRODUCT, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SWIGGY_PRODUCT,
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
                    params.put (AppConfigTags.SWIGGY_BRAND_ID, String.valueOf (brand_item_id));
                    params.put (AppConfigTags.SWIGGY_BRAND_NAME, brand);
                    params.put (AppConfigTags.SWIGGY_CATEGORY_ID, String.valueOf (category_item_id));
                    params.put (AppConfigTags.SWIGGY_CATEGORY_NAME, category);
                    params.put (AppConfigTags.SWIGGY_MODEL_NUMBER, model);
                    params.put (AppConfigTags.SWIGGY_SERIAL_NUMBER, serial_number);
                    params.put (AppConfigTags.SWIGGY_PURCHASE_DATE, purchase_date);
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