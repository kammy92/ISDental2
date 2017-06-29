package com.indiasupply.isdental.model;


public class Category {
    boolean selected;
    int id;
    String logo, name;
    
    public Category (int id, String logo, String name) {
        this.id = id;
        this.logo = logo;
        this.name = name;
        
    }
    
    public Category () {
    }
    
    public boolean isSelected () {
        return selected;
    }
    
    public void setSelected (boolean selected) {
        this.selected = selected;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getLogo () {
        return logo;
    }
    
    public void setLogo (String logo) {
        this.logo = logo;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
}