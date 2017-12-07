package com.indiasupply.isdental.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.indiasupply.isdental.R;

public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView (R.layout.activity_splash_screen);
        
        new Handler ().postDelayed (new Runnable () {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */
            
            @Override
            public void run () {
                Intent intent = new Intent (SplashScreenActivity.this, SwiggyMainActivity.class);
                intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity (intent);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                
            }
        }, SPLASH_TIME_OUT);
    }
    
}