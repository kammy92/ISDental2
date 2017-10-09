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
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 27/09/2017.
 */

public class SwiggyExhibitorsFragment extends Fragment {
    RecyclerView rvBanners;
    RecyclerView rvBrands;
    List<SwiggyBanner> bannerList = new ArrayList<> ();
    List<SwiggyBrand> brandList = new ArrayList<> ();
    SwiggyBannerAdapter bannerAdapter;
    SwiggyBrandsAdapter brandsAdapter;
    Button btFilter;
    
    
    public static SwiggyExhibitorsFragment newInstance () {
        return new SwiggyExhibitorsFragment ();
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_swiggy_home, container, false);
        initView (rootView);
        initData ();
        initListener ();
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvBanners = (RecyclerView) rootView.findViewById (R.id.rvBanners);
        rvBrands = (RecyclerView) rootView.findViewById (R.id.rvBrands);
        btFilter = (Button) rootView.findViewById (R.id.btFilter);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), btFilter);
        rvBanners.setNestedScrollingEnabled (false);
        rvBrands.setNestedScrollingEnabled (false);
    
        bannerAdapter = new SwiggyBannerAdapter (getActivity (), bannerList);
        brandsAdapter = new SwiggyBrandsAdapter (getActivity (), brandList);
    
        rvBanners.setAdapter (bannerAdapter);
        rvBanners.setHasFixedSize (true);
        rvBanners.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.HORIZONTAL, false));
        rvBanners.setItemAnimator (new DefaultItemAnimator ());
        rvBanners.addItemDecoration (new RecyclerViewMargin (0, 0, (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 0, 1, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_HORIZONTAL));
    
        rvBrands.setAdapter (brandsAdapter);
        rvBrands.setHasFixedSize (true);
        rvBrands.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvBrands.setItemAnimator (new DefaultItemAnimator ());
        rvBrands.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        btFilter.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
            }
        });
    }
    
    private void setData () {
        bannerList.add (new SwiggyBanner (1, R.drawable.default_banner, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner1.jpg", "E X P L O R E"));
        bannerList.add (new SwiggyBanner (2, R.drawable.default_banner, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner2.jpg", "O F F E R"));
        bannerList.add (new SwiggyBanner (3, R.drawable.default_banner, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner3.jpg", "D I S C O V E R"));
        bannerList.add (new SwiggyBanner (4, R.drawable.default_banner, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner4.jpg", "I N T R O D U C I N G"));
        bannerList.add (new SwiggyBanner (5, R.drawable.default_banner, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner2.jpg", "O F F E R S"));
        bannerList.add (new SwiggyBanner (6, R.drawable.default_banner, "http://famdent.indiasupply.com/isdental/api/images/banners/new/banner1.jpg", "D I S C O V E R"));
        bannerAdapter.notifyDataSetChanged ();
        
        brandList.add (new SwiggyBrand (true, true, 1, R.drawable.ic_person, "Chesa", "12 CONTACTS", "4.3", "13 OFFERS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand1.jpg"));
        brandList.add (new SwiggyBrand (false, false, 2, R.drawable.ic_person, "Duerr", "10 CONTACTS", "3.3", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        brandList.add (new SwiggyBrand (true, false, 3, R.drawable.ic_person, "Woodpecker", "8 CONTACTS", "2.7", "10 OFFERS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        brandList.add (new SwiggyBrand (false, true, 4, R.drawable.ic_person, "Satelec", "20 CONTACTS", "4.9", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        brandList.add (new SwiggyBrand (false, true, 5, R.drawable.ic_person, "MicroNX", "10 CONTACTS", "3.9", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        brandList.add (new SwiggyBrand (true, true, 6, R.drawable.ic_person, "Doctor Smile", "5 CONTACTS", "3.5", "5 OFFERS", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand3.jpg"));
        brandList.add (new SwiggyBrand (false, false, 7, R.drawable.ic_person, "Vatech", "8 CONTACTS", "4.3", "", "Dental Implants", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg"));
        brandsAdapter.notifyDataSetChanged ();
    }
}