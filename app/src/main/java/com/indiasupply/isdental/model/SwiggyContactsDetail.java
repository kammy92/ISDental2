package com.indiasupply.isdental.model;

public class SwiggyContactsDetail {
    int id, type;
    boolean is_favourite;
    String name, location, image_url, contact_number;
    
    public SwiggyContactsDetail (int id, int type, boolean is_favourite, String name, String location, String image_url, String contact_number) {
        this.id = id;
        this.type = type;
        this.is_favourite = is_favourite;
        this.name = name;
        this.location = location;
        this.image_url = image_url;
        this.contact_number = contact_number;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getType () {
        return type;
    }
    
    public void setType (int type) {
        this.type = type;
    }
    
    public boolean is_favourite () {
        return is_favourite;
    }
    
    public void setIs_favourite (boolean is_favourite) {
        this.is_favourite = is_favourite;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
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
    
    public String getContact_number () {
        return contact_number;
    }
    
    public void setContact_number (String contact_number) {
        this.contact_number = contact_number;
    }
}