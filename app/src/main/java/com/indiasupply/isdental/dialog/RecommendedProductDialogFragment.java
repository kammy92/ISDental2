package com.indiasupply.isdental.dialog;

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
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.RecommendedProductAdapter2;
import com.indiasupply.isdental.model.Product;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class RecommendedProductDialogFragment extends DialogFragment {
    RecyclerView rv3;
    List<Product> productList = new ArrayList<> ();
    RecommendedProductAdapter2 productAdapter;
    LinearLayoutManager linearLayoutManager;
    
    ImageView ivCancel;
    TextView tvTitle;
    int position;
    
    public RecommendedProductDialogFragment newInstance (ArrayList<Product> product_list, int position) {
        RecommendedProductDialogFragment f = new RecommendedProductDialogFragment ();
        Bundle args = new Bundle ();
        args.putParcelableArrayList (AppConfigTags.SWIGGY_PRODUCTS, product_list);
        args.putInt ("position", position);
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
        View root = inflater.inflate (R.layout.fragment_dialog_product_recommended, container, false);
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
        Bundle bundle = this.getArguments ();
        productList = bundle.getParcelableArrayList (AppConfigTags.SWIGGY_PRODUCTS);
        position = bundle.getInt ("position");
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
        linearLayoutManager = new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false);
    
        productAdapter = new RecommendedProductAdapter2 (getActivity (), productList);
        rv3.setAdapter (productAdapter);
        rv3.setHasFixedSize (true);
        rv3.setLayoutManager (linearLayoutManager);
        rv3.setItemAnimator (new DefaultItemAnimator ());
        rv3.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
//        tvTitle.setText ("Recommended 1/" + productList.size ());
        tvTitle.setText ("Recommended Products");
        
        rv3.scrollToPosition (position);
    }
    
    private void initListener () {
/*
        rv3.setOnScrollListener (new RecyclerView.OnScrollListener () {
//            @Override
//            public void onScrollStateChanged (RecyclerView recyclerView, int newState) {
//                switch (newState){
//                    case SCROLL_STATE_SETTLING:
//                        tvTitle.setText ("Recommended " + (((LinearLayoutManager) recyclerView.getLayoutManager ()).findFirstCompletelyVisibleItemPosition ()+1) + "/" + productList.size ());
//                        break;
//                    case SCROLL_STATE_DRAGGING:
//                        tvTitle.setText ("Recommended " + (((LinearLayoutManager) recyclerView.getLayoutManager ()).findFirstCompletelyVisibleItemPosition ()+1) + "/" + productList.size ());
//                        break;
//                    case SCROLL_STATE_IDLE:
//                        tvTitle.setText ("Recommended " + (((LinearLayoutManager) recyclerView.getLayoutManager ()).findFirstCompletelyVisibleItemPosition ()+1) + "/" + productList.size ());
//                        break;
//                }
//
//
//
//                super.onScrollStateChanged (recyclerView, newState);
//            }
    
            @Override
            public void onScrolled (RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition () < 0) {
//                    tvTitle.setText ("Recommended 1/" + productList.size ());
                } else {
                    tvTitle.setText ("Recommended " + (linearLayoutManager.findFirstCompletelyVisibleItemPosition () + 1) + "/" + productList.size ());
                }
                super.onScrolled (recyclerView, dx, dy);
            }
        });
  */
        
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
}
