package com.example.dark.appsaloon.SalonManager;

import com.example.dark.appsaloon.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.SyncStateContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.AddSalonRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddSalon extends AppCompatActivity {

    Place place;

    String latitude,longitude, salon_name,city;

    TextInputLayout salonName;

    Button submit_salon,pick_location;

    int PLACE_PICKER_REQUEST = 1;

    TextView selected_address;

    Boolean check= false;

    Spinner city_spinner;

    ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon);
        initiliaze();
        fetchCity();
        pick_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(AddSalon.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        submit_salon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addSaloon();

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            city = cities.get(i);
                Toast.makeText(AddSalon.this, "City: "+city, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//do nothing
            }
        });


    }

    private void fetchCity() {

    city_spinner = (Spinner)findViewById(R.id.spinner);
        final ProgressDialog progressDialog = new ProgressDialog(AddSalon.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching cities");
        progressDialog.setCancelable(false);
        progressDialog.show();


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().fetch_city, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
progressDialog.dismiss();

                int count = 0;
                JSONObject object;

                while (count<response.length()){
                    try {
                        object = response.getJSONObject(count);
                        cities.add(object.getString("city_name"));
                        count++;
                    } catch (JSONException e) {
                        Log.i("Exception", e.getMessage());
                    }
                }
                setValues(cities);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley exception", error.getMessage());
            }
        });
        RequestQueues.getInstance(AddSalon.this).addToRequestQue(request);

    }


    private void setValues(ArrayList<String> arrayList) {
    this.cities = cities;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSalon.this,android.R.layout.simple_list_item_1,cities);
    city_spinner.setAdapter(adapter);
    }


    private void addSaloon() {

        salon_name = salonName.getEditText().getText().toString();
        if(salon_name.equals("")){
            Toast.makeText(this, "Salon name empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(check==false){
            Toast.makeText(this, "pick lcation first.", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(AddSalon.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Adding Salon");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AddSalonRequest request = new AddSalonRequest(salon_name, latitude, longitude,city,new SessionManager(AddSalon.this).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response add salon", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("status")){
                        progressDialog.dismiss();
                        Toast.makeText(AddSalon.this, "Saloon added succesfully.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(AddSalon.this,SalonDashboard.class));
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(AddSalon.this, "Unable to add saloon", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("volley error", error.getMessage());
            }
        });
        RequestQueues.getInstance(AddSalon.this).addToRequestQue(request);

        selected_address.setText("");
    }

@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PLACE_PICKER_REQUEST) {

                    place = PlacePicker.getPlace(this, data);


                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);

                        selected_address.setText(place.getAddress().toString());

                        check = true;
                    }
        }



    private void initiliaze() {

        submit_salon = (Button)findViewById(R.id.add_location);
        salonName = (TextInputLayout)findViewById(R.id.salon_name);
        selected_address = (TextView)findViewById(R.id.show_selected_address);
        pick_location = (Button)findViewById(R.id.pick_location);

    }


}
