package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class SwiggyMyAccountEnquiry {
    int id, enquiry_status;
    String enquiry_ticket_number, enquiry_remark, company_name, product_name, product_price, product_category, product_image, product_description, product_packaging;
    
    public SwiggyMyAccountEnquiry (int id, int enquiry_status, String enquiry_ticket_number, String enquiry_remark, String company_name, String product_name, String product_price, String product_category, String product_image, String product_description, String product_packaging) {
        this.id = id;
        this.enquiry_status = enquiry_status;
        this.enquiry_ticket_number = enquiry_ticket_number;
        this.enquiry_remark = enquiry_remark;
        this.company_name = company_name;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_category = product_category;
        this.product_image = product_image;
        this.product_description = product_description;
        this.product_packaging = product_packaging;
    }
    
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getEnquiry_status () {
        return enquiry_status;
    }
    
    public void setEnquiry_status (int enquiry_status) {
        this.enquiry_status = enquiry_status;
    }
    
    public String getEnquiry_ticket_number () {
        return enquiry_ticket_number;
    }
    
    public void setEnquiry_ticket_number (String enquiry_ticket_number) {
        this.enquiry_ticket_number = enquiry_ticket_number;
    }
    
    public String getEnquiry_remark () {
        return enquiry_remark;
    }
    
    public void setEnquiry_remark (String enquiry_remark) {
        this.enquiry_remark = enquiry_remark;
    }
    
    public String getCompany_name () {
        return company_name;
    }
    
    public void setCompany_name (String company_name) {
        this.company_name = company_name;
    }
    
    public String getProduct_name () {
        return product_name;
    }
    
    public void setProduct_name (String product_name) {
        this.product_name = product_name;
    }
    
    public String getProduct_price () {
        return product_price;
    }
    
    public void setProduct_price (String product_price) {
        this.product_price = product_price;
    }
    
    public String getProduct_category () {
        return product_category;
    }
    
    public void setProduct_category (String product_category) {
        this.product_category = product_category;
    }
    
    public String getProduct_image () {
        return product_image;
    }
    
    public void setProduct_image (String product_image) {
        this.product_image = product_image;
    }
    
    public String getProduct_description () {
        return product_description;
    }
    
    public void setProduct_description (String product_description) {
        this.product_description = product_description;
    }
    
    public String getProduct_packaging () {
        return product_packaging;
    }
    
    public void setProduct_packaging (String product_packaging) {
        this.product_packaging = product_packaging;
    }
}
