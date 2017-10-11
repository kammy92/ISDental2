package com.indiasupply.isdental.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyMyAccountItemAdapter;
import com.indiasupply.isdental.dialog.SwiggyEditProfileDialogFragment;
import com.indiasupply.isdental.model.SwiggyMyAccountItem;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyMyAccountFragment extends Fragment {
    RecyclerView rvMyAccount;
    RecyclerView rvHelp;
    TextView tvEdit;
    TextView tvAppVersion;
    
    List<SwiggyMyAccountItem> myAccountItemList = new ArrayList<> ();
    List<SwiggyMyAccountItem> myHelpItemList = new ArrayList<> ();
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
        setData ();
        return rootView;
    }
    
    private void initView (View rootView) {
        rvMyAccount = (RecyclerView) rootView.findViewById (R.id.rvMyAccount);
        rvHelp = (RecyclerView) rootView.findViewById (R.id.rvHelp);
        tvEdit = (TextView) rootView.findViewById (R.id.tvEdit);
        tvAppVersion = (TextView) rootView.findViewById (R.id.tvAppVersion);
    
        tvEdit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                SwiggyEditProfileDialogFragment frag1 = new SwiggyEditProfileDialogFragment ();
                frag1.show (getActivity ().getFragmentManager ().beginTransaction (), "2");
            }
        });
    }
    
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), rvMyAccount);
        rvMyAccount.setNestedScrollingEnabled (false);
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity ().getPackageManager ().getPackageInfo (getActivity ().getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        
        
        tvAppVersion.setText ("App Version " + pInfo.versionName + "(" + pInfo.versionCode + ")");
        
        
        myAccountItemAdapter = new SwiggyMyAccountItemAdapter (getActivity (), myAccountItemList);
        rvMyAccount.setAdapter (myAccountItemAdapter);
        rvMyAccount.setHasFixedSize (true);
        rvMyAccount.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyAccount.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
        myAccountItemAdapter = new SwiggyMyAccountItemAdapter (getActivity (), myHelpItemList);
        rvHelp.setAdapter (myAccountItemAdapter);
        rvHelp.setHasFixedSize (true);
        rvHelp.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvHelp.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
    }
    
    private void initListener () {
    }
    
    private void setData () {
        myAccountItemList.add (new SwiggyMyAccountItem (1, R.drawable.ic_favourite, "Favourites", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (2, R.drawable.ic_favourite, "Offers", "", ""));
        myAccountItemList.add (new SwiggyMyAccountItem (3, R.drawable.ic_favourite, "Enquiries", "", ""));
        
        myHelpItemList.add (new SwiggyMyAccountItem (5, R.drawable.ic_favourite, "Help & Support", "", ""));
        myHelpItemList.add (new SwiggyMyAccountItem (6, R.drawable.ic_favourite, "FAQs", "", ""));
        myHelpItemList.add (new SwiggyMyAccountItem (7, R.drawable.ic_favourite, "Terms of Use", "", ""));
        myHelpItemList.add (new SwiggyMyAccountItem (8, R.drawable.ic_favourite, "Privacy Policy", "", ""));
        
        myAccountItemAdapter.notifyDataSetChanged ();
    }
}