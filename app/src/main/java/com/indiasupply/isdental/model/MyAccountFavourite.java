package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class MyAccountFavourite {
    int id, icon, type;
    String name, image, phone, location, email, website;
    boolean is_favourite;
    
    
    public MyAccountFavourite (int id, int icon, int type, String name, String image, String phone, String location, String email, String website, boolean is_favourite) {
        this.id = id;
        this.icon = icon;
        this.type = type;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.location = location;
        this.email = email;
        this.website = website;
        this.is_favourite = is_favourite;
    }
    
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
    }
    
    public int getType () {
        return type;
    }
    
    public void setType (int type) {
        this.type = type;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    public String getPhone () {
        return phone;
    }
    
    public void setPhone (String phone) {
        this.phone = phone;
    }
    
    public String getLocation () {
        return location;
    }
    
    public void setLocation (String location) {
        this.location = location;
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
    
    public boolean is_favourite () {
        return is_favourite;
    }
    
    public void setIs_favourite (boolean is_favourite) {
        this.is_favourite = is_favourite;
    }
}