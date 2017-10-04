package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class EventTab {
    int id;
    String date, description, location;
    
    public EventTab (int id, String date, String description, String location) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.location = location;
    }
    
    public EventTab (int id, String date) {
        this.id = id;
        this.date = date;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getDate () {
        return date;
    }
    
    public void setDate (String date) {
        this.date = date;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getLocation () {
        return location;
    }
    
    public void setLocation (String location) {
        this.location = location;
    }
}
