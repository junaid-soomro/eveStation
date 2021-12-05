package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;



public class LoginRequest extends StringRequest {

    private Map<String,String> parameters;

    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().LOGIN, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("username", email);
        parameters.put("password",password);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
