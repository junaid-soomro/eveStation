package com.example.dark.appsaloon.SalonManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.Customer.SearchSalon;
import com.example.dark.appsaloon.Customer.UserDashboard;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.RatingRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Rating extends AppCompatActivity {

    TextView name;
    RatingBar ratingBar;

    String id,rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        name = (TextView)findViewById(R.id.salon_rate_name);
        ratingBar = (RatingBar)findViewById(R.id.rate_salon);

        name.setText(getIntent().getStringExtra("salon_name"));
        id = getIntent().getStringExtra("salonId");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating =  String.valueOf(ratingBar.getRating());

                AlertDialog.Builder builder = new AlertDialog.Builder(Rating.this);
                builder.setTitle("Sure!");
                builder.setMessage("Give "+rating+" rating to the salon");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeRequest();
                    }
                });
                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   //do nothing
                    }
                });
                builder.show();
            }

        });

    }

    private void makeRequest() {

        final ProgressDialog progressDialog = new ProgressDialog(Rating.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Rating salon");
        progressDialog.setCancelable(true);
        progressDialog.show();

        RatingRequest request = new RatingRequest(id, rating,new SessionManager(Rating.this).getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
progressDialog.dismiss();
                Log.i("Response", response.toString());
                try {
                    if(new JSONObject(response).getBoolean("status")){
                        Toast.makeText(Rating.this, "Rated.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Rating.this,UserDashboard.class));
                    }else{
                        Toast.makeText(Rating.this, "Unable to rate salon.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.i("Exception", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley exception", error.getMessage());
            }
        });
        RequestQueues.getInstance(Rating.this).addToRequestQue(request);

    }
}
