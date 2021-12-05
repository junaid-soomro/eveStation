package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 18-Apr-18.
 */

public class ManagerServicesRequest extends StringRequest {

    Map<String,String> parameters;

    public ManagerServicesRequest(String manager_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().manager_service,listener,errorListener);

        parameters = new HashMap<>();

        parameters.put("manager_id",manager_id);
    }
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }



}
