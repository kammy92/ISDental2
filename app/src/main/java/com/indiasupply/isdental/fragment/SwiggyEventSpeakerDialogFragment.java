package com.indiasupply.isdental.fragment;

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
import com.indiasupply.isdental.adapter.SwiggyEventSpeakerAdapter;
import com.indiasupply.isdental.model.SwiggySpeaker;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyEventSpeakerDialogFragment extends DialogFragment {
    RecyclerView rvSpeakerList;
    List<SwiggySpeaker> speakerList = new ArrayList<> ();
    LinearLayoutManager linearLayoutManager;
    SwiggyEventSpeakerAdapter speakerAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_speakers, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvSpeakerList = (RecyclerView) root.findViewById (R.id.rvSpeakers);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
        
        speakerList.add (new SwiggySpeaker (1, "Dr.Zakir Khan", "http://famdent.indiasupply.com/isdental/api/images/brands/brand1.jpg", "9 Fail"));
        speakerList.add (new SwiggySpeaker (2, "Dr.Zakir Nayak", "http://famdent.indiasupply.com/isdental/api/images/brands/brand2.jpg", "10 Fail"));
        speakerList.add (new SwiggySpeaker (3, "Dr.Salman khursheed", "http://famdent.indiasupply.com/isdental/api/images/brands/brand3.jpg", "11 Fail"));
        speakerList.add (new SwiggySpeaker (4, "Dr.Usama", "http://famdent.indiasupply.com/isdental/api/images/brands/brand3.jpg", "12 Fail"));
        speakerAdapter = new SwiggyEventSpeakerAdapter (getActivity (), speakerList);
        rvSpeakerList.setAdapter (speakerAdapter);
        rvSpeakerList.setHasFixedSize (true);
        rvSpeakerList.setLayoutManager (linearLayoutManager);
        rvSpeakerList.setItemAnimator (new DefaultItemAnimator ());
        LinearLayoutManager layoutManager = ((LinearLayoutManager) rvSpeakerList.getLayoutManager ());
//            tvTitle.setText ("Recommended 1/" + speakerList.size ());
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
