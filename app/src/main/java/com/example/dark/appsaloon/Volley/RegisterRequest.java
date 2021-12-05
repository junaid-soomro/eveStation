package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 17-Jan-18.
 */

public class RegisterRequest extends StringRequest {

    private Map<String, String> parameters;
    public RegisterRequest(String email, String name, String password, String type, String username,String address,String phone,String city,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().REGISTER, listener, errorListener);
        parameters = new HashMap<>();

        parameters.put("email", email);
        parameters.put("name", name);
        parameters.put("password", password);
        parameters.put("address",address);
        parameters.put("user_type", type);
        parameters.put("username", username);
        parameters.put("phone", phone);
        parameters.put("city",city);

            }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
