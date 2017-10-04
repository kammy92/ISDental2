package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyEventAdapter;
import com.indiasupply.isdental.model.SwiggyEvent;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyEventFragment extends Fragment {
    RecyclerView rv1;
    List<SwiggyEvent> swiggyEventList = new ArrayList<> ();
    SwiggyEventAdapter swiggyEventAdapter;
    
    
    public static SwiggyEventFragment newInstance () {
        SwiggyEventFragment fragment = new SwiggyEventFragment ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_event, container, false);
        initView (rootView);
        initData ();
        initListener ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rv1 = (RecyclerView) rootView.findViewById (R.id.rvEvents);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rv1);
        rv1.setNestedScrollingEnabled (false);
    
        swiggyEventList.add (new SwiggyEvent (1, "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg", "EXPODENT", "2 - 5 Oct", "Mumbai"));
        swiggyEventList.add (new SwiggyEvent (2, "http://famdent.indiasupply.com/isdental/api/images/delhi.jpg", "EXPODENT", "2 - 5 Oct", "Delhi"));
        swiggyEventList.add (new SwiggyEvent (3, "http://famdent.indiasupply.com/isdental/api/images/banners/expodent_mumbai.jpg", "EXPODENT", "2 - 5 Oct", "Mumbai"));
        swiggyEventList.add (new SwiggyEvent (4, "http://famdent.indiasupply.com/isdental/api/images/banners/expodent_mumbai.jpg", "EXPODENT", "2 - 5 Oct", "Mumbai"));
        swiggyEventList.add (new SwiggyEvent (5, "http://famdent.indiasupply.com/isdental/api/images/banners/expodent_mumbai.jpg", "EXPODENT", "2 - 5 Oct", "Mumbai"));
    
    
        swiggyEventAdapter = new SwiggyEventAdapter (getActivity (), swiggyEventList);
        rv1.setAdapter (swiggyEventAdapter);
        rv1.setHasFixedSize (true);
        rv1.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rv1.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
    }
}