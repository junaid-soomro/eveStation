package com.example.dark.appsaloon.Customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.PaymentRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    public static final String PAYPAL_CLIENT_ID = "AbCo3ryKuececWzRgrLMnP7V0n0oyuzVhKyPH-_QroNdRrDVgRdgDHvfSGtYiO18UJdO8L-xZ37dwoy6";

    private Button paypal,cash;
    TextView amount;

    String date,time,lat,lon;

    Double price =0.0;

    ArrayList<ServiceModel> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initialize();
        fetchData();


        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
    }

    private void fetchData() {

        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lon");

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("array_services", "");
        Type type = new TypeToken<ArrayList<ServiceModel>>(){}.getType();
        arrayList = gson.fromJson(json,type);

        for (int i=0;i<arrayList.size();i++) {

            price +=  Double.parseDouble(arrayList.get(i).getService_price());

        }

        amount.setText(String.valueOf(price));

        //adding values to database

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(Payment.this);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Booking appointment");
                progressDialog.setCancelable(true);
                progressDialog.show();
                PaymentRequest request = new PaymentRequest(new SessionManager(Payment.this).getId(), arrayList, date, time, "cash", price, lat, lon,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("Response", response.toString());
                                try {
                                    progressDialog.dismiss();
                                    if(new JSONObject(response).getBoolean("status")){

                                        Toast.makeText(Payment.this, "Appointment submitted.", Toast.LENGTH_SHORT).show();
                                    }else{

                                        Toast.makeText(Payment.this, "Appintment submission failed.", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Payment.this, "Exception occured", Toast.LENGTH_SHORT).show();
                                    Log.i("Exception", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Payment.this, "Volley error", Toast.LENGTH_SHORT).show();
                        Log.i("Volley error", error.getMessage());
                    }
                });
                RequestQueues.getInstance(Payment.this).addToRequestQue(request);

                finish();
                startActivity(new Intent(Payment.this,UserDashboard.class));
            }
        });

    }

    public void paypalrequest(){

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(Payment.this);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Booking appointment");
                progressDialog.setCancelable(true);
                progressDialog.show();
                PaymentRequest request = new PaymentRequest(new SessionManager(Payment.this).getId(), arrayList, date, time, "paypal", price, lat, lon,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("Response", response.toString());
                                try {
                                    progressDialog.dismiss();
                                    if(new JSONObject(response).getBoolean("status")){

                                        Toast.makeText(Payment.this, "Appointment submitted.", Toast.LENGTH_SHORT).show();
                                    }else{

                                        Toast.makeText(Payment.this, "Appintment submission failed.", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Payment.this, "Exception occured", Toast.LENGTH_SHORT).show();
                                    Log.i("Exception", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Payment.this, "Volley error", Toast.LENGTH_SHORT).show();
                        Log.i("Volley error", error.getMessage());
                    }
                });
                RequestQueues.getInstance(Payment.this).addToRequestQue(request);

                finish();
                startActivity(new Intent(Payment.this,UserDashboard.class));
            }
        });



    }

    private void initialize() {

        paypal = (Button)findViewById(R.id.paypal);
        cash = (Button)findViewById(R.id.cash);

        amount = (TextView)findViewById(R.id.amount);

    }


    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    if (confirm != null) {
                        try {

                            JSONObject jsonObject = new JSONObject(confirm.toJSONObject().toString(4));
                            JSONObject response = new JSONObject(jsonObject.getString("response"));

                            Toast.makeText(this, "Payment Successful transction Id:-" + response.getString("id"), Toast.LENGTH_SHORT).show();
                            paypalrequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    private void pay() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.00"), "USD", "sample item",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);

    }

}
