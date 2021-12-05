package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 08-Mar-18.
 */

public class historyRequest extends StringRequest {

    Map<String,String> parameters;

    public historyRequest(String cust_id, Response.Listener<String> listener,Response.ErrorListener errorListener){
        super(Method.POST,new Constants().HISTORY,listener,errorListener);

        parameters = new HashMap<>();

        parameters.put("cust_id",cust_id);
    }
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
