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
import com.indiasupply.isdental.adapter.SwiggyBannerAdapter;
import com.indiasupply.isdental.adapter.SwiggyBrandsAdapter;
import com.indiasupply.isdental.model.SwiggyBanner;
import com.indiasupply.isdental.model.SwiggyBrand;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 27/09/2017.
 */

public class SwiggyHomeFragment1 extends Fragment {
    RecyclerView rv1;
    RecyclerView rv2;
    List<SwiggyBanner> swiggyBannerList = new ArrayList<> ();
    List<SwiggyBrand> swiggyBrandList = new ArrayList<> ();
    SwiggyBannerAdapter swiggyBannerAdapter;
    SwiggyBrandsAdapter swiggyBrandsAdapter;
    Button btFilter;
    
    
    public static SwiggyHomeFragment1 newInstance () {
        SwiggyHomeFragment1 fragment = new SwiggyHomeFragment1 ();
        return fragment;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_home_1, container, false);
        initView (rootView);
        initData ();
        initListener ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rv1 = (RecyclerView) rootView.findViewById (R.id.rvBannerList);
        rv2 = (RecyclerView) rootView.findViewById (R.id.rvBrandsList);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rv1);
        
        rv1.setNestedScrollingEnabled (false);
        rv2.setNestedScrollingEnabled (false);
        
        swiggyBannerList.add (new SwiggyBanner (1, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner1.jpg", "E X P L O R E"));
        swiggyBannerList.add (new SwiggyBanner (2, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner2.jpg", "O F F E R"));
        swiggyBannerList.add (new SwiggyBanner (3, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner3.jpg", "D I S C O V E R"));
        swiggyBannerList.add (new SwiggyBanner (4, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner4.jpg", "I N T R O D U C I N G"));
        swiggyBannerList.add (new SwiggyBanner (5, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner2.jpg", "O F F E R S"));
        swiggyBannerList.add (new SwiggyBanner (6, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner1.jpg", "D I S C O V E R"));
        
        swiggyBannerAdapter = new SwiggyBannerAdapter (getActivity (), swiggyBannerList);
        rv1.setAdapter (swiggyBannerAdapter);
        rv1.setHasFixedSize (true);
        rv1.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.HORIZONTAL, false));
        rv1.setItemAnimator (new DefaultItemAnimator ());
        
        
        swiggyBrandList.add (new SwiggyBrand (true, true, 1, "Chesa", "12 CONTACTS", "4.3", "13 Offers inside", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand1.jpg"));
        swiggyBrandList.add (new SwiggyBrand (false, false, 2, "Duerr", "10 CONTACTS", "3.3", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        swiggyBrandList.add (new SwiggyBrand (true, false, 3, "Woodpecker", "8 CONTACTS", "2.7", "10 Offers inside", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        swiggyBrandList.add (new SwiggyBrand (false, true, 4, "Satelec", "20 CONTACTS", "4.9", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        swiggyBrandList.add (new SwiggyBrand (false, true, 5, "MicroNX", "10 CONTACTS", "3.9", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        swiggyBrandList.add (new SwiggyBrand (true, true, 6, "Doctor Smile", "5 CONTACTS", "3.5", "5 Offers inside", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand3.jpg"));
        swiggyBrandList.add (new SwiggyBrand (false, false, 7, "Vatech", "8 CONTACTS", "4.3", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        
        
        swiggyBrandsAdapter = new SwiggyBrandsAdapter (getActivity (), swiggyBrandList);
        rv2.setAdapter (swiggyBrandsAdapter);
        rv2.setHasFixedSize (true);
        rv2.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rv2.setItemAnimator (new DefaultItemAnimator ());
        
        
        swiggyBrandsAdapter.SetOnItemClickListener (new SwiggyBrandsAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Utils.showToast (getActivity (), "position " + position, false);
            }
        });
        
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                
            }
        });
    }
}