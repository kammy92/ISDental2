package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyBanner {
    int id;
    String banner_image, banner_title;
    
    public SwiggyBanner (int id, String banner_image, String banner_title) {
        this.id = id;
        this.banner_image = banner_image;
        this.banner_title = banner_title;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getBanner_image () {
        return banner_image;
    }
    
    public void setBanner_image (String banner_image) {
        this.banner_image = banner_image;
    }
    
    public String getBanner_title () {
        return banner_title;
    }
    
    public void setBanner_title (String banner_title) {
        this.banner_title = banner_title;
    }
}
