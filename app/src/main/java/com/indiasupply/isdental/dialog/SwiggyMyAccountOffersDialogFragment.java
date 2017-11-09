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
import com.indiasupply.isdental.adapter.SwiggyMyAccountOfferAdapter;
import com.indiasupply.isdental.model.SwiggyMyAccountOffer;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwiggyMyAccountOffersDialogFragment extends DialogFragment {
    RecyclerView rvMyOffers;
    List<SwiggyMyAccountOffer> myAccountOfferList = new ArrayList<> ();
    SwiggyMyAccountOfferAdapter myAccountOfferAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    RelativeLayout rlNoOffersFound;
    
    String myOffers = "";
    
    public static SwiggyMyAccountOffersDialogFragment newInstance (String myOffers) {
        SwiggyMyAccountOffersDialogFragment fragment = new SwiggyMyAccountOffersDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_OFFERS, myOffers);
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_my_account_offers, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvMyOffers = (RecyclerView) root.findViewById (R.id.rvMyOffers);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rlNoOffersFound = (RelativeLayout) root.findViewById (R.id.rlNoOffersFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myOffers = bundle.getString (AppConfigTags.SWIGGY_OFFERS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        
        myAccountOfferAdapter = new SwiggyMyAccountOfferAdapter (getActivity (), myAccountOfferList);
        rvMyOffers.setAdapter (myAccountOfferAdapter);
        rvMyOffers.setHasFixedSize (true);
        rvMyOffers.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyOffers.setItemAnimator (new DefaultItemAnimator ());
        rvMyOffers.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
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
        List<SwiggyMyAccountOffer> tempMyAccountOfferList = new ArrayList<> ();
        try {
            rvMyOffers.setVisibility (View.VISIBLE);
            rlNoOffersFound.setVisibility (View.GONE);
            JSONArray jsonArrayOffer = new JSONArray (myOffers);
            if (jsonArrayOffer.length () > 0) {
                for (int i = 0; i < jsonArrayOffer.length (); i++) {
                    JSONObject jsonObjectOffer = jsonArrayOffer.getJSONObject (i);
                    tempMyAccountOfferList.add (new SwiggyMyAccountOffer (
                            jsonObjectOffer.getInt (AppConfigTags.OFFER_ID),
                            R.drawable.ic_person,
                            jsonObjectOffer.getInt (AppConfigTags.OFFER_USER_ID),
                            jsonObjectOffer.getInt (AppConfigTags.OFFER_STATUS),
                            jsonObjectOffer.getString (AppConfigTags.OFFER_TEXT),
                            jsonObjectOffer.getString (AppConfigTags.OFFER_EXPIRE),
                            jsonObjectOffer.getString (AppConfigTags.OFFER_START)
                    ));
                }
            
                for (int i = 0; i < tempMyAccountOfferList.size (); i++) {
                    SwiggyMyAccountOffer offer = tempMyAccountOfferList.get (i);
                    if (offer.getStatus () != 0) {
                        myAccountOfferList.add (offer);
                    }
                }
            
                for (int i = 0; i < tempMyAccountOfferList.size (); i++) {
                    SwiggyMyAccountOffer offer = tempMyAccountOfferList.get (i);
                    if (offer.getStatus () == 0) {
                        myAccountOfferList.add (offer);
                    }
                }
            } else {
                rvMyOffers.setVisibility (View.GONE);
                rlNoOffersFound.setVisibility (View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        myAccountOfferAdapter.notifyDataSetChanged ();
    }
}
