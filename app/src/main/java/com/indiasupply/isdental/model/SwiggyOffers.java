package com.indiasupply.isdental.model;

public class SwiggyOffers {
    int id, icon;
    String name, image, description, packaging, mrp, regular_price, price, saving, start_date, end_date, html_dates, html_details, html_tandc;
    
    public SwiggyOffers (int id, int icon, String name, String image, String description, String packaging, String mrp, String regular_price, String price, String saving, String start_date, String end_date, String html_dates, String html_details, String html_tandc) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.image = image;
        this.description = description;
        this.packaging = packaging;
        this.mrp = mrp;
        this.regular_price = regular_price;
        this.price = price;
        this.saving = saving;
        this.start_date = start_date;
        this.end_date = end_date;
        this.html_dates = html_dates;
        this.html_details = html_details;
        this.html_tandc = html_tandc;
    }
    
    public String getRegular_price () {
        return regular_price;
    }
    
    public void setRegular_price (String regular_price) {
        this.regular_price = regular_price;
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
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getPackaging () {
        return packaging;
    }
    
    public void setPackaging (String packaging) {
        this.packaging = packaging;
    }
    
    public String getMrp () {
        return mrp;
    }
    
    public void setMrp (String mrp) {
        this.mrp = mrp;
    }
    
    public String getPrice () {
        return price;
    }
    
    public void setPrice (String price) {
        this.price = price;
    }
    
    public String getSaving () {
        return saving;
    }
    
    public void setSaving (String saving) {
        this.saving = saving;
    }
    
    public String getStart_date () {
        return start_date;
    }
    
    public void setStart_date (String start_date) {
        this.start_date = start_date;
    }
    
    public String getEnd_date () {
        return end_date;
    }
    
    public void setEnd_date (String end_date) {
        this.end_date = end_date;
    }
    
    public String getHtml_dates () {
        return html_dates;
    }
    
    public void setHtml_dates (String html_dates) {
        this.html_dates = html_dates;
    }
    
    public String getHtml_details () {
        return html_details;
    }
    
    public void setHtml_details (String html_details) {
        this.html_details = html_details;
    }
    
    public String getHtml_tandc () {
        return html_tandc;
    }
    
    public void setHtml_tandc (String html_tandc) {
        this.html_tandc = html_tandc;
    }
}
