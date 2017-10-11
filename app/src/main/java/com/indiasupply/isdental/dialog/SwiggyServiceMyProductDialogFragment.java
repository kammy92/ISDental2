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
import com.indiasupply.isdental.adapter.SwiggyServiceMyProductAdapter;
import com.indiasupply.isdental.model.SwiggyMyProduct;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyServiceMyProductDialogFragment extends DialogFragment {
    ImageView ivCancel;
    TextView tvTitle;
    RecyclerView rvProductList;
    SwiggyServiceMyProductAdapter productAdapter;
    List<SwiggyMyProduct> swiggyMyProducts = new ArrayList<> ();
    
    public static SwiggyServiceMyProductDialogFragment newInstance (int contact_id) {
        SwiggyServiceMyProductDialogFragment f = new SwiggyServiceMyProductDialogFragment ();
        // Supply num input as an argument.
        Bundle args = new Bundle ();
        // args.putInt("contact_id", contact_id);
        //args.putString("contact_name", contact_name);
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
        rvProductList = (RecyclerView) root.findViewById (R.id.rvMyProduct);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        productAdapter = new SwiggyServiceMyProductAdapter (getActivity (), swiggyMyProducts);
        rvProductList.setAdapter (productAdapter);
        rvProductList.setHasFixedSize (true);
        rvProductList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvProductList.setItemAnimator (new DefaultItemAnimator ());
        rvProductList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
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
        swiggyMyProducts.add (new SwiggyMyProduct (1, R.drawable.ic_person, "Product 1", "Description 1", ""));
        swiggyMyProducts.add (new SwiggyMyProduct (2, R.drawable.ic_person, "Product 2", "Description 2", ""));
        swiggyMyProducts.add (new SwiggyMyProduct (3, R.drawable.ic_person, "Product 3", "Description 3", ""));
        swiggyMyProducts.add (new SwiggyMyProduct (4, R.drawable.ic_person, "Product 4", "Description 4", ""));
        swiggyMyProducts.add (new SwiggyMyProduct (5, R.drawable.ic_person, "Product 5", "Description 5", ""));
        swiggyMyProducts.add (new SwiggyMyProduct (6, R.drawable.ic_person, "Product 6", "Description 6", ""));
        productAdapter.notifyDataSetChanged ();
    }
}