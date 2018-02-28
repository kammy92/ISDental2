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
import com.indiasupply.isdental.adapter.EventSpeakerAdapter;
import com.indiasupply.isdental.model.EventSpeaker;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventSpeakerDialogFragment extends DialogFragment {
    RecyclerView rvSpeakerList;
    List<EventSpeaker> eventSpeakerList = new ArrayList<> ();
    EventSpeakerAdapter eventSpeakerAdapter;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    String eventSpeakers;
    
    public static EventSpeakerDialogFragment newInstance (String eventSpeakers) {
        EventSpeakerDialogFragment fragment = new EventSpeakerDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.EVENT_SPEAKERS, eventSpeakers);
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
        View root = inflater.inflate (R.layout.fragment_dialog_event_speakers, container, false);
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
        eventSpeakers = bundle.getString (AppConfigTags.EVENT_SPEAKERS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        eventSpeakerAdapter = new EventSpeakerAdapter (getActivity (), eventSpeakerList);
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
//        eventSpeakerList.add (new EventSpeaker (1, R.drawable.ic_person, "Dr. Mohammad Atta", "9 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerList.add (new EventSpeaker (2, R.drawable.ic_person, "Dr. Zakir Nayak", "10 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerList.add (new EventSpeaker (3, R.drawable.ic_person, "Dr. Abu Baghdadi", "11 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerList.add (new EventSpeaker (4, R.drawable.ic_person, "Dr. Osama Bin Laden", "12 Fail", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        eventSpeakerAdapter.notifyDataSetChanged ();
    
    
        try {
            JSONArray jsonArray = new JSONArray (eventSpeakers);
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObjectSpeaker = jsonArray.getJSONObject (j);
                eventSpeakerList.add (j, new EventSpeaker (jsonObjectSpeaker.getInt (AppConfigTags.SPEAKER_ID),
                        R.drawable.default_speaker,
                        jsonObjectSpeaker.getString (AppConfigTags.SPEAKERS_NAME),
                        jsonObjectSpeaker.getString (AppConfigTags.SPEAKERS_DESCRIPTION),
                        jsonObjectSpeaker.getString (AppConfigTags.SPEAKERS_IMAGE)));
            }
            eventSpeakerAdapter.notifyDataSetChanged ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}
