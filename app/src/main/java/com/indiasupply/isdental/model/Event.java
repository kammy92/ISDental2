package com.indiasupply.isdental.model;


public class Event {
    int id;
    String name, start_date, end_date, type, city, organisers;
    
    public Event (int id, String name, String start_date, String end_date, String type, String city, String organisers) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.type = type;
        this.city = city;
        this.organisers = organisers;
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
    
    public String getStart_date () {
        return start_date;
    }
    
    public void setStart_date (String start_date) {
        this.start_date = start_date;
    }
    
    public String getEnd_date () {
        return end_date;
    }
    
    public void setEnd_date (String end_date) {
        this.end_date = end_date;
    }
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    public String getCity () {
        return city;
    }
    
    public void setCity (String city) {
        this.city = city;
    }
    
    public String getOrganisers () {
        return organisers;
    }
    
    public void setOrganisers (String organisers) {
        this.organisers = organisers;
    }
}