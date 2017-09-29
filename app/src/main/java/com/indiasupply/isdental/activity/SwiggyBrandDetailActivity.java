package com.indiasupply.isdental.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyProductAdapter;
import com.indiasupply.isdental.adapter.SwiggyRecommendProductAdapter;
import com.indiasupply.isdental.model.Product;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;

/**
 * Created by sud on 26/9/17.
 */

public class SwiggyBrandDetailActivity extends AppCompatActivity {
    ArrayList<Product> recommendedlist = new ArrayList<> ();
    ArrayList<Product> productlist = new ArrayList<> ();
    RecyclerView rvRecommended;
    RecyclerView rvProducts;
    NestedScrollView nestedScrollView;
    CoordinatorLayout clMain;
    AppBarLayout appBarLayout;
    TextView tvTitleBrandCategory;
    TextView tvTitleBrandName;
    View v1;
    SwiggyRecommendProductAdapter swiggyRecommendProductAdapter;
    SwiggyProductAdapter swiggyProductAdapter;
    RelativeLayout rlBack;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_brand_detail);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                
            }
        });
        
        nestedScrollView.setOnScrollChangeListener (new NestedScrollView.OnScrollChangeListener () {
            @Override
            public void onScrollChange (NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
//                    Log.i (TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
//                    Log.i (TAG, "Scroll UP");
                }
                
                if (scrollY == 0) {
//                    Log.i (TAG, "TOP SCROLL");
                    v1.setVisibility (View.GONE);
                }
                
                if (scrollY > 0) {
                    v1.setVisibility (View.VISIBLE);
                    tvTitleBrandCategory.setVisibility (View.VISIBLE);
                    tvTitleBrandName.setVisibility (View.VISIBLE);
                }
                
                tvTitleBrandName.setAlpha ((float) (scrollY * 0.003));
                tvTitleBrandCategory.setAlpha ((float) (scrollY * 0.003));

//                if (scrollY >250){
//                    tvTitleBrandCategory.setVisibility (View.VISIBLE);
//                    tvTitleBrandName.setVisibility (View.VISIBLE);
//                }
                
                
                if (scrollY == (v.getChildAt (0).getMeasuredHeight () - v.getMeasuredHeight ())) {
//                    Log.i (TAG, "BOTTOM SCROLL");
                }
            }
        });
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        v1 = (View) findViewById (R.id.v1);
        rvRecommended = (RecyclerView) findViewById (R.id.rvRecommended);
        rvProducts = (RecyclerView) findViewById (R.id.rvProducts);
        nestedScrollView = (NestedScrollView) findViewById (R.id.nestedScrollView);
        appBarLayout = (AppBarLayout) findViewById (R.id.appBar);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvTitleBrandCategory = (TextView) findViewById (R.id.tvTitleBrandCategory);
        tvTitleBrandName = (TextView) findViewById (R.id.tvTitleBrandName);
    }
    
    private void initData () {
        rvProducts.setNestedScrollingEnabled (false);
        rvRecommended.setNestedScrollingEnabled (false);
        rvRecommended.setFocusable (false);
        rvProducts.setFocusable (false);
        
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
        
        
        Utils.setTypefaceToAllViews (SwiggyBrandDetailActivity.this, rvProducts);
        recommendedlist.add (new Product (true, 1, "Name 1", "Description 1", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg", "Rs 12,000/-", "Recommended"));
        recommendedlist.add (new Product (true, 2, "Name 2", "Description 2", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product2.jpg", "Rs 8,000/-", "Recommended"));
        recommendedlist.add (new Product (true, 3, "Name 3", "Description 3", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product3.jpg", "Rs 3,000/-", "Recommended"));
        recommendedlist.add (new Product (true, 4, "Name 4", "Description 4", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product4.jpg", "Rs 10,000/-", "Recommended"));
        recommendedlist.add (new Product (true, 5, "Name 5", "Description 5", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg", "Rs 2,000/-", "Recommended"));
        
        productlist.add (new Product (false, 6, "Name 6", "Description 6", "", "", "Rs 15,599/-", "Offers"));
        productlist.add (new Product (false, 7, "Name 7", "", "Dental Equipments", "", "Rs 18,399/-", "Offers"));
        productlist.add (new Product (false, 8, "Name 8", "Description 8 askj kas  kja sdkj asdkj asdkj asdkj asdkja sdkja sdkj asdkj askdj askddj ask dakj sjd aks dkja sdk asd lk sadksadkj asdk as", "", "", "Rs 12,230/-", "Offers"));
        productlist.add (new Product (false, 9, "Name 9", "", "Dental Equipments", "", "Rs 499/-", "Offers"));
        productlist.add (new Product (false, 10, "Name 10", "Description 10", "", "", "Rs 1,499/-", "Offers"));
        
        swiggyProductAdapter = new SwiggyProductAdapter (SwiggyBrandDetailActivity.this, productlist);
        rvProducts.setAdapter (swiggyProductAdapter);
        rvProducts.setHasFixedSize (true);
        rvProducts.setLayoutManager (new LinearLayoutManager (SwiggyBrandDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        rvProducts.setItemAnimator (new DefaultItemAnimator ());
        
        
        swiggyRecommendProductAdapter = new SwiggyRecommendProductAdapter (SwiggyBrandDetailActivity.this, recommendedlist);
        rvRecommended.setAdapter (swiggyRecommendProductAdapter);
        rvRecommended.setHasFixedSize (true);
        rvRecommended.setLayoutManager (new GridLayoutManager (SwiggyBrandDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvRecommended.setItemAnimator (new DefaultItemAnimator ());
        
        
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams ();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior ();
        if (behavior != null) {
            behavior.onNestedFling (clMain, appBarLayout, null, 0, 10000, true);
        }
        
        
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
}