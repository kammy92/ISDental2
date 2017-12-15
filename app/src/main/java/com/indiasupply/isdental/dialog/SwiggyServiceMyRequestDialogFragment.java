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
import com.indiasupply.isdental.adapter.SwiggyServiceMyRequestAdapter;
import com.indiasupply.isdental.model.SwiggyMyRequest;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwiggyServiceMyRequestDialogFragment extends DialogFragment {
    TextView tvTitle;
    ImageView ivCancel;
    RecyclerView rvRequests;
    SwiggyServiceMyRequestAdapter myRequestAdapter;
    List<SwiggyMyRequest> myRequestList = new ArrayList<> ();
    
    String myRequests;
    RelativeLayout rlNoRequestFound;
    
    
    public static SwiggyServiceMyRequestDialogFragment newInstance (String myRequests) {
        SwiggyServiceMyRequestDialogFragment f = new SwiggyServiceMyRequestDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_REQUESTS, myRequests);
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_my_request, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rvRequests = (RecyclerView) root.findViewById (R.id.rvRequest);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rlNoRequestFound = (RelativeLayout) root.findViewById (R.id.rlNoRequestFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myRequests = bundle.getString (AppConfigTags.SWIGGY_REQUESTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        myRequestAdapter = new SwiggyServiceMyRequestAdapter (getActivity (), myRequestList);
        rvRequests.setAdapter (myRequestAdapter);
        rvRequests.setHasFixedSize (true);
        rvRequests.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvRequests.setItemAnimator (new DefaultItemAnimator ());
        rvRequests.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
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
            JSONArray jsonArrayBrand = new JSONArray (myRequests);
            rvRequests.setVisibility (View.VISIBLE);
            rlNoRequestFound.setVisibility (View.GONE);
            if (jsonArrayBrand.length () > 0) {
                for (int i = 0; i < jsonArrayBrand.length (); i++) {
                    JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject (i);
                    myRequestList.add (new SwiggyMyRequest (
                            jsonObjectBrand.getInt (AppConfigTags.SWIGGY_REQUEST_ID),
                            R.drawable.default_my_equipment,
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_TICKET_NUMBER),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_DESCRIPTION),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_IMAGE),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_REQUEST_GENERATED_AT)
                    ));
                }
                myRequestAdapter.notifyDataSetChanged ();
            } else {
                rvRequests.setVisibility (View.GONE);
                rlNoRequestFound.setVisibility (View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}