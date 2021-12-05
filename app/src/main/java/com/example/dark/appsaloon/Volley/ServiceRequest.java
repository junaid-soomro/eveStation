package com.example.dark.appsaloon.Volley;

import android.provider.SyncStateContract;

import com.android.volley.AuthFailureError;
import com.example.dark.appsaloon.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Models.AppointmentsModel;
import com.example.dark.appsaloon.Models.ServiceModel;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 03-Mar-18.
 */

public class ServiceRequest extends StringRequest {

    ArrayList<String> arrayList = new ArrayList<>();

    Map<String,String> parameters;

    public ServiceRequest(String customer_id,String manager, Response.Listener<String> listener,Response.ErrorListener errorListener){ //fetching ordered services in appointments
        super(Request.Method.POST,new Constants().fetch_ordered_services,listener,errorListener);

        parameters = new HashMap<>();

        parameters.put("customer_id",customer_id);
        parameters.put("manager",manager);


    }
//add service to service_detail table
    public ServiceRequest(String service_name,String service_price,String service_detail,String image,String category,String id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, new Constants().add_fetch_service, listener, errorListener);

        parameters = new HashMap<>();

        parameters.put("service_name",service_name);
        parameters.put("detail",service_detail);
        parameters.put("price",service_price);
        parameters.put("image",image);
        parameters.put("category",category);
        parameters.put("id",id);

    }
//responding to customer with ordered services
    public ServiceRequest(String customer_id, ArrayList<AppointmentsModel> arrayList,String status,String manager, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST,new Constants().respond_customer,listener,errorListener);

        parameters = new HashMap<>();

        for(int i=0;i<arrayList.size();i++){

            this.arrayList.add(arrayList.get(i).getAppointed_services_id());

        }

        JSONArray array = new JSONArray(this.arrayList);

        parameters.put("customer_id",customer_id);
        parameters.put("service_id", array.toString());
        parameters.put("status",status);
        parameters.put("manager",manager);
    }

    //fetchting selected services
    public ServiceRequest(String customer_id, ArrayList<String> service_id,Response.Listener<String> listener,Response.ErrorListener errorListener)
    {
        super(Method.POST,new Constants().add_fetch_service,listener,errorListener);

        parameters = new HashMap<>();

        JSONArray array = new JSONArray(service_id);

        parameters.put("customer_id",customer_id);
        parameters.put("service_id",service_id.toString());

    }



    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
