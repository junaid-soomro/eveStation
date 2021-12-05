package com.example.dark.appsaloon.Models;

/**
 * Created by abd on 02-Mar-18.
 */

public class AppointmentsModel {

    String appoint_id,customer_name,customer_id,appoint_status,date,time,payment_status,price,latitude,longitude,appointed_servies,appointed_services_id;

    public AppointmentsModel(String appointed_services_id,String appointed_servies) {
        this.appointed_servies = appointed_servies;
        this.appointed_services_id = appointed_services_id;
    }

    public String getAppointed_servies() {
        return appointed_servies;
    }

    public String getAppointed_services_id() {
        return appointed_services_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public AppointmentsModel(String appoint_id, String customer_name,String customer_id, String appoint_status, String latitude, String longitude, String payment_status, String price, String time, String date) {
        this.appoint_id = appoint_id;
        this.customer_name = customer_name;
        this.appoint_status = appoint_status;
        this.date = date;
        this.time = time;
        this.payment_status = payment_status;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getAppoint_id() {
        return appoint_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getAppoint_status() {
        return appoint_status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getPrice() {
        return price;
    }


    public AppointmentsModel(String appoint_id, String service_name, String appoint_status, String date, String time, String payment_status, String price) {
        this.appoint_id = appoint_id;
        this.customer_name = service_name;
        this.date = date;
        this.time = time;
        this.payment_status = payment_status;
        this.price = price;
        this.appoint_status = appoint_status;
        this.appointed_servies = appointed_servies;
        this.appointed_services_id = appointed_services_id;
    }

}
