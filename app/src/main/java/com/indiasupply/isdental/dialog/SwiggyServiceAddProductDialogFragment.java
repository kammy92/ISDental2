package com.indiasupply.isdental.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.indiasupply.isdental.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SwiggyServiceAddProductDialogFragment extends DialogFragment {
    
    ImageView ivCancel;
    EditText etProductCategory;
    EditText etModelNo;
    EditText etSerialNo;
    EditText etPurchaseDate;
    TextView tvSubmit;
    
    List<String> categoryList = new ArrayList<> ();
    String date = "";
    private int mYear, mMonth, mDay;
    
    public static SwiggyServiceAddProductDialogFragment newInstance (int contact_id) {
        SwiggyServiceAddProductDialogFragment f = new SwiggyServiceAddProductDialogFragment ();
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_service_add_product, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        etProductCategory = (EditText) root.findViewById (R.id.etProductCategory);
        etSerialNo = (EditText) root.findViewById (R.id.etSerialNo);
        etPurchaseDate = (EditText) root.findViewById (R.id.etPurchaseDate);
        etModelNo = (EditText) root.findViewById (R.id.etModelNo);
        tvSubmit = (TextView) root.findViewById (R.id.tvSubmit);
        
        
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        categoryList.add ("Category 1");
        categoryList.add ("Category 2");
        categoryList.add ("Category 3");
        categoryList.add ("Category 4");
        categoryList.add ("Category 5");
        categoryList.add ("Category 6");
        
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
        
        
        etProductCategory.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                new MaterialDialog.Builder (getActivity ())
                        .title ("Product Category")
                        .items (categoryList)
                        .itemsCallback (new MaterialDialog.ListCallback () {
                            @Override
                            public void onSelection (MaterialDialog dialog, View view, int which, CharSequence text) {
                                etProductCategory.setText (text);
                            }
                        })
                        .show ();
            }
        });
        
        
        etPurchaseDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                
                final Calendar c = Calendar.getInstance ();
                mYear = c.get (Calendar.YEAR);
                mMonth = c.get (Calendar.MONTH);
                mDay = c.get (Calendar.DAY_OF_MONTH);
                
                DatePickerDialog datePickerDialog = new DatePickerDialog (getActivity (), new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet (DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etPurchaseDate.setText (String.format ("%02d", dayOfMonth) + "-" + String.format ("%02d", monthOfYear + 1) + "-" + year);
                        date = etPurchaseDate.getText ().toString ().trim ();
                        Log.e ("date", date);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show ();
            }
            
        });
        
        
    }
    
    
}