package com.indiasupply.isdental.model;

import java.util.ArrayList;

/**
 * Created by Admin on 16-05-2017.
 */

public class CompanyContact {
    int id;
    String attendant, designation, type, address, email, website, phone1, phone2, title, open_time, close_time, holidays;
    ArrayList<String> contactList = new ArrayList<> ();
    
    
    public CompanyContact (int id, String attendant, String designation, String type, String address, String email, String website, String phone1, String phone2, String title, String open_time, String close_time, String holidays) {
        this.id = id;
        this.attendant = attendant;
        this.designation = designation;
        this.type = type;
        this.address = address;
        this.email = email;
        this.website = website;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.title = title;
        this.open_time = open_time;
        this.close_time = close_time;
        this.holidays = holidays;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getAttendant () {
        return attendant;
    }
    
    public void setAttendant (String attendant) {
        this.attendant = attendant;
    }
    
    public String getAddress () {
        return address;
    }
    
    public void setAddress (String address) {
        this.address = address;
    }
    
    public String getEmail () {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }
    
    public String getWebsite () {
        return website;
    }
    
    public void setWebsite (String website) {
        this.website = website;
    }
    
    public ArrayList<String> getContactList () {
        return contactList;
    }
    
    public void setContactList (ArrayList<String> contactList) {
        this.contactList = contactList;
    }
    
    public String getPhone1 () {
        return phone1;
    }
    
    public void setPhone1 (String phone1) {
        this.phone1 = phone1;
    }
    
    public String getPhone2 () {
        return phone2;
    }
    
    public void setPhone2 (String phone2) {
        this.phone2 = phone2;
    }
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    public String getOpen_time () {
        return open_time;
    }
    
    public void setOpen_time (String open_time) {
        this.open_time = open_time;
    }
    
    public String getClose_time () {
        return close_time;
    }
    
    public void setClose_time (String close_time) {
        this.close_time = close_time;
    }
    
    public String getHolidays () {
        return holidays;
    }
    
    public void setHolidays (String holidays) {
        this.holidays = holidays;
    }
    
    public String getDesignation () {
        return designation;
    }
    
    public void setDesignation (String designation) {
        this.designation = designation;
    }
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
}
