package com.indiasupply.isdental.model;

/**
 * Created by l on 06/10/2017.
 */

public class SwiggyMyRequest {
    int id, icon;
    String request_ticket_number, request_serial_number, request_description, image;
    
    
    public SwiggyMyRequest (int id, int icon, String request_ticket_number, String request_serial_number, String request_description, String image) {
        this.id = id;
        this.icon = icon;
        this.request_ticket_number = request_ticket_number;
        this.request_serial_number = request_serial_number;
        this.request_description = request_description;
        this.image = image;
    }
    
    public String getRequest_ticket_number () {
        return request_ticket_number;
    }
    
    public void setRequest_ticket_number (String request_ticket_number) {
        this.request_ticket_number = request_ticket_number;
    }
    
    public String getRequest_serial_number () {
        return request_serial_number;
    }
    
    public void setRequest_serial_number (String request_serial_number) {
        this.request_serial_number = request_serial_number;
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
