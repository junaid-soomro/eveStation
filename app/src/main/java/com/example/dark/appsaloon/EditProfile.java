package com.example.dark.appsaloon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dark.appsaloon.SessionManager.SessionManager;
import com.example.dark.appsaloon.Singletons.RequestQueues;
import com.example.dark.appsaloon.Volley.EditProdile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    LinearLayout password_layout;

    EditText email,name,password,username,address,phone,new_pass1,new_pass2;

    String ID,EMAIL,NAME,PASS,USERNAME,ADDRESS,PHONE,NEW_PASS,NEW_PASS2;

    CheckBox up_pass;

    TextView UpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initiliaze();
        ShowHidePass();
        setcredentials();
        update();

    }

    private void update() {

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EMAIL = email.getText().toString();
                NAME = name.getText().toString();
                USERNAME = username.getText().toString();
                ADDRESS = address.getText().toString();
                PHONE = phone.getText().toString();
                ID = new SessionManager(EditProfile.this).getId();

                Toast.makeText(EditProfile.this, ID, Toast.LENGTH_SHORT).show();

                if(up_pass.isChecked()){updatePassword();}
                else{
                    if(Validation(NAME,EMAIL,USERNAME,ADDRESS,PHONE)){
                        updateprofile();
                    }

                }

            }
        });


    }

    private boolean Validation(String name, String email, String username, String address, String phone) {
        if(name.equals("") || email.equals("") || phone.equals("") || username.equals("") || address.equals("")){
            Toast.makeText(this, "One or more fields empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updateprofile() {

        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Updating profile");
        progressDialog.setCancelable(false);
        progressDialog.show();
        EditProdile req = new EditProdile(ID, NAME, EMAIL, PHONE, ADDRESS,USERNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Response", response.toString());
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getBoolean("success")){
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        new SessionManager(EditProfile.this).Logout();
                        startActivity(new Intent(EditProfile.this,Login.class));
                        finish();
                    }else if(object.getBoolean("PASSWORD")){
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Incorrect account paasword", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.i("Exception", e.getMessage().toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Volley", error.getMessage());
            }
        }) ;
        RequestQueues.getInstance(EditProfile.this).addToRequestQue(req);

    }

    private void updatePassword() {

        PASS = password.getText().toString();
        NEW_PASS = new_pass1.getText().toString();
        NEW_PASS2 = new_pass2.getText().toString();

            if(NEW_PASS.equals(NEW_PASS2)) {
                final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Updating profile");
                progressDialog.setCancelable(false);
                progressDialog.show();
                EditProdile req = new EditProdile(ID, NAME, EMAIL, PASS,NEW_PASS, PHONE, ADDRESS, USERNAME, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getBoolean("success")) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                new SessionManager(EditProfile.this).Logout();
                                startActivity(new Intent(EditProfile.this,Login.class));
                                finish();
                            } else if (object.getBoolean("PASSWORD")) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "Incorrect account paasword", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.i("Exception", e.getMessage().toString());
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.i("Volley", error.getMessage());
                    }
                });
                RequestQueues.getInstance(EditProfile.this).addToRequestQue(req);
            }else{

                Toast.makeText(this, "New Password mismatch", Toast.LENGTH_SHORT).show();

            }



    }

    private void setcredentials() {

        name.setText(new SessionManager(EditProfile.this).getName());
        email.setText(new SessionManager(EditProfile.this).getEmail());
        username.setText(new SessionManager(EditProfile.this).getUsername());
        address.setText(new SessionManager(EditProfile.this).getAddress());
        phone.setText(new SessionManager(EditProfile.this).getPhone());

    }

    private void ShowHidePass() {

        password_layout.setVisibility(View.GONE);

        up_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(up_pass.isChecked()){
                    TranslateAnimation slide = new TranslateAnimation(0, 0, 500,0 );
                    slide.setDuration(1000);

                    password_layout.setVisibility(View.VISIBLE);
                    password_layout.setAnimation(slide);
                }else{
                    TranslateAnimation slide = new TranslateAnimation(0, 0, 0,1000 );
                    slide.setDuration(1000);

                    password_layout.setVisibility(View.GONE);
                    password_layout.setAnimation(slide);
                }
            }
        });
    }

    private void initiliaze() {

        name = (EditText) findViewById(R.id.edit_name);
        email = (EditText) findViewById(R.id.edit_email);
        username = (EditText) findViewById(R.id.edit_username);
        address = (EditText) findViewById(R.id.edit_address);
        phone = (EditText) findViewById(R.id.edit_phone);
        password = (EditText) findViewById(R.id.edit_cpass);
        new_pass1 = (EditText) findViewById(R.id.edit_newpass);
        new_pass2 = (EditText) findViewById(R.id.edit_newpass2);

        password_layout = (LinearLayout)findViewById(R.id.password_layout);

        UpdateProfile = (TextView)findViewById(R.id.UpdateProfileUser);

        up_pass = (CheckBox)findViewById(R.id.up_edit_pass);

    }
}
