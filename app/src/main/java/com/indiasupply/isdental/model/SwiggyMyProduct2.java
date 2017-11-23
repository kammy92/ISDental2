package com.indiasupply.isdental.model;

public class SwiggyMyProduct2 {
    int id, icon;
    String name, description, brand, model_number, serial_number, purchase_date, request_created_at, request_status, request_ticket_number;
    
    public SwiggyMyProduct2 (int id, int icon, String name, String description, String brand, String model_number, String serial_number, String purchase_date, String request_created_at, String request_status, String request_ticket_number) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.request_created_at = request_created_at;
        this.request_status = request_status;
        this.request_ticket_number = request_ticket_number;
        this.brand = brand;
        this.model_number = model_number;
        this.serial_number = serial_number;
        this.purchase_date = purchase_date;
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
    
    public String getPurchase_date () {
        return purchase_date;
    }
    
    public void setPurchase_date (String purchase_date) {
        this.purchase_date = purchase_date;
    }
    
    public String getRequest_created_at () {
        return request_created_at;
    }
    
    public void setRequest_created_at (String request_created_at) {
        this.request_created_at = request_created_at;
    }
    
    public String getRequest_status () {
        return request_status;
    }
    
    public void setRequest_status (String request_status) {
        this.request_status = request_status;
    }
    
    public String getRequest_ticket_number () {
        return request_ticket_number;
    }
    
    public void setRequest_ticket_number (String request_ticket_number) {
        this.request_ticket_number = request_ticket_number;
    }
}
