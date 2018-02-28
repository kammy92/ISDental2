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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.adapter.EventScheduleAdapter;
import com.indiasupply.isdental.model.EventSchedule;
import com.indiasupply.isdental.model.EventScheduleDate;
import com.indiasupply.isdental.utils.AppConfigTags;
import com.indiasupply.isdental.utils.RecyclerViewMargin;
import com.indiasupply.isdental.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventScheduleDialogFragment extends DialogFragment {
    RecyclerView rvDayList;
    RecyclerView rvEventList;
    
    ImageView ivCancel;
    ImageView ivDate;
    TextView tvTitle;
    
    List<EventScheduleDate> eventScheduleDateList = new ArrayList<> ();
    List<EventSchedule> eventScheduleList = new ArrayList<> ();
    List<EventSchedule> eventScheduleTempList = new ArrayList<> ();
    
    String event_date = "";
    EventDayAdapter eventDayAdapter;
    EventScheduleAdapter eventScheduleAdapter;
    
    RelativeLayout rlDate;
    
    String eventSchedule;
    
    public static EventScheduleDialogFragment newInstance (String eventSchedule) {
        EventScheduleDialogFragment f = new EventScheduleDialogFragment ();
        Bundle args = new Bundle ();
        args.putString (AppConfigTags.EVENT_SCHEDULE, eventSchedule);
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
        View root = inflater.inflate (R.layout.fragment_dialog_event_schedule, container, false);
        initView (root);
        initBundle ();
        initData ();
        initListener ();
        setData ();
        return root;
    }
    
    private void initView (View root) {
        rvEventList = (RecyclerView) root.findViewById (R.id.rvEventList);
        rvDayList = (RecyclerView) root.findViewById (R.id.rvDayList);
        ivCancel = (ImageView) root.findViewById (R.id.ivCancel);
        ivDate = (ImageView) root.findViewById (R.id.ivDate);
        rlDate = (RelativeLayout) root.findViewById (R.id.rlDate);
        tvTitle = (TextView) root.findViewById (R.id.tvTitle);
    }
    
    private void initBundle () {
        Bundle bundle = this.getArguments ();
        eventSchedule = bundle.getString (AppConfigTags.EVENT_SCHEDULE);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (getActivity (), tvTitle);
    
        eventDayAdapter = new EventDayAdapter (getActivity (), eventScheduleDateList);
        rvDayList.setAdapter (eventDayAdapter);
        rvDayList.setHasFixedSize (true);
        rvDayList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.HORIZONTAL, false));
        rvDayList.setItemAnimator (new DefaultItemAnimator ());
        rvDayList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 0, 1, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_HORIZONTAL));
    
        rvEventList.addItemDecoration (new RecyclerViewMargin ((int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), (int) Utils.pxFromDp (getActivity (), 16), 1, 0, RecyclerViewMargin.LAYOUT_MANAGER_LINEAR, RecyclerViewMargin.ORIENTATION_VERTICAL));
    }
    
    private void initListener () {
        ivCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
//                if (rlDate.getVisibility () == View.VISIBLE) {
//                    rlDate.setVisibility (View.GONE);
//                } else {
                getDialog ().dismiss ();
//                }
            }
        });
        ivDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlDate.getVisibility () == View.VISIBLE) {
                    rlDate.setVisibility (View.GONE);
                } else {
                    rlDate.setVisibility (View.VISIBLE);
                }
            }
        });
    }
    
    private void setData () {
//        eventScheduleDateList.add (new EventScheduleDate (true, 1, R.drawable.ic_date, "2017-10-05", "05/10/2017", ""));
//        eventScheduleDateList.add (new EventScheduleDate (false, 2, R.drawable.ic_date, "2017-10-06", "06/10/2017", ""));
//        eventScheduleDateList.add (new EventScheduleDate (false, 3, R.drawable.ic_date, "2017-10-07", "07/10/2017", ""));
//        eventScheduleDateList.add (new EventScheduleDate (false, 4, R.drawable.ic_date, "2017-10-08", "08/10/2017", ""));
//        eventScheduleDateList.add (new EventScheduleDate (false, 5, R.drawable.ic_date, "2017-10-09", "09/10/2017", ""));
//        eventDayAdapter.notifyDataSetChanged ();
//
//        eventScheduleList.add (new EventSchedule (1, R.drawable.ic_date, "2017-10-05", "16:40", "17:40", "Event Name1", "New Delhi", ""));
//        eventScheduleList.add (new EventSchedule (2, R.drawable.ic_date, "2017-10-05", "06:40", "07:40", "Event Name2", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (3, R.drawable.ic_date, "2017-10-05", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (4, R.drawable.ic_date, "2017-10-06", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (5, R.drawable.ic_date, "2017-10-06", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (6, R.drawable.ic_date, "2017-10-07", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (7, R.drawable.ic_date, "2017-10-07", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (8, R.drawable.ic_date, "2017-10-07", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (9, R.drawable.ic_date, "2017-10-08", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (10, R.drawable.ic_date, "2017-10-09", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//        eventScheduleList.add (new EventSchedule (11, R.drawable.ic_date, "2017-10-09", "07:40", "08:40", "Event Name3", "Mumbai", ""));
//
    
        try {
            JSONObject jsonObj = new JSONObject (eventSchedule);
            JSONArray jsonArrayDays = jsonObj.getJSONArray ("dates");
            for (int i = 0; i < jsonArrayDays.length (); i++) {
                JSONObject jsonObjectDays = jsonArrayDays.getJSONObject (i);
                if (i == 0) {
                    eventScheduleDateList.add (new EventScheduleDate (true,
                            jsonObjectDays.getInt ("date_id"),
                            R.drawable.ic_date,
                            jsonObjectDays.getString ("date_date"),
                            jsonObjectDays.getString ("date_title"),
                            jsonObjectDays.getString ("date_image")
                    ));
                } else {
                    eventScheduleDateList.add (new EventScheduleDate (false,
                            jsonObjectDays.getInt ("date_id"),
                            R.drawable.ic_date,
                            jsonObjectDays.getString ("date_date"),
                            jsonObjectDays.getString ("date_title"),
                            jsonObjectDays.getString ("date_image")
                    ));
                }
            }
            eventDayAdapter.notifyDataSetChanged ();
            JSONArray jsonArraySchedules = jsonObj.getJSONArray ("schedules");
            for (int j = 0; j < jsonArraySchedules.length (); j++) {
                JSONObject jsonObjectSchedules = jsonArraySchedules.getJSONObject (j);
                eventScheduleList.add (new EventSchedule (
                        jsonObjectSchedules.getInt ("schedule_id"),
                        R.drawable.ic_date,
                        jsonObjectSchedules.getInt ("schedule_date_id"),
                        jsonObjectSchedules.getString ("schedule_date"),
                        jsonObjectSchedules.getString ("schedule_start_time"),
                        jsonObjectSchedules.getString ("schedule_end_time"),
                        jsonObjectSchedules.getString ("schedule_description"),
                        jsonObjectSchedules.getString ("schedule_location"),
                        jsonObjectSchedules.getString ("schedule_image")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        
    }
    
    public class EventDayAdapter extends RecyclerView.Adapter<EventDayAdapter.ViewHolder> {
        private Activity activity;
        private List<EventScheduleDate> dayList = new ArrayList<> ();
    
        public EventDayAdapter (Activity activity, List<EventScheduleDate> dayList) {
            this.activity = activity;
            this.dayList = dayList;
        }
        
        @Override
        public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
            final View sView = mInflater.inflate (R.layout.list_item_event_schedule_date, parent, false);
            return new ViewHolder (sView);
        }
        
        @Override
        public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
            final EventScheduleDate eventDay = dayList.get (position);
            Utils.setTypefaceToAllViews (activity, holder.tvDate);
//            holder.tvDate.setText (Utils.convertTimeFormat (eventDay.getDate (), "yyyy-MM-dd", "dd/MM/yyyy"));
            holder.tvDate.setText (eventDay.getTitle ().replace ("\\n", "\n"));
    
            if (eventDay.isSelected ()) {
                eventScheduleTempList.clear ();
            }
    
            for (int i = 0; i < eventScheduleList.size (); i++) {
                EventSchedule eventSchedule = EventScheduleDialogFragment.this.eventScheduleList.get (i);
                if (eventDay.getId () == eventSchedule.getDay_id () && eventDay.isSelected ()) {
                    eventScheduleTempList.add (eventSchedule);
                }
            }
            
            if (eventDay.isSelected ()) {
                holder.tvFooterLine.setBackgroundResource (R.color.primary_text2);
                eventScheduleAdapter = new EventScheduleAdapter (getActivity (), eventScheduleTempList);
                rvEventList.setAdapter (eventScheduleAdapter);
                rvEventList.setHasFixedSize (true);
                rvEventList.setLayoutManager (new LinearLayoutManager (getActivity (), LinearLayoutManager.VERTICAL, false));
                rvEventList.setItemAnimator (new DefaultItemAnimator ());
    
            } else {
                holder.tvFooterLine.setBackgroundResource (android.R.color.transparent);
            }
    
            if (eventDay.getLogo ().length () == 0) {
                holder.ivDate.setImageResource (eventDay.getIcon ());
                holder.progressBar.setVisibility (View.GONE);
            } else {
//                holder.progressBar.setVisibility (View.VISIBLE);
                Glide.with (activity)
                        .load (eventDay.getLogo ())
                        .placeholder (eventDay.getIcon ())
                        .listener (new RequestListener<String, GlideDrawable> () {
                            @Override
                            public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                holder.progressBar.setVisibility (View.GONE);
                                return false;
                            }
                    
                            @Override
                            public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                holder.progressBar.setVisibility (View.GONE);
                                return false;
                            }
                        })
                        .error (eventDay.getIcon ())
                        .into (holder.ivDate);
            }
            if (dayList.size () == 1) {
                rlDate.setVisibility (View.GONE);
            }
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
            ProgressBar progressBar;
            
            public ViewHolder (View view) {
                super (view);
                tvDate = (TextView) view.findViewById (R.id.tvDate);
                tvFooterLine = (TextView) view.findViewById (R.id.tvFooterLine);
                ivDate = (ImageView) view.findViewById (R.id.ivDate);
                rlItem = (RelativeLayout) view.findViewById (R.id.rlItem);
                progressBar = (ProgressBar) view.findViewById (R.id.progressBar);
                view.setOnClickListener (this);
            }
            
            @Override
            public void onClick (View v) {
                EventScheduleDate eventScheduleDate = dayList.get (getLayoutPosition ());
                event_date = eventScheduleDate.getDate ();
                for (int i = 0; i < eventScheduleDateList.size (); i++) {
                    EventScheduleDate dayTemp = eventScheduleDateList.get (i);
                    dayTemp.setSelected (false);
                }
                eventScheduleDate.setSelected (true);
                eventDayAdapter.notifyDataSetChanged ();
            }
        }
    }
}