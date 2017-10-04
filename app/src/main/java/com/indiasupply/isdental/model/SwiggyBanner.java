package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyBanner {
    int id;
    String image_url, title;
    
    public SwiggyBanner (int id, String image_url, String title) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getImage_url () {
        return image_url;
    }
    
    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
}
