package com.indiasupply.isdental.utils;

public class AppConfigURL {
    public static String version = "v1.1.3";
    public static String version2 = "v2.0";
    public static String BASE_URL2 = "https://project-isdental-cammy92.c9users.io/api/" + version2 + "/";
    //    public static String BASE_URL2 = "http://34.210.142.70/isdental/api/" + version2 + "/";
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
    
    
    public static String URL_SWIGGY_HOME_EVENT = BASE_URL2 + "home/events";
    public static String URL_SWIGGY_EVENT_DETAILS = BASE_URL2 + "event";
    
    public static String URL_SWIGGY_HOME_SERVICE = BASE_URL2 + "home/service";
    public static String URL_SWIGGY_HOME_COMPANIES = BASE_URL2 + "home/companies";
    public static String URL_SWIGGY_HOME_ACCOUNT = BASE_URL2 + "home/account";
    
    public static String URL_SWIGGY_INIT = BASE_URL2 + "/init/application";
    public static String URL_SWIGGY_ADD_PRODUCT = BASE_URL2 + "user/product";
    public static String URL_SWIGGY_ADD_REQUEST = BASE_URL2 + "user/request";
    public static String URL_SWIGGY_COMPANY_DETAILS = BASE_URL2 + "company";
    public static String URL_SWIGGY_FEATURED_LIST = BASE_URL2 + "home/featured";
    
    
    public static String URL_SWIGGY_USER_EXIST = BASE_URL2 + "user/exist";
    public static String URL_SWIGGY_GETOTP = BASE_URL2 + "user/otp";
    public static String URL_SWIGGY_REGISTER = BASE_URL2 + "user/register";
    public static String URL_SWIGGY_FAVOURITE = BASE_URL2 + "favourite";
    public static String URL_SWIGGY_ENQUIRY = BASE_URL2 + "enquiry";
    
    public static String URL_SWIGGY_MY_PRODUCT_DETAIL = BASE_URL2 + "/user/product/";
    public static String URL_SWIGGY_SERVICE_REQUEST_COMMENTS = BASE_URL2 + "/user/request/comment";
}
