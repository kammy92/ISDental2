package com.indiasupply.isdental.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.EventDetailsPref;

public class EventDetailFragment extends Fragment {
    WebView webView;
    EventDetailsPref eventDetailsPref;
    String type;
    String datahtml;
    
    public static EventDetailFragment newInstance (String type, String html) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString ("TYPE", type);
        args.putString ("HTML", html);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate (R.layout.fragment_event_detail, container, false);
        initView(rootView);
        initBundle ();
        initData ();
        initListener();
        return rootView;
    }

    private void initView(View v) {
        webView = (WebView)v.findViewById(R.id.webView);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        type = bundle.getString ("TYPE");
    }
    
    private void initData() {
        eventDetailsPref = EventDetailsPref.getInstance();
        switch (type){
            case "FAQ":
                datahtml = eventDetailsPref.getStringPref(getActivity(), EventDetailsPref.EVENT_FAQ);
                break;
            case "FEES":
                datahtml = eventDetailsPref.getStringPref(getActivity(), EventDetailsPref.EVENT_FEES);
                break;
            case "SCHEDULE":
                datahtml = eventDetailsPref.getStringPref(getActivity(), EventDetailsPref.EVENT_SCHEDULE);
                break;
            case "VENUE":
                datahtml = eventDetailsPref.getStringPref(getActivity(), EventDetailsPref.EVENT_VENUE);
                break;
            case "INCLUSIONS":
                datahtml = eventDetailsPref.getStringPref(getActivity(), EventDetailsPref.EVENT_INCLUSIONS);
                break;
            case "CONTACT":
                datahtml = eventDetailsPref.getStringPref(getActivity(), EventDetailsPref.EVENT_CONTACT_DETAILS);
                break;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + datahtml);
        webView.loadDataWithBaseURL ("www.google.com", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
        WebSettings webSettings = webView.getSettings ();
        webSettings.setStandardFontFamily (Constants.font_name);
    }

    private void initListener() {
    }
}
