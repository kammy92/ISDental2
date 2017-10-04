package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class SwiggySpeaker {
    int id;
    String name, image, qualification;
    
    public SwiggySpeaker (int id, String name, String image, String qualification) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.qualification = qualification;
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
    
    public String getQualification () {
        return qualification;
    }
    
    public void setQualification (String qualification) {
        this.qualification = qualification;
    }
}
