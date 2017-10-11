package com.indiasupply.isdental.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;


public class SwiggyServiceAddRequestDialogFragment extends DialogFragment {
    ImageView ivCancel;
    TextView tvModelNumber;
    TextView tvSerialNumber;
    TextView tvSelectProduct;
    TextView tvCounter;
    CardView cv1;
    EditText etRequestDescription;
    
    ArrayList<String> productList = new ArrayList<> ();
    
    public static SwiggyServiceAddRequestDialogFragment newInstance (int contact_id) {
        SwiggyServiceAddRequestDialogFragment f = new SwiggyServiceAddRequestDialogFragment ();
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_add_request, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvModelNumber = (TextView) root.findViewById (R.id.tvModelNumber);
        tvSerialNumber = (TextView) root.findViewById (R.id.tvSerialNumber);
        tvSelectProduct = (TextView) root.findViewById (R.id.tvSelectProduct);
        tvCounter = (TextView) root.findViewById (R.id.tvCounter);
        cv1 = (CardView) root.findViewById (R.id.cv1);
        etRequestDescription = (EditText) root.findViewById (R.id.etRequestDescription);
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvSerialNumber);
    
        productList.add ("Product 1");
        productList.add ("Product 2");
        productList.add ("Product 3");
        productList.add ("Product 4");
        productList.add ("Product 5");
        productList.add ("Product 6");
        productList.add ("Product 7");

    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    
    
        tvSelectProduct.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (getActivity ())
                        .title ("Select a Product")
                        .typeface (SetTypeFace.getTypeface (getActivity ()), SetTypeFace.getTypeface (getActivity ()))
                        .items (productList)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                cv1.setVisibility (View.VISIBLE);
                            }
                        })
                        .show ();
            }
        });
        etRequestDescription.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () < 256) {
                    tvCounter.setTextColor (getResources ().getColor (R.color.secondary_text2));
                } else {
                    tvCounter.setTextColor (getResources ().getColor (R.color.md_edittext_error));
                }
                tvCounter.setText (s.length () + "/255");
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    }
    
    
}