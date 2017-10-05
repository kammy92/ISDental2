package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class SwiggyEventExhibitor {
    int id;
    String name, image, stall;
    
    public SwiggyEventExhibitor (int id, String name, String image, String stall) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.stall = stall;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
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
    
    public String getStall () {
        return stall;
    }
    
    public void setStall (String stall) {
        this.stall = stall;
    }
}
