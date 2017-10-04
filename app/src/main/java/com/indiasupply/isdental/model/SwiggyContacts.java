package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyContacts {
    
    int id;
    String title, contacts, category, image, email, website;
    
    public SwiggyContacts (int id, String title, String contacts, String category, String image, String email, String website) {
        this.id = id;
        this.title = title;
        this.contacts = contacts;
        this.category = category;
        this.image = image;
        this.email = email;
        this.website = website;
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
