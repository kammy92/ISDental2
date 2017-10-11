package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyServiceItemAdapter;
import com.indiasupply.isdental.model.SwiggyServiceItem;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 27/09/2017.
 */

public class SwiggyServiceFragment extends Fragment {
    ImageView ivCancel;
    RecyclerView rvServiceList;
    SwiggyServiceItemAdapter swiggyServiceAdapter;
    List<SwiggyServiceItem> swiggyServiceItemList = new ArrayList<> ();
    
    
    public static SwiggyServiceFragment newInstance () {
        SwiggyServiceFragment fragment = new SwiggyServiceFragment ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_swiggy_service, container, false);
        initView (root);
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View rootView) {
        ivCancel = (ImageView) rootView.findViewById (R.id.ivCancel);
        rvServiceList = (RecyclerView) rootView.findViewById (R.id.rvServiceList);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvServiceList);
        swiggyServiceItemList.add (new SwiggyServiceItem (1, R.drawable.ic_information, "MY PRODUCTS", ""));
        swiggyServiceItemList.add (new SwiggyServiceItem (2, R.drawable.ic_information, "ADD PRODUCT", ""));
        swiggyServiceItemList.add (new SwiggyServiceItem (3, R.drawable.ic_information, "MY REQUESTS", ""));
        swiggyServiceItemList.add (new SwiggyServiceItem (4, R.drawable.ic_information, "ADD REQUEST", ""));
        
        swiggyServiceAdapter = new SwiggyServiceItemAdapter (getActivity (), swiggyServiceItemList);
        rvServiceList.setAdapter (swiggyServiceAdapter);
        rvServiceList.setHasFixedSize (true);
        rvServiceList.setLayoutManager (new GridLayoutManager (getActivity (), 2, GridLayoutManager.VERTICAL, false));
        rvServiceList.setItemAnimator (new DefaultItemAnimator ());
        rvServiceList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
    }
}