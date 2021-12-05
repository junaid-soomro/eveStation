package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 13-Apr-18.
 */

public class RatingRequest extends StringRequest {

    Map<String,String> parameters;

        public RatingRequest(String salon_id,String rate,String user_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, new Constants().rating, listener, errorListener);

        parameters = new HashMap<>();

        parameters.put("id",salon_id);
        parameters.put("rate",rate);
        parameters.put("user",user_id);

    }
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
