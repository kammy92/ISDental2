package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class SwiggyBrand {
    boolean is_offer;
    boolean is_isassured;
    int id;
    String title, description, rating, offers, category, image;
    
    public SwiggyBrand (boolean is_offer, boolean is_isassured, int id, String title, String description, String rating, String offers, String category, String image) {
        this.is_offer = is_offer;
        this.is_isassured = is_isassured;
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.offers = offers;
        this.category = category;
        this.image = image;
    }
    
    public boolean is_offer () {
        return is_offer;
    }
    
    public void setIs_offer (boolean is_offer) {
        this.is_offer = is_offer;
    }
    
    public boolean is_isassured () {
        return is_isassured;
    }
    
    public void setIs_isassured (boolean is_isassured) {
        this.is_isassured = is_isassured;
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
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getRating () {
        return rating;
    }
    
    public void setRating (String rating) {
        this.rating = rating;
    }
    
    public String getOffers () {
        return offers;
    }
    
    public void setOffers (String offers) {
        this.offers = offers;
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
}
