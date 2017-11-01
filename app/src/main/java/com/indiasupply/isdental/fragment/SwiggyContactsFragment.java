package com.indiasupply.isdental.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyCompanyAdapter2;
import com.indiasupply.isdental.dialog.SwiggyCategoryFilterDialogFragment;
import com.indiasupply.isdental.dialog.SwiggyContactDetailDialogFragment;
import com.indiasupply.isdental.model.SwiggyCompany2;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
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
import java.util.List;
import java.util.Map;

public class SwiggyContactsFragment extends Fragment {
    RecyclerView rvContacts;
    CoordinatorLayout clMain;
    List<SwiggyCompany2> companyList = new ArrayList<> ();
    List<SwiggyCompany2> tempCompanyList = new ArrayList<> ();
    SwiggyCompanyAdapter2 companyAdapter;
    Button btFilter;
    Button btSearch;
    ImageView ivCancel;
    ImageView ivBack;
    RelativeLayout rlSearch;
    RelativeLayout rlToolbar;
    EditText etSearch;
    
    
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout rlMain;
    
    String filters = "";
    
    
    public static SwiggyContactsFragment newInstance () {
        return new SwiggyContactsFragment ();
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_contact, container, false);
        initView (rootView);
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvContacts = (RecyclerView) rootView.findViewById (R.id.rvContacts);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
        btSearch = (Button) rootView.findViewById (R.id.btSearch);
        etSearch = (EditText) rootView.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) rootView.findViewById (R.id.rlSearch);
        rlToolbar = (RelativeLayout) rootView.findViewById (R.id.rlToolbar);
        ivBack = (ImageView) rootView.findViewById (R.id.ivBack);
        ivCancel = (ImageView) rootView.findViewById (R.id.ivCancel);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        shimmerFrameLayout = (ShimmerFrameLayout) rootView.findViewById (R.id.shimmer_view_container);
        rlMain = (RelativeLayout) rootView.findViewById (R.id.rlMain);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvContacts);
    
        companyAdapter = new SwiggyCompanyAdapter2 (getActivity (), companyList);
        rvContacts.setAdapter (companyAdapter);
        rvContacts.setNestedScrollingEnabled (false);
        rvContacts.setFocusable (false);
        rvContacts.setHasFixedSize (true);
        rvContacts.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvContacts.setItemAnimator (new DefaultItemAnimator ());
        rvContacts.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                new SwiggyCategoryFilterDialogFragment ().newInstance (filters).show (ft, "Filter");
            }
        });
        companyAdapter.SetOnItemClickListener (new SwiggyCompanyAdapter2.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyCompany2 contact = companyList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                new SwiggyContactDetailDialogFragment ().newInstance (contact.getName (), contact.getContacts ()).show (ft, "Contacts");
            }
        });
        
        
/*
        getParentFragment ().getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction () != KeyEvent.ACTION_UP)
                        return true;
                    else {
                        if (rlSearch.getVisibility () == View.VISIBLE) {
                            final Handler handler = new Handler ();
                            handler.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    etSearch.setText ("");
                                }
                            }, 300);
                            final Handler handler2 = new Handler ();
                            handler2.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                                }
                            }, 600);
                            rlSearch.setVisibility (View.GONE);
                        } else {
                            getActivity ().onBackPressed ();
                        }
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
*/
    
    
        etSearch.addTextChangedListener (new TextWatcher () {
            
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0) {
                    ivCancel.setVisibility (View.VISIBLE);
                } else {
                    ivCancel.setVisibility (View.GONE);
                }
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count,
                                           int after) {
            
            }
        
            @Override
            public void afterTextChanged (Editable s) {
                tempCompanyList.clear ();
                for (SwiggyCompany2 swiggyCompany2 : companyList) {
                    if (swiggyCompany2.getName ().toUpperCase ().contains (s.toString ().toUpperCase ()) ||
                            swiggyCompany2.getName ().toLowerCase ().contains (s.toString ().toLowerCase ()) ||
                            swiggyCompany2.getCategory ().toLowerCase ().contains (s.toString ().toLowerCase ()) ||
                            swiggyCompany2.getCategory ().toUpperCase ().contains (s.toString ().toUpperCase ())) {
                        tempCompanyList.add (swiggyCompany2);
                    }
                }
            
                companyAdapter = new SwiggyCompanyAdapter2 (getActivity (), tempCompanyList);
                rvContacts.setAdapter (companyAdapter);
                companyAdapter.SetOnItemClickListener (new SwiggyCompanyAdapter2.OnItemClickListener () {
                    @Override
                    public void onItemClick (View view, int position) {
                        SwiggyCompany2 contact = tempCompanyList.get (position);
                        android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                        new SwiggyContactDetailDialogFragment ().newInstance (contact.getName (), contact.getContacts ()).show (ft, "Contacts");
                    }
                });
            
            }
        });
    
        ivBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                    }
                }, 1000);
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        etSearch.setText ("");
                        rlToolbar.setVisibility (View.VISIBLE);
                    }
                }, 300);
                rlSearch.setVisibility (View.GONE);
            }
        });
    
        btSearch.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        rlSearch.setVisibility (View.VISIBLE);
                        etSearch.requestFocus ();
                    }
                }, 300);
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }, 1000);
                rlToolbar.setVisibility (View.GONE);
            }
        });
    
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                etSearch.setText ("");
            }
        });
    }
    
    private void setData () {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SWIGGY_HOME_COMPANIES, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_SWIGGY_HOME_COMPANIES,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (getActivity () != null && isAdded ()) {
                                if (response != null) {
                                    companyList.clear ();
                                    try {
                                        JSONObject jsonObj = new JSONObject (response);
                                        boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                        String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                        if (! is_error) {
                                            JSONArray jsonArrayCompany = jsonObj.getJSONArray (AppConfigTags.SWIGGY_COMPANIES);
                                            filters = jsonObj.getJSONArray (AppConfigTags.SWIGGY_CATEGORY_FILTERS).toString ();
                                            for (int i = 0; i < jsonArrayCompany.length (); i++) {
                                                JSONObject jsonObjectCompany = jsonArrayCompany.getJSONObject (i);
                                                companyList.add (new SwiggyCompany2 (
                                                        jsonObjectCompany.getInt (AppConfigTags.SWIGGY_COMPANY_ID),
                                                        R.drawable.ic_person,
                                                        jsonObjectCompany.getJSONArray (AppConfigTags.SWIGGY_COMPANY_CONTACTS).length (),
                                                        jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_NAME),
                                                        jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_DESCRIPTION),
                                                        jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_CATEGORIES),
                                                        jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_EMAIL),
                                                        jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_WEBSITE),
                                                        jsonObjectCompany.getString (AppConfigTags.SWIGGY_COMPANY_IMAGE),
                                                        jsonObjectCompany.getJSONArray (AppConfigTags.SWIGGY_COMPANY_CONTACTS).toString ()));
                                            }
                                            companyAdapter.notifyDataSetChanged ();
                                            rlMain.setVisibility (View.VISIBLE);
                                            shimmerFrameLayout.setVisibility (View.GONE);
                                            new Handler ().postDelayed (new Runnable () {
                                                @Override
                                                public void run () {
                                                    btSearch.setVisibility (View.VISIBLE);
                                                    btFilter.setVisibility (View.VISIBLE);
                                                }
                                            }, 500);
                                        } else {
                                            Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace ();
                                        Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
                                } else {
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            }
                        }
                    }) {
    
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
            Utils.sendRequest (strRequest, 5);
        } else {
            if (getActivity () != null && isAdded ()) {
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
}