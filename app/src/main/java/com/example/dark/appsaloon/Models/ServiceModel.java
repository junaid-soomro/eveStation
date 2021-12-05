package com.example.dark.appsaloon.Models;

import java.io.Serializable;

/**
 * Created by abd on 04-Mar-18.
 */

public class ServiceModel  implements Serializable{

    String service_id,service_name,service_price,service_detail,service_image,Category,manager;

    public String getCategory() {
        return Category;
    }

    public String getService_name() {
        return service_name;
    }

    public String getService_price() {
        return service_price;
    }

    public String getService_detail() {
        return service_detail;
    }

    public String getService_image() {
        return service_image;
    }

    public String getService_id() {
        return service_id;
    }

    public ServiceModel(String service_id, String service_name, String service_price, String service_detail, String service_image,String categ) {
        this.service_name = service_name;
        this.service_id = service_id;
        this.service_price = service_price;
        this.service_detail = service_detail;
        this.service_image = service_image;
        this.Category = categ;
    }
    public ServiceModel(String service_id, String service_name, String service_price, String service_detail, String service_image,String categ,String manager) {
        this.service_name = service_name;
        this.service_id = service_id;
        this.service_price = service_price;
        this.service_detail = service_detail;
        this.service_image = service_image;
        this.Category = categ;
        this.manager=manager;
    }

    public String getManager() {
        return manager;
    }
}
