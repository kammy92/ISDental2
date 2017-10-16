package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyEventExhibitorAdapter;
import com.indiasupply.isdental.model.SwiggyEventExhibitor;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwiggyEventExhibitorDialogFragment extends DialogFragment {
    RecyclerView rvExhibitor;
    List<SwiggyEventExhibitor> exhibitorList = new ArrayList<> ();
    LinearLayoutManager linearLayoutManager;
    SwiggyEventExhibitorAdapter exhibitorAdapter;
    
    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    
    String eventExhibitor;
    
    public static SwiggyEventExhibitorDialogFragment newInstance (String eventExhibitor) {
        SwiggyEventExhibitorDialogFragment fragment = new SwiggyEventExhibitorDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_EVENT_EXHIBITORS, eventExhibitor);
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
    public void onResume () {
        super.onResume ();
        getDialog ().setOnKeyListener (new DialogInterface.OnKeyListener () {
            @Override
            public boolean onKey (android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction () != KeyEvent.ACTION_UP)
                        return true;
                    else {
                        if (rlSearch.getVisibility () == View.VISIBLE) {
                            final Handler handler = new Handler ();
                            handler.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    ivSearch.setVisibility (View.VISIBLE);
                                    etSearch.setText ("");
                                }
                            }, 300);
                            final Handler handler2 = new Handler ();
                            handler2.postDelayed (new Runnable () {
                                @Override
                                public void run () {
                                    final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                                }
                            }, 600);
                            rlSearch.setVisibility (View.GONE);
                        } else {
                            getDialog ().dismiss ();
                        }
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_event_exhibitors, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rvExhibitor = (RecyclerView) root.findViewById (R.id.rvExhibitors);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        eventExhibitor = bundle.getString (AppConfigTags.SWIGGY_EVENT_EXHIBITORS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
    
        exhibitorAdapter = new SwiggyEventExhibitorAdapter (getActivity (), exhibitorList);
        rvExhibitor.setAdapter (exhibitorAdapter);
        rvExhibitor.setHasFixedSize (true);
        rvExhibitor.setLayoutManager (linearLayoutManager);
        rvExhibitor.setItemAnimator (new DefaultItemAnimator ());
        rvExhibitor.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlSearch.getVisibility () == View.VISIBLE) {
                    final Handler handler = new Handler ();
                    handler.postDelayed (new Runnable () {
                        @Override
                        public void run () {
                            ivSearch.setVisibility (View.VISIBLE);
                            etSearch.setText ("");
                        }
                    }, 600);
                    final Handler handler2 = new Handler ();
                    handler2.postDelayed (new Runnable () {
                        @Override
                        public void run () {
                            final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow (getView ().getWindowToken (), 0);
                        }
                    }, 300);
                    rlSearch.setVisibility (View.GONE);
                } else {
                    getDialog ().dismiss ();
                }
            }
        });
        ivSearch.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                final Handler handler = new Handler ();
                handler.postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        ivSearch.setVisibility (View.GONE);
                        etSearch.requestFocus ();
                    }
                }, 300);
                final Handler handler2 = new Handler ();
                handler2.postDelayed (new Runnable () {
                    @Override
                    public void run () {
                        final InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }, 600);
                rlSearch.setVisibility (View.VISIBLE);
            }
        });
    }
    
    private void setData () {
//        exhibitorList.add (new SwiggyEventExhibitor (1, R.drawable.ic_program, "3M ESPE", "A-31", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        exhibitorList.add (new SwiggyEventExhibitor (2, R.drawable.ic_program, "DEURR DENTAL", "H-Island", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        exhibitorList.add (new SwiggyEventExhibitor (3, R.drawable.ic_program, "CHESA", "B-3, B-4", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        exhibitorList.add (new SwiggyEventExhibitor (4, R.drawable.ic_program, "WOODPECKER", "A-20", "http://famdent.indiasupply.com/isdental/api/images/speakers/speaker1.png"));
//        exhibitorAdapter.notifyDataSetChanged ();
    
    
        try {
            JSONArray jsonArray = new JSONArray (eventExhibitor);
            for (int j = 0; j < jsonArray.length (); j++) {
                JSONObject jsonObjectExhibitor = jsonArray.getJSONObject (j);
                exhibitorList.add (j, new SwiggyEventExhibitor (jsonObjectExhibitor.getInt (AppConfigTags.SWIGGY_EXHIBITOR_ID),
                        R.drawable.ic_person,
                        jsonObjectExhibitor.getString (AppConfigTags.SWIGGY_EXHIBITOR_NAME),
                        jsonObjectExhibitor.getString (AppConfigTags.SWIGGY_EXHIBITOR_DESCRIPTION),
                        jsonObjectExhibitor.getString (AppConfigTags.SWIGGY_EXHIBITOR_IMAGE)));
            }
            exhibitorAdapter.notifyDataSetChanged ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
}