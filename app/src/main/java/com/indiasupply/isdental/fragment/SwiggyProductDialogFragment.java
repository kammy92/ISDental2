package com.indiasupply.isdental.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
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

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyRecommendedProductAdapter2;
import com.indiasupply.isdental.model.Product;

import java.util.ArrayList;
import java.util.List;


public class SwiggyProductDialogFragment extends DialogFragment {
    RecyclerView rv3;
    List<Product> productList = new ArrayList<> ();
    SwiggyRecommendedProductAdapter2 swiggyAdapter;
    
    ImageView ivCancel;
    
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_product, container, false);
        
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        rv3 = (RecyclerView) root.findViewById (R.id.rvProductList);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    private void initBundle () {
        
    }
    
    private void initData () {
        productList.add (new Product (true, 1, "Name 1", "Description 1", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg", "Rs 12,000/-", "Recommended"));
        productList.add (new Product (true, 2, "Name 2", "Description 2 aksjd kjlaskd laks dllk sadlk asldk asdldk sadlk asdlka sdlk asldk asd ask jasdkj asd kj sadk kj asdk kj asdkn kaj sdk aakj sdk aksj dkj kj kjas aksj dkasjdka skdjnaksd kajs dk askdj aksd ka kajs dka sdk asdaksdajsdk asj dkja sdk kj askjd akjsd kajs dkja sdkja sdkj asdkasj ddkjasd kasdkjaslkdmalks dlkasd kasdlkmls dlk sadlk askjnhgldk", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product2.jpg", "Rs 8,000/-", "Recommended"));
        productList.add (new Product (true, 3, "Name 3", "Description 3", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product3.jpg", "Rs 3,000/-", "Recommended"));
        productList.add (new Product (true, 4, "Name 4", "Description 4", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product4.jpg", "Rs 10,000/-", "Recommended"));
        productList.add (new Product (true, 5, "Name 5", "Description 5", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg", "Rs 2,000/-", "Recommended"));
        
        swiggyAdapter = new SwiggyRecommendedProductAdapter2 (getActivity (), productList);
        rv3.setAdapter (swiggyAdapter);
        rv3.setHasFixedSize (true);
        rv3.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rv3.setItemAnimator (new DefaultItemAnimator ());
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
    
}
