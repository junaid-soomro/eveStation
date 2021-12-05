package com.example.dark.appsaloon.Volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.example.dark.appsaloon.*;
import com.android.volley.toolbox.StringRequest;
import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.SessionManager.SessionManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abd on 08-Mar-18.
 */

public class PaymentRequest extends StringRequest {

    private Map<String,String> parameters;
    ArrayList<String> arrayList = new ArrayList<>();

    public PaymentRequest(String customer_id, ArrayList<ServiceModel> server_id, String date, String time, String payment_status, Double price, String lat, String lon,
                          Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,new Constants().customer_payment,listener,errorListener);

        parameters = new HashMap<>();

        for(int i=0;i<server_id.size();i++){

            this.arrayList.add(server_id.get(i).getService_id());

        }

        JSONArray array = new JSONArray(this.arrayList);

        parameters.put("customer_id",customer_id);
        parameters.put("service_id",array.toString());
        parameters.put("date",date);
        parameters.put("time",time);
        parameters.put("payment_status",payment_status);
        parameters.put("price",String.valueOf(price));
        parameters.put("lat",lat);
        parameters.put("lon",lon);

    }
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
