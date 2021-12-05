package com.example.dark.appsaloon.SalonManager;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Adapter.AppointmentsAdapter;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Models.AppointmentsModel;
import com.example.dark.appsaloon.Volley.AppointmentsRequest;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class ViewAppointments extends AppCompatActivity {

    ListView appointments_list;

    AppointmentsAdapter adapter;

    ArrayList<AppointmentsModel> appointments = new ArrayList<>();

    AppointmentsModel models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);
        appointments_list = (ListView)findViewById(R.id.appointment_list);
        fetchappointment();
        listener();
    }

    private void listener() {

        appointments_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final TextView user_id = view.findViewById(R.id.user_id);
                TextView view_details = view.findViewById(R.id.view_details);
                final TextView lat = view.findViewById(R.id.lat);
                final TextView lon = view.findViewById(R.id.lon);

                view_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ViewAppointments.this,respond_customer.class);
                        intent.putExtra("customer_id",user_id.getText().toString());
                        intent.putExtra("latitude",lat.getText().toString());
                        intent.putExtra("longitude",lon.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private void fetchappointment() {

        final ProgressDialog progressDialog = new ProgressDialog(ViewAppointments.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching appointments");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final AppointmentsRequest request = new AppointmentsRequest(new SessionManager(ViewAppointments.this).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int count = 0;
                JSONObject object;

                try{
                    JSONArray array = new JSONArray(response);
                    while(count<array.length()){

                        object   = array.getJSONObject(count);
                        models = new AppointmentsModel(object.getString("appoint_id"),object.getString("customer_name"),object.getString("user_id"),
                                object.getString("appoint_status"),object.getString("latitude"),object.getString("longitude"),
                                object.getString("payment_status")
                                ,object.getString("price"),object.getString("time"),object.getString("date"));
                        appointments.add(models);
                        count++;
                    }
                    setAppoints(appointments);
                    progressDialog.dismiss();

                }catch (Exception e){
                    progressDialog.dismiss();
                    Log.i("Volley error", e.getMessage().toString());
                    Toast.makeText(ViewAppointments.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(ViewAppointments.this, "Volley "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueues.getInstance(ViewAppointments.this).addToRequestQue(request);


    }

    private void setAppoints(ArrayList<AppointmentsModel> appointments) {

            this.appointments = appointments;
            adapter = new AppointmentsAdapter(ViewAppointments.this,appointments);
            appointments_list.setAdapter(adapter);

    }
}
