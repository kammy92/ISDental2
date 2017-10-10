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
    RecyclerView rvEvents;
    List<SwiggyEvent> eventList = new ArrayList<> ();
    SwiggyEventAdapter eventAdapter;
    
    public static SwiggyEventFragment newInstance () {
        return new SwiggyEventFragment ();
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
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvEvents = (RecyclerView) rootView.findViewById (R.id.rvEvents);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvEvents);
        rvEvents.setNestedScrollingEnabled (false);
        rvEvents.setFocusable (false);
    
        eventAdapter = new SwiggyEventAdapter (getActivity (), eventList);
        rvEvents.setAdapter (eventAdapter);
        rvEvents.setHasFixedSize (true);
        rvEvents.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvEvents.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
    }
    
    private void setData () {
        eventList.add (new SwiggyEvent (1, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
        eventList.add (new SwiggyEvent (2, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Delhi", "http://famdent.indiasupply.com/isdental/api/images/delhi.jpg"));
        eventList.add (new SwiggyEvent (3, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
        eventList.add (new SwiggyEvent (4, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
        eventList.add (new SwiggyEvent (5, R.drawable.expodent_mumbai, "EXPODENT", "2 - 5 Oct", "Mumbai", "http://famdent.indiasupply.com/isdental/api/images/mumbai.jpg"));
        eventAdapter.notifyDataSetChanged ();
    }
}