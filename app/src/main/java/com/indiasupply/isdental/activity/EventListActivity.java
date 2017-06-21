package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.fragment.EventListFragment;
import com.indiasupply.isdental.helper.DatabaseHandler;
import com.indiasupply.isdental.model.Banner;
import com.indiasupply.isdental.utils.CustomImageSlider;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class EventListActivity extends AppCompatActivity {
    Toolbar toolbar;
    CoordinatorLayout clMain;
    RelativeLayout rlBack;
    TabLayout tabLayout;
    private ViewPager viewPager;
    ProgressDialog progressDialog;
  
    ImageView ivFilter;
    ImageView ivSort;
    TextView tvTitle;
    SearchView searchView;
    
    DatabaseHandler db;
    private SliderLayout slider;
    AppBarLayout appBar;
    
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_event_list);
        initView ();
        initData ();
        initSlider ();
        initListener ();
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        tabLayout = (TabLayout) findViewById (R.id.tabs);
        viewPager = (ViewPager) findViewById (R.id.viewpager);
        ivSort = (ImageView) findViewById (R.id.ivSort);
        tvTitle = (TextView) findViewById (R.id.tvType);
        searchView = (SearchView) findViewById (R.id.searchView);
        slider = (SliderLayout) findViewById (R.id.slider);
        appBar = (AppBarLayout) findViewById (R.id.appBar);
    }
    
    private void initData () {
        db = new DatabaseHandler (getApplicationContext ());
        progressDialog = new ProgressDialog (this);
        tabLayout.setupWithViewPager (viewPager);
        tabLayout.setTabGravity (TabLayout.GRAVITY_FILL);
        searchView.setQueryHint (Html.fromHtml ("<font color = #ffffff>" + "Search" + "</font>"));
        setupViewPager (viewPager);
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initSlider () {
        slider.removeAllSliders ();
        for (int i = 0; i < db.getAllEventsBanners ().size (); i++) {
            final Banner banner = db.getAllEventsBanners ().get (i);
            CustomImageSlider slider2 = new CustomImageSlider (this);
            slider2
                    .image (banner.getImage ())
                    .setScaleType (BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener (new BaseSliderView.OnSliderClickListener () {
                        @Override
                        public void onSliderClick (BaseSliderView slider) {
                            Uri uri;
                            if (banner.getUrl ().contains ("http://") || banner.getUrl ().contains ("https://")) {
                                uri = Uri.parse (banner.getUrl ());
                            } else {
                                uri = Uri.parse ("http://" + banner.getUrl ());
                            }
                            Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                            startActivity (intent);
                        }
                    });
            slider.addSlider (slider2);
        }
        slider.getPagerIndicator ().setVisibility (View.GONE);
        slider.setPresetTransformer (SliderLayout.Transformer.Fade);
        slider.setCustomAnimation (new DescriptionAnimation ());
        slider.setDuration (5000);
        slider.addOnPageChangeListener (new ViewPagerEx.OnPageChangeListener () {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
            }
            
            @Override
            public void onPageSelected (int position) {
            }
            
            @Override
            public void onPageScrollStateChanged (int state) {
                switch (state) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        slider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        appBar.addOnOffsetChangedListener (new AppBarLayout.OnOffsetChangedListener () {
            @Override
            public void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //expanded
                    toolbar.setBackgroundResource (R.drawable.toolbar_bg);
                } else if (Math.abs (verticalOffset) >= appBarLayout.getTotalScrollRange ()) {
                    //collapsed
                    toolbar.setBackgroundResource (R.drawable.blank_bg);
                } else {
                    //idle
                    toolbar.setBackgroundResource (R.drawable.toolbar_bg);
                }
            }
        });
    }
    
    private void setupViewPager (ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager ());
        adapter.addFragment (new EventListFragment (), "CONFERENCE");
        adapter.addFragment (new EventListFragment(), "EXPO");
        adapter.addFragment (new EventListFragment(), "WORKSHOP");
        viewPager.setAdapter (adapter);
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();
        
        public ViewPagerAdapter (FragmentManager manager) {
            super (manager);
        }
        
        @Override
        public Fragment getItem (int position) {
            return EventListFragment.newInstance (mFragmentTitleList.get (position));
        }
        
        @Override
        public int getCount () {
            return mFragmentList.size ();
        }
        
        public void addFragment (Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }
        
        @Override
        public CharSequence getPageTitle (int position) {
            return mFragmentTitleList.get (position);
        }
    }
}