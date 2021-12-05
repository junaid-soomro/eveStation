package com.example.dark.appsaloon.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abd on 04-Mar-18.
 */

public class FilterServicesAdapter extends ArrayAdapter<ServiceModel> {

    ArrayList<ServiceModel> filteredproducts = new ArrayList<>();

    ArrayList<ServiceModel> arrayList = new ArrayList<>();
    Context context;

    TextView service_name,service_price,service_detail,service_id,categ;
    ImageView service_image;

    ServiceModel model;
    CheckBox box;


    public FilterServicesAdapter(ArrayList<ServiceModel> arrayList, Context context) {
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
        view = LayoutInflater.from(context).inflate(R.layout.view_service_list_layout,null,false);
        initiliaze(view);
            setvalues(model);
        return view;
    }

    private void setvalues(ServiceModel model) {

        service_name.setText(model.getService_name());
        service_price.setText(model.getService_price());
        service_detail.setText(model.getService_detail());
        service_id.setText(model.getService_id());
categ.setText(model.getCategory());

        Picasso.with(context).load(model.getService_image()).into(service_image);

    }

    private void initiliaze(View view) {

        service_name = (TextView)view.findViewById(R.id.service_name_list);
        service_detail = (TextView)view.findViewById(R.id.service_details_list);
        service_price = (TextView)view.findViewById(R.id.service_price_list);
        service_id = (TextView)view.findViewById(R.id.service_id);
        categ = (TextView)view.findViewById(R.id.service_categ_list);

        box = (CheckBox) view.findViewById(R.id.selected_services);
        box.setFocusable(false);
        service_image = (ImageView)view.findViewById(R.id.service_image_list);
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return new FilterServicesAdapter.product_filter(this,arrayList);
    }

    private class product_filter extends Filter{


        FilterServicesAdapter adapter;
        ArrayList<ServiceModel> original = new ArrayList<>();
        ArrayList<ServiceModel> filtered = new ArrayList<>();

        public product_filter(FilterServicesAdapter adapter, ArrayList<ServiceModel> list){
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
                for(final ServiceModel product_list : original){
                    if(product_list.getService_name().toLowerCase().contains(filterpattern)){
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
            adapter.filteredproducts.addAll((ArrayList<ServiceModel>) filterResults.values);
            adapter.notifyDataSetChanged();
        }

    }

}
