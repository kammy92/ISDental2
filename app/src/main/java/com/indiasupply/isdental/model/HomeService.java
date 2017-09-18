package com.indiasupply.isdental.model;

/**
 * Created by l on 27/04/2017.
 */

public class HomeService {

    int id,icon;
    String service_name, image_url;
    
    public HomeService (int id, int icon) {
        this.id = id;
        this.icon = icon;
    }
    
    public HomeService (int id, String image_url) {
        this.id = id;
        this.image_url = image_url;
    }
    
    public HomeService (int id, int icon, String service_name) {
        this.id = id;
        this.icon = icon;
        this.service_name = service_name;
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
    
    public String getImage_url () {
        return image_url;
    }
    
    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }
    
    public String getService_name () {
        return service_name;
    }

    public void setService_name (String service_name) {
        this.service_name = service_name;
    }
}
