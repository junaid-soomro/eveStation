package com.example.dark.appsaloon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.appsaloon.Models.ServiceModel;
import com.example.dark.appsaloon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abd on 07-Mar-18.
 */

public class selectedServicesadapter extends BaseAdapter {

    ArrayList<ServiceModel> arrayList = new ArrayList<>();
    Context context;

    TextView service_name,service_price,service_detail,service_id,categ;
    ImageView service_image;

    CheckBox box;

    ServiceModel model;

    public selectedServicesadapter(ArrayList<ServiceModel> arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.view_service_list_layout,null,false);
        initiliaze(view);
        setvalues(i);
        return view;
    }

    private void setvalues(int i) {

        service_name.setText(arrayList.get(i).getService_name());
        service_price.setText(arrayList.get(i).getService_price());
        service_detail.setText(arrayList.get(i).getService_detail());
        service_id.setText(arrayList.get(i).getService_id());
        categ.setText(arrayList.get(i).getCategory());

        Picasso.with(context).load(arrayList.get(i).getService_image()).into(service_image);

    }

    private void initiliaze(View view) {

        service_name = (TextView)view.findViewById(R.id.service_name_list);
        service_detail = (TextView)view.findViewById(R.id.service_details_list);
        service_price = (TextView)view.findViewById(R.id.service_price_list);
        service_id = (TextView)view.findViewById(R.id.service_id);
        categ = (TextView)view.findViewById(R.id.service_categ_list);


        box = (CheckBox) view.findViewById(R.id.selected_services);
        box.setFocusable(false);
        box.setVisibility(View.GONE);
        service_image = (ImageView)view.findViewById(R.id.service_image_list);
    }
}
