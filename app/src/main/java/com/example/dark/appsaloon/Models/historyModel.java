package com.example.dark.appsaloon.Models;


public class historyModel {

    String appoint_id,service_name,appoint_status,date,time,payment_status,price,latitude,longitude,appointed_servies,appointed_services_id;

    public String getAppoint_id() {
        return appoint_id;
    }

    public String getService_name() {
        return service_name;
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

    public String getAppoint_status() {
        return appoint_status;
    }

    public historyModel(String appoint_id, String service_name, String appoint_status, String date, String time, String payment_status, String price) {
        this.appoint_id = appoint_id;
        this.service_name = service_name;
        this.date = date;
        this.time = time;
        this.payment_status = payment_status;
        this.price = price;
        this.appoint_status = appoint_status;
        this.appointed_servies = appointed_servies;
        this.appointed_services_id = appointed_services_id;
    }
}
