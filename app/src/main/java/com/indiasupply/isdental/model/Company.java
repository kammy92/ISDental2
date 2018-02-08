package com.indiasupply.isdental.model;

/**
 * Created by l on 26/09/2017.
 */

public class Company {
    boolean is_isassured;
    int id, icon;
    String title, description, rating, offers, category, image, total_ratings, total_contacts, total_products;
    
    public Company (boolean is_isassured, int id, int icon, String title, String description, String rating, String offers, String category, String image, String total_ratings, String total_contacts, String total_products) {
        this.is_isassured = is_isassured;
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.offers = offers;
        this.category = category;
        this.image = image;
        this.total_ratings = total_ratings;
        this.total_contacts = total_contacts;
        this.total_products = total_products;
    }
    
    public boolean isIs_isassured () {
        return is_isassured;
    }
    
    public void setIs_isassured (boolean is_isassured) {
        this.is_isassured = is_isassured;
    }
    
    public String getTotal_products () {
        return total_products;
    }
    
    public void setTotal_products (String total_products) {
        this.total_products = total_products;
    }
    
    public String getTotal_ratings () {
        return total_ratings;
    }
    
    public void setTotal_ratings (String total_ratings) {
        this.total_ratings = total_ratings;
    }
    
    public String getTotal_contacts () {
        return total_contacts;
    }
    
    public void setTotal_contacts (String total_contacts) {
        this.total_contacts = total_contacts;
    }
    
    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
    }
    
    public boolean is_isassured () {
        return is_isassured;
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
