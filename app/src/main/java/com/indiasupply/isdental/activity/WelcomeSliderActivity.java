package com.indiasupply.isdental.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Utils;
import com.rd.PageIndicatorView;

public class WelcomeSliderActivity extends AppCompatActivity {
    
    PageIndicatorView pageIndicatorView;
    
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] sliders;
    private Button btSkip, btNext, btGetStarted;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener () {
        @Override
        public void onPageSelected (int position) {
            if (position == sliders.length - 1) {
                btNext.setVisibility (View.GONE);
                btSkip.setVisibility (View.GONE);
                btGetStarted.setVisibility (View.VISIBLE);
            } else {
                btGetStarted.setVisibility (View.GONE);
                btNext.setVisibility (View.VISIBLE);
                btSkip.setVisibility (View.VISIBLE);
            }
        }
        
        @Override
        public void onPageScrolled (int arg0, float arg1, int arg2) {
        }
        
        @Override
        public void onPageScrollStateChanged (int arg0) {
        }
    };
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_welcome_slider);
        initView ();
        initData ();
        initListener ();
        
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
    
    private void initView () {
        viewPager = (ViewPager) findViewById (R.id.view_pager);
        pageIndicatorView = (PageIndicatorView) findViewById (R.id.pageIndicatorView);
        btSkip = (Button) findViewById (R.id.btSkip);
        btNext = (Button) findViewById (R.id.btNext);
        btGetStarted = (Button) findViewById (R.id.btGetStarted);
    }
    
    private void initData () {
        pageIndicatorView.setViewPager (viewPager);
        pageIndicatorView.setInteractiveAnimation (true);
        
        Utils.setTypefaceToAllViews (this, btNext);
        sliders = new int[] {
                R.drawable.our_brands,
                R.drawable.expodent_chandigarh,
                R.drawable.upcoming_events,
                R.drawable.shop_by_category
        };
        
        changeStatusBarColor ();
        
        myViewPagerAdapter = new MyViewPagerAdapter ();
        viewPager.setAdapter (myViewPagerAdapter);
        viewPager.addOnPageChangeListener (viewPagerPageChangeListener);
    }
    
    private void initListener () {
        btSkip.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (WelcomeSliderActivity.this, LoginActivity.class);
                startActivity (intent);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                finish ();
            }
        });
        
        btNext.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                int current = getItem (+ 1);
                if (current < sliders.length) {
                    viewPager.setCurrentItem (current);
                }
            }
        });
        
        btGetStarted.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (WelcomeSliderActivity.this, LoginActivity.class);
                startActivity (intent);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                finish ();
            }
        });
    }
    
    @Override
    protected void onRestart () {
        super.onRestart ();
//        finish ();
    }
    
    @Override
    public void onBackPressed () {
        if (viewPager.getCurrentItem () > 0) {
            viewPager.setCurrentItem (viewPager.getCurrentItem () - 1, true);
        } else {
            finish ();
        }
    }
    
    private int getItem (int i) {
        return viewPager.getCurrentItem () + i;
    }
    
    private void changeStatusBarColor () {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow ();
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (Color.TRANSPARENT);
        }
    }
    
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        
        public MyViewPagerAdapter () {
        }
        
        @Override
        public Object instantiateItem (ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate (R.layout.welcome_slide, container, false);
            container.addView (view);
            ImageView imageView = (ImageView) view.findViewById (R.id.ivWelcomeSlide);
            imageView.setImageResource (sliders[position]);
            Utils.setTypefaceToAllViews (WelcomeSliderActivity.this, view);
            return view;
        }
        
        @Override
        public int getCount () {
            return sliders.length;
        }
        
        @Override
        public boolean isViewFromObject (View view, Object obj) {
            return view == obj;
        }
        
        @Override
        public void destroyItem (ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView (view);
        }
    }
}