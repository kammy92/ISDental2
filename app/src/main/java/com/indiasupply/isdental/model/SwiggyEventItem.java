package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyEventItem {
    boolean enabled;
    int id, icon;
    String name, image;
    
    public SwiggyEventItem (boolean enabled, int id, int icon, String name, String image) {
        this.enabled = enabled;
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.image = image;
    }
    
    public boolean isEnabled () {
        return enabled;
    }
    
    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }
    
    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
}
