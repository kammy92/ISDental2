package com.indiasupply.isdental.model;

public class MyAccountSaving {
    int id, status, savings;
    String offer_name, claimed_date;
    
    public MyAccountSaving (int id, int status, int savings, String offer_name, String claimed_date) {
        this.id = id;
        this.status = status;
        this.savings = savings;
        this.offer_name = offer_name;
        this.claimed_date = claimed_date;
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
    
    public int getSavings () {
        return savings;
    }
    
    public void setSavings (int savings) {
        this.savings = savings;
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
