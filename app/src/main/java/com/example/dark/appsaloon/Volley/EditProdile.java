package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 21-Jan-18.
 */

public class EditProdile extends StringRequest {

    private Map<String,String> parameters;

    public EditProdile(String id, String name, String email, String password, String new_password, String phone,String address,String username, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().EDIT_PROFILE, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("ID",id);
        parameters.put("Name",name);
        parameters.put("Email",email);
        parameters.put("password",password);
        parameters.put("new_password",new_password);
        parameters.put("address",address);
        parameters.put("phone",phone);
        parameters.put("username",username);
    }

    public EditProdile(String id, String name, String email,String phone,String address,String username, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, new Constants().EDIT_PROFILE, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("ID",id);
        parameters.put("Name",name);
        parameters.put("Email",email);
        parameters.put("phone",phone);
        parameters.put("address",address);
        parameters.put("username",username);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
