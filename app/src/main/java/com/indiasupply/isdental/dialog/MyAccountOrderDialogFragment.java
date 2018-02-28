package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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
import com.indiasupply.isdental.activity.MainActivity;
import com.indiasupply.isdental.adapter.MyAccountOrderAdapter;
import com.indiasupply.isdental.model.MyAccountOrder;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountOrderDialogFragment extends DialogFragment {
    RecyclerView rvMyOrders;
    List<MyAccountOrder> myAccountOrderList = new ArrayList<> ();
    MyAccountOrderAdapter orderAdapter;
    ImageView ivCancel;
    TextView tvTitle;
    String myOrder;
    RelativeLayout rlMain;
    RelativeLayout rlNoOrderFound;
    
    TextView tvClaimOffers;
    
    public static MyAccountOrderDialogFragment newInstance (String myOrder) {
        MyAccountOrderDialogFragment fragment = new MyAccountOrderDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.ORDERS, myOrder);
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
        View root = inflater.inflate (R.layout.fragment_dialog_my_account_order, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rvMyOrders = (RecyclerView) root.findViewById (R.id.rvOrders);
        rlMain = (RelativeLayout) root.findViewById (R.id.rlMain);
        rlNoOrderFound = (RelativeLayout) root.findViewById (R.id.rlNoOrdersFound);
        tvClaimOffers = (TextView) root.findViewById (R.id.tvClaimOffers);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myOrder = bundle.getString (AppConfigTags.ORDERS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        orderAdapter = new MyAccountOrderAdapter (getActivity (), myAccountOrderList);
        rvMyOrders.setAdapter (orderAdapter);
        rvMyOrders.setHasFixedSize (true);
        rvMyOrders.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyOrders.setItemAnimator (new DefaultItemAnimator ());
        rvMyOrders.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        tvClaimOffers.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (getActivity (), MainActivity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity ().startActivity (intent);
                getActivity ().overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    
    private void setData () {
        myAccountOrderList.clear ();
        try {
            JSONArray jsonArray = new JSONArray (myOrder);
            rlMain.setVisibility (View.VISIBLE);
            rlNoOrderFound.setVisibility (View.GONE);
        
            if (jsonArray.length () > 0) {
                for (int j = 0; j < jsonArray.length (); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (j);
                    myAccountOrderList.add (new MyAccountOrder (
                            jsonObject.getInt (AppConfigTags.ORDER_ID),
                            jsonObject.getInt (AppConfigTags.ORDER_STATUS),
                            jsonObject.getInt (AppConfigTags.ORDER_QTY),
                            jsonObject.getInt (AppConfigTags.OFFER_PRICE),
                            jsonObject.getInt (AppConfigTags.OFFER_MRP),
                            jsonObject.getInt (AppConfigTags.OFFER_QTY),
                            jsonObject.getString (AppConfigTags.ORDER_UNIQUE_ID),
                            jsonObject.getString (AppConfigTags.OFFER_NAME),
                            jsonObject.getString (AppConfigTags.ORDER_CREATED_AT)
                    ));
                }
                orderAdapter.notifyDataSetChanged ();
            } else {
                rlMain.setVisibility (View.GONE);
                rlNoOrderFound.setVisibility (View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}

