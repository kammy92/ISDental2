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
import com.indiasupply.isdental.adapter.SwiggyEventSpeakerAdapter;
import com.indiasupply.isdental.model.SwiggyEventSpeaker;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwiggyEventSpeakerDialogFragment extends DialogFragment {
    RecyclerView rvSpeakerList;
    List<SwiggyEventSpeaker> eventSpeakerList = new ArrayList<> ();
    SwiggyEventSpeakerAdapter eventSpeakerAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    String eventSpeakers;
    
    public static SwiggyEventSpeakerDialogFragment newInstance (String eventSpeakers) {
        SwiggyEventSpeakerDialogFragment fragment = new SwiggyEventSpeakerDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_EVENT_SPEAKERS, eventSpeakers);
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_event_speakers, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvSpeakerList = (RecyclerView) root.findViewById (R.id.rvSpeakers);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        eventSpeakers = bundle.getString (AppConfigTags.SWIGGY_EVENT_SPEAKERS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        eventSpeakerAdapter = new SwiggyEventSpeakerAdapter (getActivity (), eventSpeakerList);
        rvSpeakerList.setAdapter (eventSpeakerAdapter);
        rvSpeakerList.setHasFixedSize (true);
        rvSpeakerList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvSpeakerList.setItemAnimator (new DefaultItemAnimator ());
        rvSpeakerList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
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
//        eventSpeakerList.add (new SwiggyEventSpeaker (1, R.drawable.ic_person, "Dr. Mohammad Atta", "9 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerList.add (new SwiggyEventSpeaker (2, R.drawable.ic_person, "Dr. Zakir Nayak", "10 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerList.add (new SwiggyEventSpeaker (3, R.drawable.ic_person, "Dr. Abu Baghdadi", "11 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerList.add (new SwiggyEventSpeaker (4, R.drawable.ic_person, "Dr. Osama Bin Laden", "12 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerAdapter.notifyDataSetChanged ();
    
    
        try {
            JSONArray jsonArray = new JSONArray (eventSpeakers);
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObjectSpeaker = jsonArray.getJSONObject (j);
                eventSpeakerList.add (j, new SwiggyEventSpeaker (jsonObjectSpeaker.getInt (AppConfigTags.SWIGGY_SPEAKER_ID),
                        R.drawable.ic_person,
                        jsonObjectSpeaker.getString (AppConfigTags.SWIGGY_SPEAKERS_NAME),
                        jsonObjectSpeaker.getString (AppConfigTags.SWIGGY_SPEAKERS_DESCRIPTION),
                        jsonObjectSpeaker.getString (AppConfigTags.SWIGGY_SPEAKERS_IMAGE)));
            }
            eventSpeakerAdapter.notifyDataSetChanged ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}
