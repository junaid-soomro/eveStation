package com.example.dark.appsaloon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.LoginRequest;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.example.dark.appsaloon.Customer.UserDashboard;
import com.example.dark.appsaloon.SalonManager.SalonDashboard;
import com.example.dark.appsaloon.SessionManager.SessionManager;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    TextInputLayout email,password;

    Button reg_user;

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiliaze();
        checkSession();

        reg_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        login();

    }

    private boolean validation(String Name,String Pass){

        if(Name.equals(""))
        {
            email.setError("Enter a username");
            return false;

        }

        else if(Pass.equals(""))

        {

            password.setError("Enter a password");
            return false;

        }
        email.setErrorEnabled(false);
        password.setErrorEnabled(false);
        return true;
    }


    private void login() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EMAIL = email.getEditText().getText().toString();
                String PASS = password.getEditText().getText().toString();
                if(validation(EMAIL,PASS))
                {
                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Logging You In");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    LoginRequest loginRequest = new LoginRequest(EMAIL, PASS, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Login Response", response);
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {

                                    new SessionManager(Login.this,jsonObject.getString("ID"),jsonObject.getString("Name"),
                                            jsonObject.getString("Email"),jsonObject.getString("Phone"),jsonObject.getString("Address")
                                            ,jsonObject.getString("Username"),jsonObject.getString("Type"),jsonObject.getString("city"));


                                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();

                                    if(jsonObject.getString("Type").equals("manager")){

                                        startActivity(new Intent(Login.this,SalonDashboard.class));
                                        finish();
                                        return;
                                    }else if(jsonObject.getString("Type").equals("customer")){

                                        startActivity(new Intent(Login.this,UserDashboard.class));
                                        finish();
                                        return;
                                    }
                                    Toast.makeText(Login.this, jsonObject.getString("Type"), Toast.LENGTH_SHORT).show();

                                } else {
                                    if(jsonObject.getString("status").equals("INVALID"))
                                        Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                    else {

                                        Toast.makeText(Login.this, "Passwords dont match.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Login.this, "Bad Response From Server "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(Login.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(Login.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueues.getInstance(Login.this).addToRequestQue(loginRequest);
                }
            }
        });



    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

        private void checkSession() {

            if(new SessionManager(Login.this).CheckIfSessionExist()){

                if(new SessionManager(Login.this).getType().equals("manager")){

                    startActivity(new Intent(Login.this,SalonDashboard.class));
                    finish();
                }
                else if(new SessionManager(Login.this).getType().equals("customer")){

                    startActivity(new Intent(Login.this,UserDashboard.class));
                    finish();
                }


            }

        }


    private void initiliaze() {
        reg_user = (Button)findViewById(R.id.reg_user);
        email = (TextInputLayout)findViewById(R.id.email);
        password = (TextInputLayout)findViewById(R.id.password);
        login = (Button)findViewById(R.id.Login);
    }
}
