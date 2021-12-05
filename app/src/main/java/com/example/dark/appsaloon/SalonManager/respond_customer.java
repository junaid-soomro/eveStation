package com.example.dark.appsaloon.SalonManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Adapter.AppointmentsAdapter;
import com.example.dark.appsaloon.Adapter.service_respond_customerAdapter;
import com.example.dark.appsaloon.Models.AppointmentsModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.ServiceRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class respond_customer extends AppCompatActivity{

    ListView all_services_list;

    AppointmentsModel model;
    service_respond_customerAdapter adapter;

    String appoint_status;

    ArrayList<AppointmentsModel> arrayList = new ArrayList<>();

    String latitude,longitude,customer_id;

    Button show_location,respond_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_customer);
        initiliaze();

        show_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%s,%s", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        fetchCustomerServices();

        service_remove_listener();

        respondToCustomer();

    }

    private void respondToCustomer() {

        respond_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(arrayList.size()==0){
    Toast.makeText(respond_customer.this, "No services to respond.", Toast.LENGTH_SHORT).show();
    return;
}
                AlertDialog.Builder builder = new AlertDialog.Builder(com.example.dark.appsaloon.SalonManager.respond_customer.this);
                builder.setTitle("Choose action");

                builder.setPositiveButton("Accept Appointment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            appoint_status = "accepted";
                            sendRequest(appoint_status);
                     }
                });
                builder.setNegativeButton("Reject Appointment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        appoint_status = "rejected";
                        sendRequest(appoint_status);
                    }
                });
                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing.
                    }
                });
                builder.show();

            }
        });


    }

    private void sendRequest(String appoint_status) {

        if(arrayList.size()==0){

            Toast.makeText(this, "Can not respond no services are present in the list.", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(respond_customer.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Responding to appointment");
        progressDialog.setCancelable(false);
        progressDialog.show();




        ServiceRequest request = new ServiceRequest(customer_id, arrayList, appoint_status,new SessionManager(com.example.dark.appsaloon.SalonManager.respond_customer.this).getId(),new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                try{
                    progressDialog.dismiss();
                    if(new JSONObject(response).getBoolean("status")){
                        progressDialog.dismiss();
                        Toast.makeText(respond_customer.this, "Responded to appointment", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(com.example.dark.appsaloon.SalonManager.respond_customer.this,SalonDashboard.class));
                    }else{

                        Toast.makeText(respond_customer.this, "Responding to appointment failed.", Toast.LENGTH_SHORT).show();

                    }

                }
                catch(Exception e){
                    progressDialog.dismiss();
                    Log.i("Exception", e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(respond_customer.this, "Volley erro"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueues.getInstance(com.example.dark.appsaloon.SalonManager.respond_customer.this).addToRequestQue(request);
    }

    private void service_remove_listener() {

        all_services_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int value, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(com.example.dark.appsaloon.SalonManager.respond_customer.this);
                builder.setTitle("Choose action");

                builder.setPositiveButton("Dismiss service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        arrayList.remove(value);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing.
                    }
                });
           builder.show();
            }
        });

    }

    private void fetchCustomerServices() {
        final ProgressDialog progressDialog = new ProgressDialog(respond_customer.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching ordered services");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ServiceRequest request = new ServiceRequest(customer_id,new SessionManager(com.example.dark.appsaloon.SalonManager.respond_customer.this).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    int count = 0;
                    JSONObject object;

                    JSONArray array = new JSONArray(response);
                    while(count<array.length()){

                        object   = array.getJSONObject(count);
                        model = new AppointmentsModel(object.getString("service_id"),object.getString("service_name"));
                        arrayList.add(model);
                        count++;
                    }
                    setServices(arrayList);
                    progressDialog.dismiss();

                }catch (Exception e){
                    progressDialog.dismiss();
                    Log.i("exception error", e.getMessage().toString());
                    Toast.makeText(com.example.dark.appsaloon.SalonManager.respond_customer.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("Volley error", error.getMessage());
            }
        });

        RequestQueues.getInstance(com.example.dark.appsaloon.SalonManager.respond_customer.this).addToRequestQue(request);

    }

    private void setServices(ArrayList<AppointmentsModel> arrayList) {

        this.arrayList = arrayList;
        adapter = new service_respond_customerAdapter(com.example.dark.appsaloon.SalonManager.respond_customer.this,this.arrayList);
        all_services_list.setAdapter(adapter);

    }

    private void initiliaze() {
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        customer_id = getIntent().getStringExtra("customer_id");

        all_services_list = (ListView)findViewById(R.id.all_services_list);

        show_location = (Button)findViewById(R.id.show_location);
        respond_customer = (Button)findViewById(R.id.respond);
    }


}
