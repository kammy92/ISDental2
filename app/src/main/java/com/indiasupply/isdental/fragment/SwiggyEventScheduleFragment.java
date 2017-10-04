package com.indiasupply.isdental.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.EventTab;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sud on 27/9/17.
 */

public class SwiggyEventScheduleFragment extends DialogFragment {
    List<EventTab> eventTabList = new ArrayList<> ();
    LinearLayoutManager linearLayoutManager;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView ivCancel;
    TextView tvTitle;
    Activity activity;
    
    
    public static DialogFragment newInstance () {
        SwiggyEventScheduleFragment fragment = new SwiggyEventScheduleFragment ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        //  setStyle(SwiggyEventScheduleFragment.STYLE_NORMAL, R.style.dialog_theme);
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
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_event_schedule, container, false);
        initView (root);
        setupViewPager (viewPager);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tabLayout = (TabLayout) root.findViewById (R.id.tabs);
        viewPager = (ViewPager) root.findViewById (R.id.viewpager);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    private void initBundle () {
        
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
        eventTabList.add (new EventTab (1, "2017-10-01"));
        eventTabList.add (new EventTab (2, "2017-10-02"));
        eventTabList.add (new EventTab (3, "2017-10-03"));
        
    }
    
    private void initListener () {
        
    }
    
    private void setupViewPager (ViewPager viewPager) {
        EventTab eventTab;
        viewPagerAdapter = new ViewPagerAdapter (getFragmentManager ());
        for (int i = 0; i < eventTabList.size (); i++) {
            eventTab = eventTabList.get (i);
            viewPagerAdapter.addFragment (new SwiggyEventDateFragment (), eventTab.getDate ());
        }
        //viewPagerAdapter.addFragment (new CompsFragment (), "COMPS");
        viewPager.setAdapter (viewPagerAdapter);
        
        for (int i = 0; i < tabLayout.getTabCount (); i++) {
            TextView tv = new TextView (getActivity ());
            eventTab = eventTabList.get (i);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams (params);
            tv.setText (eventTab.getDate ());
                /*tv.setGravity (Gravity.CENTER);
                switch (i) {
                    case 0:
                        tv.setText ("OVERVIEW");
                        break;
                    case 1:
                        tv.setText ("COMPS");
                        break;
                }*/
            tv.setTypeface (SetTypeFace.getTypeface (getActivity ()), Typeface.BOLD);
            tabLayout.getTabAt (i).setCustomView (tv);
        }
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();
        
        public ViewPagerAdapter (FragmentManager manager) {
            super (manager);
        }
        
        @Override
        public Fragment getItem (int position) {
            return mFragmentList.get (position);
        }
        
        @Override
        public int getCount () {
            return mFragmentList.size ();
        }
        
        public void addFragment (Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }
        
        @Override
        public CharSequence getPageTitle (int position) {
            return mFragmentTitleList.get (position);
        }
    }
}