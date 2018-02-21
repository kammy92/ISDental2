package com.indiasupply.isdental.model;

/**
 * Created by Actiknow on 20-02-2018.
 */

public class MyAddress {
    int id;
    String address, city, state, pincode;
    
    public MyAddress (int id, String address, String city, String state, String pincode) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getAddress () {
        return address;
    }
    
    public void setAddress (String address) {
        this.address = address;
    }
    
    public String getCity () {
        return city;
    }
    
    public void setCity (String city) {
        this.city = city;
    }
    
    public String getState () {
        return state;
    }
    
    public void setState (String state) {
        this.state = state;
    }
    
    public String getPincode () {
        return pincode;
    }
    
    public void setPincode (String pincode) {
        this.pincode = pincode;
    }
}
