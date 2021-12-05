package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 08-Mar-18.
 */

public class AppointmentsRequest extends StringRequest {

    Map<String,String> parameters;

    public AppointmentsRequest(String id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().fetch_history,listener,errorListener);

        parameters = new HashMap<>();

        parameters.put("manager",id);
    }
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
