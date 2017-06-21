package com.indiasupply.isdental.model;


public class Company {
    int id;
    String logo, name, description, brands;
    
    public Company (int id, String logo, String name, String description, String brands) {
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.description = description;
        this.brands = brands;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }
    
    public String getLogo () {
        return logo;
    }
    
    public void setLogo (String Brand_logo) {
        this.logo = Brand_logo;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String Brand_name) {
        this.name = Brand_name;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String Brand_description) {
        this.description = Brand_description;
    }
    
    public String getBrands () {
        return brands;
    }
    
    public void setBrands (String brands) {
        this.brands = brands;
    }
}