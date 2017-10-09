package com.indiasupply.isdental.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyContactDetailAdapter;
import com.indiasupply.isdental.model.SwiggyContactDetail;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyContactDetailDialogFragment extends DialogFragment {
    RecyclerView rvContactList;
    List<SwiggyContactDetail> contactDetailList = new ArrayList<> ();
    SwiggyContactDetailAdapter swiggyAdapter2;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    String contact_name;
    int contact_id;
    
    public SwiggyContactDetailDialogFragment newInstance (int contact_id, String contact_name) {
        SwiggyContactDetailDialogFragment f = new SwiggyContactDetailDialogFragment ();
        Bundle args = new Bundle ();
        args.putInt ("contact_id", contact_id);
        args.putString ("contact_name", contact_name);
        f.setArguments (args);
        return f;
    }
    
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        contact_id = getArguments ().getInt ("contact_id");
        contact_name = getArguments ().getString ("contact_name");
        setStyle (DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    
    @Override
    public void onActivityCreated (Bundle arg0) {
        super.onActivityCreated (arg0);
        Window window = getDialog ().getWindow ();
        window.getAttributes ().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (getActivity (), R.color.text_color_white));
        }
    }
    
    @Override
    public void onStart () {
        super.onStart ();
        Dialog d = getDialog ();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow ().setLayout (width, height);
        }
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_contact_details, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        rvContactList = (RecyclerView) root.findViewById (R.id.rvContactList);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        tvTitle.setText (contact_name.toUpperCase ());
        contactDetailList.add (new SwiggyContactDetail (1, 1, R.drawable.ic_person, true, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (2, 1, R.drawable.ic_person, false, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (3, 1, R.drawable.ic_person, false, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (4, 1, R.drawable.ic_person, true, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (5, 1, R.drawable.ic_person, false, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (6, 1, R.drawable.ic_person, true, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (7, 1, R.drawable.ic_person, false, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
        contactDetailList.add (new SwiggyContactDetail (8, 1, R.drawable.ic_person, false, "ABCD Pvt Ltd", "Dwarka Sector 8", "+919873684678", "http://famdent.indiasupply.com/isdental/api/images/contacts/sales.png"));
    
        swiggyAdapter2 = new SwiggyContactDetailAdapter (getActivity (), contactDetailList);
        rvContactList.setAdapter (swiggyAdapter2);
        rvContactList.setHasFixedSize (true);
        rvContactList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvContactList.setItemAnimator (new DefaultItemAnimator ());
        
        rvContactList.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
}