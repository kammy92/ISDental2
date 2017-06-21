package com.indiasupply.isdental.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.CompanyListAdapter;
import com.indiasupply.isdental.helper.DatabaseHandler;
import com.indiasupply.isdental.model.Banner;
import com.indiasupply.isdental.model.Company;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.AppConfigURL;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.CustomImageSlider;
import com.indiasupply.isdental.utils.NetworkConnection;
import com.indiasupply.isdental.utils.SimpleDividerItemDecoration;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class CompanyListActivity extends AppCompatActivity {
    RecyclerView rvBrandList;
    RelativeLayout rlBack;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Company> companyList = new ArrayList<> ();
    List<Company> tempCompanyList = new ArrayList<> ();
    CompanyListAdapter companyListAdapter;
    String[] category;
    List<Banner> bannerlist = new ArrayList<> ();
    ImageView ivFilter;
    ImageView ivSort;
    TextView tvTitle;
    SearchView searchView;
    
    CoordinatorLayout clMain;
    TextView tvNoResult;
    
    
    String filterCategory = "";
    String filterSubCategory = "";

//    Dialog dialog;
DatabaseHandler db;
    private SliderLayout slider;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_company_list);
        initView ();
        initData ();
        initListener ();
        getCompanyList ();
        initSlider ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rvBrandList = (RecyclerView) findViewById (R.id.rvBrandList);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        ivFilter = (ImageView) findViewById (R.id.ivFilter);
        tvNoResult = (TextView) findViewById (R.id.tvNoResult);
        
        ivSort = (ImageView) findViewById (R.id.ivSort);
        tvTitle = (TextView) findViewById (R.id.tvType);
        searchView = (SearchView) findViewById (R.id.searchView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipeRefreshLayout);
        Utils.setTypefaceToAllViews (this, tvTitle);
        
        slider = (SliderLayout) findViewById (R.id.slider);
    }
    
    private void initData () {
        db = new DatabaseHandler (getApplicationContext ());
        swipeRefreshLayout.setRefreshing (true);
        
        swipeRefreshLayout.setColorSchemeColors (getResources ().getColor (R.color.colorPrimaryDark));
        
        searchView.setQueryHint (Html.fromHtml ("<font color = #ffffff>" + "Search" + "</font>"));
        
        companyListAdapter = new CompanyListAdapter (CompanyListActivity.this, companyList);
        rvBrandList.setAdapter (companyListAdapter);
        rvBrandList.setHasFixedSize (true);
        rvBrandList.setLayoutManager (new LinearLayoutManager (CompanyListActivity.this, LinearLayoutManager.VERTICAL, false));
        rvBrandList.addItemDecoration (new SimpleDividerItemDecoration (CompanyListActivity.this));
        rvBrandList.setItemAnimator (new DefaultItemAnimator ());
    }
    
    private void initSlider () {
        slider.removeAllSliders ();
        for (int i = 0; i < db.getAllBrandsBanners ().size (); i++) {
            final Banner banner = db.getAllBrandsBanners ().get (i);
            CustomImageSlider slider2 = new CustomImageSlider (this);
            slider2
                    .image (banner.getImage ())
                    .setScaleType (BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
                        @Override
                        public void onSliderClick (BaseSliderView slider) {
                            Uri uri;
                            if (banner.getUrl ().contains ("http://") || banner.getUrl ().contains ("https://")) {
                                uri = Uri.parse (banner.getUrl ());
                            } else {
                                uri = Uri.parse ("http://" + banner.getUrl ());
                            }
                            Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                            startActivity (intent);
                        }
                    });

//            DefaultSliderView defaultSliderView = new DefaultSliderView (activity);
//            defaultSliderView
//                    .image (image)
//                    .setScaleType (BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
//                        @Override
//                        public void onSliderClick (BaseSliderView slider) {
//                            Intent intent = new Intent (activity, PropertyDetailActivity.class);
//                            intent.putExtra (AppConfigTags.PROPERTY_ID, property.getId ());
//                            activity.startActivity (intent);
//                            activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
//                        }
//                    });
//
//            defaultSliderView.bundle (new Bundle ());
            // defaultSliderView.getBundle ().putString ("extra", String.valueOf (s));
//            holder.slider.addSlider (defaultSliderView);
            slider.addSlider (slider2);
        }
        slider.getPagerIndicator ().setVisibility (View.GONE);
        slider.setPresetTransformer (SliderLayout.Transformer.Fade);
        slider.setCustomAnimation (new DescriptionAnimation ());
        slider.setDuration (5000);
        slider.addOnPageChangeListener (new ViewPagerEx.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
            }
            
            @Override
            public void onPageSelected (int position) {
            }
            
            @Override
            public void onPageScrollStateChanged (int state) {
                switch (state) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        slider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
    }
    
    private void initListener () {
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                swipeRefreshLayout.setRefreshing (true);
                getCompanyList ();
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

     /*
        ivFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                new MaterialDialog.Builder (CompanyListActivity.this)
                        .title ("Select Category")
                        .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                        .items (db.getAllCategoryName ())
                        .typeface (SetTypeFace.getTypeface (CompanyListActivity.this), SetTypeFace.getTypeface (CompanyListActivity.this))
                        .canceledOnTouchOutside (true)
                        .cancelable (true)
                        .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                        .positiveText ("RESET FILTER")
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                filterCategory = "";
                                filterSubCategory = "";
                                companyList.clear ();
                                ArrayList<Company> offlineBrand = db.getAllBrandList ();
                                for (Company Company : offlineBrand)
                                    companyList.add (Company);

                                CompanyListAdapter = new CompanyListAdapter (CompanyListActivity.this, companyList);
                                rvBrandList.setAdapter (CompanyListAdapter);

                                CompanyListAdapter.notifyDataSetChanged ();
                                ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter));
                            }
                        })
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                filterCategory = text.toString ();
                                ArrayList<String> level2CategoryList = db.getAllCategoryLevel2 (text.toString ());
                                if (level2CategoryList.size () > 1) {
                                    new MaterialDialog.Builder (CompanyListActivity.this)
                                            .title ("Select Sub Category")
                                            .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                                            .items (db.getAllCategoryLevel2 (text.toString ()))
                                            .typeface (SetTypeFace.getTypeface (CompanyListActivity.this), SetTypeFace.getTypeface (CompanyListActivity.this))
                                            .canceledOnTouchOutside (true)
                                            .cancelable (true)
                                            .itemsCallback (new MaterialDialog.ListCallback () {
                                                @Override
                                                public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                                    filterSubCategory = text.toString ();
//                                                    Utils.showToast (CompanyListActivity.this, "Filter\n Category : " + filterCategory + "\nSub Category : " + filterSubCategory, true);
//                                                    Utils.showToast (CompanyListActivity.this, "Category Ids : " + db.getAllFilteredCategoryIds (filterCategory, filterSubCategory) + "\nCompany Ids : " + db.getAllFilteredBrandIds (filterCategory, filterSubCategory), true);

                                                    companyList.clear ();
                                                    ArrayList<Company> offlineBrand = db.getAllFilteredBrandList (filterCategory, filterSubCategory);
                                                    for (Company Company : offlineBrand)
                                                        companyList.add (Company);
                                                    CompanyListAdapter.notifyDataSetChanged ();
                                                    ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter_checked));
                                                }
                                            })
                                            .show ();
                                } else {
                                    filterSubCategory = level2CategoryList.get (0);
//                                    Utils.showToast (CompanyListActivity.this, "Filter\n Category : " + filterCategory + "\nSub Category : " + filterSubCategory, true);
//                                    Utils.showToast (CompanyListActivity.this, "Category Ids : " + db.getAllFilteredCategoryIds (filterCategory, filterSubCategory) + "\nCompany Ids : " + db.getAllFilteredBrandIds (filterCategory, filterSubCategory), true);
                                    companyList.clear ();
                                    ArrayList<Company> offlineBrand = db.getAllFilteredBrandList (filterCategory, filterSubCategory);
                                    for (Company Company : offlineBrand)
                                        companyList.add (Company);
                                    CompanyListAdapter.notifyDataSetChanged ();
                                    ivFilter.setImageDrawable (getResources ().getDrawable (R.drawable.ic_filter_checked));
                                }
                            }
                        })
                        .show ();
    /*

                boolean wrapInScrollView = true;
                final MaterialDialog dialog = new MaterialDialog.Builder (CompanyListActivity.this)
                        .customView (R.layout.dialog_filter, wrapInScrollView)
                        .positiveText ("APPLY")
                        .negativeText ("CANCEL")
                        .canceledOnTouchOutside (false)
                        .cancelable (false)
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ivFilter.setImageResource (R.drawable.ic_filter_checked);
                            }
                        })
                        .typeface (SetTypeFace.getTypeface (CompanyListActivity.this, Constants.font_name), SetTypeFace.getTypeface (CompanyListActivity.this, Constants.font_name))
                        .show ();


                //Spinner spCategory=(Spinner)dialog.findViewById(R.id.spCategoryType);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompanyListActivity.this, android.R.layout.simple_spinner_item, category);
                //spCategory.setAdapter(adapter);


                LinearLayout llCategory = (LinearLayout) dialog.findViewById (R.id.llCategory);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams (
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                Utils.setTypefaceToAllViews (CompanyListActivity.this, llCategory);

                for (int i = 0; i < category.length; i++) {
                    CheckBox checkBox = new CheckBox (CompanyListActivity.this);
                    checkBox.setLayoutParams (lparams);
                    checkBox.setText (category[i]);
                    llCategory.addView (checkBox);

                }
                */
         /*   }
        });*/
    
        searchView.setOnSearchClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
//                Toast.makeText (CompanyListActivity.this, "karman open", Toast.LENGTH_SHORT).show ();
//                ivFilter.setVisibility (View.GONE);
                rlBack.setVisibility (View.GONE);
//                ivSort.setVisibility (View.GONE);
                tvTitle.setVisibility (View.GONE);
            }
        });
    
        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit (String query) {
                return true;
            }
        
            @Override
            public boolean onQueryTextChange (String newText) {
                tempCompanyList.clear ();
                for (Company company : companyList) {
                    if (company.getName ().toUpperCase ().contains (newText.toUpperCase ()) ||
                            company.getName ().toLowerCase ().contains (newText.toLowerCase ()) ||
                            company.getBrands ().toLowerCase ().contains (newText.toLowerCase ()) ||
                            company.getBrands ().toUpperCase ().contains (newText.toUpperCase ())) {
                        tempCompanyList.add (company);
                    }
                }
                companyListAdapter = new CompanyListAdapter (CompanyListActivity.this, tempCompanyList);
                rvBrandList.setAdapter (companyListAdapter);
                rvBrandList.setHasFixedSize (true);
                rvBrandList.setLayoutManager (new LinearLayoutManager (CompanyListActivity.this, LinearLayoutManager.VERTICAL, false));
                rvBrandList.addItemDecoration (new SimpleDividerItemDecoration (CompanyListActivity.this));
                rvBrandList.setItemAnimator (new DefaultItemAnimator ());
                return true;
            }
        });
    
        searchView.setOnCloseListener (new SearchView.OnCloseListener () {
            @Override
            public boolean onClose () {
//                Toast.makeText (CompanyListActivity.this, "karman close", Toast.LENGTH_SHORT).show ();
//                ivFilter.setVisibility (View.VISIBLE);
                rlBack.setVisibility (View.VISIBLE);
//                ivSort.setVisibility (View.VISIBLE);
                tvTitle.setVisibility (View.VISIBLE);
                return false;
            }
        });
    }
    
    private void getCompanyList () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            tvNoResult.setVisibility (View.GONE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_COMPANY_LIST, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_COMPANY_LIST,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            companyList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! is_error) {
                                        JSONArray jsonArrayBrand = jsonObj.getJSONArray (AppConfigTags.COMPANIES);
                                        for (int i = 0; i < jsonArrayBrand.length (); i++) {
                                            JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject (i);
                                            Company company = new Company (
                                                    jsonObjectBrand.getInt (AppConfigTags.COMPANY_ID), "",
                                                    jsonObjectBrand.getString (AppConfigTags.COMPANY_NAME),
                                                    jsonObjectBrand.getString (AppConfigTags.COMPANY_DESCRIPTION),
                                                    jsonObjectBrand.getString (AppConfigTags.COMPANY_BRANDS)
                                            );
                                            companyList.add (i, company);
    
                                        }
                                        companyListAdapter.notifyDataSetChanged ();
                                        if (jsonArrayBrand.length () == 0) {
                                            tvNoResult.setVisibility (View.VISIBLE);
                                        }
                                    } else {
                                        tvNoResult.setVisibility (View.VISIBLE);
                                        Utils.showSnackBar (CompanyListActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    swipeRefreshLayout.setRefreshing (false);
                                } catch (Exception e) {
                                    swipeRefreshLayout.setRefreshing (false);
                                    e.printStackTrace ();
                                    Utils.showSnackBar (CompanyListActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    tvNoResult.setVisibility (View.VISIBLE);
                                }
                            } else {
                                tvNoResult.setVisibility (View.VISIBLE);
                                Utils.showSnackBar (CompanyListActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            swipeRefreshLayout.setRefreshing (false);
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
                            Utils.showSnackBar (CompanyListActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            swipeRefreshLayout.setRefreshing (false);
                            tvNoResult.setVisibility (View.VISIBLE);
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
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (CompanyListActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest, 5);
        } else {
            tvNoResult.setVisibility (View.VISIBLE);
            swipeRefreshLayout.setRefreshing (false);
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


/*
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService (Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem (R.id.action_search).getActionView ();
        if (null != searchView) {
            searchView.setSearchableInfo (searchManager.getSearchableInfo (getComponentName ()));
//            searchView.setIconifiedByDefault (false);
        }


//        final int searchBarId = searchView.getContext ().getResources ().getIdentifier ("android:id/search_bar", null, null);
//        LinearLayout searchBar = (LinearLayout) searchView.findViewById (searchBarId);

        EditText et = (EditText) searchView.findViewById (R.id.search_src_text);
//        et.getBackground ().setColorFilter (R.color.text_color_grey_dark,null);
//        et.setBackgroundColor (getResources ().getColor (R.color.text_color_grey_light)); // ← If you just want a color
//        et.setBackground (getResources ().getDrawable (R.drawable.layout_search_edittext));

//        et.setFocusableInTouchMode (true);
//        et.setFocusable (true);

        LinearLayout searchBar = (LinearLayout) searchView.findViewById (R.id.search_bar);
        searchBar.setLayoutTransition (new LayoutTransition ());

        searchView.setQueryHint ("Search ATM ID");
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener () {
            public boolean onQueryTextChange (String newText) {
//                etSearch.setText (newText);
                return true;
            }

            public boolean onQueryTextSubmit (String query) {
                //Here u can get the value "query" which is entered in the search box.
                return true;
            }
        };
        searchView.setOnQueryTextListener (queryTextListener);

        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.action_search:
//                if (etSearch.isShown ()) {
//                    etSearch.setVisibility (View.GONE);
//                    final Handler handler = new Handler ();
//                    handler.postDelayed (new Runnable () {
//                        @Override
//                        public void run () {
//                            etSearch.setText ("");
//                        }
//                    }, 1000);
//                } else {
//                    etSearch.setVisibility (View.VISIBLE);
//                }
                break;
        }
        Utils.hideSoftKeyboard (CompanyListActivity.this);
/**
 if (item != null && item.getItemId () == android.R.id.home) {
 if (mDrawerLayout.isDrawerOpen (mDrawerPanel)) {
 } else {
 mDrawerLayout.openDrawer (mDrawerPanel);
 }
 return true;
 }
 */
//        return super.onOptionsItemSelected (item);
//    }
//*/
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    
}
