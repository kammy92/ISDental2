package com.indiasupply.isdental.model;

public class SwiggyMyProduct {
    int id, icon;
    String product_name, product_description, image;
    
    
    public SwiggyMyProduct (int id, int icon, String product_name, String product_description, String image) {
        this.id = id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.icon = icon;
        this.image = image;
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
    
    public String getProduct_name () {
        return product_name;
    }
    
    public void setProduct_name (String product_name) {
        this.product_name = product_name;
    }
    
    public String getProduct_description () {
        return product_description;
    }
    
    public void setProduct_description (String product_description) {
        this.product_description = product_description;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    
}
