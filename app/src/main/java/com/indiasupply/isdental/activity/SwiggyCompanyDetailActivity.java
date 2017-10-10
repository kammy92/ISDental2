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
import com.indiasupply.isdental.adapter.SwiggyRecommendedProductAdapter;
import com.indiasupply.isdental.model.SwiggyProduct;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;

/**
 * Created by sud on 26/9/17.
 */

public class SwiggyCompanyDetailActivity extends AppCompatActivity {
    ArrayList<SwiggyProduct> recommendedList = new ArrayList<> ();
    ArrayList<SwiggyProduct> productList = new ArrayList<> ();
    RecyclerView rvRecommended;
    RecyclerView rvProducts;
    NestedScrollView nestedScrollView;
    CoordinatorLayout clMain;
    AppBarLayout appBarLayout;
    TextView tvTitleBrandCategory;
    TextView tvTitleBrandName;
    View v1;
    SwiggyRecommendedProductAdapter recommendedProductAdapter;
    SwiggyProductAdapter productAdapter;
    RelativeLayout rlBack;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_brand_detail);
        initView ();
        initData ();
        initListener ();
        setData ();
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
                    v1.setVisibility (View.INVISIBLE);
                }
                
                if (scrollY > 0) {
                    v1.setVisibility (View.VISIBLE);
                    tvTitleBrandCategory.setVisibility (View.VISIBLE);
                    tvTitleBrandName.setVisibility (View.VISIBLE);
                }
                
                tvTitleBrandName.setAlpha ((float) (scrollY * 0.003));
                tvTitleBrandCategory.setAlpha ((float) (scrollY * 0.003));
                
                if (scrollY == (v.getChildAt (0).getMeasuredHeight () - v.getMeasuredHeight ())) {
//                    Log.i (TAG, "BOTTOM SCROLL");
                }
            }
        });
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        v1 = findViewById (R.id.v1);
        rvRecommended = (RecyclerView) findViewById (R.id.rvRecommended);
        rvProducts = (RecyclerView) findViewById (R.id.rvProducts);
        nestedScrollView = (NestedScrollView) findViewById (R.id.nestedScrollView);
        appBarLayout = (AppBarLayout) findViewById (R.id.appBar);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvTitleBrandCategory = (TextView) findViewById (R.id.tvTitleBrandCategory);
        tvTitleBrandName = (TextView) findViewById (R.id.tvTitleBrandName);
    }
    
    private void initData () {
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
    
        Utils.setTypefaceToAllViews (SwiggyCompanyDetailActivity.this, rvProducts);
        rvProducts.setNestedScrollingEnabled (false);
        rvRecommended.setNestedScrollingEnabled (false);
        rvRecommended.setFocusable (false);
        rvProducts.setFocusable (false);
    
        productAdapter = new SwiggyProductAdapter (SwiggyCompanyDetailActivity.this, productList);
        rvProducts.setAdapter (productAdapter);
        rvProducts.setHasFixedSize (true);
        rvProducts.setLayoutManager (new LinearLayoutManager (SwiggyCompanyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        rvProducts.setItemAnimator (new DefaultItemAnimator ());
        rvProducts.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    
        recommendedProductAdapter = new SwiggyRecommendedProductAdapter (SwiggyCompanyDetailActivity.this, recommendedList);
        rvRecommended.setAdapter (recommendedProductAdapter);
        rvRecommended.setHasFixedSize (true);
        rvRecommended.setLayoutManager (new GridLayoutManager (SwiggyCompanyDetailActivity.this, 2, GridLayoutManager.VERTICAL, false));
        rvRecommended.setItemAnimator (new DefaultItemAnimator ());
        rvRecommended.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), (int) Utils.pxFromDp (this, 16), 2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
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
    
    private void setData () {
        recommendedList.add (new SwiggyProduct (true, 1, R.drawable.ic_information, "Name 1", "Description 1", "Dental Equipments", "Rs 12,000/-", "Recommended", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg"));
        recommendedList.add (new SwiggyProduct (true, 2, R.drawable.ic_information, "Name 2", "Description 2", "Dental Equipments", "Rs 8,000/-", "Recommended", "http://famdent.indiasupply.com/isdental/api/images/products/product2.jpg"));
        recommendedList.add (new SwiggyProduct (true, 3, R.drawable.ic_information, "Name 3", "Description 3", "Dental Equipments", "Rs 3,000/-", "Recommended", "http://famdent.indiasupply.com/isdental/api/images/products/product3.jpg"));
        recommendedList.add (new SwiggyProduct (true, 4, R.drawable.ic_information, "Name 4", "Description 4", "Dental Equipments", "Rs 10,000/-", "Recommended", "http://famdent.indiasupply.com/isdental/api/images/products/product4.jpg"));
        recommendedList.add (new SwiggyProduct (true, 5, R.drawable.ic_information, "Name 5", "Description 5", "Dental Equipments", "Rs 2,000/-", "Recommended", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg"));
        recommendedProductAdapter.notifyDataSetChanged ();
        
        productList.add (new SwiggyProduct (false, 6, R.drawable.ic_information, "Name 6", "Description 6", "", "Rs 15,599/-", "Offers", ""));
        productList.add (new SwiggyProduct (false, 7, R.drawable.ic_information, "Name 7", "", "Dental Equipments", "Rs 18,399/-", "Offers", ""));
        productList.add (new SwiggyProduct (false, 8, R.drawable.ic_information, "Name 8", "Description 8 askj kas  kja sdkj asdkj asdkj asdkj asdkja sdkja sdkj asdkj askdj askddj ask dakj sjd aks dkja sdk asd lk sadksadkj asdk as", "", "Rs 12,230/-", "Offers", ""));
        productList.add (new SwiggyProduct (false, 9, R.drawable.ic_information, "Name 9", "", "Dental Equipments", "Rs 499/-", "Offers", ""));
        productList.add (new SwiggyProduct (false, 10, R.drawable.ic_information, "Name 10", "Description 10", "", "Rs 1,499/-", "Offers", ""));
        productAdapter.notifyDataSetChanged ();
    }
}