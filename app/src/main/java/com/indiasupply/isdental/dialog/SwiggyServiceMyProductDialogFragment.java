package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyServiceMyProductAdapter;
import com.indiasupply.isdental.model.SwiggyMyProduct;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwiggyServiceMyProductDialogFragment extends DialogFragment {
    ImageView ivCancel;
    TextView tvTitle;
    RecyclerView rvMyProducts;
    CoordinatorLayout clMain;
    SwiggyServiceMyProductAdapter productAdapter;
    List<SwiggyMyProduct> myProductList = new ArrayList<> ();
    
    String myProducts;
    
    RelativeLayout rlNoProductFound;
    
    public SwiggyServiceMyProductDialogFragment newInstance (String products) {
        SwiggyServiceMyProductDialogFragment f = new SwiggyServiceMyProductDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.SWIGGY_PRODUCTS, products);
        f.setArguments (args);
        return f;
    }
    
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_my_product, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        rvMyProducts = (RecyclerView) root.findViewById (R.id.rvMyProduct);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        clMain = (CoordinatorLayout) root.findViewById (R.id.clMain);
        rlNoProductFound = (RelativeLayout) root.findViewById (R.id.rlNoProductFound);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        myProducts = bundle.getString (AppConfigTags.SWIGGY_PRODUCTS);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        productAdapter = new SwiggyServiceMyProductAdapter (getActivity (), myProductList);
        rvMyProducts.setAdapter (productAdapter);
        rvMyProducts.setHasFixedSize (true);
        rvMyProducts.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvMyProducts.setItemAnimator (new DefaultItemAnimator ());
        rvMyProducts.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
    
    private void setData () {
        try {
            JSONArray jsonArrayBrand = new JSONArray (myProducts);
            rvMyProducts.setVisibility (View.VISIBLE);
            rlNoProductFound.setVisibility (View.GONE);
    
            if (jsonArrayBrand.length () > 0) {
                for (int i = 0; i < jsonArrayBrand.length (); i++) {
                    JSONObject jsonObjectBrand = jsonArrayBrand.getJSONObject (i);
                    myProductList.add (i, new SwiggyMyProduct (
                            jsonObjectBrand.getInt (AppConfigTags.SWIGGY_PRODUCT_ID),
                            R.drawable.default_my_equipment,
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_NAME),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_DESCRIPTION),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_IMAGE),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_CATEGORY),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_BRAND),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_MODEL_NUMBER),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_SERIAL_NUMBER),
                            jsonObjectBrand.getString (AppConfigTags.SWIGGY_PRODUCT_PURCHASE_DATE)
                    ));
                }
                productAdapter.notifyDataSetChanged ();
            } else {
                rvMyProducts.setVisibility (View.GONE);
                rlNoProductFound.setVisibility (View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}