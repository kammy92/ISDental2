package com.indiasupply.isdental.model;

public class MyAccountOrder {
    int id;
    String offer_name, place_date, status;
    
    public MyAccountOrder (int id, String offer_name, String place_date, String status) {
        this.id = id;
        this.offer_name = offer_name;
        this.place_date = place_date;
        this.status = status;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getOffer_name () {
        return offer_name;
    }
    
    public void setOffer_name (String offer_name) {
        this.offer_name = offer_name;
    }
    
    public String getPlace_date () {
        return place_date;
    }
    
    public void setPlace_date (String place_date) {
        this.place_date = place_date;
    }
    
    public String getStatus () {
        return status;
    }
    
    public void setStatus (String status) {
        this.status = status;
    }
}
