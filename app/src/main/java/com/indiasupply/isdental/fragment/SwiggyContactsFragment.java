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
import com.indiasupply.isdental.adapter.SwiggyCompanyAdapter;
import com.indiasupply.isdental.dialog.SwiggyContactDetailDialogFragment;
import com.indiasupply.isdental.model.SwiggyCompany;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyContactsFragment extends Fragment {
    RecyclerView rvContacts;
    List<SwiggyCompany> contactsList = new ArrayList<> ();
    SwiggyCompanyAdapter contactAdapter;
    Button btFilter;
    
    public static SwiggyContactsFragment newInstance () {
        return new SwiggyContactsFragment ();
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
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvContacts = (RecyclerView) rootView.findViewById (R.id.rvContacts);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvContacts);
    
        contactAdapter = new SwiggyCompanyAdapter (getActivity (), contactsList);
        rvContacts.setAdapter (contactAdapter);
        rvContacts.setHasFixedSize (true);
        rvContacts.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvContacts.setItemAnimator (new DefaultItemAnimator ());
        rvContacts.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
            }
        });
        contactAdapter.SetOnItemClickListener (new SwiggyCompanyAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyCompany contact = contactsList.get (position);
                android.app.FragmentTransaction ft = getActivity ().getFragmentManager ().beginTransaction ();
                new SwiggyContactDetailDialogFragment ().newInstance (contact.getId (), contact.getTitle ()).show (ft, "Contacts");
            }
        });
    }
    
    private void setData () {
        contactsList.add (new SwiggyCompany (1, R.drawable.ic_person, "Chesa", "12 CONTACTS", "Dental Implants", "karman.singh@actiknowbi.com", "www.indiasupply.com", "http://famdent.indiasupply.com/isdental/api/images/brands/brand1.jpg"));
        contactsList.add (new SwiggyCompany (2, R.drawable.ic_person, "Duerr", "10 CONTACTS", "Dental Implants", "", "", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        contactsList.add (new SwiggyCompany (3, R.drawable.ic_person, "Woodpecker", "5 CONTACTS", "Dental Implants", "", "", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        contactsList.add (new SwiggyCompany (4, R.drawable.ic_person, "Satelec", "3 CONTACTS", "Dental Implants", "karman.singh@actiknowbi.com", "www.indiasupply.com", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        contactsList.add (new SwiggyCompany (5, R.drawable.ic_person, "MicroNX", "12 CONTACTS", "Dental Implants", "karman.singh@actiknowbi.com", "www.indiasupply.com", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        contactsList.add (new SwiggyCompany (6, R.drawable.ic_person, "Doctor Smile", "16 CONTACTS", "Dental Implants", "", "", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        contactsList.add (new SwiggyCompany (7, R.drawable.ic_person, "Vatech", "8 CONTACTS", "Dental Implants", "karman.singh@actiknowbi.com", "www.indiasupply.com", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        contactAdapter.notifyDataSetChanged ();
    }
}