package com.example.dark.appsaloon.SalonManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.ServiceRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AddService extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageReference;

    private Button add_service;

    private ImageView image;

    Bitmap product_picture;

    TextInputLayout service_name,service_price,service_detail;

    String SERV_NAME,SERV_PRICE,SERV_DETAIL,SERV_CATEGORY;

    Uri filepath;

    Spinner category;

    ArrayList<String> category_list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        initiliaze();
        fetchCategory();
        workFunction();
    }

    private void setValues(ArrayList<String> arrayList) {
        this.category_list = category_list;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddService.this,android.R.layout.simple_list_item_1,category_list);
        category.setAdapter(adapter);
    }

    private void fetchCategory() {

        final ProgressDialog progressDialog = new ProgressDialog(AddService.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching categories");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().fetch_category, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();

                int count = 0;
                JSONObject object;

                while (count<response.length()){
                    try {
                        object = response.getJSONObject(count);
                        category_list.add(object.getString("category_name"));
                        count++;
                    } catch (JSONException e) {
                        Log.i("Exception", e.getMessage());
                    }
                }
                setValues(category_list);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley exception", error.getMessage());
            }
        });
        RequestQueues.getInstance(AddService.this).addToRequestQue(request);

    }

    private void workFunction() {

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SERV_CATEGORY = category_list.get(i);
                Toast.makeText(AddService.this, "Category: "+SERV_CATEGORY, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//do nothing
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SERV_NAME = service_name.getEditText().getText().toString();
                SERV_PRICE = service_price.getEditText().getText().toString();
                SERV_DETAIL = service_detail.getEditText().getText().toString();

                if(Validation(SERV_NAME,SERV_PRICE,SERV_DETAIL)){
                    uploadRequest();
                }

            }
        });

    }

    private boolean Validation(String serv_name, String serv_price, String serv_detail) {

        if(serv_name.equals("") || serv_price.equals("") || serv_detail.equals("")){
            Toast.makeText(this, "One or more fields empty", Toast.LENGTH_SHORT).show();
            return false;
        }

    return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = AddService.this.getContentResolver().openInputStream(imageUri);//creating an imputstrea
                product_picture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                image.setImageBitmap(product_picture);
                filepath = imageUri;


            } catch (FileNotFoundException e) {
                Toast.makeText(AddService.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void uploadRequest() {

        final ProgressDialog progressDialog = new ProgressDialog(AddService.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Adding Service");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(filepath!=null){

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            StorageReference ref = storageReference.child("service_images/" + UUID.randomUUID().toString());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getDownloadUrl().toString();
                    ServiceRequest request = new ServiceRequest(SERV_NAME, SERV_PRICE, SERV_DETAIL, url,SERV_CATEGORY,new SessionManager(AddService.this).getId(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("Response servie", response.toString());
                                progressDialog.dismiss();
                                JSONObject object = new JSONObject(response);

                                if (object.getBoolean("status")) {

                                    Toast.makeText(AddService.this, "Service added.", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(AddService.this,SalonDashboard.class));
                                    finish();
                                } else {
                                   Toast.makeText(AddService.this, "Some error", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Log.i("Exception", e.getMessage().toString());
                                Toast.makeText(AddService.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof ServerError)
                                Toast.makeText(AddService.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(AddService.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(AddService.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddService.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    RequestQueues.getInstance(AddService.this).addToRequestQue(request);


                    Toast.makeText(AddService.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddService.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            });



        }else{
            progressDialog.dismiss();
            Toast.makeText(AddService.this, "Please select an image.", Toast.LENGTH_SHORT).show();

        }



    }

    private void initiliaze() {

        service_name = (TextInputLayout)findViewById(R.id.edit_service_name);
        service_detail = (TextInputLayout)findViewById(R.id.edit_service_detail);
        service_price = (TextInputLayout)findViewById(R.id.edit_service_price);

        add_service = (Button)findViewById(R.id.Update_Service);

        category = (Spinner)findViewById(R.id.category_spinner);

        image = (ImageView)findViewById(R.id.edit_service_image);
    }
}
