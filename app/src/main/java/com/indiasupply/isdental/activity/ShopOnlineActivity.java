package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Utils;


/**
 * Created by l on 13/06/2017.
 */

public class ShopOnlineActivity extends AppCompatActivity {
    WebView htmlWebView;
    RelativeLayout rlBack;
    ProgressDialog progressDialog;
    
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
    }
    
    private void initData () {
        progressDialog = new ProgressDialog (ShopOnlineActivity.this);
        Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
        final Handler handler = new Handler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run () {
                progressDialog.dismiss ();
            }
        }, 5000);
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
        htmlWebView.loadUrl ("https://www.indiasupply.com/");
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
