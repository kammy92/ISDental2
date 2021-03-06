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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.MyAccountEnquiryAdapter;
import com.indiasupply.isdental.model.MyAccountEnquiry;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountEnquiriesDialogFragment extends DialogFragment {
    RecyclerView rvMyEnquiries;
    List<MyAccountEnquiry> myAccountEnquiryList = new ArrayList<> ();
    MyAccountEnquiryAdapter myAccountEnquiryAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    String myEnquiries = "";
    
    RelativeLayout rlNoEnquiryFound;
    
    
    public static MyAccountEnquiriesDialogFragment newInstance (String myEnquiries) {
        MyAccountEnquiriesDialogFragment fragment = new MyAccountEnquiriesDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.ENQUIRIES, myEnquiries);
        fragment.setArguments (args);
        return fragment;
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
        View root = inflater.inflate (R.layout.fragment_dialog_my_account_enquiries, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvMyEnquiries = (RecyclerView) root.findViewById (R.id.rvMyEnquiries);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rlNoEnquiryFound = (RelativeLayout) root.findViewById (R.id.rlNoEnquiryFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myEnquiries = bundle.getString (AppConfigTags.ENQUIRIES);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        myAccountEnquiryAdapter = new MyAccountEnquiryAdapter (getActivity (), myAccountEnquiryList);
        rvMyEnquiries.setAdapter (myAccountEnquiryAdapter);
        rvMyEnquiries.setHasFixedSize (true);
        rvMyEnquiries.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyEnquiries.setItemAnimator (new DefaultItemAnimator ());
        rvMyEnquiries.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
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
        /*myAccountEnquiryList.add (new MyAccountEnquiry (1, R.drawable.ic_person, "Dr. Mohammad Atta", "9 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountEnquiryList.add (new MyAccountEnquiry (2, R.drawable.ic_person, "Dr. Zakir Nayak", "10 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountEnquiryList.add (new MyAccountEnquiry (3, R.drawable.ic_person, "Dr. Abu Baghdadi", "11 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountEnquiryList.add (new MyAccountEnquiry (4, R.drawable.ic_person, "Dr. Osama Bin Laden", "12 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountEnquiryAdapter.notifyDataSetChanged ();

*/
    
    
        try {
            JSONArray jsonArray = new JSONArray (myEnquiries);
            rvMyEnquiries.setVisibility (View.VISIBLE);
            rlNoEnquiryFound.setVisibility (View.GONE);
        
            if (jsonArray.length () > 0) {
                for (int j = 0; j < jsonArray.length (); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (j);
                    myAccountEnquiryList.add (new MyAccountEnquiry (
                            jsonObject.getInt (AppConfigTags.PRODUCT_ID),
                            jsonObject.getInt (AppConfigTags.ENQUIRY_STATUS),
                            jsonObject.getString (AppConfigTags.ENQUIRY_TICKET_NUMBER),
                            jsonObject.getString (AppConfigTags.ENQUIRY_REMARK),
                            jsonObject.getString (AppConfigTags.COMPANY_NAME),
                            jsonObject.getString (AppConfigTags.PRODUCT_NAME),
                            jsonObject.getString (AppConfigTags.PRODUCT_PRICE),
                            jsonObject.getString (AppConfigTags.PRODUCT_CATEGORY),
                            jsonObject.getString (AppConfigTags.PRODUCT_IMAGE),
                            jsonObject.getString (AppConfigTags.PRODUCT_DESCRIPTION),
                            jsonObject.getString (AppConfigTags.PRODUCT_PACKAGING),
                            jsonObject.getString (AppConfigTags.ENQUIRY_COMMENT)
                    ));
                }
                myAccountEnquiryAdapter.notifyDataSetChanged ();
            } else {
                rvMyEnquiries.setVisibility (View.GONE);
                rlNoEnquiryFound.setVisibility (View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}

