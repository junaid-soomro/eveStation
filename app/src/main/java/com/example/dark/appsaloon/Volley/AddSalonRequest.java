package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.*;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 01-Mar-18.
 */

public class AddSalonRequest extends StringRequest {

    private Map<String,String> parameters;

    public AddSalonRequest(String salon_name, String latitude, String longitude,String city,String manager_id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().add_fetch_salon,listener,errorListener);
        parameters = new HashMap<>();
        parameters.put("salon_name",salon_name);
        parameters.put("lat",latitude);
        parameters.put("lon",longitude);
        parameters.put("city",city);
        parameters.put("id",manager_id);

    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
