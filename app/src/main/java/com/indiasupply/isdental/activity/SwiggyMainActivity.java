package com.indiasupply.isdental.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.fragment.SwiggyHomeFragment1;
import com.indiasupply.isdental.utils.UserDetailsPref;
import com.indiasupply.isdental.utils.Utils;

import java.lang.reflect.Field;


public class SwiggyMainActivity extends AppCompatActivity {
    UserDetailsPref userDetailsPref;
    CoordinatorLayout clMain;
    
    ProgressDialog progressDialog;
    
    public static void disableShiftMode (BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt (0);
        try {
            Field shiftingMode = menuView.getClass ().getDeclaredField ("mShiftingMode");
            shiftingMode.setAccessible (true);
            shiftingMode.setBoolean (menuView, false);
            shiftingMode.setAccessible (false);
            for (int i = 0; i < menuView.getChildCount (); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt (i);
                //noinspection RestrictedApi
                item.setShiftingMode (false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked (item.getItemData ().isChecked ());
            }
        } catch (NoSuchFieldException e) {
            Log.e ("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e ("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activtiy_swiggy_main);
        initView ();
        initData ();
        initListener ();
        
        Window window = getWindow ();
//        window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (clMain != null) {
//                clMain.setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//        }
        
        
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById (R.id.navigation);
        
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener () {
                    @Override
                    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId ()) {
                            case R.id.action_item1:
                                selectedFragment = SwiggyHomeFragment1.newInstance ();
                                break;
                            case R.id.action_item2:
                                selectedFragment = SwiggyHomeFragment1.newInstance ();
                                break;
                            case R.id.action_item3:
                                selectedFragment = SwiggyHomeFragment1.newInstance ();
                                break;
                            case R.id.action_item4:
                                selectedFragment = SwiggyHomeFragment1.newInstance ();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
                        transaction.replace (R.id.frame_layout, selectedFragment);
                        transaction.commit ();
                        return true;
                    }
                });
        
        disableShiftMode (bottomNavigationView);
        
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction ();
        transaction.replace (R.id.frame_layout, SwiggyHomeFragment1.newInstance ());
        transaction.commit ();

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams ();
//        layoutParams.setBehavior (new BottomNavigationViewBehavior ());
        
        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void initListener () {
    }
    
    @Override
    public void onBackPressed () {
/*
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .content (R.string.dialog_text_quit_application)
                .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                .neutralColor (getResources ().getColor (R.color.app_text_color_dark))
                .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                .negativeColor (getResources ().getColor (R.color.app_text_color_dark))
                .typeface (SetTypeFace.getTypeface (this), SetTypeFace.getTypeface (this))
                .canceledOnTouchOutside (false)
                .cancelable (false)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .neutralText (R.string.dialog_action_logout)
                .onNeutral (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_ID, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.HEADER_USER_LOGIN_KEY, "");

                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userDetailsPref.putBooleanPref (MainActivity.this, UserDetailsPref.LOGGED_IN_SESSION, false);


                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
*/
        super.onBackPressed ();
        finish ();
    }
    
    public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {
        private int height;
        
        @Override
        public boolean onLayoutChild (CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
            height = child.getHeight ();
            return super.onLayoutChild (parent, child, layoutDirection);
        }
        
        @Override
        public boolean onStartNestedScroll (CoordinatorLayout coordinatorLayout, BottomNavigationView child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }
        
        @Override
        public void onNestedScroll (CoordinatorLayout coordinatorLayout, BottomNavigationView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyConsumed > 0) {
                slideDown (child);
            } else if (dyConsumed < 0) {
                slideUp (child);
            }
        }
        
        private void slideUp (BottomNavigationView child) {
            child.clearAnimation ();
            child.animate ().translationY (0).setDuration (200);
        }
        
        private void slideDown (BottomNavigationView child) {
            child.clearAnimation ();
            child.animate ().translationY (height).setDuration (200);
        }
    }
}