package com.indiasupply.isdental.model;

public class MyOffers {
    int id, icon;
    String name, image;
    
    public MyOffers (int id, int icon, String name, String image) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.image = image;
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
    
    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
}
