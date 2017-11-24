package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.SwiggyServiceRequestComments;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyServiceRequestCommentsAdapter extends RecyclerView.Adapter<SwiggyServiceRequestCommentsAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<SwiggyServiceRequestComments> commentsList = new ArrayList<> ();
    
    public SwiggyServiceRequestCommentsAdapter (Activity activity, List<SwiggyServiceRequestComments> commentsList) {
        this.activity = activity;
        this.commentsList = commentsList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_service_request_comment, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {
        final SwiggyServiceRequestComments comments = commentsList.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvCommentDate);
        holder.tvCommentDate.setText ("(" + Utils.convertTimeFormat (comments.getComment_created_at (), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm") + ") :");
        holder.tvCommentsFrom.setText (comments.getComment_from ());
        holder.tvCommentText.setText (comments.getComment_text ());
    }
    
    @Override
    public int getItemCount () {
        return commentsList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //    TextView tvProductName;
        TextView tvCommentsFrom;
        TextView tvCommentDate;
        TextView tvCommentText;
        
        
        public ViewHolder (View view) {
            super (view);
            //   tvProductName = (TextView) view.findViewById (R.id.tvProductName);
            
            tvCommentsFrom = (TextView) view.findViewById (R.id.tvCommentsFrom);
            tvCommentDate = (TextView) view.findViewById (R.id.tvCommentDate);
            tvCommentText = (TextView) view.findViewById (R.id.tvCommentText);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
        }
    }
}
