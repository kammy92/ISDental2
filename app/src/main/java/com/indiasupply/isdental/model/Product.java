package com.indiasupply.isdental.model;

/**
 * Created by sud on 27/9/17.
 */

public class Product {
    boolean recommended;
    int id;
    String name, description, category, image, price, type;
    
    public Product (boolean recommended, int id, String name, String description, String category, String image, String price, String type) {
        this.recommended = recommended;
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
        this.type = type;
    }
    
    public boolean isRecommended () {
        return recommended;
    }
    
    public void setRecommended (boolean recommended) {
        this.recommended = recommended;
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
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    public String getPrice () {
        return price;
    }
    
    public void setPrice (String price) {
        this.price = price;
    }
    
    public String getCategory () {
        return category;
    }
    
    public void setCategory (String category) {
        this.category = category;
    }
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
}