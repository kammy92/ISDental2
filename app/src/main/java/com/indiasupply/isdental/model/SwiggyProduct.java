package com.indiasupply.isdental.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sud on 27/9/17.
 */

public class SwiggyProduct implements Parcelable {
    public static final Creator<SwiggyProduct> CREATOR = new Creator<SwiggyProduct> () {
        @Override
        public SwiggyProduct createFromParcel (Parcel in) {
            int id = in.readInt ();
            int icon = in.readInt ();
            int group_type = in.readInt ();
            String name = in.readString ();
            String description = in.readString ();
            String category = in.readString ();
            String group_title = in.readString ();
            String image = in.readString ();
            String price = in.readString ();
            return new SwiggyProduct (id, icon, name, description, category, price, group_type, group_title, image);
        }
        
        @Override
        public SwiggyProduct[] newArray (int size) {
            return new SwiggyProduct[size];
        }
    };
    int id, icon, group_type;
    String name, description, category, image, price, group_title;
    
    public SwiggyProduct (int id, int icon, String name, String description, String category, String price, int group_type, String group_title, String image) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.price = price;
        this.group_type = group_type;
        this.group_title = group_title;
    }
    
    public SwiggyProduct () {

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
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    public String getPrice () {
        return price;
    }
    
    public void setPrice (String price) {
        this.price = price;
    }
    
    public String getCategory () {
        return category;
    }

    public void setCategory (String category) {
        this.category = category;
    }
    
    public int getGroup_type () {
        return group_type;
    }
    
    public void setGroup_type (int group_type) {
        this.group_type = group_type;
    }
    
    public String getGroup_title () {
        return group_title;
    }
    
    public void setGroup_title (String group_title) {
        this.group_title = group_title;
    }
    
    @Override
    public int describeContents () {
        return 0;
    }
    
    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt (id);
        dest.writeInt (icon);
        dest.writeInt (group_type);
        dest.writeString (name);
        dest.writeString (description);
        dest.writeString (category);
        dest.writeString (group_title);
        dest.writeString (image);
        dest.writeString (price);
    }
}