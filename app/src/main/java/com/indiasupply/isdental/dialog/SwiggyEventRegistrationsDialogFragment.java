package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Utils;


public class SwiggyEventRegistrationsDialogFragment extends DialogFragment {
    ImageView ivCancel;
    WebView webView;
    ProgressBar progressBar;
    FrameLayout fl1;
    View v1;
    
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_event_registration, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        getWebView ();
        return root;
    }
    
    private void initView (View root) {
        webView = (WebView) root.findViewById (R.id.webView);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        progressBar = (ProgressBar) root.findViewById (R.id.progressBar);
        fl1 = (FrameLayout) root.findViewById (R.id.fl1);
        v1 = root.findViewById (R.id.v1);
    }
    
    private void initBundle () {
        
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), v1);
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
    
    private void getWebView () {
        webView.setWebViewClient (new CustomWebViewClient ());
        WebSettings webSetting = webView.getSettings ();
        webSetting.setJavaScriptEnabled (true);
        webSetting.setDisplayZoomControls (true);
        webView.loadUrl ("https://www.google.com/");
        webView.setWebViewClient (new WebViewClient () {
            public void onPageFinished (WebView view, String url) {
                // progressDialog.dismiss ();
            }
        });
        
        if (Build.VERSION.SDK_INT >= 21) {
            progressBar.setProgressTintList (ColorStateList.valueOf (getResources ().getColor (R.color.primary)));
            progressBar.setIndeterminateTintList (ColorStateList.valueOf (getResources ().getColor (R.color.primary)));
        } else {
            progressBar.getProgressDrawable ().setColorFilter (
                    getResources ().getColor (R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBar.getIndeterminateDrawable ().setColorFilter (
                    getResources ().getColor (R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        
        webView.setWebViewClient (new WebViewClient () {
            public void onPageFinished (WebView view, String url) {
                //  progressDialog.dismiss ();
                fl1.setVisibility (View.GONE);
                v1.setVisibility (View.VISIBLE);
            }
        });
        
        webView.setWebChromeClient (new WebChromeClient () {
            public void onProgressChanged (WebView view, int progress) {
                if (progress > 70) {
                    progressBar.setIndeterminate (true);
                } else {
                    progressBar.setIndeterminate (false);
                    progressBar.setProgress (progress + 10);
                }
            }
        });
    }
    
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            view.loadUrl (url);
            return true;
        }
    }
}
