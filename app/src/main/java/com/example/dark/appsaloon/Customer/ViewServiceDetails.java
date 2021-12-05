package com.example.dark.appsaloon.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.Adapter.DisplayServicesAdapter;
import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SalonManager.DisplayServices;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.ManagerServicesRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewServiceDetails extends AppCompatActivity {

    ListView services_list;

    DisplayServicesAdapter adapter;
    ArrayList<ServiceModel> arrayList = new ArrayList<>();
    ServiceModel model;

    ArrayList<String> selectedservices = new ArrayList<>();

    Button selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_details);
        services_list = (ListView)findViewById(R.id.view_service_details_list);
        selected = (Button)findViewById(R.id.salon_selected_services);

        fetchservices();

        work();

    }

    private void work() {
        services_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox box = (CheckBox)view.findViewById(R.id.selected_services);
                if(!(box.isChecked())){
                    box.setChecked(true);}
                else{
                    box.setChecked(false);
                }

                TextView serv_id = (TextView)view.findViewById(R.id.service_id);
                String ID = serv_id.getText().toString();
                if(box.isChecked()) {
                    selectedservices.add(ID);
                }
                else{
                    selectedservices.remove(ID);
                }
            }
        });

        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedservices.size()>0){
                    Intent intent = new Intent(ViewServiceDetails.this,SelectedServices.class);
                    intent.putStringArrayListExtra("serv_id",selectedservices);
                    intent.putExtra("value","salon");
                    finish();
                    startActivity(intent);
                    }
                else{
                    Toast.makeText(ViewServiceDetails.this, "no services selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchservices() {

        final ProgressDialog progressDialog = new ProgressDialog(ViewServiceDetails.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Services");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final ManagerServicesRequest request = new ManagerServicesRequest(getIntent().getStringExtra("Manager"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Response services", response.toString());
                JSONObject object;
                int count = 0;
                try {
                    JSONArray array = new JSONArray(response);
                    while(count<array.length()){

                        object = array.getJSONObject(count);

                        model = new ServiceModel(object.getString("service_id"),object.getString("name"),object.getString("price"),object.getString("detail")
                                ,object.getString("image"),object.getString("categ"));
                        arrayList.add(model);
                        count++;
                    }
                    progressDialog.dismiss();
                    setServices(arrayList);
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley error",error.getMessage());
            }
        });
        RequestQueues.getInstance(ViewServiceDetails.this).addToRequestQue(request);
    }

    private void setServices(ArrayList<ServiceModel> arrayList) {

        this.arrayList = arrayList;
        adapter = new DisplayServicesAdapter(arrayList,ViewServiceDetails.this,"salon");
        services_list.setAdapter(adapter);
    }
}
