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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Constants;
import com.example.dark.appsaloon.EditProfile;
import com.example.dark.appsaloon.Login;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.Register;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.CityChangeRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SalonDashboard extends AppCompatActivity{

    ImageView settings,logout;

    TextView manager_name;

    Button add_service,appointment,service_details,change_city;

    ArrayAdapter<String> adapter;

    ArrayList<String> cities = new ArrayList<>();

    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fetchCities();
        initialize();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalonDashboard.this,EditProfile.class));
            }
        });

        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalonDashboard.this,AddSalon.class));
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SalonDashboard.this,ViewAppointments.class));
            }
        });
        service_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SalonDashboard.this);
                builder.setTitle("What do you want to do?");

                builder.setPositiveButton("Add Service(s)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(SalonDashboard.this,AddService.class));
                    }
                });
                builder.setNegativeButton("Delete/Edit Service(s)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(SalonDashboard.this,DisplayServices.class));
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SessionManager(SalonDashboard.this).Logout();
                startActivity(new Intent(SalonDashboard.this, Login.class));
                finish();
            }
        });
    }
    private void setValues(ArrayList<String> arrayList) {
        this.cities = cities;
        adapter = new ArrayAdapter<String>(SalonDashboard.this,android.R.layout.simple_list_item_1,cities);

    }

    private void fetchCities() {
        final ProgressDialog progressDialog = new ProgressDialog(SalonDashboard.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching cities");
        progressDialog.setCancelable(false);
        progressDialog.show();


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().fetch_city, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                Log.i("City response", response.toString());

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
        RequestQueues.getInstance(SalonDashboard.this).addToRequestQue(request);

    }

    private void initialize() {

        settings = (ImageView)findViewById(R.id.settings_manager);

        manager_name = (TextView)findViewById(R.id.manager_name);
        manager_name.setText(new SessionManager(SalonDashboard.this).getUsername());

        add_service = (Button)findViewById(R.id.add_salon);
        add_service.setVisibility(View.INVISIBLE);
        appointment = (Button)findViewById(R.id.appointments);
        service_details = (Button)findViewById(R.id.service_details);

        change_city = (Button)findViewById(R.id.change_city);

        logout = (ImageView)findViewById(R.id.logout_manager);

        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SalonDashboard.this);
                builder.setTitle("Choose city.");
                final Spinner spinner = new Spinner(SalonDashboard.this);
               spinner.setAdapter(adapter);
               spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       city = adapterView.getItemAtPosition(i).toString();
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> adapterView) {
//do nothing
                   }
               });

                builder.setView(spinner);
                builder.setPositiveButton("COnfirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SalonDashboard.this, "Selected"+city, Toast.LENGTH_SHORT).show();

                        final ProgressDialog progressDialog = new ProgressDialog(SalonDashboard.this);
                        progressDialog.setTitle("Please Wait");
                        progressDialog.setMessage("Changing city");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        CityChangeRequest request = new CityChangeRequest(city, new SessionManager(SalonDashboard.this).getId(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try{
                                    if(new JSONObject(response).getBoolean("status")){
                                        Toast.makeText(SalonDashboard.this, "City updated", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SalonDashboard.this, "City update failed.", Toast.LENGTH_SHORT).show();
                                    }

                                }catch (Exception e){
                                    Log.i("Exception", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.i("Volely exception", error.getMessage());
                            }
                        });
                        RequestQueues.getInstance(SalonDashboard.this).addToRequestQue(request);



                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   // do nothing.
                    }
                });
                builder.show();

            }
        });
    }
}



