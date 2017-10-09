package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyCompany {
    
    int id, icon;
    String title, contacts, category, email, website, image;
    
    public SwiggyCompany (int id, int icon, String title, String contacts, String category, String email, String website, String image) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.contacts = contacts;
        this.category = category;
        this.image = image;
        this.email = email;
        this.website = website;
    }
    
    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    public String getContacts () {
        return contacts;
    }
    
    public void setContacts (String contacts) {
        this.contacts = contacts;
    }
    
    public String getCategory () {
        return category;
    }
    
    public void setCategory (String category) {
        this.category = category;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
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
}
