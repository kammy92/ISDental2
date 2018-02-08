package com.indiasupply.isdental.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import com.indiasupply.isdental.adapter.ContactDetailAdapter;
import com.indiasupply.isdental.fragment.ContactsFragment;
import com.indiasupply.isdental.model.ContactDetail;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailDialogFragment extends DialogFragment {
    ContactsFragment.MyDialogCloseListener closeListener;
    RecyclerView rvContactList;
    List<ContactDetail> contactDetailList = new ArrayList<> ();
    ContactDetailAdapter contactDetailAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    String contacts;
    String company_name;
    
    
    public ContactDetailDialogFragment newInstance (String company_name, String contacts) {
        ContactDetailDialogFragment f = new ContactDetailDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_COMPANY_NAME, company_name);
        args.putString (AppConfigTags.SWIGGY_COMPANY_CONTACTS, contacts);
        f.setArguments (args);
        return f;
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
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
        View root = inflater.inflate (R.layout.fragment_dialog_contact_details, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        rvContactList = (RecyclerView) root.findViewById (R.id.rvContactList);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        company_name = bundle.getString (AppConfigTags.SWIGGY_COMPANY_NAME);
        contacts = bundle.getString (AppConfigTags.SWIGGY_COMPANY_CONTACTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        tvTitle.setText (company_name);
    
        contactDetailAdapter = new ContactDetailAdapter (getActivity (), contactDetailList);
        rvContactList.setAdapter (contactDetailAdapter);
        rvContactList.setHasFixedSize (true);
        rvContactList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvContactList.setItemAnimator (new DefaultItemAnimator ());
        rvContactList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
    
    private void setData () {
        try {
            JSONArray jsonArray = new JSONArray (contacts);
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject (j);
                contactDetailList.add (new ContactDetail (
                        jsonObject.getInt (AppConfigTags.SWIGGY_CONTACT_ID),
                        jsonObject.getInt (AppConfigTags.SWIGGY_CONTACT_TYPE),
                        R.drawable.default_company,
                        jsonObject.getBoolean (AppConfigTags.SWIGGY_CONTACT_FAVOURITE),
                        jsonObject.getString (AppConfigTags.SWIGGY_CONTACT_NAME),
                        jsonObject.getString (AppConfigTags.SWIGGY_CONTACT_LOCATION),
                        jsonObject.getString (AppConfigTags.SWIGGY_CONTACT_PHONE),
                        jsonObject.getString (AppConfigTags.SWIGGY_CONTACT_EMAIL),
                        jsonObject.getString (AppConfigTags.SWIGGY_CONTACT_IMAGE)
                ));
            }
            contactDetailAdapter.notifyDataSetChanged ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    
    public void setDismissListener (ContactsFragment.MyDialogCloseListener closeListener) {
        this.closeListener = closeListener;
    }
    
    @Override
    public void onDismiss (DialogInterface dialog) {
        super.onDismiss (dialog);
        
        if (closeListener != null) {
            closeListener.handleDialogClose (null);
        }
    }
}