package com.indiasupply.isdental.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.model.CategoryFilter;
import com.indiasupply.isdental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SwiggyCategoryFilterAdapter extends RecyclerView.Adapter<SwiggyCategoryFilterAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<CategoryFilter> categoryFilters = new ArrayList<> ();
    
    public SwiggyCategoryFilterAdapter (Activity activity, List<CategoryFilter> categoryFilters) {
        this.activity = activity;
        this.categoryFilters = categoryFilters;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_swiggy_category_filter, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final CategoryFilter filter = categoryFilters.get (position);
        Utils.setTypefaceToAllViews (activity, holder.tvCategoryName);
        holder.tvCategoryName.setText (filter.getName ());
        holder.cbCategorySelect.setChecked (filter.is_selected ());
       /* holder.cbCategorySelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setIs_selected(isChecked);
            }
        });*/
     /*   if (filter.is_selected()){
            holder.cbCategorySelect.setChecked(true);
        }else {
            holder.cbCategorySelect.setChecked(false);
        }
*/
       /* holder.cbCategorySelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("check id",filter.getName());

            }
        });
*/
        
    }
    
    @Override
    public int getItemCount () {
        return categoryFilters.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCategoryName;
        CheckBox cbCategorySelect;
        
        
        public ViewHolder (View view) {
            super (view);
            tvCategoryName = (TextView) view.findViewById (R.id.tvCategoryName);
            cbCategorySelect = (CheckBox) view.findViewById (R.id.cbCategorySelect);
            
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            CategoryFilter categoryFilter = categoryFilters.get (getLayoutPosition ());
            if (cbCategorySelect.isChecked ()) {
                cbCategorySelect.setChecked (false);
            } else {
                cbCategorySelect.setChecked (true);
            }
        }
    }
}
