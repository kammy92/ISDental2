package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.MyAccountFavouriteAdapter;
import com.indiasupply.isdental.model.MyAccountFavourite;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountFavouritesDialogFragment extends DialogFragment {
    RecyclerView rvMyFavourites;
    List<MyAccountFavourite> myAccountFavouriteList = new ArrayList<> ();
    MyAccountFavouriteAdapter myAccountFavouriteAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    String myFavourites = "";
    RelativeLayout rlNoFavouriteFound;
    
    public static MyAccountFavouritesDialogFragment newInstance (String myFavourites) {
        Log.e ("jsonarray3", myFavourites);
        MyAccountFavouritesDialogFragment fragment = new MyAccountFavouritesDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.FAVOURITES, myFavourites);
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
        View root = inflater.inflate (R.layout.fragment_dialog_my_account_favourites, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvMyFavourites = (RecyclerView) root.findViewById (R.id.rvMyFavourites);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rlNoFavouriteFound = (RelativeLayout) root.findViewById (R.id.rlNoFavouriteFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myFavourites = bundle.getString (AppConfigTags.FAVOURITES);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        myAccountFavouriteAdapter = new MyAccountFavouriteAdapter (getActivity (), myAccountFavouriteList);
        rvMyFavourites.setAdapter (myAccountFavouriteAdapter);
        rvMyFavourites.setHasFixedSize (true);
        rvMyFavourites.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyFavourites.setItemAnimator (new DefaultItemAnimator ());
        rvMyFavourites.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
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
    
       /* myAccountFavouriteList.add (new MyAccountFavourite (1, R.drawable.ic_person, "Dr. Mohammad Atta", "9 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountFavouriteList.add (new MyAccountFavourite (2, R.drawable.ic_person, "Dr. Zakir Nayak", "10 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountFavouriteList.add (new MyAccountFavourite (3, R.drawable.ic_person, "Dr. Abu Baghdadi", "11 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountFavouriteList.add (new MyAccountFavourite (4, R.drawable.ic_person, "Dr. Osama Bin Laden", "12 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
        myAccountFavouriteAdapter.notifyDataSetChanged ();*/
    
    
        try {
            JSONArray jsonArray = new JSONArray (myFavourites);
            rvMyFavourites.setVisibility (View.VISIBLE);
            rlNoFavouriteFound.setVisibility (View.GONE);
        
            if (jsonArray.length () > 0) {
                for (int j = 0; j < jsonArray.length (); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (j);
                    myAccountFavouriteList.add (new MyAccountFavourite (
                            jsonObject.getInt (AppConfigTags.CONTACT_ID),
                            R.drawable.default_company,
                            jsonObject.getInt (AppConfigTags.CONTACT_TYPE),
                            jsonObject.getString (AppConfigTags.CONTACT_NAME),
                            jsonObject.getString (AppConfigTags.CONTACT_IMAGE),
                            jsonObject.getString (AppConfigTags.CONTACT_PHONE),
                            jsonObject.getString (AppConfigTags.CONTACT_LOCATION),
                            jsonObject.getString (AppConfigTags.CONTACT_EMAIL),
                            jsonObject.getString (AppConfigTags.CONTACT_WEBSITE),
                            jsonObject.getBoolean (AppConfigTags.CONTACT_FAVOURITE)
                    ));
                }
                myAccountFavouriteAdapter.notifyDataSetChanged ();
            } else {
                rvMyFavourites.setVisibility (View.GONE);
                rlNoFavouriteFound.setVisibility (View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}