package com.example.dark.appsaloon.Models;

/**
 * Created by abd on 05-Mar-18.
 */

public class SalonModel {

    String salon_id,salon_name,latitude,longitude,city,rate,manager_id;


    public String getRate() {
        return rate;
    }

    public String getSalon_id() {
        return salon_id;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getManager_id() {
        return manager_id;
    }

    public SalonModel(String salon_id, String salon_name, String latitude, String longitude, String city, String rate, String manager_id) {
        this.salon_id = salon_id;
        this.salon_name = salon_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.rate = rate;
        this.manager_id = manager_id;
    }
}
