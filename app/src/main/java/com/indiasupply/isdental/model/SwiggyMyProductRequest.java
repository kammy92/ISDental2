package com.indiasupply.isdental.model;

/**
 * Created by l on 21/11/2017.
 */

public class SwiggyMyProductRequest {
    int request_id;
    String description, status, created_at, ticket_number, request_comments;
    String brands, model_number, product_description, serial_number;
    String image1, image2, image3;
    
    public SwiggyMyProductRequest (int request_id, String description, String status, String created_at, String ticket_number, String request_comments, String image1, String image2, String image3) {
        this.description = description;
        this.status = status;
        this.created_at = created_at;
        this.request_id = request_id;
        this.ticket_number = ticket_number;
        this.request_comments = request_comments;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getStatus () {
        return status;
    }
    
    public void setStatus (String status) {
        this.status = status;
    }
    
    public String getCreated_at () {
        return created_at;
    }
    
    public void setCreated_at (String created_at) {
        this.created_at = created_at;
    }
    
    
    public int getRequest_id () {
        return request_id;
    }
    
    public void setRequest_id (int request_id) {
        this.request_id = request_id;
    }
    
    public String getTicket_number () {
        return ticket_number;
    }
    
    public void setTicket_number (String ticket_number) {
        this.ticket_number = ticket_number;
    }
    
    public String getRequest_comments () {
        return request_comments;
    }
    
    public void setRequest_comments (String request_comments) {
        this.request_comments = request_comments;
    }
    
    
    public String getBrands () {
        return brands;
    }
    
    public void setBrands (String brands) {
        this.brands = brands;
    }
    
    public String getModel_number () {
        return model_number;
    }
    
    public void setModel_number (String model_number) {
        this.model_number = model_number;
    }
    
    public String getProduct_description () {
        return product_description;
    }
    
    public void setProduct_description (String product_description) {
        this.product_description = product_description;
    }
    
    public String getSerial_number () {
        return serial_number;
    }
    
    public void setSerial_number (String serial_number) {
        this.serial_number = serial_number;
    }
    
    public String getImage1 () {
        return image1;
    }
    
    public void setImage1 (String image1) {
        this.image1 = image1;
    }
    
    public String getImage2 () {
        return image2;
    }
    
    public void setImage2 (String image2) {
        this.image2 = image2;
    }
    
    public String getImage3 () {
        return image3;
    }
    
    public void setImage3 (String image3) {
        this.image3 = image3;
    }
}
