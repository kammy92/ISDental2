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
    RecyclerView rvMyAccount;
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
        rvMyAccount = (RecyclerView) rootView.findViewById (R.id.rvMyAccount);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvMyAccount);
        rvMyAccount.setNestedScrollingEnabled (false);
    
        myAccountItemList.add (new SwiggyMyAccountItem (1, R.drawable.ic_favourite, "My Favourites", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (2, R.drawable.ic_favourite, "Offers", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (3, R.drawable.ic_favourite, "Terms of Use", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (4, R.drawable.ic_favourite, "Privacy Policy", "", ""));
        
        myAccountItemAdapter = new SwiggyMyAccountItemAdapter (getActivity (), myAccountItemList);
        rvMyAccount.setAdapter (myAccountItemAdapter);
        rvMyAccount.setHasFixedSize (true);
        rvMyAccount.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyAccount.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
    }
}