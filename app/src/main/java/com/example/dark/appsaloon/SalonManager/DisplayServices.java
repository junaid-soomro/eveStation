package com.example.dark.appsaloon.SalonManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Adapter.DisplayServicesAdapter;
import com.example.dark.appsaloon.Adapter.FilterServicesAdapter;
import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.ManagerServicesRequest;
import com.example.dark.appsaloon.Volley.ServiceEditDeleteRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayServices extends AppCompatActivity {

    ArrayList<ServiceModel> arrayList = new ArrayList<>();
    DisplayServicesAdapter adapter;
    ServiceModel model;

    int service_id;

    Intent intent;

    ListView service_list;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_services);

        service_list = (ListView)findViewById(R.id.all_services_list);

        fetchservices();

        service_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                service_id = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayServices.this);
                builder.setTitle("What do you want to do?");

                builder.setPositiveButton("Delete Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        deleteService();
                    }
                });
                builder.setNegativeButton("Edit Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        intent = new Intent(DisplayServices.this,EditService.class);

                        intent.putExtra("service_id",arrayList.get(service_id).getService_id());
                        intent.putExtra("service_name",arrayList.get(service_id).getService_name());
                        intent.putExtra("service_price",arrayList.get(service_id).getService_price());
                        intent.putExtra("service_detail",arrayList.get(service_id).getService_detail());
                        intent.putExtra("service_image",arrayList.get(service_id).getService_image());
                        intent.putExtra("service_category",arrayList.get(service_id).getCategory());


                        startActivity(intent);
                    }
                });

                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

    }

    private void deleteService() {

        final ProgressDialog progressDialog = new ProgressDialog(DisplayServices.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Deleting selected service");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ServiceEditDeleteRequest request = new ServiceEditDeleteRequest(arrayList.get(service_id).getService_id(), "delete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Reponse",response.toString());

                try {

                    if(new JSONObject(response).getBoolean("status")){
                            progressDialog.dismiss();
                        Toast.makeText(DisplayServices.this, "Selected service deleted.", Toast.LENGTH_SHORT).show();
                        arrayList.remove(service_id);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(DisplayServices.this, "Unable to delete selected service.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.i("Exception",e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DisplayServices.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueues.getInstance(DisplayServices.this).addToRequestQue(request);

    }

    private void fetchservices() {

        final ProgressDialog progressDialog = new ProgressDialog(DisplayServices.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Services");
        progressDialog.setCancelable(false);
        progressDialog.show();


final ManagerServicesRequest request = new ManagerServicesRequest(new SessionManager(DisplayServices.this).getId(), new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {

                Log.i("Response services", response.toString());
                JSONObject object;
                int count = 0;
                try {
                    JSONArray array = new JSONArray(response);
                while(count<array.length()){

                        object = array.getJSONObject(count);

                        model = new ServiceModel(object.getString("service_id"),object.getString("name"),object.getString("price"),object.getString("detail")
                        ,object.getString("image"),object.getString("categ"));
                        arrayList.add(model);
                        count++;
                 }
                 progressDialog.dismiss();
                setServices(arrayList);
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley error",error.getMessage());
            }
        });
        RequestQueues.getInstance(DisplayServices.this).addToRequestQue(request);
    }

    private void setServices(ArrayList<ServiceModel> arrayList) {

    this.arrayList = arrayList;
    adapter = new DisplayServicesAdapter(arrayList,DisplayServices.this,"manager");
    service_list.setAdapter(adapter);
    }
}
