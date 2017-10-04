package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyContactAdapter;
import com.indiasupply.isdental.model.SwiggyContacts;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 27/09/2017.
 */

public class SwiggyContactsFragment extends Fragment {
    
    RecyclerView rvCompanyContactList;
    List<SwiggyContacts> swiggyContactsList = new ArrayList<> ();
    SwiggyContactAdapter swiggyContactAdapter;
    Button btFilter;
    
    
    public static SwiggyContactsFragment newInstance () {
        SwiggyContactsFragment fragment = new SwiggyContactsFragment ();
        return fragment;
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
        return rootView;
    }
    
    private void initView (View rootView) {
        rvCompanyContactList = (RecyclerView) rootView.findViewById (R.id.rvCompanyContactList);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvCompanyContactList);
        
        swiggyContactsList.add (new SwiggyContacts (1, "Chesa", "12 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand1.jpg", "karman.singh@actiknowbi.com", "www.indiasupply.com"));
        swiggyContactsList.add (new SwiggyContacts (2, "Duerr", "10 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg", "", ""));
        swiggyContactsList.add (new SwiggyContacts (3, "Woodpecker", "5 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg", "", ""));
        swiggyContactsList.add (new SwiggyContacts (4, "Satelec", "3 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg", "karman.singh@actiknowbi.com", "www.indiasupply.com"));
        swiggyContactsList.add (new SwiggyContacts (5, "MicroNX", "12 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg", "karman.singh@actiknowbi.com", "www.indiasupply.com"));
        swiggyContactsList.add (new SwiggyContacts (6, "Doctor Smile", "16 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand3.jpg", "", ""));
        swiggyContactsList.add (new SwiggyContacts (7, "Vatech", "8 CONTACTS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg", "karman.singh@actiknowbi.com", "www.indiasupply.com"));
        
        swiggyContactAdapter = new SwiggyContactAdapter (getActivity (), swiggyContactsList);
        rvCompanyContactList.setAdapter (swiggyContactAdapter);
        rvCompanyContactList.setHasFixedSize (true);
        rvCompanyContactList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvCompanyContactList.setItemAnimator (new DefaultItemAnimator ());
        rvCompanyContactList.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
            }
        });
        swiggyContactAdapter.SetOnItemClickListener (new SwiggyContactAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyContacts swiggyContacts = swiggyContactsList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                SwiggyContactDetailDialogFragment frag = new SwiggyContactDetailDialogFragment ().newInstance (swiggyContacts.getId (), swiggyContacts.getTitle ());
                frag.show (ft, "Dialog");
            }
        });
    }
}