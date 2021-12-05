package com.example.dark.appsaloon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.SalonManager.AddSalon;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.RegisterRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText email,name,password,username,address,phone;

    String EMAIL,NAME,PASS,USERNAME,ADDRESS,PHONE,TYPE,City;

    TextView register,cancel,select;

    RadioButton customer,manager;

    Spinner reg_city;

    ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initiliaze();
        fetchCities();
        RegisterUser();
    }

    private void setValues(ArrayList<String> arrayList) {
        this.cities = cities;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this,android.R.layout.simple_list_item_1,cities);
        reg_city.setAdapter(adapter);
    }

    private void fetchCities() {
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
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
        RequestQueues.getInstance(Register.this).addToRequestQue(request);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this,Login.class));
        finish();

    }

    private void RegisterUser() {

        reg_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                City = cities.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//do nothing
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NAME = name.getText().toString();
                EMAIL = email.getText().toString();
                PASS = password.getText().toString();
                USERNAME = username.getText().toString();
                ADDRESS = address.getText().toString();
                PHONE = phone.getText().toString();



                if(customer.isChecked()){TYPE="customer";}
                else{TYPE="manager";}
if(Validation(NAME,EMAIL,PASS,USERNAME,ADDRESS,PHONE)){
    registeruser();
}

            }
        });



    }

    private boolean Validation(String name, String email, String pass, String username, String address, String phone) {

        if(name.equals("") || email.equals("") || phone.equals("") || pass.equals("") || username.equals("") || address.equals("")){
            Toast.makeText(this, "One or more fields empty.", Toast.LENGTH_SHORT).show();
        return false;
        }

    return true;
    }

    private void registeruser() {

        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating your account");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RegisterRequest registerRequest = new RegisterRequest(EMAIL, NAME, PASS, TYPE, USERNAME,ADDRESS,PHONE,City, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);
                progressDialog.dismiss();
                try {
                    if (new JSONObject(response).getBoolean("success")) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Something Has Happened. Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Register.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(Register.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(Register.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(Register.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueues.getInstance(Register.this).addToRequestQue(registerRequest);

    }

    private void initiliaze() {

        reg_city = (Spinner)findViewById(R.id.reg_city);
        select = (TextView)findViewById(R.id.select_city);

        reg_city.setVisibility(View.INVISIBLE);
        select.setVisibility(View.INVISIBLE);

        cancel = (TextView)findViewById(R.id.cancel);
        register = (TextView)findViewById(R.id.Register);

        customer = (RadioButton)findViewById(R.id.customer);
        manager = (RadioButton)findViewById(R.id.manager);

        manager.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(manager.isChecked()){
                    reg_city.setVisibility(View.VISIBLE);
                    select.setVisibility(View.VISIBLE);
                }else{
                    reg_city.setVisibility(View.INVISIBLE);
                    select.setVisibility(View.INVISIBLE);
                }
            }
        });

        customer.setChecked(true);

        email = (EditText)findViewById(R.id.reg_email);
        name = (EditText)findViewById(R.id.reg_name);
        username = (EditText)findViewById(R.id.reg_username);
        password = (EditText)findViewById(R.id.reg_password);
        phone = (EditText)findViewById(R.id.reg_phone);
        address = (EditText)findViewById(R.id.reg_address);
    }
}
