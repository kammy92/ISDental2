package com.indiasupply.isdental.model;

/**
 * Created by l on 21/11/2017.
 */

public class SwiggyServiceRequestComments {
    int comment_id, comment_type;
    String comment_from, comment_text, comment_created_at;
    
    
    public SwiggyServiceRequestComments (int comment_id, String comment_from, String comment_text, int comment_type, String comment_created_at) {
        this.comment_id = comment_id;
        this.comment_from = comment_from;
        this.comment_text = comment_text;
        this.comment_type = comment_type;
        this.comment_created_at = comment_created_at;
    }
    
    public int getComment_id () {
        return comment_id;
    }
    
    public void setComment_id (int comment_id) {
        this.comment_id = comment_id;
    }
    
    public String getComment_from () {
        return comment_from;
    }
    
    public void setComment_from (String comment_from) {
        this.comment_from = comment_from;
    }
    
    public String getComment_text () {
        return comment_text;
    }
    
    public void setComment_text (String comment_text) {
        this.comment_text = comment_text;
    }
    
    public int getComment_type () {
        return comment_type;
    }
    
    public void setComment_type (int comment_type) {
        this.comment_type = comment_type;
    }
    
    public String getComment_created_at () {
        return comment_created_at;
    }
    
    public void setComment_created_at (String comment_created_at) {
        this.comment_created_at = comment_created_at;
    }
}
