package com.indiasupply.isdental.model;

public class MyProduct {
    int id, icon;
    String name, description, image, category, brand, model_number, serial_number, purchase_date;
    
    public MyProduct (int id, int icon, String name, String description, String image, String category, String brand, String model_number, String serial_number, String purchase_date) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.image = image;
        this.category = category;
        this.brand = brand;
        this.model_number = model_number;
        this.serial_number = serial_number;
        this.purchase_date = purchase_date;
    }
    
    public String getPurchase_date () {
        return purchase_date;
    }
    
    public void setPurchase_date (String purchase_date) {
        this.purchase_date = purchase_date;
    }
    
    public String getCategory () {
        return category;
    }
    
    public void setCategory (String category) {
        this.category = category;
    }
    
    public String getBrand () {
        return brand;
    }
    
    public void setBrand (String brand) {
        this.brand = brand;
    }
    
    public String getModel_number () {
        return model_number;
    }
    
    public void setModel_number (String model_number) {
        this.model_number = model_number;
    }
    
    public String getSerial_number () {
        return serial_number;
    }
    
    public void setSerial_number (String serial_number) {
        this.serial_number = serial_number;
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
    
    
}
