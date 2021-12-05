package com.example.dark.appsaloon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dark.appsaloon.Models.AppointmentsModel;
import com.example.dark.appsaloon.Models.historyModel;
import com.example.dark.appsaloon.R;

import java.util.ArrayList;



public class AppointmentsAdapter extends BaseAdapter {

    TextView customer_name,date,time,appoint_status,price,payment_method,user_id;

    TextView view_details,lat,lon;

    ArrayList<AppointmentsModel> arrayList = new ArrayList<>();
    Context context;

    View view;

    public AppointmentsAdapter(Context context,ArrayList<AppointmentsModel> arrayList) {
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
        view = LayoutInflater.from(context).inflate(R.layout.appointment_list_show,null,false);
        initialize(view);
        customer_name.setText(arrayList.get(i).getCustomer_name());
        date.setText(arrayList.get(i).getDate());
        time.setText(arrayList.get(i).getTime());
        payment_method.setText(arrayList.get(i).getPayment_status());
        appoint_status.setText(arrayList.get(i).getAppoint_status());
        price.setText(arrayList.get(i).getPrice());
        user_id.setText(arrayList.get(i).getCustomer_id());
        lat.setVisibility(View.INVISIBLE);
        lon.setVisibility(View.INVISIBLE);
        lat.setText(arrayList.get(i).getLatitude());
        lon.setText(arrayList.get(i).getLongitude());
        return view;
    }


    private void initialize(View view) {

        customer_name = (TextView)view.findViewById(R.id.customer_name);
        date = (TextView)view.findViewById(R.id.appointment_date);
        time = (TextView)view.findViewById(R.id.appointment_time);
        payment_method = (TextView)view.findViewById(R.id.payment_method);
        appoint_status = (TextView)view.findViewById(R.id.appoint_status);
        price = (TextView)view.findViewById(R.id.appoint_price);
        user_id = (TextView)view.findViewById(R.id.user_id);
        lat = (TextView)view.findViewById(R.id.lat);
        lon = (TextView)view.findViewById(R.id.lon);

        view_details = (TextView)view.findViewById(R.id.view_details);

    }
}
