package com.example.dark.appsaloon.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Adapter.FilterServicesAdapter;
import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.Singletons.RequestQueues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchServices extends AppCompatActivity {

    ArrayList<ServiceModel> arrayList = new ArrayList<>();
    ArrayList<ServiceModel> tempArrayList = new ArrayList<>();
    FilterServicesAdapter adapter;

    ArrayAdapter<String> autodcompleteadapter;
    ArrayList<String> services_names = new ArrayList<>();

    ServiceModel model;

    ArrayList<String> selectedservices = new ArrayList<>();

    AutoCompleteTextView search_query;
    ListView services_list;

    Button show_selected_services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);
        initialize();
        fetchValues();
        searchBookService();

    }

    private void searchBookService() {
         TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s){

            }
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){
                // you can check for enter key here
            }
            public void  onTextChanged (CharSequence s, int start, int before,int count) {

                adapter = new FilterServicesAdapter(arrayList,SearchServices.this);
                adapter.getFilter().filter(s.toString());
                services_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
        };
        search_query.addTextChangedListener(tw);

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

        show_selected_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedservices.size()>0){
                Intent intent = new Intent(SearchServices.this,SelectedServices.class);
                intent.putStringArrayListExtra("serv_id",selectedservices);
                intent.putExtra("value","search");
                finish();
                startActivity(intent);
                ;}
                else{
                    Toast.makeText(SearchServices.this, "no services selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchValues() {
        final ProgressDialog progressDialog = new ProgressDialog(SearchServices.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Services");
        progressDialog.setCancelable(true);
        progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, new Constants().add_fetch_service, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();


                JSONObject object;
                int count =0;

                while (count<response.length()){

                    try {
                        object = response.getJSONObject(count);
                        model = new ServiceModel(object.getString("service_id"),object.getString("name"),object.getString("price"),object.getString("detail"),
                                object.getString("image"),object.getString("categ"),object.getString("manager"));
                        arrayList.add(model);
                    services_names.add(object.getString("name"));
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        Toast.makeText(SearchServices.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    count++;
                }
            setvalues(arrayList,services_names);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchServices.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueues.getInstance(SearchServices.this).addToRequestQue(request);
    }

    private void setvalues(ArrayList<ServiceModel> arrayList, ArrayList<String> services_names) {

        this.arrayList = arrayList;


        this.services_names = services_names;

        autodcompleteadapter = new ArrayAdapter<String>(SearchServices.this,android.R.layout.simple_list_item_1,services_names);
        search_query.setThreshold(1);
        search_query.setAdapter(autodcompleteadapter);

    }

    private void initialize() {

        show_selected_services = (Button)findViewById(R.id.show_selected_services);
        search_query = (AutoCompleteTextView)findViewById(R.id.search_service_text);
        services_list = (ListView)findViewById(R.id.search_services_list);

    }
}
