package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.CompanyContact;
import com.indiasupply.isdental.utils.SetTypeFace;
import com.indiasupply.isdental.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CompanyContactAdapter extends RecyclerView.Adapter<CompanyContactAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<CompanyContact> brandsContactDetails = new ArrayList<> ();
    
    public CompanyContactAdapter (Activity activity, List<CompanyContact> brandsContactDetails) {
        this.activity = activity;
        this.brandsContactDetails = brandsContactDetails;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_company_contact, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final CompanyContact contactDetails = brandsContactDetails.get (position);

//        holder.tvContactPerson.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvEmail.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvWebsite.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvFullAddress.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvPhone1.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvPhone2.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvTime.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvTitle.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvType.setTypeface (SetTypeFace.getTypeface (activity));
//        holder.tvDesignation.setTypeface (SetTypeFace.getTypeface (activity));
    
        Utils.setTypefaceToAllViews (activity, holder.tvTime);
    
        if (contactDetails.getType ().length () > 0) {
            holder.tvType.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getTitle ().length () > 0) {
            holder.rlTitle.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getAttendant ().length () > 0) {
            holder.rlContactPerson.setVisibility (View.VISIBLE);
        }
//        if (contactDetails.getDesignation ().length () > 0) {
//            holder.rlDesignation.setVisibility (View.VISIBLE);
//        }
        if ((contactDetails.getPhone1 ().length () > 0) || (contactDetails.getPhone2 ().length () > 0)) {
            holder.rlPhone.setVisibility (View.VISIBLE);
            if (contactDetails.getPhone1 ().length () > 0) {
                holder.rlPhone1.setVisibility (View.VISIBLE);
                
            }
            if (contactDetails.getPhone2 ().length () > 0) {
                holder.rlPhone2.setVisibility (View.VISIBLE);
            }
        }
        if (contactDetails.getEmail ().length () > 0) {
            holder.rlEmail.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getWebsite ().length () > 0) {
            holder.rlWebsite.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getOpen_time ().length () > 0 && contactDetails.getClose_time ().length () > 0 && contactDetails.getHolidays ().length () > 0) {
            holder.rlTime.setVisibility (View.VISIBLE);
        }
        if (contactDetails.getAddress ().length () > 0) {
            holder.rlAddress.setVisibility (View.VISIBLE);
        }
    
        if (contactDetails.getDesignation ().length () > 0) {
            holder.tvContactPerson.setText (contactDetails.getAttendant () + " (" + contactDetails.getDesignation () + ")");
        } else {
            holder.tvContactPerson.setText (contactDetails.getAttendant ());
        }
    
    
        holder.tvType.setText (contactDetails.getType ());
        holder.tvTitle.setText (contactDetails.getTitle ());
        //holder.tvDesignation.setText (contactDetails.getDesignation ());
        holder.tvPhone1.setText (contactDetails.getPhone1 ());//Html.fromHtml ("<u><font color='#01579b'>" + contactDetails.getPhone1 () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvPhone2.setText (contactDetails.getPhone2 ());//Html.fromHtml ("<u><font color='#01579b'>" + contactDetails.getPhone2 () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvEmail.setText (contactDetails.getEmail ());//Html.fromHtml ("<u><font color='#01579b'>" + contactDetails.getEmail () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvWebsite.setText (contactDetails.getWebsite ());//Html.fromHtml ("<u><font color='#01579b'>" + contactDetails.getWebsite () + "</font></u>"), TextView.BufferType.SPANNABLE);
        holder.tvFullAddress.setText (contactDetails.getAddress ());
    
        int open_status = 0;
    
        try {
            Calendar c = Calendar.getInstance ();
            String string1 = contactDetails.getOpen_time ();
            Date time1 = new SimpleDateFormat ("HH:mm:ss").parse (string1);
            Calendar calendar1 = Calendar.getInstance ();
            calendar1.setTime (time1);
            
            String string2 = contactDetails.getClose_time ();
            Date time2 = new SimpleDateFormat ("HH:mm:ss").parse (string2);
            Calendar calendar2 = Calendar.getInstance ();
            calendar2.setTime (time2);
            calendar2.add (Calendar.DATE, 1);
    
    
            String someRandomTime = new SimpleDateFormat ("HH:mm:ss").format (c.getTime ());
            Date d = new SimpleDateFormat ("HH:mm:ss").parse (someRandomTime);
            Calendar calendar3 = Calendar.getInstance ();
            calendar3.setTime (d);
            calendar3.add (Calendar.DATE, 1);
            
            Date currentTime = calendar3.getTime ();
            Date endTime = calendar2.getTime ();
            
            String day[] = contactDetails.getHolidays ().trim ().split (",");
            SimpleDateFormat dayFormat = new SimpleDateFormat ("EEE", Locale.US);
            Calendar calendar = Calendar.getInstance ();
    
    
            if (currentTime.after (calendar1.getTime ()) && currentTime.before (calendar2.getTime ())) {
                open_status = 2;
                holder.tvTime.setText ("Open till " + Utils.convertTimeFormat (contactDetails.getClose_time (), "HH:mm:ss", "HH:mm"));
            } else {
                open_status = 3;
                holder.tvTime.setText ("Closed Now");
            }
    
    
            if (contactDetails.getOpen_time ().equalsIgnoreCase ("00:00:00") || contactDetails.getClose_time ().equalsIgnoreCase ("00:00:00")) {
                holder.tvTime.setVisibility (View.GONE);
                open_status = 1;
            }
    
    
            for (int i = 0; i < day.length; i++) {
                if (dayFormat.format (calendar.getTime ()).equalsIgnoreCase (day[i])) {
                    holder.tvTime.setText ("Closed Today");
                    open_status = 3;
                }
            }
            /*
                if (dayFormat.format (calendar.getTime ()).equalsIgnoreCase (day[i])) {
                    holder.tvTime.setText ("today is holiday");
                } else if (currentTime.after (calendar1.getTime ()) && currentTime.before (calendar2.getTime ())) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    
                    long diff = endTime.getTime () - currentTime.getTime ();
                    diff = diff / (60 * 1000);
                    float diffHours = diff / 60;
                    float diffMin = diff % 60;
    
                    holder.tvTime.setText ("Time in the range" + "Left time is " + " " + diffHours + ":" + diffMin);
                    
                } else {
                    holder.tvTime.setText ("closed");
                }*/
        } catch (java.text.ParseException e) {
            e.printStackTrace ();
        }
    
    
        final int finalOpen_status = open_status;
        holder.rlPhone1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (finalOpen_status != 3) {
                    Utils.showLog (Log.ERROR, "karman", "in if flag false", true);
                    Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactDetails.getPhone1 ()));
                    sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity (sIntent);
                } else {
                    Utils.showLog (Log.ERROR, "karman", "in if flag true", true);
                    MaterialDialog dialog = new MaterialDialog.Builder (activity)
                            .content ("The Office may be closed now, Do you still want to continue?")
                            .positiveText ("YES")
                            .negativeText ("NO")
                            .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                            .canceledOnTouchOutside (false)
                            .cancelable (false)
                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                @Override
                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactDetails.getPhone1 ()));
                                    sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity (sIntent);
                                }
                            })
                            .build ();
                    dialog.show ();
                }
            }
        });
    
        holder.rlPhone2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (finalOpen_status != 3) {
                    Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactDetails.getPhone2 ()));
                    sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity (sIntent);
                } else {
                    Utils.showLog (Log.ERROR, "karman", "in if flag true", true);
                    MaterialDialog dialog = new MaterialDialog.Builder (activity)
                            .content ("The Office may be closed now, Do you still want to continue?")
                            .positiveText ("YES")
                            .negativeText ("NO")
                            .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                            .canceledOnTouchOutside (false)
                            .cancelable (false)
                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                @Override
                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent sIntent = new Intent (Intent.ACTION_DIAL, Uri.parse ("tel:" + contactDetails.getPhone2 ()));
                                    sIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity (sIntent);
                                }
                            })
                            .build ();
                    dialog.show ();
                }
            }
        });
    
        holder.rlEmail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent email = new Intent (Intent.ACTION_SEND);
                email.putExtra (Intent.EXTRA_EMAIL, new String[] {contactDetails.getEmail ()});
                email.putExtra (Intent.EXTRA_SUBJECT, "Enquiry");
                email.putExtra (Intent.EXTRA_TEXT, "");
                email.setType ("message/rfc822");
                activity.startActivity (Intent.createChooser (email, "Choose an Email client :"));
            }
        });
    
        holder.rlWebsite.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Uri uri;
                if (contactDetails.getWebsite ().contains ("http://") || contactDetails.getWebsite ().contains ("https://")) {
                    uri = Uri.parse (contactDetails.getWebsite ());
                } else {
                    uri = Uri.parse ("http://" + contactDetails.getWebsite ());
                }
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                activity.startActivity (intent);
            }
        });
    }
    
    
    @Override
    public int getItemCount () {
        return brandsContactDetails.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvType;
        TextView tvTitle;
        TextView tvContactPerson;
        TextView tvDesignation;
        TextView tvPhone1;
        TextView tvPhone2;
        LinearLayout llPhone;
        TextView tvTime;
        TextView tvEmail;
        TextView tvWebsite;
        TextView tvFullAddress;
    
        RelativeLayout rlTitle;
        RelativeLayout rlContactPerson;
        RelativeLayout rlDesignation;
        RelativeLayout rlPhone;
        RelativeLayout rlPhone1;
        RelativeLayout rlPhone2;
        RelativeLayout rlTime;
        RelativeLayout rlEmail;
        RelativeLayout rlWebsite;
        RelativeLayout rlAddress;
        
        public ViewHolder (View view) {
            super (view);
            tvType = (TextView) view.findViewById (R.id.tvType);
            tvTitle = (TextView) view.findViewById (R.id.tvTitle);
            tvContactPerson = (TextView) view.findViewById (R.id.tvContactPerson);
            tvDesignation = (TextView) view.findViewById (R.id.tvDesignation);
            tvPhone1 = (TextView) view.findViewById (R.id.tvPhone1);
            tvPhone2 = (TextView) view.findViewById (R.id.tvPhone2);
            tvTime = (TextView) view.findViewById (R.id.tvTime);
            tvEmail = (TextView) view.findViewById (R.id.tvEmail);
            tvWebsite = (TextView) view.findViewById (R.id.tvWebsite);
            tvFullAddress = (TextView) view.findViewById (R.id.tvFullAddress);
            llPhone = (LinearLayout) view.findViewById (R.id.llPhone);
    
            rlTitle = (RelativeLayout) view.findViewById (R.id.rlTitle);
            rlContactPerson = (RelativeLayout) view.findViewById (R.id.rlContactPerson);
            rlDesignation = (RelativeLayout) view.findViewById (R.id.rlDesignation);
            rlPhone = (RelativeLayout) view.findViewById (R.id.rlPhone);
            rlPhone1 = (RelativeLayout) view.findViewById (R.id.rlPhone1);
            rlPhone2 = (RelativeLayout) view.findViewById (R.id.rlPhone2);
            rlTime = (RelativeLayout) view.findViewById (R.id.rlTime);
            rlEmail = (RelativeLayout) view.findViewById (R.id.rlEmail);
            rlWebsite = (RelativeLayout) view.findViewById (R.id.rlWebsite);
            rlAddress = (RelativeLayout) view.findViewById (R.id.rlAddress);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
