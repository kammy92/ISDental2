package com.indiasupply.isdental.model;

/**
 * Created by l on 06/10/2017.
 */

public class MyRequest {
    int id, icon;
    String ticket_number, serial_number, description, image, generated_at;
    
    
    public MyRequest (int id, int icon, String ticket_number, String serial_number, String description, String image, String generated_at) {
        this.id = id;
        this.icon = icon;
        this.ticket_number = ticket_number;
        this.serial_number = serial_number;
        this.description = description;
        this.image = image;
        this.generated_at = generated_at;
    }
    
    public String getGenerated_at () {
        return generated_at;
    }
    
    public void setGenerated_at (String generated_at) {
        this.generated_at = generated_at;
    }
    
    public String getTicket_number () {
        return ticket_number;
    }
    
    public void setTicket_number (String ticket_number) {
        this.ticket_number = ticket_number;
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
