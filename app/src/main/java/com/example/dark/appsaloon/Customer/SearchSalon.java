package com.example.dark.appsaloon.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Adapter.salonViewAdapter;
import com.example.dark.appsaloon.Models.SalonModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.SalonManager.respond_customer;
import com.example.dark.appsaloon.Singletons.RequestQueues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SearchSalon extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView salon_list;
    Spinner city_spinner;

    ArrayList<String> city_names = new ArrayList<>();
    ArrayAdapter city_adapter;

    ArrayList<SalonModel> arrayList = new ArrayList<>();
    salonViewAdapter adapter;

    SalonModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_salon);
        initiliaze();
        fetchValues();
        fetchCities();
    }

    private void fetchCities() {
        final ProgressDialog progressDialog = new ProgressDialog(SearchSalon.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Cities");
        progressDialog.setCancelable(true);
        progressDialog.show();
        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.POST, new Constants().fetch_city, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
progressDialog.dismiss();
                JSONObject object;
                int count = 0;
                while (count<response.length()) {

                    try {
                        object = response.getJSONObject(count);

                        city_names.add(object.getString("city_name"));
                        count++;

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchSalon.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setCities(city_names);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
                if(error instanceof ServerError){

                    Toast.makeText(SearchSalon.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                if(error instanceof TimeoutError){
                    Toast.makeText(SearchSalon.this, "Time out Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueues.getInstance(SearchSalon.this).addToRequestQue(request1);
    }

    private void fetchValues() {
        final ProgressDialog progressDialog = new ProgressDialog(SearchSalon.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching salons");
        progressDialog.setCancelable(true);
        progressDialog.show();
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().add_fetch_salon, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Response", response.toString());
                JSONObject object;
                int count = 0;
                progressDialog.dismiss();
                while (count<response.length()){

                    try {

                        object = response.getJSONObject(count);

                        model = new SalonModel(object.getString("salon_id"),object.getString("salon_name"),object.getString("lat"),
                                object.getString("lon"),object.getString("city"),object.getString("rate"),object.getString("manager_id"));
                        arrayList.add(model);
                        count++;

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchSalon.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            setValues(arrayList);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Log.i("Volley exception", error.getLocalizedMessage());
                if(error instanceof ServerError){

                    Toast.makeText(SearchSalon.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                if(error instanceof TimeoutError){
                    Toast.makeText(SearchSalon.this, "Time out Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueues.getInstance(SearchSalon.this).addToRequestQue(request);

    }


    private void setValues(ArrayList<SalonModel> arrayList) {
    this.arrayList = arrayList;
    }

    private void setCities(ArrayList<String> arrayList){
        city_adapter = new ArrayAdapter(SearchSalon.this,android.R.layout.simple_list_item_1,arrayList);
        city_spinner.setAdapter(city_adapter);
    }

    private void initiliaze() {
        salon_list = (ListView)findViewById(R.id.salon_list);
        city_spinner = (Spinner)findViewById(R.id.city_spinner);
        city_spinner.setOnItemSelectedListener(SearchSalon.this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            String city = city_spinner.getItemAtPosition(i).toString();
        Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
            adapter = new salonViewAdapter(arrayList,SearchSalon.this);
            adapter.getFilter().filter(city);
            salon_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //do nothing.
    }
}
