package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Utils;


public class SwiggyEventFloorPlanDialogFragment extends DialogFragment {
    ImageView ivCancel;
    ImageView ivSearch;
    TextView tvTitle;
    RelativeLayout rlSearch;
    EditText etSearch;
    
    SubsamplingScaleImageView ivFloorPlan;
    
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
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_event_floor_plan, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivSearch = (ImageView) root.findViewById (R.id.ivSearch);
        etSearch = (EditText) root.findViewById (R.id.etSearch);
        rlSearch = (RelativeLayout) root.findViewById (R.id.rlSearch);
        ivFloorPlan = (SubsamplingScaleImageView) root.findViewById (R.id.ivFloorPlan);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        ivFloorPlan.setImage (ImageSource.resource (R.drawable.hallplan));
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
}