package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class SwiggyMyAccountItem {
    int id, icon;
    String title, image, description;
    
    public SwiggyMyAccountItem (int id, int icon, String title, String image, String description) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.image = image;
        this.description = description;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
    }
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
}