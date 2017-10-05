package com.indiasupply.isdental.activity;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyEventItemAdapter;
import com.indiasupply.isdental.fragment.SwiggyEventExhibitorDialogFragment;
import com.indiasupply.isdental.fragment.SwiggyEventFloorPlanDialogFragment;
import com.indiasupply.isdental.fragment.SwiggyEventInformationDialogFragment;
import com.indiasupply.isdental.fragment.SwiggyEventRegistrationsDialogFragment;
import com.indiasupply.isdental.fragment.SwiggyEventSpeakerDialogFragment;
import com.indiasupply.isdental.model.SwiggyEventItem;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sud on 3/10/17.
 */

public class SwiggyEventDetailActivity extends AppCompatActivity {
    RelativeLayout rlBack;
    RecyclerView rvEventItems;
    
    List<SwiggyEventItem> swiggyEventItemList = new ArrayList<> ();
    SwiggyEventItemAdapter swiggyEventItemAdapter;
    
    TextView tvTitleEventName;
    TextView tvTitleEventDetail;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swiggy_event_detail);
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
        swiggyEventItemAdapter.SetOnItemClickListener (new SwiggyEventItemAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                SwiggyEventItem eventItem = swiggyEventItemList.get (position);
                FragmentTransaction ft = getFragmentManager ().beginTransaction ();
                switch (eventItem.getId ()) {
                    case 1:
                        Utils.showToast (SwiggyEventDetailActivity.this, "ID 1", false);
                        break;
                    case 2:
                        SwiggyEventSpeakerDialogFragment frag2 = new SwiggyEventSpeakerDialogFragment ();
                        frag2.show (ft, "2");
                        break;
                    case 3:
                        SwiggyEventExhibitorDialogFragment frag3 = new SwiggyEventExhibitorDialogFragment ();
                        frag3.show (ft, "2");
                        break;
                    case 4:
                        SwiggyEventFloorPlanDialogFragment frag4 = new SwiggyEventFloorPlanDialogFragment ();
                        frag4.show (ft, "2");
                        break;
                    case 5:
                        SwiggyEventInformationDialogFragment frag5 = new SwiggyEventInformationDialogFragment ();
                        frag5.show (ft, "2");
                        break;
                    case 6:
                        SwiggyEventRegistrationsDialogFragment frag6 = new SwiggyEventRegistrationsDialogFragment ();
                        frag6.show (ft, "2");
                        break;
                }
            }
        });
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, tvTitleEventDetail);
        Window window = getWindow ();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor (ContextCompat.getColor (this, R.color.text_color_white));
        }
        
        swiggyEventItemList.add (new SwiggyEventItem (1, R.drawable.ic_information, "EVENT SCHEDULE"));
        swiggyEventItemList.add (new SwiggyEventItem (2, R.drawable.ic_information, "SPEAKERS"));
        swiggyEventItemList.add (new SwiggyEventItem (3, R.drawable.ic_information, "EXHIBITORS"));
        swiggyEventItemList.add (new SwiggyEventItem (4, R.drawable.ic_information, "FLOOR PLAN"));
        swiggyEventItemList.add (new SwiggyEventItem (5, R.drawable.ic_information, "GENERAL INFORMATION"));
        swiggyEventItemList.add (new SwiggyEventItem (6, R.drawable.ic_information, "REGISTRATIONS"));
    
        swiggyEventItemAdapter = new SwiggyEventItemAdapter (SwiggyEventDetailActivity.this, swiggyEventItemList);
        rvEventItems.setAdapter (swiggyEventItemAdapter);
        rvEventItems.setHasFixedSize (true);
        GridLayoutManager lm = new GridLayoutManager (SwiggyEventDetailActivity.this, 2, GridLayoutManager.VERTICAL, false);
//        lm.setSpanSizeLookup (new MySizeLookup ());
        rvEventItems.setLayoutManager (lm);
        rvEventItems.setItemAnimator (new DefaultItemAnimator ());
        rvEventItems.addItemDecoration (new RecyclerViewMargin (
                (int) Utils.pxFromDp (this, 16),
                (int) Utils.pxFromDp (this, 16),
                (int) Utils.pxFromDp (this, 16),
                (int) Utils.pxFromDp (this, 16),
                2, 0, RecyclerViewMargin.LAYOUT_MANAGER_GRID, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rvEventItems = (RecyclerView) findViewById (R.id.rvEventItems);
        tvTitleEventName = (TextView) findViewById (R.id.tvTitleEventName);
        tvTitleEventDetail = (TextView) findViewById (R.id.tvTitleEventDetail);
    }
    
    @Override
    public void onBackPressed () {
        finish ();
        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    public class MySizeLookup extends GridLayoutManager.SpanSizeLookup {
        public int getSpanSize (int position) {
            int span;
            span = swiggyEventItemList.size () % 2;
            if (swiggyEventItemList.size () < 2) {
                return 1;
            } else if (span == 0 || (position <= ((swiggyEventItemList.size () - 1) - span))) {
                return 1;
            } else if (span == 1) {
                return 2;
            } else {
                return 1;
            }
        }
    }
}