package com.indiasupply.isdental.model;

/**
 * Created by sud on 3/10/17.
 */

public class SwiggyMyAccountOffer {
    int id, icon, user_id, status;
    String name, expire, start;
    
    
    public SwiggyMyAccountOffer (int id, int icon, int user_id, int status, String name, String expire, String start) {
        this.id = id;
        this.icon = icon;
        this.user_id = user_id;
        this.status = status;
        this.name = name;
        this.expire = expire;
        this.start = start;
    }
    
    public int getUser_id () {
        return user_id;
    }
    
    public void setUser_id (int user_id) {
        this.user_id = user_id;
    }
    
    public int getStatus () {
        return status;
    }
    
    public void setStatus (int status) {
        this.status = status;
    }
    
    public String getExpire () {
        return expire;
    }
    
    public void setExpire (String expire) {
        this.expire = expire;
    }
    
    public String getStart () {
        return start;
    }
    
    public void setStart (String start) {
        this.start = start;
    }


    public int getIcon () {
        return icon;
    }
    
    public void setIcon (int icon) {
        this.icon = icon;
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
