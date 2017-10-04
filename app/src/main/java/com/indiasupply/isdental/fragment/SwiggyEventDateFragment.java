package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indiasupply.isdental.R;


/**
 * Created by l on 24/02/2017.
 */


/**
 * Created by l on 24/02/2017.
 */

public class SwiggyEventDateFragment extends Fragment {
    
    public static SwiggyEventDateFragment newInstance (String type, String html) {
        SwiggyEventDateFragment fragment = new SwiggyEventDateFragment ();
        Bundle args = new Bundle ();
        //  args.putString ("TITLE", type);
        //args.putString ("HTML", html);
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_event_date, container, false);
        initView (rootView);
        initData ();
        return rootView;
    }
    
    private void initView (View rootView) {
    }
    
    private void initData () {
        
    }
}