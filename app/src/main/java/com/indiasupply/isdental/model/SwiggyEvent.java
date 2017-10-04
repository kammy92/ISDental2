package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyEvent {
    int id;
    String image_url, name, date, location;
    
    public SwiggyEvent (int id, String image_url, String name, String date, String location) {
        this.id = id;
        this.image_url = image_url;
        this.name = name;
        this.date = date;
        this.location = location;
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
    
    public String getDate () {
        return date;
    }
    
    public void setDate (String date) {
        this.date = date;
    }
    
    public String getLocation () {
        return location;
    }
    
    public void setLocation (String location) {
        this.location = location;
    }
    
    public String getImage_url () {
        return image_url;
    }
    
    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }
}
