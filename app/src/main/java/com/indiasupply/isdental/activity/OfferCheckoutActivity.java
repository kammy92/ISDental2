package com.indiasupply.isdental.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.MyAddressAdapter;
import com.indiasupply.isdental.dialog.AddNewAddressDialogFragment;
import com.indiasupply.isdental.model.MyAddress;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;

public class OfferCheckoutActivity extends AppCompatActivity {
    RecyclerView rvDeliveryAddress;
    MyAddressAdapter addressAdapter;
    TextView tvAddNewAddress;
    ArrayList<MyAddress> addressList = new ArrayList<> ();
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_offer_checkout);
        initView ();
        initData ();
        initListener ();
        
    }
    
    private void initListener () {
        tvAddNewAddress.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                android.app.FragmentManager fm = OfferCheckoutActivity.this.getFragmentManager ();
                android.app.FragmentTransaction ft = fm.beginTransaction ();
                AddNewAddressDialogFragment dialog = new AddNewAddressDialogFragment ().newInstance ();
                dialog.show (ft, "MyDialog");
            }
        });
    }
    
    private void initData () {
        addressList.add (new MyAddress (1, "Karman", "G-87 near Sanathan Dharm mandir school road New Delhi G-87 near Sanathan Dharm mandir school road New Delhi"));
        addressList.add (new MyAddress (2, "Rahul", "G-87 near Sanathan Dharm mandir school road New Delhi"));
        addressList.add (new MyAddress (3, "Sudhanshu", "G-87 near Sanathan Dharm mandir school road New Delhi"));
        Log.e ("AddressList", "" + addressList);
        addressAdapter = new MyAddressAdapter (OfferCheckoutActivity.this, addressList);
        rvDeliveryAddress.setAdapter (addressAdapter);
        rvDeliveryAddress.setLayoutManager (new LinearLayoutManager (OfferCheckoutActivity.this, LinearLayoutManager.VERTICAL, false));
        rvDeliveryAddress.setHasFixedSize (true);
        rvDeliveryAddress.setItemAnimator (new DefaultItemAnimator ());
        rvDeliveryAddress.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (OfferCheckoutActivity.this, 16), (int) Utils.pxFromDp (OfferCheckoutActivity.this, 16), (int) Utils.pxFromDp (OfferCheckoutActivity.this, 16), (int) Utils.pxFromDp (OfferCheckoutActivity.this, 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
        
    }
    
    public void initView () {
        rvDeliveryAddress = (RecyclerView) findViewById (R.id.rvDeliveryAddress);
        tvAddNewAddress = (TextView) findViewById (R.id.tvAddNewAddress);
    }
    
}
