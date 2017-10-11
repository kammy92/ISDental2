package com.indiasupply.isdental.model;

/**
 * Created by l on 06/10/2017.
 */

public class SwiggyMyRequest {
    int id, icon;
    String request_name, request_description, image;
    
    
    public SwiggyMyRequest (int id, int icon, String request_name, String request_description, String image) {
        this.id = id;
        this.icon = icon;
        this.request_name = request_name;
        this.request_description = request_description;
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
    
    public String getRequest_name () {
        return request_name;
    }
    
    public void setRequest_name (String request_name) {
        this.request_name = request_name;
    }
    
    public String getRequest_description () {
        return request_description;
    }
    
    public void setRequest_description (String request_description) {
        this.request_description = request_description;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
}
