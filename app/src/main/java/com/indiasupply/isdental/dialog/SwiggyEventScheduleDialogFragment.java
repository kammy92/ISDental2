package com.indiasupply.isdental.dialog;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.SwiggyEventScheduleAdapter;
import com.indiasupply.isdental.model.SwiggyEventSchedule;
import com.indiasupply.isdental.model.SwiggyEventScheduleDate;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyEventScheduleDialogFragment extends DialogFragment {
    RecyclerView rvDayList;
    RecyclerView rvEventList;
    
    ImageView ivCancel;
    TextView tvTitle;
    
    
    List<SwiggyEventScheduleDate> swiggyEventScheduleDateList = new ArrayList<> ();
    List<SwiggyEventSchedule> swiggyEventSchedules = new ArrayList<> ();
    
    //    String contact_name;
//    int contact_id;
    String event_date = "";
    EventDayAdapter eventDayAdapter;
    SwiggyEventScheduleAdapter swiggyEventScheduleAdapter;
    
    
    LinearLayout llDateLayout;
    List<String> dateList = new ArrayList<String> ();
    
    public static SwiggyEventScheduleDialogFragment newInstance (int contact_id, String contact_name) {
        SwiggyEventScheduleDialogFragment f = new SwiggyEventScheduleDialogFragment ();
        // Supply num input as an argument.
        Bundle args = new Bundle ();
        args.putInt ("contact_id", contact_id);
        args.putString ("contact_name", contact_name);
        f.setArguments (args);
        return f;
    }
    
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
//        contact_id = getArguments().getInt("contact_id");
//        contact_name = getArguments().getString("contact_name");
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
        View root = inflater.inflate (R.layout.fragment_dialog_swiggy_event_schedule, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        return root;
    }
    
    private void initView (View root) {
        rvEventList = (RecyclerView) root.findViewById (R.id.rvEventList);
        rvDayList = (RecyclerView) root.findViewById (R.id.rvDayList);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
        
    }
    
    private void initBundle () {
    }
    
    private void initData () {
        swiggyEventScheduleDateList.add (new SwiggyEventScheduleDate (1, "05/10/2017"));
        swiggyEventScheduleDateList.add (new SwiggyEventScheduleDate (2, "06/10/2017"));
        swiggyEventScheduleDateList.add (new SwiggyEventScheduleDate (3, "07/10/2017"));
        swiggyEventScheduleDateList.add (new SwiggyEventScheduleDate (4, "08/10/2017"));
        swiggyEventScheduleDateList.add (new SwiggyEventScheduleDate (5, "09/10/2017"));
        
        
        eventDayAdapter = new EventDayAdapter (getActivity (), swiggyEventScheduleDateList);
        rvDayList.setAdapter (eventDayAdapter);
        rvDayList.setHasFixedSize (true);
        rvDayList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.HORIZONTAL, false));
        rvDayList.setItemAnimator (new DefaultItemAnimator ());
        
        
        swiggyEventSchedules.add (new SwiggyEventSchedule (1, "16:40", "17:40", "Event Name1", "Actiknow business"));
        swiggyEventSchedules.add (new SwiggyEventSchedule (2, "06:40", "07:40", "Event Name2", "Actiknow business"));
        swiggyEventSchedules.add (new SwiggyEventSchedule (3, "07:40", "08:40", "Event Name3", "Actiknow business"));
        swiggyEventSchedules.add (new SwiggyEventSchedule (4, "05:40", "07:40", "Event Name4", "Actiknow business"));
        swiggyEventSchedules.add (new SwiggyEventSchedule (5, "16:40", "17:40", "Event Name5", "Actiknow business"));
        
        
        swiggyEventScheduleAdapter = new SwiggyEventScheduleAdapter (getActivity (), swiggyEventSchedules);
        rvEventList.setAdapter (swiggyEventScheduleAdapter);
        rvEventList.setHasFixedSize (true);
        rvEventList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
        rvEventList.setItemAnimator (new DefaultItemAnimator ());
        
        
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getDialog ().dismiss ();
            }
        });
    }
    
    public class EventDayAdapter extends RecyclerView.Adapter<EventDayAdapter.ViewHolder> {
        private Activity activity;
        private List<SwiggyEventScheduleDate> dayList = new ArrayList<> ();
        
        public EventDayAdapter (Activity activity, List<SwiggyEventScheduleDate> dayList) {
            this.activity = activity;
            this.dayList = dayList;
        }
        
        @Override
        public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
            final View sView = mInflater.inflate (R.layout.list_item_swiggy_event_schedule_date, parent, false);
            return new ViewHolder (sView);
        }
        
        @Override
        public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
            final SwiggyEventScheduleDate eventDay = dayList.get (position);
            Utils.setTypefaceToAllViews (activity, holder.tvDate);
            holder.tvDate.setText (eventDay.getDate ());
            if (eventDay.isSelected ()) {
                holder.tvFooterLine.setBackgroundResource (R.color.primary_text2);
            } else {
                holder.tvFooterLine.setBackgroundResource (android.R.color.transparent);
            }

//            Glide.with(activity).load(eventDay.getLogo()).into(holder.ivDate);
        }
        
        @Override
        public int getItemCount () {
            return dayList.size ();
        }
        
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvDate;
            ImageView ivDate;
            TextView tvFooterLine;
            RelativeLayout rlItem;
            
            public ViewHolder (View view) {
                super (view);
                tvDate = (TextView) view.findViewById (R.id.tvDate);
                tvFooterLine = (TextView) view.findViewById (R.id.tvFooterLine);
                ivDate = (ImageView) view.findViewById (R.id.ivDate);
                rlItem = (RelativeLayout) view.findViewById (R.id.rlItem);
                view.setOnClickListener (this);
            }
            
            @Override
            public void onClick (View v) {
                SwiggyEventScheduleDate swiggyEventScheduleDate = dayList.get (getLayoutPosition ());
                event_date = swiggyEventScheduleDate.getDate ();
                for (int i = 0; i < swiggyEventScheduleDateList.size (); i++) {
                    SwiggyEventScheduleDate dayTemp = swiggyEventScheduleDateList.get (i);
                    dayTemp.setSelected (false);
                }
                swiggyEventScheduleDate.setSelected (true);
                eventDayAdapter.notifyDataSetChanged ();
            }
        }
    }
    
}