package com.indiasupply.isdental.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyCategoryFilterAdapter;
import com.indiasupply.isdental.fragment.SwiggyContactsFragment;
import com.indiasupply.isdental.model.CategoryFilter;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwiggyCategoryFilterDialogFragment extends DialogFragment {
    SwiggyContactsFragment.MyDialogCloseListener closeListener;
    ImageView ivCancel;
    LinearLayout llDynamic;
    NestedScrollView nestedScrollView;
    SwiggyCategoryFilterAdapter swiggyCategoryFilterAdapter;
    ArrayList<CategoryFilter> categoryFilterList = new ArrayList<> ();
    String filter;
    TextView tvApply;
    
    
    public SwiggyCategoryFilterDialogFragment newInstance (String filter) {
        SwiggyCategoryFilterDialogFragment f = new SwiggyCategoryFilterDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_CATEGORY_FILTERS, filter);
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_category_filter, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        nestedScrollView = (NestedScrollView) root.findViewById (R.id.nestedScrollView);
        llDynamic = (LinearLayout) root.findViewById (R.id.llDynamic);
        tvApply = (TextView) root.findViewById (R.id.tvApply);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        filter = bundle.getString (AppConfigTags.SWIGGY_CATEGORY_FILTERS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvApply);
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    
        tvApply.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
//
//                Intent i = new Intent ()
//                        .putExtra ("month", "karman")
//                        .putExtra ("year", "2017");
//                getTargetFragment ().onActivityResult (getTargetRequestCode (), Activity.RESULT_OK, i);
                getDialog ().dismiss ();
            }
        });
    }
    
    private void setData () {
        try {
            JSONArray jsonArray = new JSONArray (filter);
            for (int i = 0; i < jsonArray.length (); i++) {
                categoryFilterList = new ArrayList<> ();
                categoryFilterList.clear ();
                JSONObject jsonObjectfilter = jsonArray.getJSONObject (i);
                
                TextView tv = new TextView (getActivity ());
                tv.setText (jsonObjectfilter.getString (AppConfigTags.SWIGGY_GROUP_NAME));
                tv.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextSize (TypedValue.COMPLEX_UNIT_SP, 24);
                tv.setTypeface (SetTypeFace.getTypeface (getActivity (), "AvenirNextLTPro-Demi.otf"), Typeface.BOLD);
                tv.setTextColor (getResources ().getColor (R.color.primary_text2));
                tv.setPadding ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 0);
                llDynamic.addView (tv);
                
                JSONArray jsonArrayCategory = jsonObjectfilter.getJSONArray (AppConfigTags.CATEGORIES);
                for (int j = 0; j < jsonArrayCategory.length (); j++) {
                    JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject (j);
                    categoryFilterList.add (new CategoryFilter (
                            jsonObjectCategory.getInt (AppConfigTags.SWIGGY_CATEGORY_ID),
                            "",
                            jsonObjectCategory.getString (AppConfigTags.SWIGGY_CATEGORY_NAME)
                    ));
                }
                RecyclerView rv = new RecyclerView (getActivity ());
                swiggyCategoryFilterAdapter = new SwiggyCategoryFilterAdapter (getActivity (), categoryFilterList);
                rv.setAdapter (swiggyCategoryFilterAdapter);
                rv.setHasFixedSize (true);
                rv.setNestedScrollingEnabled (false);
                rv.setFocusable (false);
                rv.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
                rv.setItemAnimator (new DefaultItemAnimator ());
                //    rv.addItemDecoration(new RecyclerViewMargin((int) Utils.pxFromDp(getActivity(), 16), (int) Utils.pxFromDp(getActivity(), 16), (int) Utils.pxFromDp(getActivity(), 16), (int) Utils.pxFromDp(getActivity(), 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));

//                swiggyCategoryFilterAdapter.SetOnItemClickListener (new SwiggyCategoryFilterAdapter.OnItemClickListener () {
//                    @Override
//                    public void onItemClick (View view, int position) {
//                        CategoryFilter categoryFilter = categoryFilterList.get (position);
//                        Log.e ("karman", "in adapter" + categoryFilter.getName ());
//                    }
//                });
//
                
                llDynamic.addView (rv);
                
                View view = new View (getActivity ());
                view.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.pxFromDp (getActivity (), 16)));
                view.setBackgroundColor (getResources ().getColor (R.color.view_color));
                llDynamic.addView (view);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
    public void setDismissListener (SwiggyContactsFragment.MyDialogCloseListener closeListener) {
        this.closeListener = closeListener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        if (closeListener != null) {
            closeListener.handleDialogClose (null);
        }
    }
}