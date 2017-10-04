package com.indiasupply.isdental.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyRecommendedProductAdapter2;
import com.indiasupply.isdental.model.SwiggyProduct;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SwiggyProductDetailDialogFragment extends DialogFragment {
    RecyclerView rv3;
    List<SwiggyProduct> swiggyProductList = new ArrayList<> ();
    SwiggyRecommendedProductAdapter2 swiggyAdapter;
    LinearLayoutManager linearLayoutManager;
    
    ImageView ivCancel;
    TextView tvTitle;
    
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_product_recommended, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        rv3 = (RecyclerView) root.findViewById (R.id.rvProductList);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
    }
    
    private void initBundle () {
        
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
    
        swiggyProductList.add (new SwiggyProduct (true, 1, "Name 1", "Description 1", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg", "Rs 12,000/-", "Recommended"));
        swiggyProductList.add (new SwiggyProduct (true, 2, "Name 2", "Description 2 aksjd kjlaskd laks dllk sadlk asldk asdldk sadlk asdlka sdlk asldk asd ask jasdkj asd kj sadk kj asdk kj asdkn kaj sdk aakj sdk aksj dkj kj kjas aksj dkasjdka skdjnaksd kajs dk askdj aksd ka kajs dka sdk asdaksdajsdk asj dkja sdk kj askjd akjsd kajs dkja sdkja sdkj asdkasj ddkjasd kasdkjaslkdmalks dlkasd kasdlkmls dlk sadlk askjnhgldk", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product2.jpg", "Rs 8,000/-", "Recommended"));
        swiggyProductList.add (new SwiggyProduct (true, 3, "Name 3", "Description 3", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product3.jpg", "Rs 3,000/-", "Recommended"));
        swiggyProductList.add (new SwiggyProduct (true, 4, "Name 4", "Description 4", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product4.jpg", "Rs 10,000/-", "Recommended"));
        swiggyProductList.add (new SwiggyProduct (true, 5, "Name 5", "Description 5", "Dental Equipments", "http://famdent.indiasupply.com/isdental/api/images/products/product1.jpg", "Rs 2,000/-", "Recommended"));
    
        swiggyAdapter = new SwiggyRecommendedProductAdapter2 (getActivity (), swiggyProductList);
        rv3.setAdapter (swiggyAdapter);
        rv3.setHasFixedSize (true);
        rv3.setLayoutManager (linearLayoutManager);
        rv3.setItemAnimator (new DefaultItemAnimator ());
        rv3.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                (int) Utils.pxFromDp (getActivity (), 16),
                1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        LinearLayoutManager layoutManager = ((LinearLayoutManager) rv3.getLayoutManager ());
        tvTitle.setText ("Recommended 1/" + swiggyProductList.size ());
    }
    
    private void initListener () {
        rv3.setOnScrollListener (new RecyclerView.OnScrollListener () {
//            @Override
//            public void onScrollStateChanged (RecyclerView recyclerView, int newState) {
//                switch (newState){
//                    case SCROLL_STATE_SETTLING:
//                        tvTitle.setText ("Recommended " + (((LinearLayoutManager) recyclerView.getLayoutManager ()).findFirstCompletelyVisibleItemPosition ()+1) + "/" + swiggyProductList.size ());
//                        break;
//                    case SCROLL_STATE_DRAGGING:
//                        tvTitle.setText ("Recommended " + (((LinearLayoutManager) recyclerView.getLayoutManager ()).findFirstCompletelyVisibleItemPosition ()+1) + "/" + swiggyProductList.size ());
//                        break;
//                    case SCROLL_STATE_IDLE:
//                        tvTitle.setText ("Recommended " + (((LinearLayoutManager) recyclerView.getLayoutManager ()).findFirstCompletelyVisibleItemPosition ()+1) + "/" + swiggyProductList.size ());
//                        break;
//                }
//
//
//
//                super.onScrollStateChanged (recyclerView, newState);
//            }
        
            @Override
            public void onScrolled (RecyclerView recyclerView, int dx, int dy) {
                Log.e ("karman", " " + linearLayoutManager.findFirstCompletelyVisibleItemPosition ());
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition () < 0) {
//                    tvTitle.setText ("Recommended 1/" + swiggyProductList.size ());
                } else {
                    tvTitle.setText ("Recommended " + (linearLayoutManager.findFirstCompletelyVisibleItemPosition () + 1) + "/" + swiggyProductList.size ());
                }
                super.onScrolled (recyclerView, dx, dy);
            }
        });
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
}
