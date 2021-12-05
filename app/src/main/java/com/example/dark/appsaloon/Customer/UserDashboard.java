package com.example.dark.appsaloon.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dark.appsaloon.EditProfile;
import com.example.dark.appsaloon.Login;
import com.example.dark.appsaloon.R;
import com.example.dark.appsaloon.SessionManager.SessionManager;

public class UserDashboard extends AppCompatActivity {

    Button view_services, search_salon,search_services;

    ImageView settings;

    ImageView logout;

    TextView c_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        initiliaze();

        c_name.setText(new SessionManager(UserDashboard.this).getName());

        view_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboard.this,ViewServices.class));
            }
        });
        search_salon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboard.this,SearchSalon.class));
            }
        });
        search_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboard.this,SearchServices.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDashboard.this, EditProfile.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SessionManager(UserDashboard.this).Logout();
                startActivity(new Intent(UserDashboard.this, Login.class));
                finish();
            }
        });
    }

    private void initiliaze() {

        c_name = (TextView)findViewById(R.id.his_customer_name);

        logout = (ImageView)findViewById(R.id.logout_user);
        settings = (ImageView)findViewById(R.id.settings_user);
        view_services = (Button)findViewById(R.id.view_services);
        search_salon = (Button)findViewById(R.id.search_salon);
        search_services = (Button)findViewById(R.id.search_services);

    }
}
