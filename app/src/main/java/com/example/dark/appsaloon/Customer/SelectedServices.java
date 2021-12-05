package com.example.dark.appsaloon.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.Adapter.selectedServicesadapter;
import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.ServiceRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectedServices extends AppCompatActivity {

    selectedServicesadapter adapter;
    ArrayList<String> arrayList = new ArrayList<>();

    ArrayList<ServiceModel> seletec_services = new ArrayList<>();
    ServiceModel model;

    ListView selected_services_list;
    Button book_selected_services;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_services);
        initiliaze();
        setArray();
        BookService();
    }



    private void BookService() {

        book_selected_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(SelectedServices.this);
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(seletec_services);
                prefsEditor.putString("array_services", json);
                prefsEditor.commit();
                
                if(seletec_services.size()>0){
                startActivity(new Intent(SelectedServices.this,book_service.class));
                finish();}else{
                    Toast.makeText(SelectedServices.this, "No services exist in the list.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setArray() {

        arrayList = getIntent().getStringArrayListExtra("serv_id");

        selected_services_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                seletec_services.remove(i);
                adapter.notifyDataSetChanged();
            }
        });


        final ProgressDialog progressDialog = new ProgressDialog(SelectedServices.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Selected services");
        progressDialog.setCancelable(true);
        progressDialog.show();

        final ServiceRequest request = new ServiceRequest(new SessionManager(SelectedServices.this).getId(), arrayList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    progressDialog.dismiss();

                    try {
                        Log.i("Response services", response.toString());
                        JSONArray array = new JSONArray(response);
                        JSONObject object;
                        int count = 0;
                        while (count<array.length()){

                            object = array.getJSONObject(count);

                            model = new ServiceModel(object.getString("service_id"),object.getString("name"),object.getString("price"),object.getString("detail"),
                                    object.getString("image"),object.getString("categ"));
                            seletec_services.add(model);
                            count++;
                        }
                        setValues(seletec_services);
                    }catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(SelectedServices.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SelectedServices.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueues.getInstance(SelectedServices.this).addToRequestQue(request);
    }

    private void setValues(ArrayList<ServiceModel> seletec_services) {

        this.seletec_services = seletec_services;
        adapter = new selectedServicesadapter(seletec_services,SelectedServices.this);
        selected_services_list.setAdapter(adapter);
    }

    private void initiliaze() {
        selected_services_list = (ListView)findViewById(R.id.selected_service_list);
        book_selected_services = (Button)findViewById(R.id.book_service);
    }

    @Override
    public void onBackPressed() {
       if(getIntent().getStringExtra("value").equals("salon")){
           finish();
           startActivity(new Intent(SelectedServices.this, SearchSalon.class));
       }else if(getIntent().getStringExtra("value").equals("search")) {
           finish();
           startActivity(new Intent(SelectedServices.this, SearchServices.class));
       }
       }
}
