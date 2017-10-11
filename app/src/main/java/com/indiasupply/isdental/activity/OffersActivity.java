package com.indiasupply.isdental.activity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Constants;
import com.indiasupply.isdental.utils.Utils;

import static com.indiasupply.isdental.R.id.webView;


/**
 * Created by l on 13/06/2017.
 */

public class OffersActivity extends AppCompatActivity {
    WebView htmlWebView;
    RelativeLayout rlBack;
    // ProgressDialog progressDialog;
    ProgressBar progressBar;
    FrameLayout fl1;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_offers);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initView () {
        htmlWebView = (WebView) findViewById (webView);
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
        //htmlWebView.loadUrl ("https://www.indiasupply.com/");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder ("<style>@font-face{font-family: myFont;src: url(file:///android_asset/" + Constants.font_name + ");}</style>" + "<p>3M Espe Filtek Z250 XT My Kit</p>\n" +
                "<p>Offer : 1 Alginate Free</p>\n" +
                "<p style=\"font-family: myfont; font-size: 18px;\"><a href=\"https://www.indiasupply.com/3m-espe-filtek-z250-xt-my-kit.html\">Buy</a></p>\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "<p>3M Espe Filtek Z350 XT My Kit</p>\n" +
                "<p>Offer : 2 Alginate Free</p>\n" +
                "<p style=\"font-family: myfont; font-size: 18px;\"><a href=\"https://www.indiasupply.com/3m-espe-offer-filtek-z350-xt-my-kit-2-alginate-impression-material-mint.html\">Buy</a></p>\n" +
                "\n");
        htmlWebView.loadDataWithBaseURL ("", spannableStringBuilder.toString (), "text/html", "UTF-8", "");
        
        
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
            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon) {
                super.onPageStarted (view, url, favicon);
                if (url.length () > 0) {
                    fl1.setVisibility (View.VISIBLE);
                }
            }
            
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
