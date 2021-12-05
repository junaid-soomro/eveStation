package com.example.dark.appsaloon.Customer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.Adapter.AppointmentsAdapter;
import com.example.dark.appsaloon.Adapter.UserAppointmentsAdapter;
import com.example.dark.appsaloon.Models.AppointmentsModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.historyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewServices extends AppCompatActivity {

    ListView history;
     AppointmentsModel model;
    UserAppointmentsAdapter adapter;

    ArrayList<AppointmentsModel> appointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);
        history = findViewById(R.id.hisory_list);
        fetchdata();
    }

    private void fetchdata() {
        final ProgressDialog progressDialog = new ProgressDialog(ViewServices.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching History");
        progressDialog.setCancelable(true);
        progressDialog.show();
        historyRequest request = new historyRequest(new SessionManager(ViewServices.this).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject object;
                int count = 0;

                try {
                    JSONArray array = new JSONArray(response);
                    while (count<array.length()){

                        object = array.getJSONObject(count);
                        model = new AppointmentsModel(object.getString("appoint_id"),object.getString("serv_name"),
                                object.getString("appoint_status"),object.getString("date"),object.getString("time"),
                                object.getString("payment_status")
                                ,object.getString("price"));
                        appointments.add(model);
                        count++;
                    }
                        setAppoints(appointments);
                        Toast.makeText(ViewServices.this, "History Fetched.", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(ViewServices.this, "Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ViewServices.this, "volely error"+error.getMessage()  , Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueues.getInstance(ViewServices.this).addToRequestQue(request);

    }
    private void setAppoints(ArrayList<AppointmentsModel> appointments) {

        this.appointments = appointments;
        adapter = new UserAppointmentsAdapter(ViewServices.this,appointments);


        history.setAdapter(adapter);


    }
}
