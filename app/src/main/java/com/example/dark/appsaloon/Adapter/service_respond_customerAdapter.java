package com.example.dark.appsaloon.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.appsaloon.Models.AppointmentsModel;
import com.example.dark.appsaloon.R;

import java.util.ArrayList;

/**
 * Created by abd on 02-Mar-18.
 */

public class service_respond_customerAdapter extends BaseAdapter {

    ArrayList<AppointmentsModel> arrayList = new ArrayList<>();
    Context context;

    TextView service_name;
    ImageView remove_service;

    public service_respond_customerAdapter(Context context,ArrayList<AppointmentsModel> arrayList) {
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
        view = LayoutInflater.from(context).inflate(R.layout.manager_service_list,null,false);
        initialize(view);
        setvalues(i);
        return view;
    }




    private void setvalues(int i) {

        service_name.setText(arrayList.get(i).getAppointed_servies());



    }

    private void initialize(View view) {

        service_name = (TextView)view.findViewById(R.id.service_name_show);
        remove_service = (ImageView)view.findViewById(R.id.remove_service);

    }
}
