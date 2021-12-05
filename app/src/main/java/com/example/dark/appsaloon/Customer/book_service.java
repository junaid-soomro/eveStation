package com.example.dark.appsaloon.Customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SalonManager.AddSalon;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Locale;

public class book_service extends AppCompatActivity {

    TextView set_date,set_time,selected_address;

    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    ImageView date,time;

    Place place;

    String DATE,TIME;

    Button pick_location,gotopayment;

    boolean check = false;

    String latitude,longitude;

    int PLACE_PICKER_REQUEST = 1;

    String select_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);
        initiliaze();
        selectdatetime() ;
        fetchLocation();
    }

    private void fetchLocation() {


        pick_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;

                try {
                    intent = builder.build(book_service.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        gotopayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(set_date.getText().toString().equals("التاريخ المختار") || set_time.getText().toString().equals("الوقت المختار")){
                    Toast.makeText(book_service.this, "Select time/date", Toast.LENGTH_SHORT).show();
                return;
                }
                if(check==false){
                    Toast.makeText(book_service.this, "Please pick location", Toast.LENGTH_SHORT).show();
                return;
                }

                DATE = set_date.getText().toString();
                TIME = set_time.getText().toString();




                Intent intent = new Intent(book_service.this,Payment.class);
                intent.putExtra("date",DATE);
                intent.putExtra("time",TIME);
                intent.putExtra("lat",latitude);
                intent.putExtra("lon",longitude);
                startActivity(intent);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);


                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {

                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                    select_address = place.getAddress().toString();
                    selected_address.setText(select_address);
check = true;

                } catch (Exception e) {

                    e.printStackTrace();
                }


            }
        }
    }
    private void selectdatetime() {

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(book_service.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,date
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int minutes = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR);

                TimePickerDialog dialog = new TimePickerDialog(book_service.this
                        ,timeSetListener,minutes,hour, android.text.format.DateFormat.is24HourFormat(book_service.this));

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                set_date.setText(date);
            }
        };
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String time = hour+":"+min;
                set_time.setText(time);
            }
        };

    }

    private void initiliaze() {
        set_date = (TextView)findViewById(R.id.date_set);
        set_time = (TextView)findViewById(R.id.time_set);
        selected_address = (TextView)findViewById(R.id.selected_address_customer);

        date = (ImageView)findViewById(R.id.date);
        time = (ImageView)findViewById(R.id.imageView2);

        pick_location = (Button)findViewById(R.id.fetch_current_location);
        gotopayment = (Button)findViewById(R.id.payment_go);

    }


}
