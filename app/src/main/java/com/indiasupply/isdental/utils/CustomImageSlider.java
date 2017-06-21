package com.indiasupply.isdental.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.indiasupply.isdental.R;


public class CustomImageSlider extends BaseSliderView {
    public CustomImageSlider (Context context) {
        super (context);
    }
    
    @Override
    public View getView () {
        View v = LayoutInflater.from (getContext ()).inflate (R.layout.slider_banner, null);
        ImageView target = (ImageView) v.findViewById (R.id.slider_image);
        bindEventAndShow (v, target);
        return v;
    }
}