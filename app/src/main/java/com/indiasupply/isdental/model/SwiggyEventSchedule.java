package com.indiasupply.isdental.model;

/**
 * Created by l on 05/10/2017.
 */

public class SwiggyEventSchedule {
    int id;
    String start_time, end_time, description, location;
    
    
    public SwiggyEventSchedule (int id, String start_time, String end_time, String description, String location) {
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.location = location;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getStart_time () {
        return start_time;
    }
    
    public void setStart_time (String start_time) {
        this.start_time = start_time;
    }
    
    public String getEnd_time () {
        return end_time;
    }
    
    public void setEnd_time (String end_time) {
        this.end_time = end_time;
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
