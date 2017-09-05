package com.indiasupply.isdental.activity;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Utils;


/**
 * Created by l on 13/06/2017.
 */

public class ShopOnlineActivity extends AppCompatActivity {
    WebView htmlWebView;
    RelativeLayout rlBack;
    // ProgressDialog progressDialog;
    ProgressBar progressBar;
    FrameLayout fl1;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_shop_online);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initView () {
        htmlWebView = (WebView) findViewById (R.id.webView);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        fl1 = (FrameLayout) findViewById (R.id.fl1);
    }
    
    private void initData () {
        // progressDialog = new ProgressDialog (ShopOnlineActivity.this);
        //  Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
        getWebView ();
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    
    private void getWebView () {
        htmlWebView.setWebViewClient (new CustomWebViewClient ());
        WebSettings webSetting = htmlWebView.getSettings ();
        webSetting.setJavaScriptEnabled (true);
        webSetting.setDisplayZoomControls (true);
        htmlWebView.loadUrl ("https://www.indiasupply.com/shopbycategory/");
        htmlWebView.setWebViewClient (new WebViewClient () {
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
    
    
        htmlWebView.setWebViewClient (new WebViewClient () {
            public void onPageFinished (WebView view, String url) {
                //  progressDialog.dismiss ();
                fl1.setVisibility (View.GONE);
            }
        });
    
        htmlWebView.setWebChromeClient (new WebChromeClient () {
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
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            view.loadUrl (url);
            return true;
        }
    }
}
