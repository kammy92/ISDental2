package com.indiasupply.isdental.model;

public class MyAccountOrder {
    int id, status, order_qty, price, mrp, offer_qty;
    String order_unique_id, offer_name, claimed_date;
    
    public MyAccountOrder (int id, int status, int order_qty, int price, int mrp, int offer_qty, String order_unique_id, String offer_name, String claimed_date) {
        this.id = id;
        this.status = status;
        this.order_qty = order_qty;
        this.price = price;
        this.mrp = mrp;
        this.offer_qty = offer_qty;
        this.order_unique_id = order_unique_id;
        this.offer_name = offer_name;
        this.claimed_date = claimed_date;
    }
    
    public String getOrder_unique_id () {
        return order_unique_id;
    }
    
    public void setOrder_unique_id (String order_unique_id) {
        this.order_unique_id = order_unique_id;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getStatus () {
        return status;
    }
    
    public void setStatus (int status) {
        this.status = status;
    }
    
    public int getOrder_qty () {
        return order_qty;
    }
    
    public void setOrder_qty (int order_qty) {
        this.order_qty = order_qty;
    }
    
    public int getPrice () {
        return price;
    }
    
    public void setPrice (int price) {
        this.price = price;
    }
    
    public int getMrp () {
        return mrp;
    }
    
    public void setMrp (int mrp) {
        this.mrp = mrp;
    }
    
    public int getOffer_qty () {
        return offer_qty;
    }
    
    public void setOffer_qty (int offer_qty) {
        this.offer_qty = offer_qty;
    }
    
    public String getOffer_name () {
        return offer_name;
    }
    
    public void setOffer_name (String offer_name) {
        this.offer_name = offer_name;
    }
    
    public String getClaimed_date () {
        return claimed_date;
    }
    
    public void setClaimed_date (String claimed_date) {
        this.claimed_date = claimed_date;
    }
}
