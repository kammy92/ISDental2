package com.indiasupply.isdental.utils;

public class AppConfigURL {
    public static String version = "v1.1.3";
    public static String BASE_URL2 = "https://project-isdental-cammy92.c9users.io/api/v2.0/";// + version + "/";
    public static String BASE_URL = "http://famdent.indiasupply.com/isdental/api/" + version + "/";
    
    
    public static String URL_GETOTP = BASE_URL + "user/otp";
    
    public static String URL_REGISTER = BASE_URL + "user/register";
    
    public static String URL_INIT = BASE_URL + "/init/application";
    
    public static String URL_CATEGORY_LIST = BASE_URL + "category";
    public static String URL_COMPANY_LIST = BASE_URL + "company";
    public static String URL_COMPANY_DETAILS = BASE_URL + "company";
    
    
    public static String URL_EVENT_LIST = BASE_URL + "event";
    public static String URL_EVENT_DETAILS = BASE_URL + "event";
    
    public static String URL_ORGANISER_DETAILS = BASE_URL + "organiser";
    
    public static String URL_REGISTER_EVENT = BASE_URL + "user/register/event";
    
    public static String URL_SPECIAL_EVENT_DETAILS = BASE_URL + "event/special";
    
    
    public static String URL_SWIGGY_EVENT = BASE_URL2 + "events";
    public static String URL_SWIGGY_EVENT_DETAILS = BASE_URL2 + "event";
    
    public static String URL_SWIGGY_INIT = BASE_URL2 + "/init/application";
    public static String URL_SWIGGY_PRODUCT = BASE_URL2 + "user/product";
    public static String URL_SWIGGY_MY_PRODUCT_LIST = BASE_URL2 + "user/products";
    public static String URL_SWIGGY_REQUEST = BASE_URL2 + "user/request";
    public static String URL_SWIGGY_MY_REQUEST_LIST = BASE_URL2 + "user/requests";
}
