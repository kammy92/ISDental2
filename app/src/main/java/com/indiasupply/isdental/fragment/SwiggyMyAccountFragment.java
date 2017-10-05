package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyMyAccountItemAdapter;
import com.indiasupply.isdental.model.SwiggyMyAccountItem;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyMyAccountFragment extends Fragment {
    RecyclerView rv1;
    List<SwiggyMyAccountItem> myAccountItemList = new ArrayList<> ();
    SwiggyMyAccountItemAdapter myAccountItemAdapter;
    
    
    public static SwiggyMyAccountFragment newInstance () {
        SwiggyMyAccountFragment fragment = new SwiggyMyAccountFragment ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_my_account, container, false);
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
        
        myAccountItemList.add (new SwiggyMyAccountItem (1, "", "http://famdent.indiasupply.com/isdental/ai.jpg", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (1, "", "http://famdent.indiasupply.com/isdental/ai.jpg", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (1, "", "http://famdent.indiasupply.com/isdental/ai.jpg", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (1, "", "http://famdent.indiasupply.com/isdental/ai.jpg", ""));
        
        myAccountItemAdapter = new SwiggyMyAccountItemAdapter (getActivity (), myAccountItemList);
        rv1.setAdapter (myAccountItemAdapter);
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