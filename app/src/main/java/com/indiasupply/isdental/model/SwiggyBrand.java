package com.indiasupply.isdental.model;

/**
 * Created by sud on 16/10/17.
 */

public class SwiggyBrand {
    int id;
    String name;
    
    public SwiggyBrand (int id, String name) {
        this.id = id;
        this.name = name;
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
}