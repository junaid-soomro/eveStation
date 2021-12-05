package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 04-Mar-18.
 */

public class ServiceEditDeleteRequest extends StringRequest {

    Map<String,String> parameters;
//deleting service
    public ServiceEditDeleteRequest(String service_id,String operation, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, new Constants().edit_delete_service, listener, errorListener);

        parameters = new HashMap<>();

        parameters.put("operation",operation);
        parameters.put("service_id",service_id);

    }
//editing service with image
    public ServiceEditDeleteRequest(String service_id,String name,String price,String detail,String image,String categ, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, new Constants().edit_delete_service, listener, errorListener);

        parameters = new HashMap<>();

        parameters.put("service_id",service_id);
        parameters.put("service_name",name);
        parameters.put("service_price",price);
        parameters.put("service_detail",detail);
        parameters.put("service_image",image);
        parameters.put("categ",categ);
    }
//editing without image
    public ServiceEditDeleteRequest(String service_id,String name,String price,String detail,String categ,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, new Constants().edit_delete_service, listener, errorListener);

        parameters = new HashMap<>();

        parameters.put("service_id",service_id);
        parameters.put("service_name",name);
        parameters.put("service_price",price);
        parameters.put("service_detail",detail);
        parameters.put("categ",categ);
    }


    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
