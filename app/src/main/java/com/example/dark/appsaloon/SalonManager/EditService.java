package com.example.dark.appsaloon.SalonManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Constants;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.ServiceEditDeleteRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditService extends AppCompatActivity {

    Bitmap profilePicture;

    Uri filepath;

    FirebaseStorage storage;
    StorageReference storageReference;


    TextInputLayout service_name,service_price,service_detail;
    ImageView service_image;

    String ID,NAME,PRICE,DETAIL,CATEG;

    Button update_service;

    TextView current_categ;

    Spinner category_spinner;

    ArrayList<String> category_list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        initiliaze();
        fetchCategory();
        setValues();
        updateService();

    }
    private void fetchCategory() {

        final ProgressDialog progressDialog = new ProgressDialog(EditService.this);
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
        RequestQueues.getInstance(EditService.this).addToRequestQue(request);

    }

    private void setValues(ArrayList<String> arrayList) {
        this.category_list = category_list;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditService.this,android.R.layout.simple_list_item_1,category_list);
        category_spinner.setAdapter(adapter);
    }

    private void updateService() {

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CATEG = category_list.get(i);
                Toast.makeText(EditService.this, "Category: "+CATEG, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//do nothing
            }
        });

        service_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        update_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ID = getIntent().getStringExtra("service_id");
                NAME = service_name.getEditText().getText().toString();
                PRICE = service_price.getEditText().getText().toString();
                DETAIL = service_detail.getEditText().getText().toString();

                if(Validation(NAME,PRICE,DETAIL)){
                    UploadService();
                }


            }
        });

    }

    private boolean Validation(String name, String price, String detail) {
    if(name.equals("") || price.equals("") || detail.equals("")){
        Toast.makeText(this, "One or more fields empty", Toast.LENGTH_SHORT).show();
        return false;
    }

    return true;
    }

    private void UploadService() {

        final ProgressDialog progressDialog = new ProgressDialog(EditService.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Editing Service");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if(filepath!=null){

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference().getRoot();

            StorageReference ref = storageReference.child("product_images/");
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String url = taskSnapshot.getDownloadUrl().toString();
                    ServiceEditDeleteRequest request = new ServiceEditDeleteRequest(ID, NAME, PRICE, DETAIL, url,CATEG, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Response", response.toString());
                            try {
                                if(new JSONObject(response).getBoolean("status")){
                                    progressDialog.dismiss();
                                    Toast.makeText(EditService.this, "Service Updated.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditService.this,SalonDashboard.class));
                                    finish();

                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(EditService.this, "No changes made.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditService.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueues.getInstance(EditService.this).addToRequestQue(request);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditService.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else if(filepath==null){

            ServiceEditDeleteRequest request = new ServiceEditDeleteRequest(ID, NAME, PRICE, DETAIL,CATEG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Response", response.toString());

                    try {
                        if(new JSONObject(response).getBoolean("status")){

                            Toast.makeText(EditService.this, "Service Updated.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditService.this,SalonDashboard.class));
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(EditService.this, "No changes made.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditService.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueues.getInstance(EditService.this).addToRequestQue(request);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getContentResolver().openInputStream(imageUri);//creating an imputstrea
                profilePicture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                service_image.setImageBitmap(profilePicture);
                filepath = imageUri;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setValues() {

        Picasso.with(EditService.this).load(getIntent().getStringExtra("service_image")).into(service_image);

        service_name.getEditText().setText(getIntent().getStringExtra("service_name"));
        service_price.getEditText().setText(getIntent().getStringExtra("service_price"));
        service_detail.getEditText().setText(getIntent().getStringExtra("service_detail"));
        current_categ.setText(getIntent().getStringExtra("service_category"));


    }

    private void initiliaze() {

        current_categ = (TextView)findViewById(R.id.edit_category_name);
        category_spinner = (Spinner)findViewById(R.id.spinner2);

        service_name = (TextInputLayout)findViewById(R.id.edit_service_name);
        service_detail = (TextInputLayout)findViewById(R.id.edit_service_detail);
        service_price = (TextInputLayout)findViewById(R.id.edit_service_price);

        service_image = (ImageView)findViewById(R.id.edit_service_image);

        update_service = (Button)findViewById(R.id.Update_Service);
    }
}
