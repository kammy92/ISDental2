package com.indiasupply.isdental.model;


public class SwiggyEventScheduleDate {
    boolean selected;
    int id;
    String logo, date;
    
    public SwiggyEventScheduleDate (int id, String logo, String date) {
        this.id = id;
        this.logo = logo;
        this.date = date;
        
    }
    
    public SwiggyEventScheduleDate (int id, String date) {
        
        this.id = id;
        this.date = date;
    }
    
    public SwiggyEventScheduleDate () {
    }
    
    public String getDate () {
        return date;
    }
    
    public void setDate (String date) {
        this.date = date;
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
    
    
}