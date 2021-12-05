package com.example.dark.appsaloon.Adapter;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dark.appsaloon.Customer.ViewServiceDetails;
import com.example.dark.appsaloon.Models.SalonModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.dark.appsaloon.*;
import com.example.dark.appsaloon.SalonManager.Rating;

import org.json.JSONArray;

/**
 * Created by abd on 05-Mar-18.
 */

public class salonViewAdapter extends ArrayAdapter<SalonModel> {

    ArrayList<SalonModel> arrayList = new ArrayList<>();
    Context context;

    SalonModel model;

    ArrayList<SalonModel> filteredproducts = new ArrayList<>();

    TextView salon_name, salon_city;

    Button location,salon_rating_button,view_services;

    String lat, longi;

    RatingBar salon_rating_bar;

    public salonViewAdapter(ArrayList<SalonModel> arrayList, Context context) {
        super(context,0,arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredproducts.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        model = filteredproducts.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.salon_view, null, false);
        initiliaze(view);
        setValues(model,i);
        return view;
    }

    private void setValues(final SalonModel model, final int i) {

        salon_name.setText(model.getSalon_name());
        salon_city.setText(model.getCity());
        salon_rating_bar.setRating(Float.parseFloat(model.getRate()));

        lat = model.getLatitude();
        longi = model.getLongitude();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%s,%s", lat, longi);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });
        salon_rating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Rating.class);
                intent.putExtra("salon_name",model.getSalon_name());
                intent.putExtra("salonId",model.getSalon_id());
                getContext().startActivity(intent);
            }
        });

        view_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewServiceDetails.class);
                intent.putExtra("salon_name",model.getSalon_name());
                intent.putExtra("Manager",model.getManager_id());
                getContext().startActivity(intent);

            }
        });

    }

    private void initiliaze(View view) {

        salon_name = (TextView) view.findViewById(R.id.saloon_view_name);
        salon_city = (TextView) view.findViewById(R.id.salon_view_city);

        view_services = (Button)view.findViewById(R.id.salon_view_services);

        location = (Button) view.findViewById(R.id.salon_view_location);

        salon_rating_button = (Button)view.findViewById(R.id.salon_view_rate_salon);
        salon_rating_bar = (RatingBar)view.findViewById(R.id.salon_rating_bar);

    }

    @NonNull
    @Override
    public Filter getFilter() {

    return new product_filter(this,arrayList);
    }

    private class product_filter extends Filter{


        salonViewAdapter adapter;
        ArrayList<SalonModel> original = new ArrayList<>();
        ArrayList<SalonModel> filtered = new ArrayList<>();

        public product_filter(salonViewAdapter adapter,ArrayList<SalonModel> list){
            super();
            this.adapter = adapter;
            this.original = list;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filtered.clear();
            final FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length()==0){
                filtered.addAll(original);
            }else{

                final String filterpattern =charSequence.toString().toLowerCase().trim();
                for(final SalonModel product_list : original){
                    if(product_list.getCity().toLowerCase().contains(filterpattern)){
                        filtered.add(product_list);
                    }
                }
            }
            results.values = filtered;
            results.count =filtered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filteredproducts.clear();
            adapter.filteredproducts.addAll((ArrayList<SalonModel>) filterResults.values);
            adapter.notifyDataSetChanged();
        }

    }

}